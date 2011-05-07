package com.gwt.ss.client;

/**
 * Denote Server side occur {@link org.springframework.security.web.authentication.session.SessionAuthenticationException SessionAuthenticationException}<br/>
 * 表示主機端發生{@link org.springframework.security.web.authentication.session.SessionAuthenticationException SessionAuthenticationException}
 */
public class GWTSessionAuthenticationException extends GwtSecurityException {

    public GWTSessionAuthenticationException(String message) {
        super(message);
    }
}
