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

    void setLoginHandlerRegistration(HandlerRegistration hr);

    void onLogin(LoginEvent e);
}
