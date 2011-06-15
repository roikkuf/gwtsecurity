package com.gwt.ss.demo3.server;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.demo3.client.GreetingService;

@Service("greet")
public class GreetingServiceImpl extends StaffServiceImpl implements GreetingService {

    @Override
    @Secured("ROLE_ADMIN")
    public String whisperServer(String name) throws GwtSecurityException {
        return super.whisperServer(name);
    }
}
