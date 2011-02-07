package com.gwt.ss;

import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.gwt.ss.client.GwtAccessDeniedException;
import com.gwt.ss.client.GwtAuthenticationException;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ServletContextAware;

/**
 * Rquired
 * <ul>
 * <li>Intruduce aop naming space</li>
 * <li>Declare &lt;aop:aspectj-autoproxy&gt;</li>
 * <li>Intruduce aop naming space</li>
 * <li>Instantiate this bean</li>
 * </url>
 */
@Aspect
public class GwtExceptionTranslator implements ServletContextAware, ApplicationListener<AuthorizationFailureEvent> {

    protected static Logger logger = LoggerFactory.getLogger(GwtExceptionTranslator.class);
    private static ThreadLocal<HttpHolder> httpHolder = new InheritableThreadLocal<HttpHolder>();
    private static final String GWT_RPC_CONTENT_TYPE = "text/x-gwt-rpc";
    private ServletContext servletContext;
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private HttpHolder extract(JoinPoint jp) {
        HttpHolder holder = new HttpHolder();
        for (Object obj : jp.getArgs()) {
            if (obj != null) {
                if (obj instanceof HttpServletRequest) {
                    holder.setRequest((HttpServletRequest) obj);
                } else if (obj instanceof HttpServletResponse) {
                    holder.setResponse((HttpServletResponse) obj);
                }
                if (holder.getRequest() != null && holder.getResponse() != null) {
                    break;
                }
            }
        }
        return holder;
    }

    @Around("execution(* org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(..))")
    public Object doFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder holder = extract(pjp);
        if (logger.isDebugEnabled()) {
            logger.debug("Capture of " + (holder.isGwt() ? "Gwt" : "Non Gwt") + " ExceptionTranslationFilter.doFilter!"
                    + " with "+holder.getRequest().getRequestURI());
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
        if (logger.isDebugEnabled()) {
            logger.debug("Receive AuthorizationFailureEvent " + event.getAccessDeniedException().getMessage());
        }
        HttpHolder holder = httpHolder.get();
        if (holder != null && holder.isGwt()) {
            processGwtException(holder, event.getAccessDeniedException());
        }
    }

    private boolean shouldCompressResponse(String responsePayload) {
        return RPCServletUtils.exceedsUncompressedContentLengthLimit(responsePayload);
    }

    private void writeResponse(HttpServletRequest request,
            HttpServletResponse response, String responsePayload)
            throws IOException {
        boolean gzipEncode = RPCServletUtils.acceptsGzipEncoding(request)
                && shouldCompressResponse(responsePayload);

        RPCServletUtils.writeResponse(servletContext, response,
                responsePayload, gzipEncode);
        response.flushBuffer();
    }

    private void doUnexpectedFailure(HttpServletResponse response, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error("Exception while dispatching incoming RPC call", e);
        }
        try {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        } catch (IOException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private void processGwtException(HttpHolder holder, Exception ex) {
        HttpServletRequest request = holder.getRequest();
        HttpServletResponse response = holder.getResponse();
        try {
            if (ex instanceof AuthenticationException) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Authentication exception occurred;", ex);
                }
                writeResponse(request, response,
                        RPC.encodeResponseForFailure(
                        null,
                        new GwtAuthenticationException(ex.getMessage(), ex)));
            } else if (ex instanceof AccessDeniedException) {
                if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Access is denied (user is anonymous);", ex);
                    }
                    writeResponse(request, response,
                            RPC.encodeResponseForFailure(null, new GwtAuthenticationException(ex.getMessage(), ex)));
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Access is denied (user is not anonymous);", ex);
                    }
                    writeResponse(request, response,
                            RPC.encodeResponseForFailure(null, new GwtAccessDeniedException(ex.getMessage(), ex)));
                }
            } else {
                writeResponse(request, response,
                        RPC.encodeResponseForFailure(null, ex));
            }
        } catch (Exception e) {
            doUnexpectedFailure(response, e);
        }
    }

    private class HttpHolder {

        HttpServletRequest request;
        HttpServletResponse response;

        public HttpServletRequest getRequest() {
            return request;
        }

        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public HttpServletResponse getResponse() {
            return response;
        }

        public void setResponse(HttpServletResponse response) {
            this.response = response;
        }

        public boolean isGwt() {
            return request != null && request.getContentType() != null && request.getContentType().startsWith(GWT_RPC_CONTENT_TYPE);
        }
    }
}
