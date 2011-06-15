package com.gwt.ss.client;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.AccountExpiredException AccountExpiredException}
 * 表示主機端發生{@link org.springframework.security.authentication.AccountExpiredException 賬戶中過期失效異常}
 */
public class GwtAccountExpiredException extends GwtSecurityException {

    private static final long serialVersionUID = 5465544835530531001L;

    public GwtAccountExpiredException() {
        super();
    }

    public GwtAccountExpiredException(String message) {
        super(message);
    }

    public GwtAccountExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAccountExpiredException(Throwable cause) {
        super(cause);
    }

}
