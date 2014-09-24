/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.AccountExpiredException
 * AccountExpiredException}. <br/>
 * 表示主機端發生{@link org.springframework.security.authentication.AccountExpiredException 賬戶中過期失效異常}
 */
public class GwtAccountExpiredException extends GwtAccountStatusException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5465544835530531001L;

    /**
     * Instantiates a new {@link GwtAccountExpiredException}.
     */
    public GwtAccountExpiredException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtAccountExpiredException}.
     * 
     * @param message the message
     */
    public GwtAccountExpiredException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtAccountExpiredException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtAccountExpiredException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtAccountExpiredException}.
     * 
     * @param cause the cause
     */
    public GwtAccountExpiredException(final Throwable cause) {
        super(cause);
    }

}
