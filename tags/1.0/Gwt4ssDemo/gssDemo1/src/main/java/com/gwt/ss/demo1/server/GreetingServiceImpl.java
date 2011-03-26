package com.gwt.ss.demo1.server;

import com.gwt.ss.client.GwtSecurityException;
import com.gwt.ss.demo1.client.GreetingService;
import org.springframework.security.access.annotation.Secured;

public class GreetingServiceImpl extends StaffServiceImpl implements GreetingService {
    /**
     * Secured method not working.
     */
    @Override
    @Secured("ROLE_ADMIN")
    public String whisperServer(String name) throws GwtSecurityException {
        return super.whisperServer(name);
    }
}
