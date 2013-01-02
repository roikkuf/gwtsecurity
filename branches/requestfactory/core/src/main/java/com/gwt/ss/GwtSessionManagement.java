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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.context.ServletContextAware;

/**
 * If you apply <a href=
 * "http://static.springsource.org/spring-security/site/docs/3.1.x/reference/ns-config.html#ns-session-mgmt">
 * Session Management</a> in your secured gwt application,you need to make sure
 * <ul>
 * <li>property &quot;proxy-target-class&quot; of &lt;aop:aspectj-autoproxy&gt; set as &quot;false&quot; owing
 * to Spring's <a href=
 * "http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/aop.html#aop-proxying"
 * >proxy mechanism</a> only allow null constructor,unfortunately
 * {@link org.springframework.security.web.session.SessionManagementFilter SessionManagementFilter} didn't.</li>
 * <li>Instantiate this bean</li>
 * </ul>
 * </br> 如果您的GWT程式設定了<a href=
 * "http://static.springsource.org/spring-security/site/docs/3.1.x/reference/ns-config.html#ns-session-mgmt">
 * Session Management</a>，您必須
 * <ul>
 * <li>將&lt;aop:aspectj-autoproxy&gt;的屬性&quot;proxy-target-class&quot;設為&quot;false&quot;，這是由於 Sprig AOP的<a
 * href=
 * "http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/aop.html#aop-proxying"
 * >proxy 機制</a>不接受有參數的建構子，而{@link org.springframework.security.web.session.SessionManagementFilter
 * SessionManagementFilter} 的建構子卻含有參數</li>
 * <li>建立本類別實例</li>
 * </ul>
 */
@Aspect
public class GwtSessionManagement implements ServletContextAware {

    static final String FILTER_APPLIED = "__spring_security_session_mgmt_filter_applied";

    private static final Logger LOG = LoggerFactory.getLogger(GwtSessionManagement.class);

    private ConcurrentSessionFilter csTarget = null;

    private Object invalidSessionStrategy = null;

    private String invalidSessionUrl = null;

    private LogoutHandler[] logoutHandlers = null;

    private SecurityContextRepository securityContextRepository = null;

    private ServletContext servletContext;

    private SessionRegistry sessionRegistry;

    private SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();

    private SessionManagementFilter smTarget = null;

    @Around("execution(* org.springframework.security.web.session.ConcurrentSessionFilter.doFilter(..))")
    public Object doCSFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = HttpHolder.getInstance(pjp);
        SessionInformation info = holder.getRequest().getSession(false) != null
                && GwtResponseUtil.isGwt(holder.getRequest()) ? getSessionRegistry(getCSTarget(pjp))
            .getSessionInformation(holder.getRequest().getSession(false).getId()) : null;
        if (info != null) {
            if (info.isExpired()) {
                String msg = "Session is expired (Possibly do to multiple concurrent logins being attempted as the same user).";
                if (LOG.isDebugEnabled()) {
                    LOG.debug(msg);
                }
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                for (LogoutHandler handler : getLogoutHandlers(getCSTarget(pjp))) {
                    handler.logout(holder.getRequest(), holder.getResponse(), auth);
                }
                GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(),
                    new SessionAuthenticationException(msg));
                return null;
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Session is not expired.");
                }
                // Non-expired - update last request date/time
                info.refreshLastRequest();
                getFilterChain(pjp).doFilter(holder.getRequest(), holder.getResponse());
                return null;
            }
        } else {
            return pjp.proceed();
        }
    }

    @Around("execution(* org.springframework.security.web.session.SessionManagementFilter.doFilter(..))")
    public Object doSMFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = HttpHolder.getInstance(pjp);
        if (holder.isGwt()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("GwtSessionManagementFilter instead of SessionManagementFilter");
            }
            if (holder.getRequest().getAttribute(FILTER_APPLIED) != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("SessionManagementFilter already applied.");
                }
                // do next filter
                getFilterChain(pjp).doFilter(holder.getRequest(), holder.getResponse());
                return null;
            }

            holder.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);

            if (!getSecurityContextRepository(getSMTarget(pjp)).containsContext(holder.getRequest())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && !GwtResponseUtil.isAnonymous(authentication)) {
                    // The user has been authenticated during the current request, so call the session
                    // strategy
                    try {
                        sessionStrategy.onAuthentication(authentication, holder.getRequest(), holder.getResponse());
                    } catch (SessionAuthenticationException e) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("SessionAuthenticationStrategy rejected the authentication object", e);
                        }
                        SecurityContextHolder.clearContext();
                        GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(),
                            e);
                        return null;
                    }
                    // Eagerly save the security context to make it available for any possible re-entrant
                    // requests which may occur before the current request completes. SEC-1396.
                    getSecurityContextRepository(getSMTarget(pjp)).saveContext(SecurityContextHolder.getContext(),
                        holder.getRequest(), holder.getResponse());
                } else {
                    // No security context or authentication present. Check for a session timeout
                    if (holder.getRequest().getRequestedSessionId() != null
                            && !holder.getRequest().isRequestedSessionIdValid()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Requested session id {} is invalid", holder.getRequest()
                                .getRequestedSessionId());
                        }
                        if (getInvalidSessionUrl(getSMTarget(pjp)) != null
                                || getInvalidSessionStrategy(getSMTarget(pjp)) != null) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Starting new session (if required) and notify front-end user");
                            }
                            holder.getRequest().getSession();
                            GwtResponseUtil.processGwtException(servletContext, holder.getRequest(),
                                holder.getResponse(), new SessionAuthenticationException("Session is invalid"));
                            return null;
                        }
                    }
                }
            }

            getFilterChain(pjp).doFilter(holder.getRequest(), holder.getResponse());
            return null;
        } else {
            // back to the original SessionManagementFilter
            return pjp.proceed();
        }
    }

    private ConcurrentSessionFilter getCSTarget(ProceedingJoinPoint pjp) {
        if (csTarget == null) {
            csTarget = (ConcurrentSessionFilter) pjp.getTarget();
        }
        return csTarget;
    }

    private FilterChain getFilterChain(JoinPoint jp) {
        if (jp == null) {
            return null;
        } else {
            for (Object obj : jp.getArgs()) {
                if (obj instanceof FilterChain) { return (FilterChain) obj; }
            }
            return null;
        }
    }

    private Object getInvalidSessionStrategy(SessionManagementFilter target) throws IllegalArgumentException,
            IllegalAccessException {
        if (invalidSessionStrategy == null) {
            try {
                Field f = SessionManagementFilter.class.getDeclaredField("invalidSessionStrategy");
                f.setAccessible(true);
                invalidSessionStrategy = f.get(target);
            } catch (NoSuchFieldException e) {
                // Do nothing. We are probably using Spring Security 3.0.5.RELEASE.
            }
        }
        return invalidSessionStrategy;
    }

    private String getInvalidSessionUrl(SessionManagementFilter target) throws IllegalArgumentException,
            IllegalAccessException {
        if (invalidSessionUrl == null) {
            try {
                Field f = SessionManagementFilter.class.getDeclaredField("invalidSessionUrl");
                f.setAccessible(true);
                invalidSessionUrl = (String) f.get(target);
            } catch (NoSuchFieldException e) {
                // Do nothing. We are probably using Spring Security 3.1.0.RELEASE.
            }
        }
        return invalidSessionUrl == null ? null : invalidSessionUrl.isEmpty() ? null : invalidSessionUrl;
    }

    private LogoutHandler[] getLogoutHandlers(ConcurrentSessionFilter target) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        if (logoutHandlers == null) {
            Field f = ConcurrentSessionFilter.class.getDeclaredField("handlers");
            f.setAccessible(true);
            logoutHandlers = (LogoutHandler[]) f.get(target);
        }
        return logoutHandlers;
    }

    private SecurityContextRepository getSecurityContextRepository(SessionManagementFilter target)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (securityContextRepository == null) {
            Field f = SessionManagementFilter.class.getDeclaredField("securityContextRepository");
            f.setAccessible(true);
            securityContextRepository = (SecurityContextRepository) f.get(target);
        }
        return securityContextRepository;
    }

    private SessionRegistry getSessionRegistry(ConcurrentSessionFilter target) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        if (sessionRegistry == null) {
            Field f = ConcurrentSessionFilter.class.getDeclaredField("sessionRegistry");
            f.setAccessible(true);
            sessionRegistry = (SessionRegistry) f.get(target);
        }
        return sessionRegistry;
    }

    private SessionManagementFilter getSMTarget(ProceedingJoinPoint pjp) {
        if (smTarget == null) {
            smTarget = (SessionManagementFilter) pjp.getTarget();
        }
        return smTarget;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
