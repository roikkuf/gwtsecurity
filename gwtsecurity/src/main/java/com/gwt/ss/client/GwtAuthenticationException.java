package com.gwt.ss.client;

/**
 * Denote Server side occur ${@link org.springframework.security.core.AuthenticationException AuthenticationException}
 */
public class GwtAuthenticationException extends GwtSecurityException {

    private static final long serialVersionUID = 5760665135753385196L;

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
