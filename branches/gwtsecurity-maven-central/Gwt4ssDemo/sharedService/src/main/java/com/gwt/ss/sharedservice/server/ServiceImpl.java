package com.gwt.ss.sharedservice.server;

import com.gwt.ss.client.GwtSecurityException;
import com.gwt.ss.sharedservice.client.Remote;
/**
 * Default service implement.
 */
public class ServiceImpl implements Remote {
    private static ServiceImpl impl;
    public static ServiceImpl getInstance(){
        if(impl==null)
            impl = new ServiceImpl();
        return impl;
    }
    @Override
    public String greetServer(String name) throws GwtSecurityException {
        name = escapeHtml(name);
        return "Hello, " + name;
    }

    @Override
    public String whisperServer(String name) throws GwtSecurityException {
        name = escapeHtml(name);
        return "Hello staff ," + name;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;");
    }
}
