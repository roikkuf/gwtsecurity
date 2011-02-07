package com.gwt.ss.demo2.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface GwtGreetingServiceAsync extends RemoteAsync{

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static GwtGreetingServiceAsync instance;

        public static GwtGreetingServiceAsync getInstance() {
            if (instance == null) {
                instance = (GwtGreetingServiceAsync) GWT.create(GwtGreetingService.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "../gwtsl/greet");
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
