package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.ss.client.utils.Utils;

public interface GwtLoginAsEnterAsync {
  void j_gwt_security_switch_user(String username, AsyncCallback<Void> async);

  public static final class Util {
    private static GwtLoginAsEnterAsync instance;
    private static String processUrl = "j_spring_security_switch_user";

    public static String getProcessUrl() {
      return processUrl;
    }

    public static void setProcessUrl(String processUrl) {
      if (Utils.isEmptyString(processUrl)) {
        throw new WrongServiceEntryPointException();
      }
      if (!Util.processUrl.equals(processUrl)) {
        Util.processUrl = processUrl;
        if (instance != null) {
          Utils.setServiceEntryPoint(instance, processUrl);
        }
      }
    }

    public static GwtLoginAsEnterAsync getInstance(String processUrl) {
      setProcessUrl(processUrl);
      return getInstance();
    }

    public static GwtLoginAsEnterAsync getInstance() {
      if (instance == null) {
        instance = (GwtLoginAsEnterAsync) GWT.create(GwtLoginAsEnter.class);
        Utils.setServiceEntryPoint(instance, getProcessUrl());
      }
      return instance;
    }

    private Util() {
      // Utility class
    }
  }
}
