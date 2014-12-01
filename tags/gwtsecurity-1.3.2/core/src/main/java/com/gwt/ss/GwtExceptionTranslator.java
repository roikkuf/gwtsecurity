/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss;

import javax.servlet.ServletContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.web.context.ServletContextAware;

/**
 * Required.
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

    /** The http holder. */
    private static ThreadLocal<HttpHolder> httpHolder = new InheritableThreadLocal<HttpHolder>();

    /** The Constant LOG. */
    protected static final Logger LOG = LoggerFactory.getLogger(GwtExceptionTranslator.class);

    /** The servlet context. */
    private ServletContext servletContext;

    /**
     * Do filter.
     *
     * @param pjp the pjp
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("execution(* org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(..))")
    public Object doFilter(final ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = HttpHolder.getInstance(pjp);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Capture of " + (holder.isGwt() ? "Gwt" : "Non Gwt") + " ExceptionTranslationFilter.doFilter!"
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

    /** {@inheritDoc} */
    @Override
    public void onApplicationEvent(final AuthorizationFailureEvent event) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Receive AuthorizationFailureEvent:" + event.getAccessDeniedException().getMessage(),
                event.getAccessDeniedException());
        }
        HttpHolder holder = httpHolder.get();
        if (holder != null && holder.isGwt()) {
            GwtResponseUtil.processGwtException(servletContext, holder.getRequest(), holder.getResponse(),
                event.getAccessDeniedException());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
