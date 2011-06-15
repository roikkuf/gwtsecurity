package com.gwt.ss.demo1.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.ss.client.GwtSecurityException;
import com.gwt.ss.demo1.client.StaffService;
import com.gwt.ss.sharedservice.client.Remote;
import com.gwt.ss.sharedservice.server.ServiceImpl;
import org.springframework.security.access.annotation.Secured;

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
