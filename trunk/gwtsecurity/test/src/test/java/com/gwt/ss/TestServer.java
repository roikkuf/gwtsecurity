/**
 * $Id$
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 */
package com.gwt.ss;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version $Rev$
 * @author Steven Jardine
 */
public class TestServer {

	/**
	 * Throws an exception if there was an error starting the webapp.
	 */
	public static class ThrowyWebAppContext extends WebAppContext {
		@Override
		protected void doStart() throws Exception {
			super.doStart();
			if (getUnavailableException() != null) {
				throw (Exception) getUnavailableException();
			}
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(TestServer.class);

	private Server server;

	private static final int port = 17000;

	public static void main(String[] args) {
		new TestServer();
	}

	/**
     * 
     */
	public TestServer() {
		try {
			LOG.info("Starting Jetty Server.");

			// Server config.
			server = new Server(port);
			server.setAttribute(
					"org.eclipse.jetty.server.Request.maxFormContentSize", -1);
			server.setAttribute("maxFormContentSize", -1);
			server.setAttribute("org.eclipse.jetty.server.Request.maxFormKeys",
					-1);
			server.setAttribute("maxFormKeys", -1);

			// Handlers config.
			HandlerCollection handlers = new HandlerCollection();

			WebAppContext webAppContext = new ThrowyWebAppContext();
			webAppContext.setContextPath("/");
			webAppContext
					.setWar("target/gwtsecurity-test-1.3.1-SNAPSHOT/");
			handlers.addHandler(webAppContext);

			server.setHandler(handlers);
			server.start();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void stop() throws Exception {
		server.stop();
	}

	public void start() throws Exception {
		server.start();
	}

}
