/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;

/**
 * Assign to &lt;logout success-handler-ref=&quot;hander-reference&quot;/&gt; fot handle logout action.<br/>
 * When logout from GWT RPC, just void message return. If logout from normal URL will redirect page to
 * {@link #setLogoutSuccessUrl(java.lang.String) logoutSuccessUrl}.<br/>
 * 建立本類別實例並設定到&lt;logout success-handler-ref=&quot;實例參照&quot;/&gt; 以處理登錄作業，<br/>
 * 當使用GWT RPC進行登出時，本顯別會回應GWT RPC程式。<br/>
 * 若使用正常址址登出，則頁面會回轉到{@link #setLogoutSuccessUrl(java.lang.String) logoutSuccessUrl}
 */
public class GwtLogoutSuccessHandler implements LogoutSuccessHandler, InitializingBean, ServletContextAware {

    /** The default handler. */
    private SimpleUrlLogoutSuccessHandler defaultHandler = new SimpleUrlLogoutSuccessHandler();

    /** The logout success url. */
    private String logoutSuccessUrl = "/";

    /** The servlet context. */
    private ServletContext servletContext;

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(logoutSuccessUrl, "logoutSuccessUrl must be specified");
        defaultHandler.setDefaultTargetUrl(logoutSuccessUrl);
    }

    /**
     * Gets the logout success url.
     *
     * @return the logout success url
     */
    public String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    /** {@inheritDoc} */
    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
            final Authentication authentication) throws IOException, ServletException {
        if (GwtResponseUtil.isGwt(request)) {
            String responsePayload = String.format("//OK[[],%d,%s]", AbstractSerializationStream.DEFAULT_FLAGS,
                AbstractSerializationStream.SERIALIZATION_STREAM_VERSION);
            GwtResponseUtil.writeResponse(servletContext, request, response, responsePayload);
        } else {
            defaultHandler.onLogoutSuccess(request, response, authentication);
        }

    }

    /**
     * Sets the logout success url.
     *
     * @param logoutSuccessUrl the new logout success url
     */
    public void setLogoutSuccessUrl(final String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
        defaultHandler.setDefaultTargetUrl(logoutSuccessUrl);
    }

    /** {@inheritDoc} */
    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
