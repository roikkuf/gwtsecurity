/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface GwtLoginAsync.
 */
public interface GwtLoginAsync {

    /**
     * Maintain singleton {@link GwtLoginAsync GwtLoginAsync} instance.<br/>
     * 管控{@link GwtLoginAsync GwtLoginAsync}單一實例工具
     */
    static final class Util {

        /** The Constant util. */
        private static final AsyncUtil<GwtLoginAsync> UTIL = new AsyncUtil<GwtLoginAsync>("j_spring_security_check") {
            @Override
            protected GwtLoginAsync newInstance() {
                return (GwtLoginAsync) GWT.create(GwtLogin.class);
            }
        };

        /**
         * Gets the single instance of Util.
         *
         * @return single instance of Util
         */
        public static GwtLoginAsync getInstance() {
            return UTIL.getInstance();
        }

        /**
         * return {@link GwtLoginAsync GwtLoginAsync} instance with specified service entry point.<br/>
         * 指定服務進入點並取得{@link GwtLoginAsync GwtLoginAsync} 實例
         * @param processUrl Spring login processing url
         * @return the instance.
         */
        public static GwtLoginAsync getInstance(final String processUrl) {
            return UTIL.getInstance(processUrl);
        }

        /**
         * Gets the process url.
         *
         * @return the process url
         */
        public static String getProcessUrl() {
            return UTIL.getProcessUrl();
        }

        /**
         * Sets the process url.
         *
         * @param processUrl the new process url
         */
        public static void setProcessUrl(final String processUrl) {
            UTIL.setProcessUrl(processUrl);
        }

        /**
         * Instantiates a new util.
         */
        private Util() {
            // Utility class should not be instantiated.
        }

    }

    // CHECKSTYLE:OFF

    /**
     * J_gwt_security_check.
     *
     * @param username the username
     * @param password the password
     * @param rememberMe the remember me
     * @param callback the callback
     */
    void j_gwt_security_check(String username, String password, boolean rememberMe, AsyncCallback<Void> callback);

    /**
     * J_gwt_security_check.
     *
     * @param username the username
     * @param password the password
     * @param rememberMe the remember me
     * @param forceLogout the force logout
     * @param callback the callback
     */
    void j_gwt_security_check(String username, String password, boolean rememberMe, boolean forceLogout,
            AsyncCallback<Void> callback);

}
