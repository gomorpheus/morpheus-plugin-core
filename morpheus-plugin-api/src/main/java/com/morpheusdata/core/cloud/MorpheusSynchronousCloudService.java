package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.projection.CloudIdentityProjection;

public interface MorpheusSynchronousCloudService extends MorpheusSynchronousDataService<Cloud,CloudIdentityProjection>, MorpheusSynchronousIdentityService<CloudIdentityProjection> {
	MorpheusSynchronousCloudPoolService getPool();

	MorpheusSynchronousCloudFolderService getFolder();

	MorpheusSynchronousCloudRegionService getRegion();

	MorpheusSynchronousDatastoreService getDatastore();

	//TODO: Port Network Services to synchronous too
//	MorpheusSynchronousNetworkService getNetwork();

	MorpheusSynchronousAccountResourceService getResource();
}
