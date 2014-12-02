/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A interface interact with front user for processing login action, and add a
 * {@link com.gwt.ss.client.loginable.LoginHandler LoginHandler} to receive login result.<br/>
 * <span style="color:red">Important:</span> when {@link AbstractLoginBox#startLogin(java.lang.Throwable)
 * start Login} , no matter login succeeded or canceled, It must fire a {@link LoginEvent} to notify the
 * waiting {@link LoginableService}. <br/>
 * 實做此界面之物件可讓用戶啟始登錄作業，並透過 {@link com.gwt.ss.client.loginable.LoginHandler LoginHandler}接收登錄結果 <br/>
 * <span style="color:red">注意:</span> 一旦{@link AbstractLoginBox#startLogin(java.lang.Throwable) 啟動登錄}，無論
 * 登錄成功或用戶取消登錄，都一定要發送{@link LoginEvent}給等待中的{@link LoginableService}.
 * @author Kent Yeh
 */
public interface HasLoginHandler extends HasHandlers {

    /**
     * start login action. 開始啟始登錄作業
     * @param caught <table style="padding-left:70px">
     *            <tr>
     *            <td>then reason why should start login.</td></td>
     *            <tr>
     *            <td>啟動登錄作業的異常例外/td></td>
     *            </table>
     */
    void startLogin(Throwable caught);

    /**
     * Accept {@link LoginHandler} to handle {@link LoginEvent}<br/>
     * 接受 {@link LoginHandler} 以處理 {@link LoginEvent}.
     *
     * @param handler the handler
     * @return the handler registration
     */
    HandlerRegistration addLoginHandler(LoginHandler handler);

}
