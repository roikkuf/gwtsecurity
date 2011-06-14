package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.CredentialsExpiredException CredentialsExpiredException}
 * 表示主機端發生{@link org.springframework.security.authentication.CredentialsExpiredException 全權證書過期異常}
 */
public class GwtCredentialsExpiredException extends GwtSecurityException {

    private static final long serialVersionUID = 5047377180141870767L;

    public GwtCredentialsExpiredException() {
        super();
    }

    public GwtCredentialsExpiredException(String message) {
        super(message);
    }

    public GwtCredentialsExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtCredentialsExpiredException(Throwable cause) {
        super(cause);
    }

}
