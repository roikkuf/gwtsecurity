package com.gwt.ss.client.loginable;

/**
 * A wrapper(proxy) for asynchronous {@link com.google.gwt.user.client.rpc.RemoteService remoteService}
 * descended from {@link LoginableAsync LoginableAsync}.<br/>
 * 繼承自{@link LoginableAsync LoginableAsync}的非同步{@link com.google.gwt.user.client.rpc.RemoteService remoteService}
 * 的代理物件。 
 * @author Kent Yeh
 */
public interface LoginableService<T extends LoginableAsync> {

    HasLoginHandler getHasLoginHandler();

    void setHasLoginHandler(HasLoginHandler hasLoginHandler);

    /**
     * return then real asynchronous {@link com.google.gwt.user.client.rpc.RemoteService remoteService}, If not 
     * setup before, generate by {@link com.google.gwt.core.client.GWT GWT}.{@link com.google.gwt.core.client.GWT#create(java.lang.Class create}
     *  automaticly.<br/>
     * 回傳之前設定的非同步{@link com.google.gwt.user.client.rpc.RemoteService remoteService}，若是未曾設定，則叫用
     * {@link com.google.gwt.core.client.GWT GWT}.{@link com.google.gwt.core.client.GWT#create(java.lang.Class create()}
     * 自動建立一個
     */
    T getRemoteService();

    /**
     * setup then real asynchronous {@link com.google.gwt.user.client.rpc.RemoteService remoteService}<br/>
     * 設定非同步{@link com.google.gwt.user.client.rpc.RemoteService remoteService}.
     */
    void setRemoteService(T service);
}
