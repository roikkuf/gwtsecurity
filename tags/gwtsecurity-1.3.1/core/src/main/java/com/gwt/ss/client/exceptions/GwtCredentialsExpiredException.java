/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.CredentialsExpiredException
 * CredentialsExpiredException}. <br/>
 * 表示主機端發生{@link org.springframework.security.authentication.CredentialsExpiredException 全權證書過期異常}
 */
public class GwtCredentialsExpiredException extends GwtAccountStatusException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5047377180141870767L;

    /**
     * Instantiates a new {@link GwtCredentialsExpiredException}.
     */
    public GwtCredentialsExpiredException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtCredentialsExpiredException}.
     * 
     * @param message the message
     */
    public GwtCredentialsExpiredException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtCredentialsExpiredException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtCredentialsExpiredException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtCredentialsExpiredException}.
     * 
     * @param cause the cause
     */
    public GwtCredentialsExpiredException(final Throwable cause) {
        super(cause);
    }

}
