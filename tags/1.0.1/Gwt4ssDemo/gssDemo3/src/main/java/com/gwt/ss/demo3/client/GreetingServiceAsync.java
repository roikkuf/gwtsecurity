package com.gwt.ss.demo3.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface GreetingServiceAsync extends RemoteAsync{

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static GreetingServiceAsync instance;

        public static GreetingServiceAsync getInstance() {
            if (instance == null) {
                instance = (GreetingServiceAsync) GWT.create(GreetingService.class);
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
