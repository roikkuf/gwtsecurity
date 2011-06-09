package com.gwt.ss.client;

/**
 * Denote Server side occur {@link org.springframework.security.core.userdetails.UsernameNotFoundException UsernameNotFoundException}
 * 表示主機端發生{@link org.springframework.security.core.userdetails.UsernameNotFoundException 用戶名未找到出錯}
 */
public class GwtUsernameNotFoundException extends GwtSecurityException {

    private static final long serialVersionUID = -2737824032909995083L;

    public GwtUsernameNotFoundException() {
        super();
    }

    public GwtUsernameNotFoundException(String message) {
        super(message);
    }

    public GwtUsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtUsernameNotFoundException(Throwable cause) {
        super(cause);
    }

}
