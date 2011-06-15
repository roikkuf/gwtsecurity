package com.gwt.ss.demo3.server;

import com.gwt.ss.client.GwtSecurityException;
import com.gwt.ss.demo3.client.GreetingService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("greet")
public class GreetingServiceImpl extends StaffServiceImpl implements GreetingService {

    @Override
    @Secured("ROLE_ADMIN")
    public String whisperServer(String name) throws GwtSecurityException {
        return super.whisperServer(name);
    }
}
