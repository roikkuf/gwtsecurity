package com.gwt.ss.client;

/**
 * Denote Server side occur {@link org.springframework.security.access.AccessDeniedException AccessDeniedException}<br/>
 * 表示主機端發生{@link org.springframework.security.access.AccessDeniedException 拒絕訪問異常}
 */
public class GwtAccessDeniedException extends GwtSecurityException {

    private static final long serialVersionUID = -3223088334426471295L;

    public GwtAccessDeniedException() {
        super();
    }

    public GwtAccessDeniedException(String message) {
        super(message);
    }

    public GwtAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAccessDeniedException(Throwable cause) {
        super(cause);
    }

}
