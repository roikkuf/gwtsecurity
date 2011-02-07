package com.gwt.ss.demo2.client;

import com.gwt.ss.sharedservice.client.DefaultEntry;
import com.gwt.ss.sharedservice.client.RemoteAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class demo2 extends DefaultEntry {

    @Override
    public String getLoginUrl() {
        return "j_spring_security_check";
    }

    @Override
    public String getLogoutUrl() {
        return "j_spring_security_logout";
    }

    @Override
    public RemoteAsync getGreetingServiceAsync() {
        return GwtGreetingServiceAsync.Util.getInstance();
    }

    @Override
    public RemoteAsync getStaffServiceAsync() {
       return GwtStaffServiceAsync.Util.getInstance();
    }

}
