/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.authentication.AccountStatusException
 * AccountStatusException}. <br/>
 * 表示主機端發生{@link org.springframework.security.authentication.AccountStatusException 帳戶狀態異常}
 */
public class GwtAccountStatusException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3716209189978247689L;

    /**
     * Instantiates a new {@link GwtAccountStatusException}.
     */
    public GwtAccountStatusException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtAccountStatusException}.
     * 
     * @param message the message
     */
    public GwtAccountStatusException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtAccountStatusException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtAccountStatusException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtAccountStatusException}.
     * 
     * @param cause the cause
     */
    public GwtAccountStatusException(final Throwable cause) {
        super(cause);
    }

}
