package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.AccountStatusException
 * AccountStatusException} 表示主機端發生{@link org.springframework.security.authentication.AccountStatusException
 * 帳戶狀態異常}
 */
public class GwtAccountStatusException extends GwtAuthenticationException {

    private static final long serialVersionUID = 3716209189978247689L;

    public GwtAccountStatusException() {
        super();
    }

    public GwtAccountStatusException(String message) {
        super(message);
    }

    public GwtAccountStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAccountStatusException(Throwable cause) {
        super(cause);
    }

}
