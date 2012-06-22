/**
 * $Id$
 */
package com.gwt.ss.requestfactory.client.loginable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwt.ss.client.exceptions.GwtSecurityException;
import com.gwt.ss.client.loginable.AbstractLoginHandler;
import com.gwt.ss.client.loginable.HasLoginHandler;
import com.gwt.ss.client.loginable.LoginCancelException;
import com.gwt.ss.client.loginable.LoginHandler;
import com.gwt.ss.shared.GwtConst;

/**
 * Intercepts the requestfactory requests and starts the login process if necessary.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public class LoginableRequestTransport extends DefaultRequestTransport {

    private static final String EXCEPTION_PKG = "com.gwt.ss.client.exceptions";

    private static final String EXCEPTION_PREFIX = "//EX[";

    /**
     * Parse the rpc exception.
     * 
     * @param text the rpc exception to parse.
     * @return an array with the msg in index 0 and type in index 1.
     */
    private static GwtSecurityException parseRpcSecurityException(String payload) {
        String type = null;
        String msg = null;
        if (payload != null && payload.indexOf(EXCEPTION_PREFIX) != -1 && payload.indexOf(EXCEPTION_PKG) != -1) {
            try {
                String value = payload.substring(payload.indexOf("[\"" + EXCEPTION_PKG));
                value = value.substring(2);
                int index= value.indexOf("\",\"");
                type = value.substring(0, index);
                value = value.substring(index + 3);
                msg = value.substring(0, value.indexOf("\"]"));
            } catch (IllegalArgumentException e) {
                GWT.log(e.getMessage(), e);
                return null;
            }
        }
        return ((ExceptionCreator) GWT.create(ExceptionCreator.class)).create(type, msg);
    }

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
        builder.setCallback(new RequestCallback() {
            /** {@inheritDoc} */
            @Override
            public void onError(final Request request, final Throwable exception) {
                receiver.onTransportFailure(new ServerFailure(exception.getMessage()));
            }

            /** {@inheritDoc} */
            @Override
            public void onResponseReceived(final Request request, final Response response) {
                if (Response.SC_OK == response.getStatusCode()) {
                    String responsePayload = response.getText();
                    GwtSecurityException caught = parseRpcSecurityException(responsePayload);
                    if (loginHandler != null && caught != null) {
                        LoginHandler lh = new AbstractLoginHandler() {
                            @Override
                            public void onCancelled() {
                                ServerFailure failure = new ServerFailure(CANCELLED_MSG, LoginCancelException.class
                                    .getName(), null, false);
                                receiver.onTransportFailure(failure);
                            }

                            @Override
                            public void resendPayload() {
                                send(payload, receiver);
                            }
                        };
                        lh.setLoginHandlerRegistration(loginHandler.addLoginHandler(lh));
                        loginHandler.startLogin(caught);
                    } else {
                        receiver.onTransportSuccess(responsePayload);
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
            GWT.log(e.getMessage(), e);
        }
    }

}
