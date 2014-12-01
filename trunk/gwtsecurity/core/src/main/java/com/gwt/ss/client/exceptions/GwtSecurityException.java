/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Denote server side security exception occurrence<br/>
 * 表示主機端發生安全性事務異常.
 */
public class GwtSecurityException extends RuntimeException implements IsSerializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6376681305631679931L;

    /** The authenticated. */
    private boolean authenticated = false;

    /** The authenticated user. */
    private String authenticatedUser = null;

    /**
     * Instantiates a new {@link GwtSecurityException}.
     */
    public GwtSecurityException() {
        super();
    }

    /**
     * Instantiates a new {@link GwtSecurityException}.
     *
     * @param message the message
     */
    public GwtSecurityException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new {@link GwtSecurityException}.
     *
     * @param message the message
     * @param cause the cause
     */
    public GwtSecurityException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new {@link GwtSecurityException}.
     *
     * @param cause the cause
     */
    public GwtSecurityException(final Throwable cause) {
        super(cause);
    }

    /**
     * Gets the authenticated user.
     *
     * @return the authenticated user
     */
    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Checks if is authenticated.
     *
     * @return true, if is authenticated
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Sets the authenticated.
     *
     * @param authenticated the new authenticated
     */
    public void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * Sets the authenticated user.
     *
     * @param authenticatedUser the new authenticated user
     */
    public void setAuthenticatedUser(final String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

}
