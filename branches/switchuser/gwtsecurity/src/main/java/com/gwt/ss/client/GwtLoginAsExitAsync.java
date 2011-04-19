package com.gwt.ss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.ss.client.utils.Utils;

public interface GwtLoginAsExitAsync {
  void j_spring_security_exit_user(AsyncCallback<Void> async);

  public static final class Util {
    private static GwtLoginAsExitAsync instance;
    private static String processUrl = "j_spring_security_exit_user";

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

    public static GwtLoginAsExitAsync getInstance(String processUrl) {
      setProcessUrl(processUrl);
      return getInstance();
    }

    public static GwtLoginAsExitAsync getInstance() {
      if (instance == null) {
        instance = (GwtLoginAsExitAsync) GWT.create(GwtLoginAsExit.class);
        Utils.setServiceEntryPoint(instance, getProcessUrl());
      }
      return instance;
    }

    private Util() {
      // Utility class
    }
  }

}
