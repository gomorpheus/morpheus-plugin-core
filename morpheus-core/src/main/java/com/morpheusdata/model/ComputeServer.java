package com.morpheusdata.model;

/**
 * Representation of a Morpheus ComputeServer database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author David Estes
 */
public class ComputeServer  extends MorpheusModel {

	private String uuid;
	private String name;
	private String displayName;
	private String externalId;
	private String uniqueId;
	private Zone zone;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid",uuid);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name",name);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName",displayName);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId",externalId);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId",uniqueId);
	}

	public Zone getZone() { return zone; }

	public void setZone(Zone zone) {
		this.zone = zone;
		markDirty("zone", zone);
	}





}
