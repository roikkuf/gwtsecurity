/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.BadCredentialsException
 * BadCredentialsException} &nbsp;when login credential incorrect.<br/>
 * 登錄資料不正確所引發的{@link org.springframework.security.authentication.BadCredentialsException 錯誤憑證異常}
 */
public class GwtPreAuthenticatedCredentialsNotFoundException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8172822642247383095L;

    /**
     * Instantiates a new {@link GwtPreAuthenticatedCredentialsNotFoundException}.
     */
    public GwtPreAuthenticatedCredentialsNotFoundException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtPreAuthenticatedCredentialsNotFoundException}.
     *
     * @param message the message
     * @param cause the cause
     */
    public GwtPreAuthenticatedCredentialsNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtPreAuthenticatedCredentialsNotFoundException}.
     *
     * @param message the message
     */
    public GwtPreAuthenticatedCredentialsNotFoundException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtPreAuthenticatedCredentialsNotFoundException}.
     *
     * @param cause the cause
     */
    public GwtPreAuthenticatedCredentialsNotFoundException(final Throwable cause) {
        super(cause);
    }

}
