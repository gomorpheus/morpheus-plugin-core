package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.projection.CloudPoolIdentity;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link CloudPool} in Morpheus
 * <p><strong>This service is accessible in the {@link MorpheusContext} via the following traversal path:</strong> <br>
 * {@code morpheusContext.getAsync().getCloud().getPool()}</p>
 * @author Mike Truso
 */
public interface MorpheusCloudPoolService extends MorpheusDataService<CloudPool,CloudPoolIdentity>, MorpheusIdentityService<CloudPoolIdentity> {

	/**
	 * Get a list of {@link CloudPool} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @param category filter by category
	 * @return Observable stream of sync projection
	 */
	Observable<CloudPoolIdentity> listIdentityProjections(Long cloudId, String category, String regionCode);

	/**
	 * Get a list of {@link CloudPool} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @param category filter by category
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, String, String)}}
	 */
	@Deprecated
	Observable<CloudPoolIdentity> listSyncProjections(Long cloudId, String category);


	/**
	 * Returns a list of all pools filtered by a cloud as well as a list of externalIds. This is useful for chunked syncs where a cloud can contain
	 * a LARGE amount of pools
	 * @param cloudId the current id of the cloud we are filtering by
	 * @param externalIds a list of external ids to fetch by within the cloud scope
	 * @return Observable stream of CloudPools filtered by cloud and a collection of externalIds
	 */
	Observable<CloudPool> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);


	/**
	 * Returns a pool from a pool ID string (usually starts with pool- or poolGroup-) obtained from user inputs. In the case of a pool group ID a
	 * pool will be selected based on the group's selection mode
	 * @param poolId
	 * @param accountId
	 * @param siteId
	 * @param zoneId
	 * @return a cloud pool or null
	 */
	Maybe<CloudPool> get(String poolId, Long accountId, Long siteId, Long zoneId);

}
