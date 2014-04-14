/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.BadCredentialsException
 * BadCredentialsException}. <br/>
 * 登錄資料不正確所引發的{@link org.springframework.security.authentication.BadCredentialsException 錯誤憑證異常}
 */
public class GwtInsufficientAuthenticationException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8172822642247383095L;

    /**
     * Instantiates a new {@link GwtInsufficientAuthenticationException}.
     */
    public GwtInsufficientAuthenticationException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtInsufficientAuthenticationException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtInsufficientAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtInsufficientAuthenticationException}.
     * 
     * @param message the message
     */
    public GwtInsufficientAuthenticationException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtInsufficientAuthenticationException}.
     * 
     * @param cause the cause
     */
    public GwtInsufficientAuthenticationException(final Throwable cause) {
        super(cause);
    }

}
