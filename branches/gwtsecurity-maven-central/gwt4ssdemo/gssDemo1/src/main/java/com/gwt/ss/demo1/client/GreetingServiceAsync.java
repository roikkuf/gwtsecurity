package com.gwt.ss.demo1.client;

import com.google.gwt.core.client.GWT;
import com.gwt.ss.client.loginable.LoginableService;
import com.gwt.ss.sharedservice.client.LoginBox;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface GreetingServiceAsync extends RemoteAsync{

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static GreetingServiceAsync instance;

        public static GreetingServiceAsync getInstance(String loginurl) {
            if (instance == null) {
                instance = GWT.create(GreetingServiceAsync.class);
                LoginableService<GreetingServiceAsync> ls = (LoginableService<GreetingServiceAsync>) instance;
                ls.setRemoteService((GreetingServiceAsync) GWT.create( GreetingService.class ));
                ls.setHasLoginHandler(LoginBox.getLoginBox(loginurl));
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
