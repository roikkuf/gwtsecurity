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
public class GwtBadCredentialsException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8172822642247383095L;

    /**
     * Instantiates a new {@link GwtBadCredentialsException}.
     */
    public GwtBadCredentialsException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtBadCredentialsException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtBadCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtBadCredentialsException}.
     * 
     * @param message the message
     */
    public GwtBadCredentialsException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtBadCredentialsException}.
     * 
     * @param cause the cause
     */
    public GwtBadCredentialsException(final Throwable cause) {
        super(cause);
    }

}
