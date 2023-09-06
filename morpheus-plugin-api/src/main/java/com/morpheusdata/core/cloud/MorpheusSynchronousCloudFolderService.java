package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudFolder;
import com.morpheusdata.model.projection.CloudFolderIdentity;

public interface MorpheusSynchronousCloudFolderService  extends MorpheusSynchronousDataService<CloudFolder,CloudFolderIdentity>, MorpheusSynchronousIdentityService<CloudFolderIdentity> {
}
