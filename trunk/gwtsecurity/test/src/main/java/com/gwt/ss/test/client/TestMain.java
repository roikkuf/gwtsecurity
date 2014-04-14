/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @version $Rev$
 * @author Steven Jardine
 */
public class TestMain implements EntryPoint {

	/** {@inheritDoc} */
	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new MainPanel());
	}

}
