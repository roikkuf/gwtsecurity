package com.gwt.ss.demo1.client;

import com.google.gwt.core.client.GWT;
import com.gwt.ss.client.loginable.LoginableService;
import com.gwt.ss.sharedservice.client.LoginBox;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface StaffServiceAsync extends RemoteAsync{
    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static StaffServiceAsync instance;

        public static StaffServiceAsync getInstance(String loginurl)
        {
            if ( instance == null )
            {
                instance = GWT.create(StaffServiceAsync.class);
                @SuppressWarnings("unchecked")
                LoginableService<StaffServiceAsync> ls = (LoginableService<StaffServiceAsync>) instance;
                ls.setRemoteService((StaffServiceAsync) GWT.create( StaffService.class ));
                ls.setHasLoginHandler(LoginBox.getLoginBox(loginurl));
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
