package com.gwt.ss;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.gwt.ss.client.exceptions.GwtAccessDeniedException;
import com.gwt.ss.client.exceptions.GwtAccountExpiredException;
import com.gwt.ss.client.exceptions.GwtAccountStatusException;
import com.gwt.ss.client.exceptions.GwtAuthenticationException;
import com.gwt.ss.client.exceptions.GwtBadCredentialsException;
import com.gwt.ss.client.exceptions.GwtCredentialsExpiredException;
import com.gwt.ss.client.exceptions.GwtDisabledException;
import com.gwt.ss.client.exceptions.GwtLockedException;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.client.exceptions.GwtSessionAuthenticationException;
import com.gwt.ss.client.exceptions.GwtUsernameNotFoundException;

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

                RPCServletUtils.writeResponse(servletContext, response, responsePayload, gzipEncode);
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
            GwtSecurityException gwtEx = createGwtException(ex);
            if (gwtEx != null && logger.isErrorEnabled()) {
                String extra = "";
                if (ex instanceof AccessDeniedException) {
                    extra = "(user is ";
                    if (!isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
                        extra += "not ";
                    }
                    extra += "anonymous)";
                }
                logger.error("Encode " + gwtEx.getClass().getSimpleName() + extra + ":" + gwtEx.getMessage());
            }
            String payload = RPC.encodeResponseForFailure(null, gwtEx == null ? ex : gwtEx);
            writeResponse(servletContext, request, response, payload);
        } catch (SerializationException ex1) {
            doUnexpectedFailure(response, ex);
        }
    }

    public static GwtSecurityException createGwtException(Exception ex) {
        // 例外狀態錯誤相關的帳戶 Exceptions related to account errors.
        if (ex instanceof AccountExpiredException) return new GwtAccountExpiredException(ex.getMessage(), ex);
        if (ex instanceof CredentialsExpiredException) return new GwtCredentialsExpiredException(ex.getMessage(), ex);
        if (ex instanceof DisabledException) return new GwtDisabledException(ex.getMessage(), ex);
        if (ex instanceof LockedException) return new GwtLockedException(ex.getMessage(), ex);
        if (ex instanceof AccountStatusException) return new GwtAccountStatusException(ex.getMessage(), ex);
        // 例外有關認證錯誤 Exceptions related to authentication errors.
        if (ex instanceof UsernameNotFoundException) return new GwtUsernameNotFoundException(ex.getMessage(), ex);
        if (ex instanceof BadCredentialsException) return new GwtBadCredentialsException(ex.getMessage(), ex);
        if (ex instanceof SessionAuthenticationException)
            return new GwtSessionAuthenticationException(ex.getMessage(), ex);
        if (ex instanceof AuthenticationException) return new GwtAuthenticationException(ex.getMessage(), ex);
        // 訪問被拒絕 Access denied
        if (ex instanceof AccessDeniedException) {
            if (isAnonymous(SecurityContextHolder.getContext().getAuthentication()))
                return new GwtAuthenticationException(ex.getMessage(), ex);
            return new GwtAccessDeniedException(ex.getMessage(), ex);
        }
        // 沒有可用的映射 No mapping available
        return null;
    }

    /**
     * Determine whether request comes from GWT RPC<br/>
     * 判斷request是否來自GWT RPC
     * @param request
     * @return is the request from GWT RPC?
     */
    public static boolean isGwt(HttpServletRequest request) {
        return request != null && request.getContentType() != null
                && request.getContentType().startsWith(GWT_RPC_CONTENT_TYPE);
    }

}
