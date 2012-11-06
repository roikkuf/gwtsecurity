package com.gwt.ss;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.TextEscapeUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;
import com.gwt.ss.shared.GwtConst;

/**
 * Monitor &lt;form-login&gt; event.<br/>
 * When &lt;form-login&gt; come from GWT RPC, return void message when success and prone
 * {@link com.gwt.ss.client.exceptions.GwtBadCredentialsException GwtBadCredentialsException} when failed. Not
 * effect with web page form-login.<br/>
 * Note: Certification extracting code provide by Amit Khanna<br/>
 * 監控&lt;form-login&gt;登錄處理<br/>
 * 若使用GWT RPC進行登錄，本類別實例負責回應GWT RPC，否則交由Spring Security本身處理<br/>
 * 註:取出帳號資料之程式由Amit Khanna提供
 */
@Aspect
public class GwtUsernamePasswordAuthority implements ServletContextAware, InitializingBean, ApplicationContextAware,
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    protected static final Logger LOG = LoggerFactory.getLogger(GwtUsernamePasswordAuthority.class);

    private ServletContext servletContext;

    private static ThreadLocal<PayloadInfo> payloadHolder = new InheritableThreadLocal<PayloadInfo>();

    private ApplicationContext applicationContext;

    private AuthenticationManager authenticationManager;

    private SerializationPolicyProvider serializationPolicyProvider = DefaultSerializationPolicyProvider.getInstance();

    private String rememberMeParameter = "_spring_security_remember_me";

    private boolean suppressLoginErrorMessages = false;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String getRememberMeParameter() {
        return rememberMeParameter;
    }

    public void setRememberMeParameter(String rememberMeParameter) {
        this.rememberMeParameter = rememberMeParameter;
    }

    public boolean isSuppressLoginErrorMessages() {
        return suppressLoginErrorMessages;
    }

    public void setSuppressLoginErrorMessages(boolean suppressLoginErrorMessages) {
        this.suppressLoginErrorMessages = suppressLoginErrorMessages;
    }

    /**
     * Provide by Amit Khanna<br/>
     * 由Amit Khanna提供
     */
    private PayloadInfo extract(JoinPoint jp) throws IOException, ServletException {
        PayloadInfo result = payloadHolder.get();
        HttpHolder httpHolder = HttpHolder.getInstance(jp);
        HttpServletRequest request = httpHolder.getRequest();
        String username = null;
        String password = null;
        boolean rememberMe = false;
        boolean forceLogout = false;
        if (result == null && request != null) {
            String payload = RPCServletUtils.readContentAsGwtRpc(request);
            RPCRequest rpcRequest = RPC.decodeRequest(payload, null, serializationPolicyProvider);
            Object[] requestParams = rpcRequest.getParameters();
            assert requestParams.length > 1 : "parameter count incorrect";
            username = (String) requestParams[0];
            password = (String) requestParams[1];
            if (requestParams.length > 2) {
                try {
                    rememberMe = (Boolean) requestParams[2];
                } catch (Exception e) {
                }
            }
            if (requestParams.length > 3) {
                try {
                    forceLogout = (Boolean) requestParams[3];
                } catch (Exception e) {
                }
            }
            if (username != null && password != null) {
                result = new PayloadInfo(username, password, httpHolder, rememberMe, forceLogout);
                payloadHolder.set(result);
            }
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Around("execution(* org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(..))")
    public Object doFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder httpHolder = HttpHolder.getInstance(pjp);
        AbstractAuthenticationProcessingFilter filter = (AbstractAuthenticationProcessingFilter) pjp.getTarget();
        if (httpHolder.isGwt() && requiresAuthentication(filter, httpHolder.getRequest())) {
            PayloadInfo pi = null;
            try {
                pi = extract(pjp);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    pi.getUsername(), pi.getPassword());
                Class<AbstractAuthenticationProcessingFilter> c = AbstractAuthenticationProcessingFilter.class;
                Field f = c.getDeclaredField("allowSessionCreation");
                f.setAccessible(true);
                boolean allowSessionCreation = (Boolean) f.get(filter);
                HttpSession session = httpHolder.getRequest().getSession(allowSessionCreation);
                if (session != null) {
                    httpHolder
                        .getRequest()
                        .getSession()
                        .setAttribute(GwtConst.SPRING_SECURITY_LAST_USERNAME_KEY,
                            TextEscapeUtils.escapeEntities(pi.getUsername()));
                }
                f = c.getDeclaredField("authenticationDetailsSource");
                f.setAccessible(true);
                authRequest.setDetails(((AuthenticationDetailsSource) f.get(filter)).buildDetails(httpHolder
                    .getRequest()));
                Authentication authResult = getAuthenticationManager().authenticate(authRequest);

                // Authentication was successful. Now make sure we haven't violated the session authentication
                // strategy.
                f = c.getDeclaredField("sessionStrategy");
                f.setAccessible(true);
                SessionAuthenticationStrategy sessionStrategy = (SessionAuthenticationStrategy) f.get(filter);
                try {
                    sessionStrategy.onAuthentication(authResult, httpHolder.getRequest(), httpHolder.getResponse());
                } catch (SessionAuthenticationException e) {
                    if (pi.isForceLogout()) {
                        SessionRegistry registry = applicationContext.getBean(SessionRegistry.class);
                        if (registry != null) {
                            for (SessionInformation info : registry.getAllSessions(authResult.getPrincipal(), true)) {
                                info.expireNow();
                            }
                        }
                        sessionStrategy.onAuthentication(authResult, httpHolder.getRequest(), httpHolder.getResponse());
                    } else {
                        throw e;
                    }
                }

                // Everything looks good. Publish the authentication result.
                SecurityContextHolder.getContext().setAuthentication(authResult);
                filter.getRememberMeServices().loginSuccess(
                    pi.isRememberMe() ? new RememberMeRequestWrapper(httpHolder.getRequest(), rememberMeParameter)
                            : httpHolder.getRequest(), httpHolder.getResponse(), authResult);
                applicationContext.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
                return null;
            } catch (Exception e) {
                if (LOG.isErrorEnabled() && !isSuppressLoginErrorMessages()) {
                    LOG.error("Gwt login fail:", e);
                }
                GwtResponseUtil.processGwtException(servletContext, httpHolder.getRequest(), httpHolder.getResponse(),
                    e);
                return null;
            } finally {
                payloadHolder.remove();
            }
        } else {
            return pjp.proceed();
        }
    }

    protected boolean requiresAuthentication(AbstractAuthenticationProcessingFilter filter, HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (LOG.isDebugEnabled()) {
            LOG.debug("RequiresAuthentication check for \n" + "url = " + uri + "\ncontext path = "
                    + request.getContextPath() + "\nprocessUrl = " + filter.getFilterProcessesUrl());
        }
        if (uri == null || uri.isEmpty()) {
            return false;
        } else {
            int pathParamIndex = uri.indexOf(';');

            if (pathParamIndex > 0) {
                // strip everything after the first semi-colon
                uri = uri.substring(0, pathParamIndex);
            }

            if ("".equals(request.getContextPath())) { return uri.endsWith(filter.getFilterProcessesUrl()); }
            return uri.endsWith(request.getContextPath() + filter.getFilterProcessesUrl());
        }
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        PayloadInfo pi = payloadHolder.get();
        if (pi != null && pi.getHttpHolder().isGwt()) {
            HttpServletRequest request = pi.getHttpHolder().getRequest();
            Authentication authResult = event.getAuthentication();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Gwt authentication success. Updating SecurityContextHolder to contain: " + authResult);
            }
            GwtResponseUtil.writeResponse(servletContext, request, pi.getHttpHolder().getResponse(), String.format(
                "//OK[[],%s,%s]", AbstractSerializationStream.DEFAULT_FLAGS,
                AbstractSerializationStream.SERIALIZATION_STREAM_VERSION));
        }
    }

    private static class PayloadInfo {

        private String username;

        private String password;

        private HttpHolder httpHolder;

        private boolean rememberMe = false;

        private boolean forceLogout = false;

        public PayloadInfo(String username, String password, HttpHolder httpHolder, boolean rememberMe,
                boolean forceLogout) {
            this.username = username;
            this.password = password;
            this.httpHolder = httpHolder;
            this.rememberMe = rememberMe;
            this.forceLogout = forceLogout;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public HttpHolder getHttpHolder() {
            return httpHolder;
        }

        public boolean isRememberMe() {
            return rememberMe;
        }

        public boolean isForceLogout() {
            return forceLogout;
        }

    }

    /**
     * Provide by Amit Khanna<br/>
     * 由Amit Khanna提供
     */
    private static class DefaultSerializationPolicyProvider implements SerializationPolicyProvider {

        private static DefaultSerializationPolicyProvider instance = new DefaultSerializationPolicyProvider();

        public static DefaultSerializationPolicyProvider getInstance() {
            return instance;
        }

        @Override
        public SerializationPolicy getSerializationPolicy(String moduleBaseURL, String serializationPolicyStrongName) {
            return RPC.getDefaultSerializationPolicy();
        }
    }

    private static class RememberMeRequestWrapper extends HttpServletRequestWrapper {

        private String rememberMeParameter = "_spring_security_remember_me";

        public RememberMeRequestWrapper(HttpServletRequest request, String rememberMeParameter) {
            super(request);
            if (rememberMeParameter != null && !rememberMeParameter.isEmpty()) {
                this.rememberMeParameter = rememberMeParameter;
            }
        }

        @Override
        public String getParameter(String name) {
            return name.equals(rememberMeParameter) ? "true" : super.getParameter(name);
        }
    }

}
