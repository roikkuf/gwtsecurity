package com.gwt.ss.client.loginable;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents login process result.<br/>
 * 表示登錄的最後結果  
 * @author Kent Yeh
 */
public class LoginEvent extends GwtEvent<LoginHandler> {

    private boolean succeeded = false;

    /**
     * 
     * @param succeeded <table style="padding-left:70px"><tr><td>true meeas Login success, otherwise login is canceled.</td></td>
     *<tr><td>true 表示登錄成功，否則為用戶取消登錄作業</td></td></table>
     */
    public LoginEvent(boolean succeeded) {
        this.succeeded = succeeded;
    }
    private static Type<LoginHandler> type = new Type<LoginHandler>();

    /**
     * Does user cancel loggin process?<br/>
     * 是否用戶取消登錄?     
     */
    public boolean isCanceled() {
        return !succeeded;
    }

    /**
     * Does user login success?<br/>
     * 是否用戶已登錄成功?     
     */
    public boolean isLoginSuccessful() {
        return succeeded;
    }

    @Override
    protected void dispatch(LoginHandler handler) {
        handler.onLogin(this);
    }

    @Override
    public Type<LoginHandler> getAssociatedType() {
        return type;
    }

    public static Type<LoginHandler> getType() {
        return type;
    }
}
