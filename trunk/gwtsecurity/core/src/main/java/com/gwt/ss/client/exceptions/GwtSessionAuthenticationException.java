/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred
 * {@link org.springframework.security.web.authentication.session.SessionAuthenticationException
 * SessionAuthenticationException}. <br/>
 * 表示主機端發生{@link org.springframework.security.web.authentication.session.SessionAuthenticationException
 * 會議驗證異常}
 */
public class GwtSessionAuthenticationException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4721682851766662179L;

    /**
     * Instantiates a new {@link GwtSessionAuthenticationException}.
     */
    public GwtSessionAuthenticationException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtSessionAuthenticationException}.
     * 
     * @param message the message
     */
    public GwtSessionAuthenticationException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtSessionAuthenticationException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtSessionAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtSessionAuthenticationException}.
     * 
     * @param cause the cause
     */
    public GwtSessionAuthenticationException(final Throwable cause) {
        super(cause);
    }

}
