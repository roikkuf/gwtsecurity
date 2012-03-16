package com.gwt.ss.client.loginable;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Abstract dialogBox for {@link HasLoginHandler HasLoginHandler}.
 * @author Kent Yeh
 */
public abstract class AbstractLoginBox extends DialogBox implements HasLoginHandler {

    public AbstractLoginBox(boolean autoHide, boolean modal) {
        super(autoHide, modal);
    }

    public AbstractLoginBox(boolean autoHide) {
        super(autoHide);
    }

    public AbstractLoginBox() {
    }

    @Override
    public HandlerRegistration addLoginHandler(LoginHandler handler) {
        return super.addHandler(handler, LoginEvent.getType());
    }

}
