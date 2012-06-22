/**
 * $Id$
 * Copyright (c) 2012 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.requestfactory;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 * Request factory servlet. Provides spring security exception translation.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public class GwtRequestFactoryServlet extends RequestFactoryServlet {

    private static final long serialVersionUID = -2456572802582498427L;

    /**
     * Constructor.
     */
    public GwtRequestFactoryServlet() {
        super(new GwtExceptionHandler());
    }

    /**
     * Constructor.
     * 
     * @param serviceDecorators service decorators.
     */
    public GwtRequestFactoryServlet(ServiceLayerDecorator... serviceDecorators) {
        this(new GwtExceptionHandler(), serviceDecorators);
    }

    /**
     * @param exceptionHandler exception handler. Must extend GwtExceptionHandler.
     * @param serviceDecorators service decorators.
     */
    public <T extends GwtExceptionHandler> GwtRequestFactoryServlet(T exceptionHandler,
            ServiceLayerDecorator... serviceDecorators) {
        super(exceptionHandler, serviceDecorators);
    }

}
