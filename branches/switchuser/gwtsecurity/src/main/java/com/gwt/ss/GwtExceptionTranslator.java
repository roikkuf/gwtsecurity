package com.gwt.ss;

import javax.servlet.ServletContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.ServletContextAware;

/**
 * Rquired
 * <ul>
 * <li>Intruduce aop naming space</li>
 * <li>Declare &lt;aop:aspectj-autoproxy&gt;</li>
 * <li>Instantiate this bean</li>
 * </ul>
 * 必須
 * <ul>
 * <li>導入 aop 名稱空間</li>
 * <li>設定 &lt;aop:aspectj-autoproxy&gt;</li>
 * <li>建立本類別實例</li>
 * </ul>
 */
@Aspect
public class GwtExceptionTranslator implements ServletContextAware, ApplicationListener<AuthorizationFailureEvent> {

    protected static Logger logger = LoggerFactory.getLogger(GwtExceptionTranslator.class);
    private static ThreadLocal<HttpHolder> httpHolder = new InheritableThreadLocal<HttpHolder>();
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Around("execution(* org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(..))")
    public Object doFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = HttpHolder.getInstance(pjp);
        if (logger.isDebugEnabled()) {
            logger.debug("Capture of " + (holder.isGwt() ? "Gwt" : "Non Gwt") + " ExceptionTranslationFilter.doFilter!"
                    + " with " + holder.getRequest().getRequestURI());
        }
        if (holder.isGwt()) {
            httpHolder.set(holder);
            try {
                return pjp.proceed();
            } finally {
                httpHolder.remove();
            }
        } else {
            return pjp.proceed();
        }
    }

    @Override
    public void onApplicationEvent(AuthorizationFailureEvent event) {
        if (logger.isErrorEnabled()) {
            logger.error("Receive AuthorizationFailureEvent:" + event.getAccessDeniedException().getMessage(),
                    event.getAccessDeniedException());
        }
        HttpHolder holder = httpHolder.get();
        if (holder != null && holder.isGwt()) {
            GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(),
                    event.getAccessDeniedException());
        }
    }
}
