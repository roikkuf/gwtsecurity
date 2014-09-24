/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

/**
 * Server side error occurred {@link org.springframework.security.core.userdetails.UsernameNotFoundException
 * UsernameNotFoundException}. <br/>
 * 表示主機端發生 {@link org.springframework.security.core.userdetails.UsernameNotFoundException 用戶名未找到出錯}
 */
public class GwtUsernameNotFoundException extends GwtAuthenticationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2737824032909995083L;

    /**
     * Instantiates a new {@link GwtUsernameNotFoundException}.
     */
    public GwtUsernameNotFoundException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtUsernameNotFoundException}.
     * 
     * @param message the message
     */
    public GwtUsernameNotFoundException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtUsernameNotFoundException}.
     * 
     * @param message the message
     * @param cause the cause
     */
    public GwtUsernameNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtUsernameNotFoundException}.
     * 
     * @param cause the cause
     */
    public GwtUsernameNotFoundException(final Throwable cause) {
        super(cause);
    }

}
