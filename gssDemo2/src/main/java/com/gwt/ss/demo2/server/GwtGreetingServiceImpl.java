package com.gwt.ss.demo2.server;

import org.gwtwidgets.server.spring.GWTRequestMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.demo2.client.GwtStaffService;
import com.gwt.ss.sharedservice.client.Remote;
import com.gwt.ss.sharedservice.server.ServiceImpl;

@Component("greet")
@GWTRequestMapping("/greet")
@SuppressWarnings("serial")
public class GwtGreetingServiceImpl extends RemoteServiceServlet implements GwtStaffService {

    private Remote impl = ServiceImpl.getInstance();

    @Override
    public String greetServer(String name) throws GwtSecurityException {
        return impl.greetServer(name);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public String whisperServer(String name) throws GwtSecurityException {
        return impl.whisperServer(name);
    }
}
