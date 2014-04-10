/**
 * $Id$
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 */
package com.gwt.ss.test.server;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.gwt.ss.test.client.MainService;

/**
 * @version $Rev$
 * @author Steven Jardine
 */
@Service
public class MainServiceImpl implements MainService {

	/**
	 * Default constructor.
	 */
	public MainServiceImpl() {
		super();
	}

	@Override
	public String unrestricted() {
		return "unrestricted";
	}

	@Override
	@Secured("ROLE_USER")
	public String userSecured() {
		return "userSecured";
	}

	@Override
	@Secured("ROLE_ADMIN")
	public String adminSecured() {
		return "adminSecured";
	}

}
