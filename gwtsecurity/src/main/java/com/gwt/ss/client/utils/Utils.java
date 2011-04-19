package com.gwt.ss.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * User: Maxim S. Ivanov
 * Date: 4/19/11
 * Time: 11:34 AM
 */
public class Utils {
  public static void setServiceEntryPoint(Object serviceInstance, String serviceUrl){
    ServiceDefTarget target = (ServiceDefTarget) serviceInstance;
    String hpbl = GWT.getHostPageBaseURL();
    int idx = hpbl.indexOf("//");
    String hostContextPath = hpbl.substring(0, idx + 2);
    hpbl = hpbl.substring(idx + 2);
    idx = hpbl.indexOf("/");
    if (idx > -1) {
        hostContextPath += hpbl.substring(0, idx + 1);
        hpbl = hpbl.substring(idx + 1);
        idx = hpbl.indexOf("/");
        if (idx > -1) {
            hostContextPath += hpbl.substring(0, idx);
        } else {
            hostContextPath += hpbl;
        }
    } else {
        hostContextPath += hpbl;
    }

    if (serviceUrl.startsWith("/")) {
        serviceUrl = hostContextPath + serviceUrl;
    } else if (serviceUrl.toLowerCase().startsWith("http://") || serviceUrl.toLowerCase().startsWith("https://")) {
        //do nothing
    } else {
        serviceUrl = hostContextPath + "/" + serviceUrl;
    }
    target.setServiceEntryPoint(serviceUrl);
    GWT.log("Entry point : " + target.getServiceEntryPoint() + " defined as service entry point for class: " + serviceInstance.getClass().getName());
  }

  public static boolean isEmptyString(String str){
    return str == null && "".equals(str);
  }
}
