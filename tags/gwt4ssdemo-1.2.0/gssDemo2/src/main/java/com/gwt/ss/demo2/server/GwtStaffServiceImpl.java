package com.gwt.ss.demo2.server;

import org.gwtwidgets.server.spring.GWTRequestMapping;
import org.springframework.stereotype.Component;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.demo2.client.GwtStaffService;
import com.gwt.ss.sharedservice.client.Remote;
import com.gwt.ss.sharedservice.server.ServiceImpl;

@Component("staff")
@GWTRequestMapping("/staff")
@SuppressWarnings("serial")
public class GwtStaffServiceImpl extends RemoteServiceServlet implements GwtStaffService {

    private Remote impl = ServiceImpl.getInstance();

    @Override
    public String greetServer(String name) throws GwtSecurityException {
        return impl.greetServer(name);
    }

    @Override
    public String whisperServer(String name) throws GwtSecurityException {
        return impl.whisperServer(name);
    }
}
