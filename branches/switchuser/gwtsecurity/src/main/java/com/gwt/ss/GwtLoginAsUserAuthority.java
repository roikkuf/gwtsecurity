package com.gwt.ss;

import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.security.web.authentication.switchuser.SwitchUserAuthorityChanger;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Maxim S. Ivanov
 * Date: 4/18/11
 * Time: 5:01 PM
 */
@Aspect
public class GwtLoginAsUserAuthority implements ServletContextAware, InitializingBean,
    ApplicationListener<AuthenticationSwitchUserEvent>,
    ApplicationEventPublisherAware, MessageSourceAware {

  protected static final Logger LOGGER = LoggerFactory.getLogger(GwtLoginAsUserAuthority.class);
  private ServletContext servletContext;
  private static ThreadLocal<LoginAsPayloadInfo> payloadHolder = new InheritableThreadLocal<LoginAsPayloadInfo>();
//  private ApplicationContext applicationContext;
  private SerializationPolicyProvider serializationPolicyProvider = DefaultSerializationPolicyProvider.getInstance();
  private ApplicationEventPublisher eventPublisher;
  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  @Override
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
//    Assert.notNull(authenticationManager, "authenticationManager must be specified");
  }

//  @Override
//  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//    this.applicationContext = applicationContext;
//  }

  private LoginAsPayloadInfo extract(JoinPoint jp) throws IOException, ServletException {
    LoginAsPayloadInfo result = payloadHolder.get();
    HttpHolder httpHolder = HttpHolder.getInstance(jp);
    HttpServletRequest request = httpHolder.getRequest();
    String username = null;
    if (result == null && request != null) {
      String payload = RPCServletUtils.readContentAsGwtRpc(request);
      RPCRequest rpcRequest = RPC.decodeRequest(payload, null, serializationPolicyProvider);
      Object[] requestParams = rpcRequest.getParameters();
      username = (String) requestParams[0];
      if (username == null) {
        username = "";
      }
      result = new LoginAsPayloadInfo(username, httpHolder);
      payloadHolder.set(result);
    }
    return result;
  }

  @Around("execution(* org.springframework.security.web.authentication.switchuser.SwitchUserFilter.doFilter(..))")
  public Object doFilter(ProceedingJoinPoint pjp) throws Throwable {
    HttpHolder httpHolder = HttpHolder.getInstance(pjp);
    SwitchUserFilter filter = (SwitchUserFilter) pjp.getTarget();
    if (httpHolder.isGwt()) {
      LoginAsPayloadInfo pi;
      try {
//        AuthenticationSuccessHandler successHandler = (AuthenticationSuccessHandler) getField(SwitchUserFilter.class, filter, "successHandler");
//        AuthenticationFailureHandler failureHandler = (AuthenticationFailureHandler) getField(SwitchUserFilter.class, filter, "failureHandler");

        String switchUrl = (String) getField(SwitchUserFilter.class, filter, "switchUserUrl");
        String exitUserUrl = (String) getField(SwitchUserFilter.class, filter, "exitUserUrl");
        if (requiresSwitchUser(httpHolder.getRequest(), switchUrl)) {
          pi = extract(pjp);
          Authentication targetUserAuth = attemptSwitchUser(pi, filter);
          SecurityContextHolder.getContext().setAuthentication(targetUserAuth);
//          successHandler.onAuthenticationSuccess(pi.getHttpHolder().getRequest(), pi.getHttpHolder().getResponse(), targetUserRequest);

        } else if (requiresExitUser(httpHolder.getRequest(), exitUserUrl)) {
          Authentication originalUser = attemptExitUser();
          // update the current context back to the original user
          SecurityContextHolder.getContext().setAuthentication(originalUser);
          // redirect to target url
//          successHandler.onAuthenticationSuccess(pi.getHttpHolder().getRequest(), pi.getHttpHolder().getResponse(), originalUser);
        } else {
          return pjp.proceed();
        }
        return null;
      } catch (Exception e) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Gwt user switching failed:", e);
        }
        GwtResponseUtil.processGwtException(servletContext, httpHolder.getRequest(), httpHolder.getResponse(), e);
        return null;
      } finally {
        payloadHolder.remove();
      }
    } else {
      return pjp.proceed();
    }
  }

  protected Authentication attemptExitUser() throws AuthenticationCredentialsNotFoundException {
    // need to check to see if the current user has a SwitchUserGrantedAuthority
    Authentication current = SecurityContextHolder.getContext().getAuthentication();

    if (null == current) {
      throw new AuthenticationCredentialsNotFoundException(
          messages.getMessage("SwitchUserFilter.noCurrentUser", "No current user associated with this request"));
    }

    // check to see if the current user did actual switch to another user
    // if so, get the original source user so we can switch back
    Authentication original = getSourceAuthentication(current);

    if (original == null) {
      LOGGER.error("Could not find original user Authentication object!");
      throw new AuthenticationCredentialsNotFoundException(
          messages.getMessage("SwitchUserFilter.noOriginalAuthentication", "Could not find original Authentication object"));
    }

    // get the source user details
    UserDetails originalUser = null;
    Object obj = original.getPrincipal();

    if ((obj != null) && obj instanceof UserDetails) {
      originalUser = (UserDetails) obj;
    }

    // publish event
    if (this.eventPublisher != null) {
      eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(current, originalUser));
    }

    return original;
  }

  private Authentication getSourceAuthentication(Authentication current) {
    Authentication original = null;
    Collection<GrantedAuthority> authorities = current.getAuthorities();
    for (GrantedAuthority auth : authorities) {
      // check for switch user type of authority
      if (auth instanceof SwitchUserGrantedAuthority) {
        original = ((SwitchUserGrantedAuthority) auth).getSource();
        LOGGER.debug("Found original switch user granted authority [" + original + "]");
      }
    }

    return original;
  }

  private String stripUri(HttpServletRequest request) {
    String uri = request.getRequestURI();
    int idx = uri.indexOf(';');
    if (idx > 0) {
      uri = uri.substring(0, idx);
    }
    return uri;
  }

  protected boolean requiresExitUser(HttpServletRequest request, String exitUserUrl) {
    String uri = stripUri(request);
    return uri.endsWith(request.getContextPath() + exitUserUrl);
  }

  protected boolean requiresSwitchUser(HttpServletRequest request, String switchUserUrl) {
    String uri = stripUri(request);

    return uri.endsWith(request.getContextPath() + switchUserUrl);
  }

  private Authentication attemptSwitchUser(LoginAsPayloadInfo pi, SwitchUserFilter filter) throws NoSuchFieldException, IllegalAccessException {

    // we will extract all dependencies from initialized SwitchUserFilter.
    // it prevents complications of dependencies in our bean and base spring filter

    UserDetailsService userDetailsService = (UserDetailsService) getField(SwitchUserFilter.class, filter, "userDetailsService");
    AuthenticationDetailsSource authenticationDetailsSource = (AuthenticationDetailsSource) getField(SwitchUserFilter.class, filter, "authenticationDetailsSource");
    SwitchUserAuthorityChanger switchUserAuthorityChanger = (SwitchUserAuthorityChanger) getField(SwitchUserFilter.class, filter, "switchUserAuthorityChanger");
    UserDetailsChecker userDetailsChecker = (UserDetailsChecker) getField(SwitchUserFilter.class, filter, "userDetailsChecker");

    UserDetails targetUser = userDetailsService.loadUserByUsername(pi.getUsername());
    userDetailsChecker.check(targetUser);

    UsernamePasswordAuthenticationToken newUserToken;

    // grant an additional authority that contains the original Authentication object
    // which will be used to 'exit' from the current switched user.
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR, currentAuth);

    // get the original authorities
    Collection<GrantedAuthority> orig = targetUser.getAuthorities();

    // Allow subclasses to change the authorities to be granted
    if (switchUserAuthorityChanger != null) {
      orig = switchUserAuthorityChanger.modifyGrantedAuthorities(targetUser, currentAuth, orig);
    }

    // add the new switch user authority
    List<GrantedAuthority> newAuths = new ArrayList<GrantedAuthority>(orig);
    newAuths.add(switchAuthority);

    // create the new authentication token
    newUserToken = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword(), newAuths);

    // set details
    newUserToken.setDetails(authenticationDetailsSource.buildDetails(pi.getHttpHolder().getRequest()));

    if (this.eventPublisher != null) {
      eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
          SecurityContextHolder.getContext().getAuthentication(), targetUser));
    }

    return newUserToken;
  }


  private Object getField(Class clazz, SwitchUserFilter filter, String fieldName) throws NoSuchFieldException, IllegalAccessException {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(filter);
  }

  @Override
  public void onApplicationEvent(AuthenticationSwitchUserEvent event) {
    LoginAsPayloadInfo pi = payloadHolder.get();
    if (pi != null && pi.getHttpHolder().isGwt()) {
      HttpServletRequest request = pi.getHttpHolder().getRequest();
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Gwt switching user success. Current authentication: " + event.getAuthentication() + ", will be changed to userdetails: " + event.getTargetUser());
      }
      GwtResponseUtil.writeResponse(servletContext, request, pi.getHttpHolder().getResponse(),
          String.format("//OK[[],%s,%s]", AbstractSerializationStream.DEFAULT_FLAGS,
              AbstractSerializationStream.SERIALIZATION_STREAM_VERSION));
    }
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    Assert.notNull(messageSource, "messageSource cannot be null");
    this.messages = new MessageSourceAccessor(messageSource);
  }

  private class LoginAsPayloadInfo {
    private String username;
    private HttpHolder httpHolder;

    public LoginAsPayloadInfo(String username, HttpHolder httpHolder) {
      this.username = username;
      this.httpHolder = httpHolder;
    }

    public String getUsername() {
      return username;
    }

    public HttpHolder getHttpHolder() {
      return httpHolder;
    }

    public void setHttpHolder(HttpHolder httpHolder) {
      this.httpHolder = httpHolder;
    }
  }
}


