package com.gwt.ss.client.exceptions;

/**
 * Denote Server side occur {@link org.springframework.security.authentication.DisabledException DisabledException}
 * 表示主機端發生{@link org.springframework.security.authentication.DisabledException 殘疾人異常}
 */
public class GwtDisabledException extends GwtAccountStatusException {

    private static final long serialVersionUID = -6578336868162969303L;

    public GwtDisabledException() {
        super();
    }

    public GwtDisabledException(String message) {
        super(message);
    }

    public GwtDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtDisabledException(Throwable cause) {
        super(cause);
    }

}
