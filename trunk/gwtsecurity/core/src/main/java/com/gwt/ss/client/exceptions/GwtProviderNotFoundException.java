/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.BadCredentialsException
 * BadCredentialsException} &nbsp;when login credential incorrect. <br/>
 * 登錄資料不正確所引發的{@link org.springframework.security.authentication.BadCredentialsException 錯誤憑證異常}
 */
public class GwtProviderNotFoundException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8172822642247383095L;

    /**
     * Instantiates a new {@link GwtProviderNotFoundException}.
     */
    public GwtProviderNotFoundException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtProviderNotFoundException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtProviderNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtProviderNotFoundException}.
     * 
     * @param message the message
     */
    public GwtProviderNotFoundException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtProviderNotFoundException}.
     * 
     * @param cause the cause
     */
    public GwtProviderNotFoundException(final Throwable cause) {
        super(cause);
    }

}
