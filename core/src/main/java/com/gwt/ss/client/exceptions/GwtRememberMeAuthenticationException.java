package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.BadCredentialsException BadCredentialsException} &nbsp;when login credential incorrect.<br/>
 * 登錄資料不正確所引發的{@link org.springframework.security.authentication.BadCredentialsException 錯誤憑證異常}
 */
public class GwtRememberMeAuthenticationException extends GwtAuthenticationException {

    private static final long serialVersionUID = 8172822642247383095L;

    public GwtRememberMeAuthenticationException() {
        super();
    }

    public GwtRememberMeAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtRememberMeAuthenticationException(String message) {
        super(message);
    }

    public GwtRememberMeAuthenticationException(Throwable cause) {
        super(cause);
    }

}
