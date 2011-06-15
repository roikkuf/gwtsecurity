package com.gwt.ss.sharedservice.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.gwt.ss.client.GwtSecurityException;

public interface Remote extends RemoteService {

    String greetServer(String name) throws GwtSecurityException;

    String whisperServer(String name) throws GwtSecurityException;
}
