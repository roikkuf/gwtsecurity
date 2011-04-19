package com.gwt.ss.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * User: Maxim S. Ivanov
 * Date: 4/19/11
 * Time: 11:21 AM
 */
public interface GwtLoginAsEnter extends RemoteService {
  public void j_gwt_security_switch_user(String username) throws GwtSecurityException;
}
