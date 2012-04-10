package com.gwt.ss.demo4.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwt.ss.client.loginable.HasLoginHandler;
import com.gwt.ss.client.loginable.LoginEvent;
import com.gwt.ss.client.loginable.LoginHandler;

/**
 *
 * @author Kent Yeh
 */
public class OpenIdSelector extends SimplePanel implements HasLoginHandler, ResizeHandler {

    private static Logger logger = Logger.getLogger("OpenIdSelector");
    private final String EMPTY_PAGE = "about:blank";
    private String entry = EMPTY_PAGE;
    private static OpenIdSelector me = null;
    private HandlerRegistration handlerReg = null;
    private static String UnlockMessage = "Unlock Screen to terminate login process.";
    private Anchor ul;

    private OpenIdSelector() {
        super();
        getElement().setClassName("gwt-openid-mask");
        getElement().setAttribute("style", "background-color: black;filter: alpha(opacity=50);opacity: .50;"+
            "text-align:center;vertical-align:middle;");
        registerJSMethod();
        ul = new Anchor(UnlockMessage);
        ul.getElement().setAttribute("style", "color:white;font-weight:bold;font-size:20pt");
        ul.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get().remove(me);
                OpenIdSelector.loginAction(false);
            }
        });
        add(ul);
    }

    public static OpenIdSelector getInstance(String entry) {
        if (me == null) {
            me = new OpenIdSelector();
        }
        me.setEntry(entry);
        return me;
    }
    
    public static OpenIdSelector getInstance() {
        if (me == null) {
            me = new OpenIdSelector();
        }
        return me;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public native void registerJSMethod() /*-{
        if(!$wnd.loginResult){
            $wnd.loginResult = function(success){
                @com.gwt.ss.demo4.client.OpenIdSelector::loginAction(Z)(success);
            }
        }
    }-*/;

    public static String getUnlockMessage() {
        return UnlockMessage;
    }

    public static void setUnlockMessage(String unlockMessage) {
        UnlockMessage = unlockMessage;
    }

    @Override
    public void startLogin(Throwable caught) {
        if (this.handlerReg != null) {
            RootPanel.get().add(this);
            Element elem = getElement();
            DOM.setStyleAttribute(elem, "left", "0");
            DOM.setStyleAttribute(elem, "top", "0");
            DOM.setStyleAttribute(elem, "position", "absolute");
            onResize(null);
            this.addLoginHandler(new LoginHandler() {

                @Override
                public void setLoginHandlerRegistration(HandlerRegistration hr) {
                }

                @Override
                public void onLogin(LoginEvent e) {
                    HandlerRegistration hr = OpenIdSelector.this.handlerReg;
                    OpenIdSelector.this.handlerReg = null;
                    hr.removeHandler();
                    OpenIdSelector.this.handlerReg = null;
                    RootPanel.get().remove(me);

                }
            });
            logger.log(Level.INFO, "entry is "+ entry);
            GWT.log("entry is " + entry);
            Window.open(entry, "gwtOpenIdLogin", "status=0,menubar=0,toolbar=0,toolbar=0");
            logger.log(Level.INFO, "open "+ entry);
            GWT.log("open " + entry);
        }
    }

    @Override
    public HandlerRegistration addLoginHandler(LoginHandler handler) {
        this.handlerReg = super.addHandler(handler, LoginEvent.getType());
        return this.handlerReg;
    }

    public static void loginAction(boolean success) {
        if(OpenIdSelector.me != null){
            me.fireEvent(new LoginEvent(success));
        }
    }

    @Override
    public void onResize(ResizeEvent event) {
        setSize(Window.getClientWidth() + "px",
                Window.getClientHeight() + "px");
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            ul.setHTML(UnlockMessage);
        }
        super.setVisible(visible);
    }
}
