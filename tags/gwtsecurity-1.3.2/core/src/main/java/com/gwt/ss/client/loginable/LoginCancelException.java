/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

/**
 * Whenever user cancel login process occurence.<br/>
 * 用戶取消登錄作業時觸發的錯誤
 * @author Kent Yeh
 */
public class LoginCancelException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1265673524265096271L;

    /**
     * Instantiates a new login cancel exception.
     *
     * @param cause the cause
     */
    public LoginCancelException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new login cancel exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public LoginCancelException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new login cancel exception.
     *
     * @param message the message
     */
    public LoginCancelException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new login cancel exception.
     */
    public LoginCancelException() {
    }
}
