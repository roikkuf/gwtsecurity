/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.DisabledException
 * DisabledException}. <br/>
 * 表示主機端發生{@link org.springframework.security.authentication.DisabledException 殘疾人異常}
 */
public class GwtDisabledException extends GwtAccountStatusException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6578336868162969303L;

    /**
     * Instantiates a new {@link GwtDisabledException}.
     */
    public GwtDisabledException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtDisabledException}.
     * 
     * @param message the message
     */
    public GwtDisabledException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtDisabledException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtDisabledException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtDisabledException}.
     * 
     * @param cause the cause
     */
    public GwtDisabledException(final Throwable cause) {
        super(cause);
    }

}
