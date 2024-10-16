/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core;

import com.bertramlabs.plugins.karman.CloudFile;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	 * The context for VirtualIMageType
	 */
	MorpheusVirtualImageTypeService getType();

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
	@Deprecated(since="0.15.4")
	Observable<VirtualImageIdentityProjection> listIdentityProjectionsByCategory(Long accountId, String[] categories);

	/**
	 * Get a list of VirtualImage projections that matches one of the provided categories and either the account owns or
	 * the account can access due to the visibility setting on the VirtualImage
	 * @param accountId Account ID
	 * @param cloudId – Cloud ID
	 * @param categories Array of categories
	 * @return Observable stream of sync projections
	 */
	@Deprecated(since="0.15.4")
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
	@Deprecated(since="0.15.4")
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
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<VirtualImageIdentityProjection> virtualImages, Cloud cloud);

	Single<Collection<CloudFile>> getVirtualImageFiles(VirtualImage virtualImage);

	/**
	 * Get a one-off url for an image to upload to it to a cloud
	 * @since 0.15.13
	 * @param virtualImage the image
	 * @param cloudFile the specific file
	 * @param createdBy the user associated with the workload or server
	 * @param cloud the Cloud instance
	 * @return the url of the image file
	 */
	Single<String> getCloudFileStreamUrl(VirtualImage virtualImage, CloudFile cloudFile, User createdBy, Cloud cloud);

	/**
	 * Get metadata for a virtual image. The metadata can include a list of disks in the virtual image.
	 * @param virtualImageModel the virtual image to extract disk information about.
	 * @return the disk mapping information
	 */
	Maybe<Map<String, Object>> getImageDiskMap(VirtualImage virtualImageModel);
}
