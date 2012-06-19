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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwt.ss.client.exceptions.GwtAccessDeniedException;
import com.gwt.ss.client.exceptions.GwtAccountExpiredException;
import com.gwt.ss.client.exceptions.GwtAccountStatusException;
import com.gwt.ss.client.exceptions.GwtAuthenticationException;
import com.gwt.ss.client.exceptions.GwtBadCredentialsException;
import com.gwt.ss.client.exceptions.GwtCookieTheftException;
import com.gwt.ss.client.exceptions.GwtCredentialsExpiredException;
import com.gwt.ss.client.exceptions.GwtDisabledException;
import com.gwt.ss.client.exceptions.GwtLockedException;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.client.exceptions.GwtSessionAuthenticationException;
import com.gwt.ss.client.exceptions.GwtUsernameNotFoundException;
import com.gwt.ss.shared.GwtConst;

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

    /**
     * createGwtException.
     * 
     * @param failure the failure.
     * @return the exception.
     */
    private Throwable createGwtException(final ServerFailure failure) {
        String type = failure.getExceptionType();
        if (type != null) {
            String msg = failure.getMessage();
            if (type.startsWith(GwtAccessDeniedException.class.getName())) {
                // Access is denied.
                return new GwtAccessDeniedException(msg);
            }
            if (type.startsWith(GwtAccountExpiredException.class.getName())) {
                // Account has expired.
                return new GwtAccountExpiredException(msg);
            }
            if (type.startsWith(GwtAccountStatusException.class.getName())) {
                // Account status problem.
                return new GwtAccountStatusException(msg);
            }
            if (type.startsWith(GwtAuthenticationException.class.getName())) {
                // General authentication error.
                return new GwtAuthenticationException(msg);
            }
            if (type.startsWith(GwtBadCredentialsException.class.getName())) {
                // Bad credentials.
                return new GwtBadCredentialsException(msg);
            }
            if (type.startsWith(GwtCookieTheftException.class.getName())) {
                // Possible cookie theft.
                return new GwtCookieTheftException(msg);
            }
            if (type.startsWith(GwtCredentialsExpiredException.class.getName())) {
                // Credential have expired.
                return new GwtCredentialsExpiredException(msg);
            }
            if (type.startsWith(GwtDisabledException.class.getName())) {
                // Account is disabled.
                return new GwtDisabledException(msg);
            }
            if (type.startsWith(GwtLockedException.class.getName())) {
                // Account is locked.
                return new GwtLockedException(msg);
            }
            if (type.startsWith(GwtSecurityException.class.getName())) {
                // General security problem.
                return new GwtSecurityException(msg);
            }
            if (type.startsWith(GwtSessionAuthenticationException.class.getName())) {
                // Session authentication.
                return new GwtSessionAuthenticationException(msg);
            }
            if (type.startsWith(GwtUsernameNotFoundException.class.getName())) {
                // Username not found.
                return new GwtUsernameNotFoundException(msg);
            }
        }
        return null;
    }

    @Override
    public void send(final String payload, final TransportReceiver receiver) {
        RequestBuilder builder = createRequestBuilder();
        builder.setHeader(GwtConst.GWT_RF_HEADER, "true");
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
                    if (text.startsWith("//EX")) {
                        // This is an rpc response payload.
                        JSONValue respValue = JSONParser.parseStrict(text.substring(4));
                        JSONArray respArr = respValue.isArray().get(2).isArray();
                        String exType = respArr.get(0).isString().stringValue();
                        String exMsg = respArr.get(1).isString().stringValue();
                        ServerFailure failure = new ServerFailure(exMsg, exType, null, false);
                        Throwable caught = createGwtException(failure);
                        if (caught == null || loginHandler == null || !(caught instanceof GwtSecurityException)) {
                            receiver.onTransportFailure(failure);
                        } else {
                            LoginHandler lh = new LoginHandler() {

                                private HandlerRegistration hr;

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

                                @Override
                                public void setLoginHandlerRegistration(final HandlerRegistration hr) {
                                    this.hr = hr;
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

}
