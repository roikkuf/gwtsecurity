/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.access.AccessDeniedException
 * AccessDeniedException}. <br/>
 * 表示主機端發生{@link org.springframework.security.access.AccessDeniedException 拒絕訪問異常}
 */
public class GwtInvalidCookieException extends GwtRememberMeAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6291676869831323005L;

    /**
     * Instantiates a new {@link GwtInvalidCookieException}.
     */
    public GwtInvalidCookieException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtInvalidCookieException}.
     *
     * @param message the message
     */
    public GwtInvalidCookieException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtInvalidCookieException}.
     *
     * @param message the message
     * @param cause the cause
     */
    public GwtInvalidCookieException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtInvalidCookieException}.
     *
     * @param cause the cause
     */
    public GwtInvalidCookieException(final Throwable cause) {
        super(cause);
    }

}
