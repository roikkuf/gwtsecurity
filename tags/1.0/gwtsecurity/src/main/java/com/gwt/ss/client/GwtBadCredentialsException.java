package com.gwt.ss.client;
/**
 * Denote Server side occur {@link org.springframework.security.authentication.BadCredentialsException BadCredentialsException}
 * &nbsp;when login credential incorrect.<br/>
 * 登錄資料不正確所引發的{@link org.springframework.security.authentication.BadCredentialsException 錯誤憑證異常}
 */
public class GwtBadCredentialsException extends GwtAuthenticationException {

    public GwtBadCredentialsException(Throwable cause) {
        super(cause);
    }

    public GwtBadCredentialsException(String message) {
        super(message);
    }

    public GwtBadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtBadCredentialsException() {
    }
}
