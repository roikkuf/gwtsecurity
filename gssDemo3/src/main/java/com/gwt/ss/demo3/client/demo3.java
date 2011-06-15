package com.gwt.ss.demo3.client;

import com.gwt.ss.sharedservice.client.DefaultEntry;
import com.gwt.ss.sharedservice.client.RemoteAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class demo3 extends DefaultEntry {
    
    @Override
    protected RemoteAsync getGreetingService() {
        RemoteAsync gs = super.getGreetingService();
        if (gs == null) {
            gs = GreetingServiceAsync.Util.getInstance(getLoginUrl());
            super.setGreetingService(gs);
        }
        return gs;
    }
    
    @Override
    protected RemoteAsync getStaffService() {
        RemoteAsync sf = super.getStaffService();
        if (sf == null) {
            sf = StaffServiceAsync.Util.getInstance(getLoginUrl());
            super.setStaffService(sf);
        }
        return sf;
    }
    
    @Override
    public String getLoginUrl() {
        return "mvc/j_spring_security_check";
    }

    @Override
    public String getLogoutUrl() {
        return "mvc/j_spring_security_logout";
    }

}
