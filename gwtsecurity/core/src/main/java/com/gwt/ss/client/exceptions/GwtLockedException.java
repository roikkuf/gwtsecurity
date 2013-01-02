package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.LockedException
 * LockedException} 表示主機端發生{@link org.springframework.security.authentication.LockedException 鎖定異常}
 */
public class GwtLockedException extends GwtAccountStatusException {

    private static final long serialVersionUID = 6824900907128540394L;

    public GwtLockedException() {
        super();
    }

    public GwtLockedException(String message) {
        super(message);
    }

    public GwtLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtLockedException(Throwable cause) {
        super(cause);
    }

}
