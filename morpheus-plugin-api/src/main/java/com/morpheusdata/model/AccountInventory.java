package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.AccountInventoryIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * Represents ansible inventory files. This also represents synced inventories from external integrations such as
 * Ansible Tower
 *
 * @author David Estes
 * @since 0.8.0
 */
public class AccountInventory extends AccountInventoryIdentityProjection {


	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String name;
	protected String description;
	protected String refType;
	protected Long refId;
	protected AccountInventoryType type;

	/**
	 * Gets the name of the inventory file for display
	 * @return the current inventory name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the inventory for reference and display
	 * @param name the name to be assigned to the inventory
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the owner of the inventory. This {@link Account} is the account that owns the integration this inventory belongs to
	 * typically. However, inventory access can typically be cross tenant if the integration is shared or resource permissions set the
	 * tenant default
	 * @return the owner of the inventory
	 */
	public Account getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of the inventory. This is typically only done in a sync.
	 * @param owner the owner of the inventory
	 */
	public void setOwner(Account owner) {
		this.owner = owner;
	}

	/**
	 * Gets the more detailed description of the inventory file. users may want to set this in tower to better describe
	 * the purpose of the inventory file.
	 * @return the description information
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the inventory file typically during a sync operation. This provides more detailed information
	 * about the inventory file.
	 * @param description the description string to be saved
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the polymorphic reference association type of the object. In the case of an inventory file this can often be
	 * the ansible tower integration aka `AccountIntegration`
	 * @return the ref type model
	 */
	public String getRefType() {
		return refType;
	}


	/**
	 * Sets the polymorphic reference assocation type of the object. This is often paired with refId to figure out
	 * what this object relates to.
	 * @param refType the ref type of the object associated with the inventory (typically AccountIntegration)
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * Gets the reference id of the object this belongs to. Pair this with the refType to figure out the association.
	 * @return the unique reference id this belongs to
	 */
	public Long getRefId() {
		return refId;
	}

	/**
	 * Sets the reference id this inventory is associated with
	 * @param refId the id of the ref type object this belongs to.
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public AccountInventoryType getType() {
		return type;
	}

	public void setType(AccountInventoryType type) {
		this.type = type;
	}
}
