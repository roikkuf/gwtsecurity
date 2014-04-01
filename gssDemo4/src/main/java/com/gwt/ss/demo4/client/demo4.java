package com.gwt.ss.demo4.client;

import com.gwt.ss.sharedservice.client.DefaultEntry;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public class demo4 extends DefaultEntry {
              
    @Override
    protected RemoteAsync getGreetingService() {
        RemoteAsync gs = super.getGreetingService();
        if (gs == null) {
            gs = GwtGreetingServiceAsync.Util.getInstance(getLoginUrl());
            super.setGreetingService(gs);
        }
        return gs;
    }
    
    @Override
    protected RemoteAsync getStaffService() {
        RemoteAsync sf = super.getStaffService();
        if (sf == null) {
            sf = GwtStaffServiceAsync.Util.getInstance(getLoginUrl());
            super.setStaffService(sf);
        }
        return sf;
    }
    
    @Override
    public String getLoginUrl() {
        //No use
        return "j_spring_security_check";
    }

    @Override
    public String getLogoutUrl() {
        return "j_spring_security_logout";
    }
}
