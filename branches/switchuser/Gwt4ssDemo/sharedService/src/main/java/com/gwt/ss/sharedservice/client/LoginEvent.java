package com.gwt.ss.sharedservice.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
/**
 * Trigger when ${@link LoginBox LoginBox} finished.
 * means login success or user discard login.
 */
public class LoginEvent extends GwtEvent<LoginEvent.LoginHandler> {
    private boolean canceled = false;
    public static interface LoginHandler extends EventHandler {
        void setHandlerRegistration(HandlerRegistration hr);
        void onLoginResult(LoginEvent e);
    }
    private static Type<LoginHandler> type = new Type<LoginHandler>();

    public LoginEvent() {
        this(false);
    }

    public LoginEvent(boolean isCancel) {
        super();
        this.canceled = isCancel;
    }
    /**
     * @return true means login success,otherwise user discard login process.
     */
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    protected void dispatch(LoginHandler handler) {
        handler.onLoginResult(this);
    }

    @Override
    public Type<LoginHandler> getAssociatedType() {
        return type;
    }

    public static Type<LoginHandler> getType() {
        return type;
    }
}
