package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;
import io.reactivex.Observable;

public interface MorpheusPermissionService {

	/**
	 * Get a {@link Permission} by id.
	 *
	 * @param id Permission id
	 * @return a permission
	 */
	Single<Permission> get(Long id);

	/**
	 * Get a {@link Permission} by id.
	 *
	 * @param code Permission code
	 * @return a permission
	 */
	Single<Permission> getByCode(String code);

	/**
	 * Get a list of ids for which the given accountId and siteId have access to (optionally scoped to siteId)
	 *
	 * @param accountId    the account ID to scope the search
	 * @param resourceType the ResourceType
	 * @param siteId       (Optional)
	 * @param planId       (Optional)
	 * @return list of ids
	 */
	Observable<Long> listAccessibleResources(Long accountId, Permission.ResourceType resourceType, Long siteId, Long planId);

	/**
	 * Get a list of Zone UUIDs that a user can see
	 * @param u User to scope zone UUIDs to
	 * @return
	 */
	Observable<String> listAccessibleZonesByUUIDForUser(User u);
}
