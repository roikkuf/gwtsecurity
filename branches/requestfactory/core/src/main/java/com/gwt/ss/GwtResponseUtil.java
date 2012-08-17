package com.gwt.ss;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.shared.GwtConst;

/**
 * Utility for handle GWT RPC response.<br/>
 * GWT RPC 回應處理工具類別
 */
public final class GwtResponseUtil {

    private static AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    private static final Logger LOG = LoggerFactory.getLogger(GwtResponseUtil.class);

    private static Class<?>[] securityClasses = null;

    /**
     * @param ex the gwt exception.
     * @return the gwt equivalent exception.
     */
    public static GwtSecurityException createGwtException(Exception ex) {
        if (ex == null) { return null; }
        String className = ex.getClass().getSimpleName();
        try {
            for (Class<?> rawClass : getSecurityClasses()) {
                Class<? extends GwtSecurityException> securityClass = rawClass.asSubclass(GwtSecurityException.class);
                String gwtClassName = securityClass.getSimpleName();
                if (gwtClassName.equals("Gwt" + className)) {
                    Constructor<? extends GwtSecurityException> constructor = securityClass.getConstructor(
                        String.class, Throwable.class);
                    return constructor.newInstance(ex.getMessage(), ex);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        // Custom Mappings.
        // **** Provide any custom mappings here ****

        // 沒有可用的映射 No mapping available
        return null;
    }

    /**
     * @param response the response.
     * @param e the error.
     */
    public static void doUnexpectedFailure(HttpServletResponse response, Throwable e) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Encode eunexceptable exception!", e);
        }
        if (!response.isCommitted()) {
            try {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(e.getMessage());
                response.flushBuffer();
            } catch (IOException ex) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * @return the available security exception classes in the com.gwt.ss.client.exceptions package.
     */
    private static synchronized Class<?>[] getSecurityClasses() {
        if (securityClasses == null) {
            try {
                securityClasses = ClassUtil.getClasses("com.gwt.ss.client.exceptions");
            } catch (Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return securityClasses;
    }

    /**
     * @param authentication the current authentication.
     * @return is the user anonymous?
     */
    public static boolean isAnonymous(Authentication authentication) {
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    /**
     * Determine whether request comes from GWT.
     * 
     * @param request the request.
     * @return is the request from GWT RPC?
     */
    public static boolean isGwt(HttpServletRequest request) {
        if (request == null) { return false; }
        // Check for the request factory header.
        String header = request.getHeader(GwtConst.GWT_RF_HEADER);
        if (header != null && header.equalsIgnoreCase("true")) { return true; }
        // Check for the gwt-rpc content type.
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith(GwtConst.GWT_RPC_CONTENT_TYPE)) { return true; }
        return false;
    }

    public static void processGwtException(ServletContext servletContext, HttpServletRequest request,
            HttpServletResponse response, Exception ex) {
        try {
            GwtSecurityException gwtEx = createGwtException(ex);
            if (gwtEx != null) {
                // Get the authentication information.
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                boolean anonymous = authentication != null && isAnonymous(authentication);
                if (!anonymous) {
                    // Add authentications information to the exception.
                    gwtEx.setAuthenticated(authentication.isAuthenticated());
                    gwtEx.setAuthenticatedUser(authentication.getName());
                }
                if (LOG.isErrorEnabled()) {
                    String extra = "";
                    if (ex instanceof AccessDeniedException) {
                        extra = String.format(" (user is%1$s anonymous)", anonymous ? "" : " not");
                    }
                    LOG.error("Encode {}{}: {}",
                        new Object[] { gwtEx.getClass().getSimpleName(), extra, gwtEx.getMessage() });
                }
            }
            String payload = RPC.encodeResponseForFailure(null, gwtEx == null ? ex : gwtEx);
            writeResponse(servletContext, request, response, payload);
        } catch (SerializationException ex1) {
            doUnexpectedFailure(response, ex);
        }
    }

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

    /**
     * Private constructor.
     */
    private GwtResponseUtil() {
        super();
    }

}
