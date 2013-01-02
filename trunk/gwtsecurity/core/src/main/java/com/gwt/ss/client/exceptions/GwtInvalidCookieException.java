package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.access.AccessDeniedException AccessDeniedException}<br/>
 * 表示主機端發生{@link org.springframework.security.access.AccessDeniedException 拒絕訪問異常}
 */
public class GwtInvalidCookieException extends GwtRememberMeAuthenticationException {

    private static final long serialVersionUID = -6291676869831323005L;

    public GwtInvalidCookieException() {
        super();
    }

    public GwtInvalidCookieException(String message) {
        super(message);
    }

    public GwtInvalidCookieException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtInvalidCookieException(Throwable cause) {
        super(cause);
    }

}
