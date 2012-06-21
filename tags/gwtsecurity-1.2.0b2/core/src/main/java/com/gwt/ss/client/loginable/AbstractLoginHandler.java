/**
 * $Id$
 * Copyright (c) 2012 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Common login request code for
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public abstract class AbstractLoginHandler implements LoginHandler {

    /**
     * Cancelled message text.
     */
    public static final String CANCELLED_MSG = "User cancelled login process.";

    private HandlerRegistration registration;

    /**
     * User cancelled login process.
     */
    public abstract void onCancelled();

    /** {@inheritDoc} */
    @Override
    public void onLogin(final LoginEvent e) {
        if (registration != null) {
            registration.removeHandler();
        }
        GWT.log("Receive login " + (e.isCanceled() ? "cancel" : "succeed") + " event!");
        if (e.isCanceled()) {
            onCancelled();
        } else {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    resendPayload();
                }
            });
        }
    }

    /**
     * Resend the payload for this request.
     */
    public abstract void resendPayload();

    /** {@inheritDoc} */
    @Override
    public void setLoginHandlerRegistration(final HandlerRegistration registration) {
        this.registration = registration;
    }

}
