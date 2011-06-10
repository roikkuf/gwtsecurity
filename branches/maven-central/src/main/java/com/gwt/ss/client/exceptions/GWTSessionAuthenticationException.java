package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.web.authentication.session.SessionAuthenticationException SessionAuthenticationException}<br/>
 * 表示主機端發生{@link org.springframework.security.web.authentication.session.SessionAuthenticationException SessionAuthenticationException}
 */
public class GWTSessionAuthenticationException extends GwtSecurityException {

    private static final long serialVersionUID = -2530572304608295151L;

    public GWTSessionAuthenticationException(Throwable cause) {
        super(cause);
    }

    public GWTSessionAuthenticationException(String message) {
        super(message);
    }

    public GWTSessionAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GWTSessionAuthenticationException() {
    }
}
