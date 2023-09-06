package com.morpheusdata.core;

import com.bertramlabs.plugins.karman.CloudFile;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ImageType;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing VirtualImages in Morpheus
 * @since 0.8.0
 * @author Mike Truso
 */
public interface MorpheusVirtualImageService extends MorpheusDataService<VirtualImage,VirtualImageIdentityProjection>, MorpheusIdentityService<VirtualImageIdentityProjection> {

	/**
	 * The context for dealing with VirtualImageLocations
	 * @return MorpheusVirtualImageLocationService
	 */
	MorpheusVirtualImageLocationService getLocation();

	/**
	 * Get a list of VirtualImage projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projections
	 */
	Observable<VirtualImageIdentityProjection> listIdentityProjections(Long cloudId);

	/**
	 * Get a list of VirtualImage projections based on Provision Type Code
	 * @since 0.11.0
	 * @param provisionTypeCode Provision Type Code
	 * @return Observable stream of sync projections
	 */
	Observable<VirtualImageIdentityProjection> listIdentityProjections(String provisionTypeCode);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided ImageTypes and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account Id
	 * @param imageTypes Array of ImageTypes
	 * @return Observable stream of sync projections
	 */
	Observable<VirtualImageIdentityProjection> listIdentityProjections(Long accountId, ImageType[] imageTypes);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided categories and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account ID
	 * @param categories Array of categories
	 * @return Observable stream of sync projections
	 */
	Observable<VirtualImageIdentityProjection> listIdentityProjectionsByCategory(Long accountId, String[] categories);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided categories and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account ID
	 * @param cloudId – Cloud ID
	 * @param categories Array of categories
	 * @return Observable stream of sync projections
	 */
	Observable<VirtualImageIdentityProjection> listIdentityProjectionsByCloudAndCategory(Long accountId, Long cloudId, String[] categories);

	/**
	 * Get a list of VirtualImage projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projections
	 * @deprecated replaced by {{@link #listIdentityProjections(Long)}}
	 */
	@Deprecated
	Observable<VirtualImageIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Get a list of VirtualImage projections based on Provision Type Code
	 * @since 0.11.0
	 * @param provisionTypeCode Provision Type Code
	 * @return Observable stream of sync projections
	 * @deprecated replaced by {{@link #listIdentityProjections(String)}}
	 */
	@Deprecated
	Observable<VirtualImageIdentityProjection> listSyncProjections(String provisionTypeCode);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided ImageTypes and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account Id
	 * @param imageTypes Array of ImageTypes
	 * @return Observable stream of sync projections
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, ImageType[])}}
	 */
	@Deprecated
	Observable<VirtualImageIdentityProjection> listSyncProjections(Long accountId, ImageType[] imageTypes);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided categories and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account ID
	 * @param categories Array of categories
	 * @return Observable stream of sync projections
	 * @deprecated replaced by {{@link #listIdentityProjectionsByCategory(Long, String[])}}
	 */
	@Deprecated
	Observable<VirtualImageIdentityProjection> listSyncProjectionsByCategory(Long accountId, String[] categories);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided categories and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account ID
	 * @param cloudId – Cloud ID
	 * @param categories Array of categories
	 * @return Observable stream of sync projections
	 * @deprecated replaced by {{@link #listSyncProjectionsByCloudAndCategory(Long, Long, String[])}}
	 */
	@Deprecated
	Observable<VirtualImageIdentityProjection> listSyncProjectionsByCloudAndCategory(Long accountId, Long cloudId, String[] categories);

	/**
	 * Get a list of VirtualImage objects from a list of projection ids
	 * @param ids VirtualImage ids
	 * @return Observable stream of VirtualImages
	 */
	Observable<VirtualImage> listById(Collection<Long> ids);

	/**
	 * Save updates to existing VirtualImages
	 * NOTE: Any additions or removals of VirtualImageLocations related to the VirtualImage should be
	 * performed via the VirtualImageLocationService
	 * @param virtualImages updated VirtualImages
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<Boolean> save(List<VirtualImage> virtualImages, Cloud cloud);

	/**
	 * Create new VirtualImages in Morpheus
	 * NOTE: Any additions of VirtualImageLocations related to the VirtualImage should be
	 * performed via the VirtualImageLocationService
	 * @param virtualImages new VirtualImages to persist
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<Boolean> create(List<VirtualImage> virtualImages, Cloud cloud);

	/**
	 * Create new VirtualImage in Morpheus
	 * NOTE: Any additions of VirtualImageLocations related to the VirtualImage should be
	 * performed via the VirtualImageLocationService
	 * @param virtualImage new VirtualImage to persist
	 * @param cloud the Cloud instance
	 * @return the VirtualImage
	 */
	Single<VirtualImage> create(VirtualImage virtualImage, Cloud cloud);

	/**
	 * Remove persisted VirtualImages from Morpheus. Typically called during sync operations for the cloud
	 * to inform Morpheus that the VirtualImage no longer exists in the cloud
	 * @param virtualImages Images to remove
	 * @return success
	 */
	Single<Boolean> remove(List<VirtualImageIdentityProjection> virtualImages, Cloud cloud);

	Single<Collection<CloudFile>> getVirtualImageFiles(VirtualImage virtualImage);
}
