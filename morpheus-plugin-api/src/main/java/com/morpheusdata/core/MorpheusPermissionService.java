package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Observable;

public interface MorpheusPermissionService {

    /**
     * Get a list of ids for which the given accountId and siteId have access to (optionally scoped to siteId)
     * @param accountId the account ID to scope the search
     * @param resourceType the ResourceType
     * @param siteId (Optional)
     * @return list of ids
     */
	Observable<Long> listAccessibleResources(Long accountId, Permission.ResourceType resourceType, Long siteId);
}
