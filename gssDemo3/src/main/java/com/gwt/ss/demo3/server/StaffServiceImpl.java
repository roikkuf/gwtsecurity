package com.gwt.ss.demo3.server;

import org.springframework.stereotype.Service;

import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.demo3.client.StaffService;
import com.gwt.ss.sharedservice.client.Remote;
import com.gwt.ss.sharedservice.server.ServiceImpl;

@Service("staff")
public class StaffServiceImpl implements StaffService {

    private Remote impl =  ServiceImpl.getInstance();

    @Override
    public String greetServer(String name) throws GwtSecurityException {
        return impl.greetServer(name);
    }
    /**
     * Secured method not working.
     */
    @Override
    public String whisperServer(String name) throws GwtSecurityException {
        return impl.whisperServer(name);
    }
}
