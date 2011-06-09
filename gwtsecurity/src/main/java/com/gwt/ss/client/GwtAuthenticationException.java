package com.gwt.ss.client;

/**
 * Denote Server side occur {@link org.springframework.security.core.AuthenticationException AuthenticationException}<br/>
 * 表示主機端發生{@link org.springframework.security.core.AuthenticationException 授權異常}
 */
public class GwtAuthenticationException extends GwtSecurityException {

    private static final long serialVersionUID = -2184254725978136595L;

    public GwtAuthenticationException() {
        super();
    }

    public GwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAuthenticationException(String message) {
        super(message);
    }

    public GwtAuthenticationException(Throwable cause) {
        super(cause);
    }

}
