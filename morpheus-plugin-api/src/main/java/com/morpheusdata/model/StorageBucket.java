package com.morpheusdata.model;

import com.morpheusdata.model.projection.DatastoreIdentityProjection;
import com.morpheusdata.model.projection.StorageBucketIdentityProjection;

public class StorageBucket extends StorageBucketIdentityProjection {

	protected	String visibility;
	protected	Boolean active;
	//details
	protected	String category;
	protected	String bucketName;
	protected	String shareType;
	protected	String sharePath;
	protected	String displayPath;
	//karman config
	protected	String providerType;
	protected	String providerCategory;
	//link details
	protected	String refType;
	protected	Long refId;
	protected	String externalId;
	protected	String internalId;
	protected	String exportId;
	protected	String uniqueId;
	protected	String sourceId;
	protected	String poolName;
	protected	String shareUser;
	protected	String sharePermissions;
	//size and availability
	protected	Long maxStorage;
	protected	Long usedStorage;
	protected	Boolean resizeable;
	protected	Boolean removable;
	protected	Boolean enabled;
	protected	Boolean readOnly;
	//usage
	protected	Boolean defaultBackupTarget;
	protected	Boolean defaultDeploymentTarget;
	protected	Boolean defaultVirtualImageTarget;
	//retention
	protected	String retentionPolicyType;
	protected	Integer retentionPolicyDays;
	//related objects
	protected	StorageBucketIdentityProjection retentionProvider;
	protected	DatastoreIdentityProjection datastore;
	
	protected	StorageServer storageServer;
	protected	StorageGroup storageGroup;
	protected	AccountNamespace namespace;
	//status
	protected	String status;
	protected	String statusMessage;
	//timestamps
	protected	Date dateCreated;
	protected	Date lastUpdated;
	protected	Long createdById;		
	protected String createdByName;
	protected	String rawData;
	
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	public Collection<StorageBucketPermission> permissions;

}
