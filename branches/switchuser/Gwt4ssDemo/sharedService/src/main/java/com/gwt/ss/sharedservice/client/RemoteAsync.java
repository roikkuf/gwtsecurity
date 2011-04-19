package com.gwt.ss.sharedservice.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteAsync {
    void greetServer(java.lang.String name, AsyncCallback<java.lang.String> callback);

    void whisperServer(java.lang.String name, AsyncCallback<java.lang.String> callback);

}
