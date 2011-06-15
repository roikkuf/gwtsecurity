package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.web.authentication.session.SessionAuthenticationException SessionAuthenticationException}<br/>
 * 表示主機端發生{@link org.springframework.security.web.authentication.session.SessionAuthenticationException 會議驗證異常}
 */
public class GwtSessionAuthenticationException extends GwtSecurityException {

    private static final long serialVersionUID = -4721682851766662179L;

    public GwtSessionAuthenticationException() {
        super();
    }

    public GwtSessionAuthenticationException(String message) {
        super(message);
    }

    public GwtSessionAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtSessionAuthenticationException(Throwable cause) {
        super(cause);
    }

}
