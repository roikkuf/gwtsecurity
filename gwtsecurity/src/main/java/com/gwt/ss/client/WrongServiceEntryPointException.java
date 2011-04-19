package com.gwt.ss.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Maxim S. Ivanov
 * Date: 4/19/11
 * Time: 12:28 PM
 */
public class WrongServiceEntryPointException extends IllegalArgumentException implements IsSerializable{
  private static final long serialVersionUID = -5709863966756653096L;

  public WrongServiceEntryPointException() {
    super("Url provided for setting as entry point for service can't be NULL or EMPTY string.");
  }
}
