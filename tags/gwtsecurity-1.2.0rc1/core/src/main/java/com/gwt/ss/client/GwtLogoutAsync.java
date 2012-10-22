package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtLogoutAsync {

    /**
     * Maintain singleton {@link GwtLogoutAsync GwtLogoutAsync} instance.<br/>
     * 管控{@link GwtLogoutAsync GwtLogoutAsync}單一實例工具
     */
     static final class Util {

        private static final AsyncUtil<GwtLogoutAsync> util = new AsyncUtil<GwtLogoutAsync>("j_spring_security_logout") {
            @Override
            protected GwtLogoutAsync newInstance() {
                return (GwtLogoutAsync) GWT.create(GwtLogout.class);
            }
        };

        /**
         * return {@link GwtLoginAsync GwtLoginAsync} instance with default service entry point
         * &quot;j_spring_security_check&quot;<br/>
         * 取得預設進入點為&quot;j_spring_security_check&quot;之{@link GwtLoginAsync GwtLoginAsync} 實例
         * @return the instance.
         */
        public static GwtLogoutAsync getInstance() {
            return util.getInstance();
        }

        /**
         * return {@link GwtLoginAsync GwtLoginAsync} instance with specified service entry point.<br/>
         * 指定服務進入點並取得{@link GwtLoginAsync GwtLoginAsync} 實例
         * @param processUrl Spring login processing url
         * @return the instance.
         */
        public static GwtLogoutAsync getInstance(String processUrl) {
            return util.getInstance(processUrl);
        }

        public static String getProcessUrl() {
            return util.getProcessUrl();
        }

        public static void setProcessUrl(String processUrl) {
            util.setProcessUrl(processUrl);
        }

        private Util() {
            // Utility class should not be instantiated.
        }

    }

    void j_gwt_security_logout(AsyncCallback<Void> callback);

}
