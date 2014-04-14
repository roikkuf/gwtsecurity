/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Handler interface for {@link LoginEvent LoginEvent} events.<br/>
 * You may not require this handler. <br/>
 * 接受{@link LoginEvent LoginEvent}事件處理,<br/>
 * 您應該用不到這個類別
 * @author Kent Yeh
 */
public interface LoginHandler extends EventHandler {

    /**
     * Sets the login handler registration.
     *
     * @param hr the new login handler registration
     */
    void setLoginHandlerRegistration(HandlerRegistration hr);

    /**
     * On login.
     *
     * @param e the e
     */
    void onLogin(LoginEvent e);
}
