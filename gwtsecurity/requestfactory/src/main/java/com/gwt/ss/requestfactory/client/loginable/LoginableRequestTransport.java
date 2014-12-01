/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.requestfactory.client.loginable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwt.ss.client.GwtLogin;
import com.gwt.ss.client.exceptions.GwtAccessDeniedException;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.client.loginable.AbstractLoginHandler;
import com.gwt.ss.client.loginable.HasLoginHandler;
import com.gwt.ss.client.loginable.LoginCancelException;
import com.gwt.ss.client.loginable.LoginHandler;
import com.gwt.ss.shared.GwtConst;

/**
 * Intercepts the requestfactory requests and starts the login process if necessary.
 *
 * @author Steven Jardine
 * @version $Rev$
 */
public class LoginableRequestTransport extends DefaultRequestTransport {

    /**
     * The Class LoginableRequestCallback.
     */
    private class LoginableRequestCallback implements RequestCallback {

        /** The payload. */
        private final String payload;

        /** The receiver. */
        private final TransportReceiver receiver;

        /**
         * Instantiates a new {@link LoginableRequestTransport}.
         *
         * @param payload the payload
         * @param receiver the receiver
         */
        public LoginableRequestCallback(final String payload, final TransportReceiver receiver) {
            this.payload = payload;
            this.receiver = receiver;
        }

        /** {@inheritDoc} */
        @Override
        public void onError(final Request request, final Throwable exception) {
            receiver.onTransportFailure(new ServerFailure(exception.getMessage(), exception.getClass().getName(), null,
                false));
        }

        /** {@inheritDoc} */
        @Override
        public void onResponseReceived(final Request request, final Response response) {
            if (Response.SC_OK == response.getStatusCode()) {
                String responsePayload = response.getText();
                GwtSecurityException caught = deserializeSecurityException(responsePayload);
                if (loginHandler != null && caught != null) {
                    if (caught.isAuthenticated() && caught instanceof GwtAccessDeniedException) {
                        onError(request, caught);
                    } else {
                        LoginHandler lh = new AbstractLoginHandler() {
                            @Override
                            public void onCancelled() {
                                onError(request, new LoginCancelException("Login Cancelled"));
                            }

                            @Override
                            public void resendPayload() {
                                send(payload, receiver);
                            }
                        };
                        lh.setLoginHandlerRegistration(loginHandler.addLoginHandler(lh));
                        loginHandler.startLogin(caught);
                    }
                } else {
                    receiver.onTransportSuccess(responsePayload);
                }
            } else {
                String message = response.getStatusCode() + " " + response.getText();
                receiver.onTransportFailure(new ServerFailure(message));
            }
        }
    }

    /** The Constant EXCEPTION_PREFIX. */
    private static final String EXCEPTION_PREFIX = "//EX[";

    /** The stream factory. */
    private static SerializationStreamFactory streamFactory = null;

    /**
     * Deserialize the rpc exception.
     *
     * @param payload the exception payload to deserialize.
     * @return the deserialized security exception.
     */
    private static GwtSecurityException deserializeSecurityException(final String payload) {
        if (payload != null && payload.indexOf(EXCEPTION_PREFIX) != -1) {
            try {
                if (streamFactory == null) {
                    streamFactory = GWT.create(GwtLogin.class);
                }
                return (GwtSecurityException) streamFactory.createStreamReader(payload).readObject();
            } catch (Exception e) {
                GWT.log(e.getMessage(), e);
            }
        }
        return null;
    }

    /** The login handler. */
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

    /** {@inheritDoc} */
    @Override
    public void send(final String payload, final TransportReceiver receiver) {
        RequestBuilder builder = createRequestBuilder();
        builder.setHeader(GwtConst.GWT_RF_HEADER, "true");
        configureRequestBuilder(builder);
        builder.setRequestData(payload);
        builder.setCallback(new LoginableRequestCallback(payload, receiver));
        try {
            builder.send();
        } catch (RequestException e) {
            GWT.log(e.getMessage(), e);
        }
    }

}
