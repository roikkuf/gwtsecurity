package com.gwt.ss.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Denote server side security exception occurrence<br/>
 * 表示主機端發生安全性事務異常
 */
public class GwtSecurityException extends RuntimeException implements IsSerializable {

    private static final long serialVersionUID = 6376681305631679931L;

    private boolean authenticated = false;

    private String authenticatedUser = null;

    public GwtSecurityException() {
        super();
    }

    public GwtSecurityException(String message) {
        super(message);
    }

    public GwtSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtSecurityException(Throwable cause) {
        super(cause);
    }

    /**
     * @return the authenticatedUser
     */
    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * @return the authenticated
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * @param authenticated the authenticated to set
     */
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * @param authenticatedUser the authenticatedUser to set
     */
    public void setAuthenticatedUser(String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

}
