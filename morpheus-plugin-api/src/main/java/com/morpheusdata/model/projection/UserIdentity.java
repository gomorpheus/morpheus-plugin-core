package com.morpheusdata.model.projection;

import java.util.Map;

/**
 * Provides an interface for user so identity checks dont' require full marshalling
 * @author bdwheeler
 * @since 0.15.1
 */
public interface UserIdentity {

  /**
   * Gets the uniquely generated ID from the database record stored via the Morpheus appliance.
   * @return id
   */
  Long getId();

  AccountIdentity getAccount();

  String getUsername();

  String getEmail();

  Boolean getEnabled();

  Map<String, String> getPermissions();

}
