package com.gwt.ss.demo1.server;

import org.springframework.security.access.annotation.Secured;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.demo1.client.StaffService;
import com.gwt.ss.sharedservice.client.Remote;
import com.gwt.ss.sharedservice.server.ServiceImpl;

@SuppressWarnings("serial")
public class StaffServiceImpl extends RemoteServiceServlet implements StaffService {
    private Remote impl =  ServiceImpl.getInstance();

    @Override
    public String greetServer(String name) throws GwtSecurityException {
        return impl.greetServer(name);
    }
    /**
     * Secured method not working.
     */
    @Override
    @Secured("ROLE_ADMIN")
    public String whisperServer(String name) throws GwtSecurityException {
        return impl.whisperServer(name);
    }
}
