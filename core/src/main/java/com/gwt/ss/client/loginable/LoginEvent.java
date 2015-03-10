/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents login process result.<br/>
 * 表示登錄的最後結果
 * @author Kent Yeh
 */
public class LoginEvent extends GwtEvent<LoginHandler> {

    /** The succeeded. */
    private boolean succeeded = false;

    /**
     * Instantiates a new login event.
     *
     * @param succeeded <table style="padding-left:70px">
     *            <tr>
     *            <td>true meeas Login success, otherwise login is canceled.</td></td>
     *            <tr>
     *            <td>true 表示登錄成功，否則為用戶取消登錄作業</td></td>
     *            </table>
     */
    public LoginEvent(final boolean succeeded) {
        this.succeeded = succeeded;
    }

    /** The type. */
    private static Type<LoginHandler> type = new Type<LoginHandler>();

    /**
     * Checks if is canceled.
     *
     * @return true, if is canceled
     */
    public boolean isCanceled() {
        return !succeeded;
    }

    /**
     * Checks if is login successful.
     *
     * @return true, if is login successful
     */
    public boolean isLoginSuccessful() {
        return succeeded;
    }

    /** {@inheritDoc} */
    @Override
    protected void dispatch(final LoginHandler handler) {
        handler.onLogin(this);
    }

    /** {@inheritDoc} */
    @Override
    public Type<LoginHandler> getAssociatedType() {
        return type;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public static Type<LoginHandler> getType() {
        return type;
    }
}
