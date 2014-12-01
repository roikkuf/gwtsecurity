/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
// CHECKSTYLE:OFF
package com.gwt.ss.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwt.ss.client.exceptions.GwtSecurityException;

/**
 * &lt;form-login&gt; for Gwt, instantiate a {@link com.gwt.ss.GwtUsernamePasswordAuthority
 * GwtUsernamePasswordAuthority} is needed.<br/>
 * This interface did not annotated with {@link RemoteServiceRelativePath @RemoteServiceRelativePath}, Do not
 * generate instance by {@link com.google.gwt.core.client.GWT#create(java.lang.Class) GWT.create}, Just invoke
 * {@link GwtLoginAsync.Util#getInstance() GwtLoginAsync.Util.getInstance} instead.<br/>
 * GWT用來進行&lt;form-login&gt;的服務界面，必須建立一 {@link com.gwt.ss.GwtUsernamePasswordAuthority
 * GwtUsernamePasswordAuthority} 實例<br/>
 * 本界面並未標記{@link RemoteServiceRelativePath @RemoteServiceRelativePath}，所以不要用
 * {@link com.google.gwt.core.client.GWT#create(java.lang.Class) GWT.create}建立intance，請改用
 * {@link GwtLoginAsync.Util#getInstance() GwtLoginAsync.Util.getInstance}
 */
public interface GwtLogin extends RemoteService {

    /**
     * J_gwt_security_check.
     *
     * @param username the username
     * @param password the password
     * @param rememberMe the remember me
     * @throws GwtSecurityException the gwt security exception
     */
    void j_gwt_security_check(String username, String password, boolean rememberMe) throws GwtSecurityException;

    /**
     * J_gwt_security_check.
     *
     * @param username the username
     * @param password the password
     * @param rememberMe the remember me
     * @param forceLogout the force logout
     * @throws GwtSecurityException the gwt security exception
     */
    void j_gwt_security_check(String username, String password, boolean rememberMe, boolean forceLogout)
            throws GwtSecurityException;

}
