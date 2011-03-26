package com.gwt.ss.sharedservice.client;

import com.google.gwt.core.client.GWT;

public interface Messages extends com.google.gwt.i18n.client.Messages {

    @DefaultMessage("Please enter your name")
    @Key("enterYourName")
    String enterYourName();

    @DefaultMessage("Gwt User")
    @Key("gwtUser")
    String gwtUser();

    @DefaultMessage("Send")
    @Key("send")
    String sendButton();

    @DefaultMessage("Login Required")
    @Key("loginRequired")
    String loginRequired();

    @DefaultMessage("Account")
    @Key("userName")
    String userName();

    @DefaultMessage("Password")
    @Key("password")
    String password();

    @DefaultMessage("Submit")
    @Key("submit")
    String submitButton();

    @DefaultMessage("Cancel")
    @Key("cancel")
    String cancelButton();

    @DefaultMessage("Close")
    @Key("close")
    String closeButton();


    @DefaultMessage("not allowed empty value.")
    @Key("notNullValue")
    String notNullValue();

    @DefaultMessage("Error prone")
    @Key("errorProne")
    String errorProne();

    @DefaultMessage("Logout(if login already)")
    @Key("logoutIflogin")
    String logoutIflogin();

    @DefaultMessage("Logout")
    @Key("logout")
    String logout();


    @DefaultMessage("User logout completed.")
    @Key("logoutComplete")
    String logoutComplete();

    public static final class Util {

        private static Messages instance;

        public static Messages getInstance() {
            if (instance == null) {
                instance = GWT.create(Messages.class);
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
