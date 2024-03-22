package com.morpheusdata.core;

import com.morpheusdata.model.ResourcePermission;
import com.morpheusdata.model.projection.ResourcePermissionIdentity;
import io.reactivex.Observable;


public interface MorpheusResourcePermissionService extends MorpheusDataService<ResourcePermission, ResourcePermissionIdentity> {

	/**
	 * Get a list of ids for which the given accountId and siteId have access to (optionally scoped to siteId)
	 * @param accountId the account ID to scope the search
	 * @param resourceType the ResourceType
	 * @param siteId (Optional)
	 * @param planId (Optional)
	 * @return list of ids
	 */
	Observable<Long> listAccessibleResources(Long accountId, ResourcePermission.ResourceType resourceType, Long siteId, Long planId);

}
