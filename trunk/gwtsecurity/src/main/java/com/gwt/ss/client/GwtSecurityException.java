package com.gwt.ss.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Denote server side security exception occurrence<br/>
 * 表示主機端發生安全性事務異常
 */
public class GwtSecurityException extends RuntimeException implements IsSerializable {

    private static final long serialVersionUID = 986114451704820985L;

    public GwtSecurityException() {
        super();
    }

    public GwtSecurityException(String message) {
        super(message);
    }

    public GwtSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtSecurityException(Throwable cause) {
        super(cause);
    }

}
