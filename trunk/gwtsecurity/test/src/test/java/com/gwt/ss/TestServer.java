/**
 * $Id$
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 */
package com.gwt.ss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

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

	private static final int PORT = 17000;

	private static final String PROP_FILENAME = "gwtsecurity-test.properties";

	private static Properties PROPS = null;

	public static final Properties loadProperties() {
		Properties result = new Properties();
		File propFile = new File(new File(System.getProperty("user.home")),
				PROP_FILENAME);
		try {
			InputStream in = null;
			try {
				if (propFile.exists()) {
					in = new FileInputStream(propFile);
				} else {
					in = TestServer.class.getResourceAsStream("/"
							+ PROP_FILENAME);
				}
				if (in != null) {
					result.load(in);
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	public static void main(String[] args) {
		new TestServer();
	}

	private Server server;

	/**
     * 
     */
	public TestServer() {
		try {
			PROPS = TestServer.loadProperties();
			assert PROPS != null;
			LOG.info("Starting Jetty Server.");
			// Server config.
			server = new Server(Integer.valueOf(PROPS.getProperty(
					"webapp.port", "" + PORT)));

			// Handlers config.
			HandlerCollection handlers = new HandlerCollection();

			WebAppContext webAppContext = new ThrowyWebAppContext();
			webAppContext.setContextPath("/");
			webAppContext.setWar(PROPS.getProperty("webapp.location"));
			handlers.addHandler(webAppContext);

			server.setHandler(handlers);
			server.start();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void start() throws Exception {
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}

}
