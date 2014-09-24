/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Common util methods for Async classes.
 * 
 * @author Steven Jardine
 * @version $Rev$
 * @param <T> the generic type
 */
public abstract class AsyncUtil<T> {

    /**
     * Calc service entry point.
     * 
     * @param processUrl the process url
     * @return the string
     */
    private static String calcServiceEntryPoint(final String processUrl) {
        String url = processUrl == null ? "" : processUrl;
        if (!url.toLowerCase().matches("^https?://.*")) {
            String hpbl = GWT.getHostPageBaseURL();
            int idx = hpbl.indexOf("//");
            String hostContextPath = hpbl.substring(0, idx + 2);
            hpbl = hpbl.substring(idx + 2);
            idx = hpbl.indexOf('/');
            if (idx > -1) {
                hostContextPath += hpbl.substring(0, idx + 1);
                hpbl = hpbl.substring(idx + 1);
                idx = hpbl.indexOf('/');
                if (idx > -1) {
                    hostContextPath += hpbl.substring(0, idx);
                } else {
                    hostContextPath += hpbl;
                }
            } else {
                hostContextPath += hpbl;
            }

            if (!hostContextPath.endsWith("/")) {
                hostContextPath += "/";
            }
            if (url.equals("") || url.equals("/")) {
                url = "";
            } else if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = hostContextPath + url;
        }
        return url;
    }

    /** The instance. */
    private T instance = null;

    /** The process url. */
    private String processUrl = null;

    /**
     * Default constructor.
     * 
     * @param processUrl the process url
     */
    public AsyncUtil(final String processUrl) {
        this.processUrl = processUrl;
    }

    /**
     * Gets the instance.
     * 
     * @return the instance
     */
    public T getInstance() {
        if (instance == null) {
            instance = newInstance();
            setServiceEntryPoint(instance);
        }
        return instance;
    }

    /**
     * Gets the single instance of AsyncUtil.
     * 
     * @param processUrl the process url.
     * @return a new instance.
     */
    public T getInstance(final String processUrl) {
        setProcessUrl(processUrl);
        return getInstance();
    }

    /**
     * Gets the process url.
     * 
     * @return the process url
     */
    public String getProcessUrl() {
        return processUrl;
    }

    /**
     * Gets the simple name.
     * 
     * @return the simple name
     */
    private String getSimpleName() {
        if (instance != null) {
            try {
                String name = instance.getClass().getName();
                return name.substring(name.lastIndexOf('.'));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    /**
     * New instance.
     * 
     * @return a new instance using GWT.create().
     */
    protected abstract T newInstance();

    /**
     * Sets the process url.
     * 
     * @param processUrl the new process url
     */
    public void setProcessUrl(final String processUrl) {
        assert processUrl != null && !processUrl.isEmpty();
        if (!this.processUrl.equals(processUrl)) {
            this.processUrl = processUrl;
            if (instance != null) {
                setServiceEntryPoint(instance);
            }
        }
    }

    /**
     * Sets the service entry point.
     * 
     * @param instance the new service entry point
     */
    private void setServiceEntryPoint(final T instance) {
        ServiceDefTarget target = (ServiceDefTarget) instance;
        target.setServiceEntryPoint(AsyncUtil.calcServiceEntryPoint(getProcessUrl()));
        GWT.log("Set " + getSimpleName() + " Service Entry Point as " + target.getServiceEntryPoint());
    }

}
