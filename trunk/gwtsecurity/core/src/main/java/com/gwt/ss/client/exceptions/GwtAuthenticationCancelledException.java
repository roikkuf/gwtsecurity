package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.core.AuthenticationException AuthenticationException}<br/>
 * 表示主機端發生{@link org.springframework.security.core.AuthenticationException 授權異常}
 */
public class GwtAuthenticationCancelledException extends GwtAuthenticationException {

    private static final long serialVersionUID = -2184254725978136595L;

    public GwtAuthenticationCancelledException() {
        super();
    }

    public GwtAuthenticationCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAuthenticationCancelledException(String message) {
        super(message);
    }

    public GwtAuthenticationCancelledException(Throwable cause) {
        super(cause);
    }

}
