package com.morpheusdata.core.synchronous.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.projection.CloudPoolIdentity;
import io.reactivex.Maybe;

public interface MorpheusSynchronousCloudPoolService extends MorpheusSynchronousDataService<CloudPool,CloudPoolIdentity>, MorpheusSynchronousIdentityService<CloudPoolIdentity> {

	/**
	 * Returns a pool from a pool ID string (usually starts with pool- or poolGroup-) obtained from user inputs. In the case of a pool group ID a
	 * pool will be selected based on the group's selection mode
	 * @param poolId
	 * @param accountId
	 * @param siteId
	 * @param zoneId
	 * @return a cloud pool or null
	 */
	CloudPool get(String poolId, Long accountId, Long siteId, Long zoneId);
}
