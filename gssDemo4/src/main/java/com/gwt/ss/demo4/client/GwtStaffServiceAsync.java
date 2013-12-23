package com.gwt.ss.demo4.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwt.ss.client.loginable.LoginableService;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface GwtStaffServiceAsync extends RemoteAsync{

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static GwtStaffServiceAsync instance;

        public static GwtStaffServiceAsync getInstance(String loginurl) {
            if (instance == null) {
                GwtStaffServiceAsync rs = (GwtStaffServiceAsync) GWT.create(GwtStaffService.class);
                ServiceDefTarget target = (ServiceDefTarget) rs;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "../gwtsl/staff");
                instance = GWT.create(GwtStaffServiceAsync.class);
				@SuppressWarnings("unchecked")
				LoginableService<GwtStaffServiceAsync> ls = (LoginableService<GwtStaffServiceAsync>) instance;
                ls.setRemoteService(rs);
                ls.setHasLoginHandler(OpenIdSelector.getInstance(
                    GWT.getModuleBaseURL() + "../gwtsl/staff/gwt.openid.verify"));
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
