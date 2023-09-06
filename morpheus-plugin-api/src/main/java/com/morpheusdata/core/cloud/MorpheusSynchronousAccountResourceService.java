package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.AccountResource;
import com.morpheusdata.model.CloudFolder;
import com.morpheusdata.model.projection.AccountResourceIdentityProjection;
import com.morpheusdata.model.projection.CloudFolderIdentity;

public interface MorpheusSynchronousAccountResourceService   extends MorpheusSynchronousDataService<AccountResource,AccountResourceIdentityProjection>, MorpheusSynchronousIdentityService<AccountResourceIdentityProjection> {
}
