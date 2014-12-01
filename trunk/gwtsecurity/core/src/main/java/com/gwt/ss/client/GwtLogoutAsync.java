/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
// CHECKSTYLE:OFF
package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface GwtLogoutAsync.
 */
public interface GwtLogoutAsync {

    /**
     * Maintain singleton {@link GwtLogoutAsync GwtLogoutAsync} instance.<br/>
     * 管控{@link GwtLogoutAsync GwtLogoutAsync}單一實例工具
     */
    static final class Util {

        /** The Constant util. */
        private static final AsyncUtil<GwtLogoutAsync> UTIL = new AsyncUtil<GwtLogoutAsync>("j_spring_security_logout") {
            @Override
            protected GwtLogoutAsync newInstance() {
                return (GwtLogoutAsync) GWT.create(GwtLogout.class);
            }
        };

        /**
         * Gets the single instance of Util.
         *
         * @return single instance of Util
         */
        public static GwtLogoutAsync getInstance() {
            return UTIL.getInstance();
        }

        /**
         * return {@link GwtLoginAsync GwtLoginAsync} instance with specified service entry point.<br/>
         * 指定服務進入點並取得{@link GwtLoginAsync GwtLoginAsync} 實例
         * @param processUrl Spring login processing url
         * @return the instance.
         */
        public static GwtLogoutAsync getInstance(final String processUrl) {
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

    /**
     * J_gwt_security_logout.
     *
     * @param callback the callback
     */
    void j_gwt_security_logout(AsyncCallback<Void> callback);

}
