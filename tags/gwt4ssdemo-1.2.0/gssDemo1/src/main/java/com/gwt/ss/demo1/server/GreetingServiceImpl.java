package com.gwt.ss.demo1.server;

import org.springframework.security.access.annotation.Secured;

import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.demo1.client.GreetingService;

@SuppressWarnings("serial")
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
