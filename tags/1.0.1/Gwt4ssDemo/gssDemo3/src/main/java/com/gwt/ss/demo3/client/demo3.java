package com.gwt.ss.demo3.client;

import com.gwt.ss.sharedservice.client.DefaultEntry;
import com.gwt.ss.sharedservice.client.RemoteAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class demo3 extends DefaultEntry {

    @Override
    public String getLoginUrl() {
        return "mvc/j_spring_security_check";
    }

    @Override
    public String getLogoutUrl() {
        return "mvc/j_spring_security_logout";
    }

    @Override
    public RemoteAsync getGreetingServiceAsync() {
        return GreetingServiceAsync.Util.getInstance();
    }

    @Override
    public RemoteAsync getStaffServiceAsync() {
        return StaffServiceAsync.Util.getInstance();
    }

}
