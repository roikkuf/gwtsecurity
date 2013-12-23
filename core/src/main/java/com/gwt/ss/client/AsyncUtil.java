/**
 * $Id$
 */
package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Common util methods for Async classes.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public abstract class AsyncUtil<T> {

    private static String calcServiceEntryPoint(String processUrl) {
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

    private T instance = null;

    private String processUrl = null;

    /**
     * Default constructor.
     */
    public AsyncUtil(String processUrl) {
        this.processUrl = processUrl;
    }

    /**
     * @return a new instance.
     */
    public T getInstance() {
        if (instance == null) {
            instance = newInstance();
            setServiceEntryPoint(instance);
        }
        return instance;
    }

    /**
     * @param processUrl the process url.
     * @return a new instance.
     */
    public T getInstance(String processUrl) {
        setProcessUrl(processUrl);
        return getInstance();
    }

    /**
     * @return the processUrl
     */
    public String getProcessUrl() {
        return processUrl;
    }

    /**
     * @return the classes simple name.
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
     * @return a new instance using GWT.create().
     */
    protected abstract T newInstance();

    /**
     * Set the process url.
     * 
     * @param processUrl the processUrl to set
     */
    public void setProcessUrl(String processUrl) {
        assert processUrl != null && !processUrl.isEmpty();
        if (!this.processUrl.equals(processUrl)) {
            this.processUrl = processUrl;
            if (instance != null) {
                setServiceEntryPoint(instance);
            }
        }
    }

    /**
     * Set the services entry point.
     * 
     * @param instance the instance.
     */
    private void setServiceEntryPoint(T instance) {
        ServiceDefTarget target = (ServiceDefTarget) instance;
        target.setServiceEntryPoint(AsyncUtil.calcServiceEntryPoint(getProcessUrl()));
        GWT.log("Set " + getSimpleName() + " Service Entry Point as " + target.getServiceEntryPoint());
    }

}
