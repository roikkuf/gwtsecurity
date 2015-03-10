/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.LockedException. <br/>
 * LockedException} 表示主機端發生{@link org.springframework.security.authentication.LockedException 鎖定異常}
 */
public class GwtLockedException extends GwtAccountStatusException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6824900907128540394L;

    /**
     * Instantiates a new {@link GwtLockedException}.
     */
    public GwtLockedException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtLockedException}.
     *
     * @param message the message
     */
    public GwtLockedException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtLockedException}.
     *
     * @param message the message
     * @param cause the cause
     */
    public GwtLockedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtLockedException}.
     *
     * @param cause the cause
     */
    public GwtLockedException(final Throwable cause) {
        super(cause);
    }

}
