/*
 *  Copyright 2026 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.model;

/**
 * Represents a named QoS/performance storage tier (e.g. Silver, Gold, Platinum) exposed
 * by a storage plugin to Morpheus.
 * @author Morpheus Data
 */
public class StoragePolicy extends MorpheusModel {

	protected String code;
	protected String name;
	protected Integer displayOrder;
	protected Boolean enabled;
	protected String provisionTypeCode;
	protected String refType;
	protected String refId;

	/**
	 * Gets the stable per-tier code for this storage policy.
	 * @return the current code of the storage policy
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the stable per-tier code for this storage policy.
	 * @param code the code of the storage policy to be assigned.
	 */
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	/**
	 * Gets the display name of this storage policy.
	 * @return the current name of the storage policy
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the display name of this storage policy.
	 * @param name the name of the storage policy to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Gets the display order used for consistent presentation ordering.
	 * @return the current display order of the storage policy
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * Sets the display order used for consistent presentation ordering.
	 * @param displayOrder the display order of the storage policy to be assigned.
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	/**
	 * Gets whether this storage policy is enabled.
	 * @return the current enabled flag of the storage policy
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Sets whether this storage policy is enabled.
	 * @param enabled the enabled flag of the storage policy to be assigned.
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	/**
	 * Gets the stable code of the target provisioning technology this tier applies to.
	 * @return the current provision type code of the storage policy
	 */
	public String getProvisionTypeCode() {
		return provisionTypeCode;
	}

	/**
	 * Sets the stable code of the target provisioning technology this tier applies to.
	 * @param provisionTypeCode the provision type code of the storage policy to be assigned.
	 */
	public void setProvisionTypeCode(String provisionTypeCode) {
		this.provisionTypeCode = provisionTypeCode;
		markDirty("provisionTypeCode", provisionTypeCode);
	}

	/**
	 * Gets the type of the entity that owns (published) this storage policy, e.g.
	 * {@code 'StorageServer'}. Together with {@link #getRefId()} this scopes the policy to its
	 * producer, so a plugin can reconcile only the records it published without disturbing
	 * records seeded by Morpheus itself or published by another plugin. A {@code null}
	 * {@code refType}/{@code refId} pair denotes a Morpheus-owned (seeded) catalog record that
	 * no plugin owns.
	 * @return the current owning entity type of the storage policy
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * Sets the type of the entity that owns (published) this storage policy.
	 * @param refType the owning entity type of the storage policy to be assigned.
	 */
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	/**
	 * Gets the identifier of the entity that owns (published) this storage policy, scoped by
	 * {@link #getRefType()}. See {@link #getRefType()} for how the pair is used.
	 * @return the current owning entity identifier of the storage policy
	 */
	public String getRefId() {
		return refId;
	}

	/**
	 * Sets the identifier of the entity that owns (published) this storage policy.
	 * @param refId the owning entity identifier of the storage policy to be assigned.
	 */
	public void setRefId(String refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

}
