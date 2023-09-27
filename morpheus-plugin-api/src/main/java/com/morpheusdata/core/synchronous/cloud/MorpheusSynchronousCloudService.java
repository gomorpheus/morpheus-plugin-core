package com.morpheusdata.core.synchronous.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.projection.CloudIdentityProjection;

public interface MorpheusSynchronousCloudService extends MorpheusSynchronousDataService<Cloud,CloudIdentityProjection>, MorpheusSynchronousIdentityService<CloudIdentityProjection> {
	MorpheusSynchronousCloudPoolService getPool();

	MorpheusSynchronousCloudFolderService getFolder();

	MorpheusSynchronousCloudRegionService getRegion();

	MorpheusSynchronousDatastoreService getDatastore();

	MorpheusSynchronousNetworkService getNetwork();

	MorpheusSynchronousAccountResourceService getResource();

	MorpheusSynchronousCloudTypeService getType();
}
