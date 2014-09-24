/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss.test.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwt.ss.client.GwtLoginAsync;
import com.gwt.ss.client.GwtLogoutAsync;

/**
 * @author steve
 * 
 */
public class MainPanel extends VerticalPanel {

	class ReturnValueCallback implements AsyncCallback<String> {

		@Override
		public void onFailure(Throwable caught) {
			returnValue = caught;
			returnPanel.add(createReturnValueLabel(returnValue.toString()));
		}

		@Override
		public void onSuccess(String result) {
			returnValue = result;
			returnPanel.add(createReturnValueLabel(returnValue.toString()));
		}

	}

	public static final String ADMIN_LOGIN_BTN_ID = "adminLoginBtn";
	public static final String ADMIN_SECURED_BTN_ID = "adminSecuredBtn";
	public static final String DEBUG_ID = "mainPanelId";
	public static final String LOGOUT_BTN_ID = "logoutBtn";
	public static final String RETURN_VALUE_ID = "returnValueId";
	public static final String UNRESTRICTED_BTN_ID = "unrestrictedBtn";
	public static final String USER_LOGIN_BTN_ID = "userLoginBtn";
	public static final String USER_SECURED_BTN_ID = "userSecuredBtn";

	private static Label createReturnValueLabel(String msg) {
		Label label = new Label(msg);
		label.ensureDebugId(RETURN_VALUE_ID);
		return label;
	}

	private GwtLoginAsync loginSvc = GwtLoginAsync.Util.getInstance();
	private GwtLogoutAsync logoutSvc = GwtLogoutAsync.Util.getInstance();
	private MainServiceAsync mainSvc = GWT.create(MainServiceAsync.class);

	private final FlowPanel returnPanel;

	public Object returnValue = null;

	/**
	 * 
	 */
	public MainPanel() {
		super();
		ensureDebugId(DEBUG_ID);
		getElement().getStyle().setProperty("margin", "auto");
		getElement().getStyle().setMarginTop(25, Unit.PX);
		setSpacing(5);

		// Title
		Label title = new Label("GWTSecurity Test Application");
		Style style = title.getElement().getStyle();
		style.setFontSize(20, Unit.PX);
		style.setPaddingBottom(20, Unit.PX);
		style.setTextDecoration(TextDecoration.UNDERLINE);
		style.setColor("blue");
		add(title);
		setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);

		FlexTable table = new FlexTable();
		table.setCellSpacing(5);
		add(table);

		int row = 0;

		// Unrestricted RPC.
		Label label = new Label("Unrestricted RPC (Should always succeed):");
		Button button = new Button("Unrestricted RPC");
		button.ensureDebugId(UNRESTRICTED_BTN_ID);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				returnPanel.clear();
				mainSvc.unrestricted(new ReturnValueCallback());
			}
		});

		button.setWidth("150px");
		table.setWidget(row, 0, label);
		table.setWidget(row, 1, button);
		row++;

		// User Secured RPC.
		label = new Label(
				"User Secured RPC (Should only succeed if admin or user is logged in):");
		button = new Button("User Secured RPC");
		button.ensureDebugId(USER_SECURED_BTN_ID);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				returnPanel.clear();
				mainSvc.userSecured(new ReturnValueCallback());
			}
		});
		button.setWidth("150px");
		table.setWidget(row, 0, label);
		table.setWidget(row, 1, button);
		row++;

		// Admin Secured RPC.
		label = new Label(
				"Admin Secured RPC (Should only succeed if admin is logged in):");
		button = new Button("Admin Secured RPC");
		button.ensureDebugId(ADMIN_SECURED_BTN_ID);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				returnPanel.clear();
				mainSvc.adminSecured(new ReturnValueCallback());
			}
		});
		button.setWidth("150px");
		table.setWidget(row, 0, label);
		table.setWidget(row, 1, button);
		row++;

		// User Login Btn
		button = new Button("User Login");
		button.ensureDebugId(USER_LOGIN_BTN_ID);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				returnPanel.clear();
				loginSvc.j_gwt_security_check("user", "user", false,
						new AsyncCallback<Void>() {
							ReturnValueCallback callback = new ReturnValueCallback();

							@Override
							public void onFailure(Throwable caught) {
								callback.onFailure(caught);
							}

							@Override
							public void onSuccess(Void result) {
								callback.onSuccess("UserLoginSuccess");
							}

						});
			}
		});
		button.setWidth("150px");
		table.setWidget(row, 1, button);
		row++;

		// Admin Login Btn
		button = new Button("Admin Login");
		button.ensureDebugId(ADMIN_LOGIN_BTN_ID);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				returnPanel.clear();
				loginSvc.j_gwt_security_check("admin", "admin", false,
						new AsyncCallback<Void>() {
							ReturnValueCallback callback = new ReturnValueCallback();

							@Override
							public void onFailure(Throwable caught) {
								callback.onFailure(caught);
							}

							@Override
							public void onSuccess(Void result) {
								callback.onSuccess("AdminLoginSuccess");
							}

						});
			}
		});
		button.setWidth("150px");
		table.setWidget(row, 1, button);
		row++;

		// Logout Btn
		button = new Button("Logout");
		button.ensureDebugId(LOGOUT_BTN_ID);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				returnPanel.clear();
				logoutSvc.j_gwt_security_logout(new AsyncCallback<Void>() {
					ReturnValueCallback callback = new ReturnValueCallback();

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(Void result) {
						callback.onSuccess("LogoutSuccess");
					}

				});
			}
		});
		button.setWidth("150px");
		table.setWidget(row, 1, button);
		row++;

		label = new Label("Return Value");
		style = label.getElement().getStyle();
		style.setFontSize(16, Unit.PX);
		style.setPadding(20, Unit.PX);
		style.setTextDecoration(TextDecoration.UNDERLINE);
		add(label);
		setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);

		returnPanel = new FlowPanel();
		returnPanel.setSize("350px", "40px");
		returnPanel.getElement().getStyle()
				.setProperty("border", "1px solid black");
		add(returnPanel);
		setCellHorizontalAlignment(returnPanel,
				HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * @return the returnValue
	 */
	public Object getReturnValue() {
		return returnValue;
	}

	/**
	 * @param returnValue
	 *            the returnValue to set
	 */
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

}
