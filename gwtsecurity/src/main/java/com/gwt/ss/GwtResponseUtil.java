package com.gwt.ss;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.gwt.ss.client.GWTSessionAuthenticationException;
import com.gwt.ss.client.GwtAccessDeniedException;
import com.gwt.ss.client.GwtAuthenticationException;
import com.gwt.ss.client.GwtBadCredentialsException;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

/**
 * Utility for handle GWT RPC response.<br/>
 * GWT RPC 回應處理工具類別
 */
public class GwtResponseUtil {

    protected static Logger logger = LoggerFactory.getLogger(GwtResponseUtil.class);
    private static AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    public static final String GWT_RPC_CONTENT_TYPE = "text/x-gwt-rpc";
    public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";

    public static boolean shouldCompressResponse(String responsePayload) {
        return RPCServletUtils.exceedsUncompressedContentLengthLimit(responsePayload);
    }

    public static void writeResponse(ServletContext servletContext, HttpServletRequest request,
            HttpServletResponse response, String responsePayload) {

        if (!response.isCommitted()) {
            try {
                boolean gzipEncode = RPCServletUtils.acceptsGzipEncoding(request)
                        && shouldCompressResponse(responsePayload);

                RPCServletUtils.writeResponse(servletContext, response,
                        responsePayload, gzipEncode);
                response.flushBuffer();
            } catch (IOException ex) {
                doUnexpectedFailure(response, ex);
            }
        }
    }

    public static void doUnexpectedFailure(HttpServletResponse response, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error("Encode eunexceptable exception!", e);
        }
        if (!response.isCommitted()) {
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
    }

    public static boolean isAnonymous(Authentication authentication) {
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    public static void processGwtException(ServletContext servletContext, HttpServletRequest request,
            HttpServletResponse response, Exception ex) {
        try {
            if (ex instanceof BadCredentialsException) {
                if (logger.isErrorEnabled()) {
                    logger.error("Encode GwtBadCredentialsException:" + ex.getMessage());
                }
                writeResponse(servletContext, request, response,
                        RPC.encodeResponseForFailure(null, new GwtBadCredentialsException(ex.getMessage(), ex)));
            } else if(ex instanceof SessionAuthenticationException){
                writeResponse(servletContext, request, response,
                        RPC.encodeResponseForFailure(null, new GWTSessionAuthenticationException(ex.getMessage())));
            } else if (ex instanceof AuthenticationException) {
                if (logger.isErrorEnabled()) {
                    logger.error("Encode GwtAuthenticationException:" + ex.getMessage());
                }
                writeResponse(servletContext, request, response,
                        RPC.encodeResponseForFailure(
                        null,
                        new GwtAuthenticationException(ex.getMessage(), ex)));
            } else if (ex instanceof AccessDeniedException) {
                if (isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Encode GwtAuthenticationException(user is anonymous):" + ex.getMessage());
                    }
                    writeResponse(servletContext, request, response,
                            RPC.encodeResponseForFailure(null, new GwtAuthenticationException(ex.getMessage(), ex)));
                } else {
                    if (logger.isErrorEnabled()) {
                        logger.error("Encode GwtAccessDeniedException(user is not anonymous):" + ex.getMessage());
                    }
                    writeResponse(servletContext, request, response,
                            RPC.encodeResponseForFailure(null, new GwtAccessDeniedException(ex.getMessage(), ex)));
                }
            } else {
                writeResponse(servletContext, request, response,
                        RPC.encodeResponseForFailure(null, ex));
            }
        } catch (SerializationException ex1) {
            doUnexpectedFailure(response, ex);
        }
    }

    /**
     * Determine whether request comes from  GWT RPC<br/>
     * 判斷request是否來自GWT RPC
     * @param request
     * @return
     */
    public static boolean isGwt(HttpServletRequest request) {
        return request != null && request.getContentType() != null
                && request.getContentType().startsWith(GWT_RPC_CONTENT_TYPE);
    }
}
