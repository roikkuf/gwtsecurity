package com.gwt.ss.demo2.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface GwtStaffServiceAsync extends RemoteAsync{

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static GwtStaffServiceAsync instance;

        public static GwtStaffServiceAsync getInstance() {
            if (instance == null) {
                instance = (GwtStaffServiceAsync) GWT.create(GwtStaffService.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "../gwtsl/staff");
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
