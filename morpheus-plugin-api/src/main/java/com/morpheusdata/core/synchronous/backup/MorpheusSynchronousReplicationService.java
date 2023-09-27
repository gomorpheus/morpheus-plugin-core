package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Replication;
import com.morpheusdata.model.ReplicationGroup;
import com.morpheusdata.model.ReplicationSite;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.projection.ReplicationIdentityProjection;

public interface MorpheusSynchronousReplicationService extends MorpheusSynchronousDataService<Replication, ReplicationIdentityProjection>, MorpheusSynchronousIdentityService<ReplicationIdentityProjection> {

	/**
	 * Returns the MorpheusReplicationGroupContext used for performing updates/queries on {@link ReplicationGroup} related assets
	 * within Morpheus.
	 * @return An instance of the MorpheusReplicationGroupContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationGroupService getReplicationGroup();

	/**
	 * Returns the MorpheusReplicationTypeContext used for performing updates/queries on {@link ReplicationSite} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationSiteService getReplicationSite();

	/**
	 * Returns the MorpheusReplicationTypeContext used for performing updates/queries on {@link ReplicationType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationTypeService getType();
}
