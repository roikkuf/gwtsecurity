package com.gwt.ss.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * User: Maxim S. Ivanov
 * Date: 4/19/11
 * Time: 11:25 AM
 */
public interface GwtLoginAsExit extends RemoteService {
  public void j_spring_security_exit_user() throws GwtSecurityException;
}
