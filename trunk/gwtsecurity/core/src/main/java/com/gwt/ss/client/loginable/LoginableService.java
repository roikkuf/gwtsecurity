/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

/**
 * A wrapper(proxy) for asynchronous {@link com.google.gwt.user.client.rpc.RemoteService remoteService}
 * descended from {@link LoginableAsync LoginableAsync}.<br/>
 * 繼承自{@link LoginableAsync LoginableAsync}的非同步{@link com.google.gwt.user.client.rpc.RemoteService remoteService}
 * 的代理物件。 
 *
 * @author Kent Yeh
 * @param <T> the generic type
 */
public interface LoginableService<T extends LoginableAsync> {

    /**
     * Gets the checks for login handler.
     *
     * @return the checks for login handler
     */
    HasLoginHandler getHasLoginHandler();

    /**
     * Sets the checks for login handler.
     *
     * @param hasLoginHandler the new checks for login handler
     */
    void setHasLoginHandler(HasLoginHandler hasLoginHandler);

    /**
     * Gets the remote service.
     *
     * @return the remote service
     */
    T getRemoteService();

    /**
     * Sets the remote service.
     *
     * @param service the new remote service
     */
    void setRemoteService(T service);
}
