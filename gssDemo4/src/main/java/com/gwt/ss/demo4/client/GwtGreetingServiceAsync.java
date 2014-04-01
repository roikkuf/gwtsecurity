package com.gwt.ss.demo4.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwt.ss.client.loginable.LoginableService;
import com.gwt.ss.sharedservice.client.RemoteAsync;

public interface GwtGreetingServiceAsync extends RemoteAsync {

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static final class Util {

		private static GwtGreetingServiceAsync instance;

		public static GwtGreetingServiceAsync getInstance(String loginurl) {
			if (instance == null) {
				GwtGreetingServiceAsync rs = (GwtGreetingServiceAsync) GWT
						.create(GwtGreetingService.class);
				ServiceDefTarget target = (ServiceDefTarget) rs;
				target.setServiceEntryPoint(GWT.getModuleBaseURL()
						+ "../gwtsl/greet");
				instance = GWT.create(GwtGreetingServiceAsync.class);
				@SuppressWarnings("unchecked")
				LoginableService<GwtGreetingServiceAsync> ls = (LoginableService<GwtGreetingServiceAsync>) instance;
				ls.setRemoteService(rs);
				ls.setHasLoginHandler(OpenIdSelector.getInstance(GWT
						.getModuleBaseURL()
						+ "../gwtsl/admin/gwt.openid.verify"));
			}
			return instance;
		}

		private Util() {
			// Utility class should not be instanciated
		}
	}
}
