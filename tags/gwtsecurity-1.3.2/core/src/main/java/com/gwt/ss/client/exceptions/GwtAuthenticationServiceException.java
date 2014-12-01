/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.core.AuthenticationException
 * AuthenticationException}. <br/>
 * 表示主機端發生{@link org.springframework.security.core.AuthenticationException 授權異常}
 */
public class GwtAuthenticationServiceException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2184254725978136595L;

    /**
     * Instantiates a new {@link GwtAuthenticationServiceException}.
     */
    public GwtAuthenticationServiceException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtAuthenticationServiceException}.
     *
     * @param message the message
     * @param cause the cause
     */
    public GwtAuthenticationServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtAuthenticationServiceException}.
     *
     * @param message the message
     */
    public GwtAuthenticationServiceException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtAuthenticationServiceException}.
     *
     * @param cause the cause
     */
    public GwtAuthenticationServiceException(final Throwable cause) {
        super(cause);
    }

}
