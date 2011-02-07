package com.gwt.ss.sharedservice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwt.ss.client.GwtAccessDeniedException;
import com.gwt.ss.client.GwtSecurityException;

public abstract class DefaultEntry implements EntryPoint {

    private ServiceAction greetingAction = new ServiceAction(getGreetingServiceAsync(), "Greeting Service");
    private ServiceAction staffAction = new ServiceAction(getStaffServiceAsync(), "Staff Service");
    private final Messages messages = Messages.Util.getInstance();

    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else {
            return false;
        }
    }

    private void doGreeting(final ServiceAction action, final String name, final Button button) {
        GWT.log("start " + action.getName());
        button.setEnabled(false);
        action.doGreeting(name, new AbstractScheduledCommand<String, GwtSecurityException>() {

            @Override
            public void execute(String result) {
                Window.alert(action.getName() + ".greetServer:" + result);
                button.setEnabled(true);
            }

            @Override
            public void onException(GwtSecurityException e) {
                GWT.log(null, e);
                if (e instanceof GwtAccessDeniedException) {
                    Window.alert(e.getMessage());
                    button.setEnabled(true);
                } else {
                    LoginBox loginBox = LoginBox.getLoginBox(getLoginUrl());
                    LoginEvent.LoginHandler loginHandler = new LoginEvent.LoginHandler() {

                        private HandlerRegistration hr;

                        @Override
                        public void setHandlerRegistration(HandlerRegistration hr) {
                            this.hr = hr;
                        }

                        @Override
                        public void onLoginResult(LoginEvent e) {
                            if (hr != null) {
                                hr.removeHandler();
                            }
                            if (!e.isCanceled()) {
                                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                                    @Override
                                    public void execute() {
                                        doGreeting(action, name, button);
                                    }
                                });

                            } else {
                                button.setEnabled(true);
                            }
                        }
                    };
                    final HandlerRegistration hr = loginBox.addLoginHandler(loginHandler);
                    loginHandler.setHandlerRegistration(hr);
                    loginBox.center();
                }
            }
        });
    }

    private void doWhisper(final ServiceAction action, final String name, final Button button) {
        GWT.log("start " + action.getName());
        button.setEnabled(false);
        action.doWhisper(name, new AbstractScheduledCommand<String, GwtSecurityException>() {

            @Override
            public void execute(String result) {
                GWT.log(result);
                Window.alert(action.getName() + ".whisperServer:" + result);
                button.setEnabled(true);
            }

            @Override
            public void onException(GwtSecurityException e) {
                GWT.log("Security Error for", e);

                if (e instanceof GwtAccessDeniedException) {
                    Window.alert(e.getMessage());
                    button.setEnabled(true);
                } else {
                    LoginBox loginBox = LoginBox.getLoginBox(getLoginUrl());
                    LoginEvent.LoginHandler loginHandler = new LoginEvent.LoginHandler() {

                        private HandlerRegistration hr;

                        @Override
                        public void setHandlerRegistration(HandlerRegistration hr) {
                            this.hr = hr;
                        }

                        @Override
                        public void onLoginResult(LoginEvent e) {
                            if (hr != null) {
                                hr.removeHandler();
                            }
                            if (e.isCanceled()) {
                                button.setEnabled(true);
                            } else {
                                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                                    @Override
                                    public void execute() {
                                        GWT.log("do " + action.getName() + "again");
                                        doWhisper(action, name, button);
                                    }
                                });
                            }
                        }
                    };
                    final HandlerRegistration hr = loginBox.addLoginHandler(loginHandler);
                    loginHandler.setHandlerRegistration(hr);
                    loginBox.center();
                }
            }
        });
    }

    @Override
    public void onModuleLoad() {
        FlexTable outer = new FlexTable();
        FlexCellFormatter formatter = outer.getFlexCellFormatter();
        outer.setWidget(0, 0, new Label(messages.enterYourName()));
        final TextBox nameField = new TextBox();
        nameField.setValue(messages.gwtUser());
        outer.setWidget(0, 1, nameField);

        outer.setWidget(1, 0, new HTML(new SafeHtml() {

            @Override
            public String asString() {
                return "<span style=\"color:red;font-weight:bold\">Greeting Service</span>";
            }
        }));
        formatter.setColSpan(1, 0, 2);
        formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);

        final Button callGg = new Button(messages.sendButton());
        outer.setWidget(2, 0, new Label("greetServer method(Not secured)"));
        outer.setWidget(2, 1, callGg);
        callGg.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isEmpty(nameField.getValue())) {
                    nameField.setFocus(true);
                    Window.alert(messages.enterYourName());
                } else {
                    doGreeting(greetingAction, nameField.getValue(), callGg);
                }
            }
        });

        final Button callGw = new Button(messages.sendButton());
        outer.setWidget(3, 0, new Label("whisperServer secured method(for ROLE_ADMIN)"));
        outer.setWidget(3, 1, callGw);
        callGw.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isEmpty(nameField.getValue())) {
                    nameField.setFocus(true);
                    Window.alert(messages.enterYourName());
                } else {
                    doWhisper(greetingAction, nameField.getValue(), callGw);
                }

            }
        });

        outer.setWidget(4, 0, new HTML(new SafeHtml() {

            @Override
            public String asString() {
                return "<span style=\"color:red;font-weight:bold\">Staff Service(Only for ROLE_STAFF)</span>";
            }
        }));
        formatter.setColSpan(4, 0, 2);
        formatter.setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_CENTER);

        final Button callSg = new Button(messages.sendButton());
        outer.setWidget(5, 0, new Label("greetServer method"));
        outer.setWidget(5, 1, callSg);
        callSg.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isEmpty(nameField.getValue())) {
                    nameField.setFocus(true);
                    Window.alert(messages.enterYourName());
                } else {
                    doGreeting(staffAction, nameField.getValue(), callSg);
                }
            }
        });

        final Button callSw = new Button(messages.sendButton());
        outer.setWidget(6, 0, new Label("whisperServer method"));
        outer.setWidget(6, 1, callSw);
        callSw.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isEmpty(nameField.getValue())) {
                    nameField.setFocus(true);
                    Window.alert(messages.enterYourName());
                } else {
                    doWhisper(staffAction, nameField.getValue(), callSw);
                }

            }
        });

        final Button callLogout = new Button(messages.logout());
        outer.setWidget(7, 0, new Label(messages.logoutIflogin()));
        outer.setWidget(7, 1, callLogout);
        callLogout.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(
                        GWT.getHostPageBaseURL() + getLogoutUrl()));
                builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.setCallback(new RequestCallback() {

                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        /*Must return Success when logout success*/
                        if ("Success".equals(response.getText())) {
                            Window.alert(messages.logoutComplete());
                        } else {
                            Window.alert(messages.errorProne() + ":" + response.getStatusText());
                        }
                        callLogout.setEnabled(true);
                    }

                    @Override
                    public void onError(Request request, Throwable exception) {
                        Window.alert(messages.errorProne() + ":" + exception.getMessage());
                        callLogout.setEnabled(true);
                    }
                });
                try {
                    callLogout.setEnabled(false);
                    builder.send();
                } catch (RequestException ex) {
                }
            }
        });

        RootPanel.get("showarea").add(outer);
        nameField.setFocus(true);
    }

    public abstract String getLoginUrl();

    public abstract String getLogoutUrl();

    public abstract RemoteAsync getGreetingServiceAsync();

    public abstract RemoteAsync getStaffServiceAsync();
}
