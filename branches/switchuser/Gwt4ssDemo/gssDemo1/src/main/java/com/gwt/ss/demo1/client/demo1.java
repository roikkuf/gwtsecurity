package com.gwt.ss.demo1.client;

import com.gwt.ss.sharedservice.client.DefaultEntry;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public class demo1 extends DefaultEntry {

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
        return GreetingServiceAsync.Util.getInstance();
    }

    @Override
    public RemoteAsync getStaffServiceAsync() {
        return StaffServiceAsync.Util.getInstance();
    }

}
