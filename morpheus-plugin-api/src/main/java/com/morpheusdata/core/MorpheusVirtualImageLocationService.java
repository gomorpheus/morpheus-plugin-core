package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
	 * Looks up a VirtualImageLocation by the externalId within the context of a cloud,region,type, and account
	 * This is useful if a request is made based on a clouds remote id as opposed to the morpheus id. (i.e. AMI lookup).
	 * @param externalId the external identifier in the cloud of this image (i.e. ami-adfadsf)
	 * @param cloudId the id of the {@link Cloud} to scope the search to
	 * @param regionCode the region code scope of the image
	 * @param imageType image type to scope to
	 * @param accountId optional accountId to scope it based on user access permissions on the lookup
	 * @return an optional VirtualImageLocation with parent VirtualImage (if it exists)
	 */
	Single<Optional<VirtualImageLocation>> findVirtualImageLocationByExternalIdForCloudAndType(String externalId, Long cloudId, String regionCode, String imageType, Long accountId);

	/**
	 * Looks up a VirtualImageLocation by the externalId within the context of a cloud,region,type, and account
	 * This is useful if a request is made based on a clouds remote id as opposed to the morpheus id. (i.e. AMI lookup).
	 * @param externalId the external identifier in the cloud of this image (i.e. ami-adfadsf)
	 * @param cloudId the id of the {@link Cloud} to scope the search to
	 * @param regionCode the region code scope of the image
	 * @param imageType image type to scope to
	 * @return an optional VirtualImageLocation with parent VirtualImage (if it exists)
	 */
	Single<Optional<VirtualImageLocation>> findVirtualImageLocationByExternalIdForCloudAndType(String externalId, Long cloudId, String regionCode, String imageType);


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
	 * Create a new VirtualImageLocation in Morpheus
	 * @param virtualImageLocation a new VirtualImageLocation to persist
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<VirtualImageLocation> create(VirtualImageLocation virtualImageLocation, Cloud cloud);

	/**
	 * Remove persisted VirtualImageLocations from Morpheus
	 * @param virtualImageLocations to delete
	 * @return success
	 */
	Single<Boolean> remove(List<VirtualImageLocationIdentityProjection> virtualImageLocations);
}
