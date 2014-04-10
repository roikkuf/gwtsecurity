/**
 * $Id$
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 */
package com.gwt.ss.test.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.ss.client.loginable.LoginableAsync;

/**
 * @version $Rev$
 * @author Steven Jardine
 */
public interface MainServiceAsync extends LoginableAsync {

	void adminSecured(AsyncCallback<String> callback);

	void unrestricted(AsyncCallback<String> callback);

	void userSecured(AsyncCallback<String> callback);

}
