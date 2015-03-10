/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
// CHECKSTYLE:OFF
package com.gwt.ss.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * &lt;logout&gt; direct from GWT RPC,you need instantiate a {@link com.gwt.ss.GwtLogoutSuccessHandler
 * GwtLogoutSuccessHandler} and assign to &lt;logout success-handler-ref=&quot;hander-reference&quot;/&gt;. <br/>
 * This interface did not annotated with {@link com.google.gwt.user.client.rpc.RemoteServiceRelativePath
 * @RemoteServiceRelativePath}, Do not generate instance by
 * {@link com.google.gwt.core.client.GWT#create(java.lang.Class) GWT.create}, Just invoke
 * {@link GwtLogoutAsync.Util#getInstance() GwtLogoutAsync.Util.getInstance} instead.<br/>
 * 使用GWT RPC進行登出，必須建立一{@link com.gwt.ss.GwtLogoutSuccessHandler GwtLogoutSuccessHandler}實例， 並且指定到to &lt;logout
 * success-handler-ref=&quot;實例參照&quot;/&gt;<br/>
 * 本界面並未標記{@link com.google.gwt.user.client.rpc.RemoteServiceRelativePath @RemoteServiceRelativePath}，所以不要用
 * {@link com.google.gwt.core.client.GWT#create(java.lang.Class) GWT.create}建立intance，請改用
 * {@link GwtLogoutAsync.Util#getInstance() GwtLogoutAsync.Util.getInstance}
 */
public interface GwtLogout extends RemoteService {

    /**
     * J_gwt_security_logout.
     */
    void j_gwt_security_logout();

}
