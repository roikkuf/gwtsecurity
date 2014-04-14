/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.test.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwt.ss.client.exceptions.GwtSecurityException;

/**
 * @version $Rev$
 * @author Steven Jardine
 */
@RemoteServiceRelativePath("main.rpc")
public interface MainService extends RemoteService {

	String adminSecured() throws GwtSecurityException;

	String unrestricted() throws GwtSecurityException;

	String userSecured() throws GwtSecurityException;

}
