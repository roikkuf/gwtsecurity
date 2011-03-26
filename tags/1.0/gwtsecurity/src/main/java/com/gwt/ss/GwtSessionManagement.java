package com.gwt.ss;

import java.lang.reflect.Field;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.web.context.ServletContextAware;

/**
 * If you apply <a href="http://static.springsource.org/spring-security/site/docs/3.1.x/reference/ns-config.html#ns-session-mgmt">
 * Session Management</a> in your secured gwt application,you need to make sure
 * <ul>
 *   <li>property &quot;proxy-target-class&quot; of &lt;aop:aspectj-autoproxy&gt; set as &quot;false&quot; owing to 
 *   Spring's <a href="http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/aop.html#aop-proxying">proxy
 *   mechanism</a> only allow null constructor,unfortunately {@link org.springframework.security.web.session.SessionManagementFilter SessionManagementFilter}
 *   didn't. 
 *   </li> 
 *   <li>Instantiate this bean</li>
 * </ul></br>
 * 如果您的GWT程式設定了<a href="http://static.springsource.org/spring-security/site/docs/3.1.x/reference/ns-config.html#ns-session-mgmt">
 * Session Management</a>，您必須
 * <ul>
 *   <li>將&lt;aop:aspectj-autoproxy&gt;的屬性&quot;proxy-target-class&quot;設為&quot;false&quot;，這是由於
 *   Sprig AOP的<a href="http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/aop.html#aop-proxying">proxy
 *   機制</a>不接受有參數的建構子，而{@link org.springframework.security.web.session.SessionManagementFilter SessionManagementFilter}
 *   的建構子卻含有參數        
 *   </li>  
 *   <li>建立本類別實例</li> 
 * </ul>       
 */
@Aspect
public class GwtSessionManagement implements ServletContextAware {

    protected static Logger logger = LoggerFactory.getLogger(GwtSessionManagement.class);
    private ServletContext servletContext;
    static final String FILTER_APPLIED = "__spring_security_session_mgmt_filter_applied";
    private SessionManagementFilter smTarget = null;
    private ConcurrentSessionFilter csTarget = null;
    private SessionRegistry sessionRegistry;
    private LogoutHandler[] logoutHandlers = null;
    private SecurityContextRepository securityContextRepository = null;
    private SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
    private String invalidSessionUrl = null;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private ConcurrentSessionFilter getCSTarget(ProceedingJoinPoint pjp) {
        if (csTarget == null) {
            csTarget = (ConcurrentSessionFilter) pjp.getTarget();
        }
        return csTarget;
    }

    private SessionRegistry getSessionRegistry(ConcurrentSessionFilter target) throws
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (sessionRegistry == null) {
            Field f = ConcurrentSessionFilter.class.getDeclaredField("sessionRegistry");
            f.setAccessible(true);
            sessionRegistry = (SessionRegistry) f.get(target);
        }
        return sessionRegistry;
    }

    private LogoutHandler[] getLogoutHandlers(ConcurrentSessionFilter target) throws
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (logoutHandlers == null) {
            Field f = ConcurrentSessionFilter.class.getDeclaredField("handlers");
            f.setAccessible(false);
            logoutHandlers = (LogoutHandler[]) f.get(target);
        }
        return logoutHandlers;
    }

    private SessionManagementFilter getSMTarget(ProceedingJoinPoint pjp) {
        if (smTarget == null) {
            smTarget = (SessionManagementFilter) pjp.getTarget();
        }
        return smTarget;
    }

    private SecurityContextRepository getSecurityContextRepository(SessionManagementFilter target) throws
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (securityContextRepository == null) {
            Field f = SessionManagementFilter.class.getDeclaredField("securityContextRepository");
            f.setAccessible(true);
            securityContextRepository = (SecurityContextRepository) f.get(target);
        }
        return securityContextRepository;
    }

    private String getInvalidSessionUrl(SessionManagementFilter target) throws
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (invalidSessionUrl == null) {
            Field f = SessionManagementFilter.class.getDeclaredField("invalidSessionUrl");
            f.setAccessible(true);
            invalidSessionUrl = (String) f.get(target);
        }
        return invalidSessionUrl == null ? null : invalidSessionUrl.isEmpty() ? null : invalidSessionUrl;
    }

    private FilterChain getFilterChain(JoinPoint jp) {
        if (jp == null) {
            return null;
        } else {
            for (Object obj : jp.getArgs()) {
                if (obj != null && obj instanceof FilterChain) {
                    return (FilterChain) obj;
                }
            }
            return null;
        }
    }

    @Around("execution(* org.springframework.security.web.session.SessionManagementFilter.doFilter(..))")
    public Object doSMFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = HttpHolder.getInstance(pjp);
        if (logger.isDebugEnabled()) {
            logger.debug("sm filter  isGwt : " + holder.isGwt());
            if (holder.isGwt()) {
                logger.debug("sm filter not yet applied : " + (holder.getRequest().getAttribute(FILTER_APPLIED) == null));
                if (holder.getRequest().getAttribute(FILTER_APPLIED) == null) {
                    logger.debug("sm filter security context contain : "
                            + getSecurityContextRepository(getSMTarget(pjp)).containsContext(holder.getRequest()));
                }
            }
        }
        if (holder.isGwt() && holder.getRequest().getAttribute(FILTER_APPLIED) == null
                && !getSecurityContextRepository(getSMTarget(pjp)).containsContext(holder.getRequest())) {
            holder.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !GwtResponseUtil.isAnonymous(authentication)) {
                boolean errorProne = false;
                try {
                    sessionStrategy.onAuthentication(authentication, holder.getRequest(), holder.getResponse());
                } catch (SessionAuthenticationException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }
                    errorProne = true;
                    GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(), e);
                }
                getSecurityContextRepository(getSMTarget(pjp)).saveContext(SecurityContextHolder.getContext(), holder.getRequest(), holder.getResponse());
                if (!errorProne) {
                    getFilterChain(pjp).doFilter(holder.getRequest(), holder.getResponse());
                }
                return null;
            } else {
                if (holder.getRequest().getRequestedSessionId() != null && !holder.getRequest().isRequestedSessionIdValid()) {
                    String msg = "Requested session ID" + holder.getRequest().getRequestedSessionId() + " is invalid.";
                    if (logger.isDebugEnabled()) {
                        logger.debug(msg);
                    }
                    if (getInvalidSessionUrl(getSMTarget(pjp)) != null) {
                        GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(),
                                new AuthenticationException(msg) {
                                });
                        return null;
                    }
                }
                getFilterChain(pjp).doFilter(holder.getRequest(), holder.getResponse());
            }
            return null;
        } else {
            return pjp.proceed();
        }
    }

    @Around("execution(* org.springframework.security.web.session.ConcurrentSessionFilter.doFilter(..))")
    public Object doCSFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = HttpHolder.getInstance(pjp);
        SessionInformation info = holder.getRequest().getSession(false) != null && GwtResponseUtil.isGwt(holder.getRequest())
                ? getSessionRegistry(getCSTarget(pjp)).getSessionInformation(
                holder.getRequest().getSession(false).getId()) : null;
        if (info != null) {
            if (info.isExpired()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Concurrent Session ready logout");
                }
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                for (LogoutHandler handler : getLogoutHandlers(getCSTarget(pjp))) {
                    handler.logout(holder.getRequest(), holder.getResponse(), auth);
                }
                GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(),
                        new AccessDeniedException("Session expired!"));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Concurrent info not expired");
                }
                // Non-expired - update last request date/time
                info.refreshLastRequest();
                getFilterChain(pjp).doFilter(holder.getRequest(), holder.getResponse());
            }
            return null;
        } else {
            return pjp.proceed();
        }
    }
}
