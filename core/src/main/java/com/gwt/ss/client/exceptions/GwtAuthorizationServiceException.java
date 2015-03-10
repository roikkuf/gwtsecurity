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
public class GwtAuthorizationServiceException extends GwtAccessDeniedException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3223088334426471295L;

    /**
     * Instantiates a new {@link GwtAuthorizationServiceException}.
     */
    public GwtAuthorizationServiceException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtAuthorizationServiceException}.
     *
     * @param message the message
     */
    public GwtAuthorizationServiceException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtAuthorizationServiceException}.
     *
     * @param message the message
     * @param cause the cause
     */
    public GwtAuthorizationServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtAuthorizationServiceException}.
     *
     * @param cause the cause
     */
    public GwtAuthorizationServiceException(final Throwable cause) {
        super(cause);
    }

}
