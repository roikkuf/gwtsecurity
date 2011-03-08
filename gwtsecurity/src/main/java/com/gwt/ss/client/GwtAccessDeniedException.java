package com.gwt.ss.client;

/**
 * Denote Server side occur {@link org.springframework.security.access.AccessDeniedException AccessDeniedException}<br/>
 * 表示主機端發生{@link org.springframework.security.access.AccessDeniedException 拒絕存取}異常
 */
public class GwtAccessDeniedException extends GwtSecurityException {

    private static final long serialVersionUID = -3193865157876453606L;

    public GwtAccessDeniedException() {
        super();
    }

    public GwtAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAccessDeniedException(String message) {
        super(message);
    }

    public GwtAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
