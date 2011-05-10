package com.gwt.ss.client.loginable;

/**
 * Whenever user cancel login process occurence.<br/>
 * 用戶取消登錄作業時觸發的錯誤            
 * @author Kent Yeh
 */ 
public class LoginCancelException extends Exception {

    public LoginCancelException(Throwable cause) {
        super(cause);
    }

    public LoginCancelException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginCancelException(String message) {
        super(message);
    }

    public LoginCancelException() {
    }
}
