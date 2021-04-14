package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * Represents ansible inventory files. This is the identity projection class that is used for syncing inventory.
 *
 * @see com.morpheusdata.model.AccountInventory
 * @author David Estes
 * @since 0.8.0
 */
public class AccountInventoryIdentityProjection extends MorpheusModel {
	protected String externalId;

	/**
	 * Gets the external id associated with this inventory. This is typically the unique id of the inventory on the remote integration
	 * @return the external id
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the external id on the inventory based on the passed id. Typically this would be done in a sync job
	 * @param externalId the external reference id of the inventory file
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
