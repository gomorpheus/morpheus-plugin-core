package com.morpheusdata.model.projection;

/**
 * Provides an interface for account so identity checks dont' require full marshalling
 * @author bdwheeler
 * @since 0.15.1
 */
public interface AccountIdentity {

  /**
   * Gets the uniquely generated ID from the database record stored via the Morpheus appliance.
   * @return id
   */
  Long getId();
  
  String getName();
  
  Boolean getActive();
  
  Boolean getMasterAccount();

}
