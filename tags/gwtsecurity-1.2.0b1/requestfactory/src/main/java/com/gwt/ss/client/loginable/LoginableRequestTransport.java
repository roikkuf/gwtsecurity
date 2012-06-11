/**
 * $Id$
 * Copyright (c) 2012 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.client.loginable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.impl.MessageFactoryHolder;
import com.google.web.bindery.requestfactory.shared.messages.ServerFailureMessage;
import com.gwt.ss.client.exceptions.GwtAccessDeniedException;
import com.gwt.ss.client.exceptions.GwtAccountExpiredException;
import com.gwt.ss.client.exceptions.GwtAccountStatusException;
import com.gwt.ss.client.exceptions.GwtAuthenticationException;
import com.gwt.ss.client.exceptions.GwtBadCredentialsException;
import com.gwt.ss.client.exceptions.GwtCredentialsExpiredException;
import com.gwt.ss.client.exceptions.GwtDisabledException;
import com.gwt.ss.client.exceptions.GwtLockedException;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.client.exceptions.GwtSessionAuthenticationException;
import com.gwt.ss.client.exceptions.GwtUsernameNotFoundException;

/**
 * @version $Rev$
 * @author Steven Jardine
 */
public class LoginableRequestTransport extends DefaultRequestTransport {

    private final HasLoginHandler loginHandler;

    /**
     * Constructor.
     * 
     * @param loginHandler the login handler.
     */
    public LoginableRequestTransport(final HasLoginHandler loginHandler) {
        super();
        this.loginHandler = loginHandler;
    }

    @Override
    public void send(final String payload, final TransportReceiver receiver) {
        RequestBuilder builder = createRequestBuilder();
        configureRequestBuilder(builder);
        builder.setRequestData(payload);
        builder.setCallback(new RequestCallback() {

            @Override
            public void onError(final Request request, final Throwable exception) {
                receiver.onTransportFailure(new ServerFailure(exception.getMessage()));
            }

            @Override
            public void onResponseReceived(final Request request, final Response response) {
                if (Response.SC_OK == response.getStatusCode()) {
                    String text = response.getText();
                    if (text.indexOf("com.gwt.ss.client.exceptions") != -1) {
                        ServerFailureMessage msg = AutoBeanCodex.decode(MessageFactoryHolder.FACTORY,
                            ServerFailureMessage.class, StringQuoter.split(text).get("I").get(0)).as();
                        ServerFailure failure = new ServerFailure(msg.getMessage(), msg.getExceptionType(), msg
                            .getStackTrace(), msg.isFatal());
                        Throwable caught = createGwtException(failure);
                        if (caught == null || loginHandler == null || !(caught instanceof GwtSecurityException)) {
                            receiver.onTransportFailure(failure);
                        } else {
                            LoginHandler lh = new LoginHandler() {

                                private HandlerRegistration hr;

                                @Override
                                public void setLoginHandlerRegistration(final HandlerRegistration hr) {
                                    this.hr = hr;
                                }

                                @Override
                                public void onLogin(final LoginEvent e) {
                                    if (hr != null) {
                                        hr.removeHandler();
                                    }
                                    GWT.log("Receive login " + (e.isCanceled() ? "cancel" : "succeed") + " event!");
                                    if (e.isCanceled()) {
                                        receiver.onTransportFailure(new ServerFailure("User cancelled login process.",
                                            LoginCancelException.class.getName(), null, false));
                                    } else {
                                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                            @Override
                                            public void execute() {
                                                send(payload, receiver);
                                            }
                                        });
                                    }
                                }
                            };
                            lh.setLoginHandlerRegistration(loginHandler.addLoginHandler(lh));
                            loginHandler.startLogin(caught);
                        }
                    } else {
                        receiver.onTransportSuccess(text);
                    }
                } else {
                    String message = response.getStatusCode() + " " + response.getText();
                    receiver.onTransportFailure(new ServerFailure(message));
                }
            }
        });

        try {
            builder.send();
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * createGwtException.
     * 
     * @param failure the failure.
     * @return the exception.
     */
    private Throwable createGwtException(final ServerFailure failure) {
        // CHECKSTYLE:OFF
        String type = failure.getExceptionType();
        if (type != null) {
            String msg = failure.getMessage();
            if (type.equals(GwtAccessDeniedException.class.getName())) { return new GwtAccessDeniedException(msg); }
            if (type.equals(GwtAccountExpiredException.class.getName())) { return new GwtAccountExpiredException(msg); }
            if (type.equals(GwtAccountStatusException.class.getName())) { return new GwtAccountStatusException(msg); }
            if (type.equals(GwtAuthenticationException.class.getName())) { return new GwtAuthenticationException(msg); }
            if (type.equals(GwtBadCredentialsException.class.getName())) { return new GwtBadCredentialsException(msg); }
            if (type.equals(GwtCredentialsExpiredException.class.getName())) { return new GwtCredentialsExpiredException(
                msg); }
            if (type.equals(GwtDisabledException.class.getName())) { return new GwtDisabledException(msg); }
            if (type.equals(GwtLockedException.class.getName())) { return new GwtLockedException(msg); }
            if (type.equals(GwtSecurityException.class.getName())) { return new GwtSecurityException(msg); }
            if (type.equals(GwtSessionAuthenticationException.class.getName())) { return new GwtSessionAuthenticationException(
                msg); }
            if (type.equals(GwtUsernameNotFoundException.class.getName())) { return new GwtUsernameNotFoundException(
                msg); }
        }
        return null;
        // CHECKSTYLE:OFF
    }

}
