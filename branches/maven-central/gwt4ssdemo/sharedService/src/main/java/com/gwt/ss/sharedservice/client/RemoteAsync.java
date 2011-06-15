package com.gwt.ss.sharedservice.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.ss.client.loginable.LoginableAsync;

public interface RemoteAsync extends LoginableAsync{
    void greetServer(String name, AsyncCallback<String> callback);

    void whisperServer(String name, AsyncCallback<String> callback);

}
