package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.access.AccessDeniedException AccessDeniedException}<br/>
 * 表示主機端發生{@link org.springframework.security.access.AccessDeniedException 拒絕訪問異常}
 */
public class GwtCookieTheftException extends GwtRememberMeAuthenticationException {

    private static final long serialVersionUID = -6291676869831323005L;

    public GwtCookieTheftException() {
        super();
    }

    public GwtCookieTheftException(String message) {
        super(message);
    }

    public GwtCookieTheftException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtCookieTheftException(Throwable cause) {
        super(cause);
    }

}
