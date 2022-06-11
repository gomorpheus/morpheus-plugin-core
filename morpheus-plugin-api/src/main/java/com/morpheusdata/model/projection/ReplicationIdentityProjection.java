package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.Replication;
import com.morpheusdata.core.backup.MorpheusReplicationService;

/**
 * Provides a subset of properties from the {@link Replication} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusReplicationService
 * @author Dustin DeYoung
 */
public class ReplicationIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;
	protected String typeCode;
	protected Long replicationSiteId;
	protected Long replicationGroupId;
	protected String replicationSetId;

	public ReplicationIdentityProjection() {
	}

	public ReplicationIdentityProjection(Long id, String externalId, String name, String typeCode) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.typeCode = typeCode;
	}

	public ReplicationIdentityProjection(Long id, String externalId, String name, String typeCode, Long replicationSiteId, Long replicationGroupId, String replicationSetId) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.typeCode = typeCode;
		this.replicationSiteId = replicationSiteId;
		this.replicationGroupId = replicationGroupId;
		this.replicationSetId = replicationSetId;
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the backup. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the backup. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the backup
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Backup. Typically this isnt called directly.
	 * @param name the name of the backup to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * The {@link ReplicationType#getCode()} is mapped here in the identity projection for quick filter search.
	 * @return the associated {@link ReplicationType#getCode()}
	 */
	public String getTypeCode() {
		return typeCode;
	}

	public Long getReplicationSiteId() {
		return replicationSiteId;
	}

	public void setReplicationSiteId(Long replicationSiteId) {
		this.replicationSiteId = replicationSiteId;
		markDirty("replicationSiteId", replicationSiteId, this.replicationSiteId);
	}

	public Long getReplicationGroupId() {
		return replicationGroupId;
	}

	public void setReplicationGroupId(Long replicationGroupId) {
		this.replicationGroupId = replicationGroupId;
		markDirty("replicationGroupId", replicationGroupId, this.replicationGroupId);
	}

	public String getReplicationSetId() {
		return replicationSetId;
	}

	public void setReplicationSetId(String replicationSetId) {
		this.replicationSetId = replicationSetId;
		markDirty("replicationSetId", replicationSetId, this.replicationSetId);
	}
}
