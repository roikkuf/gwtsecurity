package com.gwt.ss.sharedservice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwt.ss.client.GwtLogoutAsync;
import com.gwt.ss.client.loginable.LoginCancelException;

public abstract class DefaultEntry implements EntryPoint {

    private final Messages messages = Messages.Util.getInstance();
    private RemoteAsync staffService;
    private RemoteAsync greetingService;

    protected RemoteAsync getGreetingService() {
        return greetingService;
    }

    protected void setGreetingService(RemoteAsync greetingService) {
        this.greetingService = greetingService;
    }

    protected RemoteAsync getStaffService() {
        return staffService;
    }

    protected void setStaffService(RemoteAsync staffService) {
        this.staffService = staffService;
    }

    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else {
            return false;
        }
    }

	@Override
    @SuppressWarnings("serial")
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
                    callGg.setEnabled(false);  
                    getGreetingService().greetServer(nameField.getValue(), new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof LoginCancelException) {
                                GWT.log("LoginCancelException occur"+caught.getMessage());
                            } else {
                                Window.alert("Greeting Service.greetServer error:" + caught.getMessage());
                            }
                            callGg.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(String result) {
                            Window.alert("Greeting Service.greetServer:" + result);
                            callGg.setEnabled(true);
                        }
                    });
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
                	  callGw.setEnabled(false);
                    getGreetingService().whisperServer(nameField.getValue(), new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof LoginCancelException) {
                                GWT.log("LoginCancelException occur"+caught.getMessage());
                            } else {
                                Window.alert("Greeting Service.whisperServer error:" + caught.getMessage());
                            }
                            callGw.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(String result) {
                            Window.alert("Greeting Service.whisperServer:" + result);
                            callGw.setEnabled(true);
                        }
                    });
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
                    callSg.setEnabled(false);
                    getStaffService().greetServer(nameField.getValue(), new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof LoginCancelException) {
                                GWT.log("LoginCancelException occur"+caught.getMessage());
                            } else {
                                Window.alert("Greeting Service.greetServer error:" + caught.getMessage());
                            }
                            callSg.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(String result) {
                            Window.alert("Greeting Service.greetServer:" + result);
                            callSg.setEnabled(true);
                        }
                    });
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
                    callSw.setEnabled(false);
                    getStaffService().whisperServer(nameField.getValue(), new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof LoginCancelException) {
                                GWT.log("LoginCancelException occur"+caught.getMessage());
                            } else {
                                Window.alert("Greeting Service.whisperServer error:" + caught.getMessage());
                            }
                            callSw.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(String result) {
                            Window.alert("Greeting Service.whisperServer:" + result);
                            callSw.setEnabled(true);
                        }
                    });
                }

            }
        });

        final Button callLogout = new Button(messages.logout());
        outer.setWidget(7, 0, new Label(messages.logoutIflogin()));
        outer.setWidget(7, 1, callLogout);
        callLogout.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                GwtLogoutAsync logoutService = GwtLogoutAsync.Util.getInstance(getLogoutUrl());
                logoutService.j_gwt_security_logout(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(messages.errorProne() + ":" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Window.alert(messages.logoutComplete());
                    }
                });
            }
        });

        RootPanel.get("showarea").add(outer);
        nameField.setFocus(true);
    }

    public abstract String getLoginUrl();

    public abstract String getLogoutUrl();
}
