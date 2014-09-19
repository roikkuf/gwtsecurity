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
public class GwtAccessDeniedException extends GwtSecurityException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3223088334426471295L;

    /**
     * Instantiates a new {@link GwtAccessDeniedException}.
     */
    public GwtAccessDeniedException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtAccessDeniedException}.
     * 
     * @param message the message
     */
    public GwtAccessDeniedException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtAccessDeniedException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtAccessDeniedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtAccessDeniedException}.
     * 
     * @param cause the cause
     */
    public GwtAccessDeniedException(final Throwable cause) {
        super(cause);
    }

}
