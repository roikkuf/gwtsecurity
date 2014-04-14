/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.test.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Controller for gwt rpc.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
@SuppressWarnings("serial")
public class GwtRpcController extends RemoteServiceServlet implements Controller, ServletConfigAware,
        ServletContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(GwtRpcController.class);

    private RemoteService remoteService;

    private Class<?> remoteServiceClass;

    private ServletContext servletContext;

    /** {@inheritDoc} */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /** {@inheritDoc} */
    @Override
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        // if (remoteService instanceof GwtService) {
        // GwtService gwtSvc = (GwtService) remoteService;
        // gwtSvc.setRequest(request);
        // gwtSvc.setSession(request.getSession(false));
        // }
        super.doPost(request, response);
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final String processCall(final String payload) throws SerializationException {
        try {
            RPCRequest rpcRequest = RPC.decodeRequest(payload, remoteServiceClass, this);
            // delegate work to the spring injected service
            return RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(),
                rpcRequest.getSerializationPolicy());
        } catch (IncompatibleRemoteServiceException e) {
            getServletContext().log("An IncompatibleRemoteServiceException was thrown while processing this call.", e);
            return RPC.encodeResponseForFailure(null, e);
        }
    }

    /**
     * @param remoteService the remote service to set.
     */
    public final void setRemoteService(final RemoteService remoteService) {
        this.remoteService = remoteService;
        this.remoteServiceClass = remoteService == null ? null : remoteService.getClass();
    }

    /** {@inheritDoc} */
    @Override
    public void setServletConfig(final ServletConfig servletConfig) {
        if (servletConfig == null) { return; }
        try {
            super.init(servletConfig);
        } catch (ServletException e) {
            LOG.debug(e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
