package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing VirtualImageLocations in Morpheus. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusVirtualImageService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getVirtualImage().getLocation()
 * }</pre>
 * @see MorpheusVirtualImageService
 * @author Bob Whiton
 */
public interface MorpheusVirtualImageLocationService {
	/**
	 * Get a list of VirtualImageLocation projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<VirtualImageLocationIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Find a VirtualImageLocation given the parameters
	 * @param virtualImageId The VirtualImage to search against
	 * @param cloudId The id of the Cloud to search against
	 * @param regionCode The regionCode of the Cloud to search against (optional) Defaults to null
	 * @param imageFolder The name of the ComputeZoneFolder to search within (optional) Defaults to null
	 * @param sharedStorage Whether to search for VirtualImageLocations with sharedStorage (optional) Defaults to false.
	 * @return Observable VirtualImageLocation matching the parameters
	 */
	Single<VirtualImageLocation> findVirtualImageLocation(Long virtualImageId, Long cloudId, String regionCode, String imageFolder, Boolean sharedStorage);

	/**
	 * Get a list of VirtualImageLocation objects from a list of projection ids
	 * @param ids VirtualImageLocation ids
	 * @return Observable stream of VirtualImageLocations
	 */
	Observable<VirtualImageLocation> listById(Collection<Long> ids);

	/**
	 * Save updates to existing VirtualImageLocations
	 * @param virtualImageLocations updated VirtualImageLocations
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<Boolean> save(List<VirtualImageLocation> virtualImageLocations, Cloud cloud);

	/**
	 * Create new VirtualImageLocations in Morpheus
	 * @param virtualImageLocations new VirtualImageLocations to persist
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<Boolean> create(List<VirtualImageLocation> virtualImageLocations, Cloud cloud);

	/**
	 * Remove persisted VirtualImageLocations from Morpheus
	 * @param virtualImageLocations to delete
	 * @return success
	 */
	Single<Boolean> remove(List<VirtualImageLocationIdentityProjection> virtualImageLocations);
}
