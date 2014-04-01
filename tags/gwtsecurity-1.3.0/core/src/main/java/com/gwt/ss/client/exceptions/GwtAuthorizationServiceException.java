package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.access.AccessDeniedException AccessDeniedException}<br/>
 * 表示主機端發生{@link org.springframework.security.access.AccessDeniedException 拒絕訪問異常}
 */
public class GwtAuthorizationServiceException extends GwtAccessDeniedException {

    private static final long serialVersionUID = -3223088334426471295L;

    public GwtAuthorizationServiceException() {
        super();
    }

    public GwtAuthorizationServiceException(String message) {
        super(message);
    }

    public GwtAuthorizationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtAuthorizationServiceException(Throwable cause) {
        super(cause);
    }

}
