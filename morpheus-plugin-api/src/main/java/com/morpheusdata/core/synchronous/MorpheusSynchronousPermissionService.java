package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Permission;
import com.morpheusdata.model.ResourcePermission;

import java.util.Collection;

public interface MorpheusSynchronousPermissionService extends MorpheusSynchronousDataService<Permission, Permission> {

	/**
	 * Get a list of ids for which the given accountId and siteId have access to (optionally scoped to siteId)
	 * @param accountId the account ID to scope the search
	 * @param resourceType the ResourceType
	 * @param siteId (Optional)
	 * @param planId (Optional)
	 * @return list of ids
	 * @deprecated Use {@link MorpheusSynchronousResourcePermissionService#listAccessibleResources(Long, ResourcePermission.ResourceType, Long, Long)} instead
	 */
	@Deprecated(since="0.15.12", forRemoval=true)
	Collection<Long> listAccessibleResources(Long accountId, Permission.ResourceType resourceType, Long siteId, Long planId);

}
