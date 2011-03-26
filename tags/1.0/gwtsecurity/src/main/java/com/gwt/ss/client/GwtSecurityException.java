package com.gwt.ss.client;       

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 * Denote server side security exception occurence<br/>
 * 表示主機端發生安全性事務異常
 */
public class GwtSecurityException extends RuntimeException  implements IsSerializable{
	private static final long serialVersionUID = -8675193365018752069L;

	public GwtSecurityException() {
		super();
	}

	public GwtSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public GwtSecurityException(String message) {
		super(message);
	}

	public GwtSecurityException(Throwable cause) {
		super(cause);
	}

}
