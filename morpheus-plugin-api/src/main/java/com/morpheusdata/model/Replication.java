/*
 *  Copyright 2024 Morpheus Data, LLC.
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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class Replication extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String name;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected BackupProvider backupProvider;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ReplicationType replicationType;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ReplicationSite replicationSite;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ReplicationGroup replicationGroup;
	protected String replicationSetId;
	protected Boolean active = true;
	protected Boolean enabled = true;
	//execution
	protected Date lastReplication;
	protected Long replicationLag = 0l;
	protected String lastStatus;
	//source
	protected Long containerId;
	protected Long instanceId;
	protected Long computeServerId;
	protected Long containerTypeId;
	protected Long instanceLayoutId;
	protected Long servicePlanId;
	protected Long zoneId;
	protected Long siteId;
	protected Long sourceProviderId;
	//target
	protected Long targetContainerId;
	protected Long targetInstanceId;
	protected Long targetComputeServerId;
	protected Long targetContainerTypeId;
	protected Long targetInstanceLayoutId;
	protected Long targetServicePlanId;
	protected Long targetZoneId;
	protected Long targetSiteId;
	protected Long targetProviderId;
	protected String targetHost;
	protected Integer targetPort;
	protected String targetUsername;
	protected String targetPassword;
	protected String targetName;
	protected String targetCustom;
	protected Boolean targetIncremental;
	//executor
	protected String sshHost;
	protected Integer sshPort;
	protected String sshUsername;
	protected String sshPassword;
	//stats
	protected Long targetSize;
	protected Long backupSize;
	protected String targetPath;
	protected String volumePath;
	protected String targetBucket;
	protected String targetArchive;
	protected Boolean compressed = false;
	//metrics
	protected Long targetLag = 0l;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Long throughput;
	protected Integer iops;
	protected String status;
	protected String subStatus;
	protected Double statusPercent;
	protected String statusMessage;
	protected Date lastFailover;
	protected String priority;
	protected Integer entityCount;
	//general
	protected Long sourceGroupId;
	protected String internalId;
	protected String externalId;
	protected String replicationConfig;
	protected Date dateCreated;
	protected Date lastUpdated;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}
}
