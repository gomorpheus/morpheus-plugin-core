package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Observable;

public interface MorpheusPermissionService extends MorpheusDataService<Permission, Permission> {

	/**
	 * Get a {@link Permission} by id.
	 * @param code Permission code
	 * @return a permission
	 */
	@Deprecated(since="0.15.4")
	Single<Permission> getByCode(String code);

	/**
	 * Get a list of ids for which the given accountId and siteId have access to (optionally scoped to siteId)
	 * @param accountId the account ID to scope the search
	 * @param resourceType the ResourceType
	 * @param siteId (Optional)
	 * @param planId (Optional)
	 * @return list of ids
	 * @deprecated Use {@link MorpheusResourcePermissionService#listAccessibleResources(Long, ResourcePermission.ResourceType, Long, Long)} instead
	 */
	@Deprecated(since="0.15.12", forRemoval=true)
	Observable<Long> listAccessibleResources(Long accountId, Permission.ResourceType resourceType, Long siteId, Long planId);

}
