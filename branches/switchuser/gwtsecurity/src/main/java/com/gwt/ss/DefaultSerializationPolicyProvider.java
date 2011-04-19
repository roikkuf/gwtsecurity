package com.gwt.ss;

import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

/**
 * User: Maxim S. Ivanov
 * Date: 4/19/11
 * Time: 12:35 PM
 */
public class DefaultSerializationPolicyProvider implements SerializationPolicyProvider {

  private static DefaultSerializationPolicyProvider instance = new DefaultSerializationPolicyProvider();

  public static DefaultSerializationPolicyProvider getInstance() {
    return instance;
  }

  @Override
  public SerializationPolicy getSerializationPolicy(String moduleBaseURL, String serializationPolicyStrongName) {
    return RPC.getDefaultSerializationPolicy();
  }
}

