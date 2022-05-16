package com.morpheusdata.core.cloud;

import com.morpheusdata.model.ComputeZoneFolder;
import com.morpheusdata.model.projection.ComputeZoneFolderIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link ComputeZoneFolder} in Morpheus. It can normally
 * be accessed via the {@link com.morpheusdata.core.MorpheusComputeServerService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getCloud().getFolder()
 * }</pre>
 *
 * @author Bob Whiton
 */
public interface MorpheusComputeZoneFolderService {

	/**
	 * Get a list of {@link ComputeZoneFolder} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<ComputeZoneFolderIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Get the default ComputeZoneFolder
	 * @param cloudId The id of the cloud
	 * @param accountId The id of the account
	 * @param siteId The id of the site (optional)
	 * @param servicePlanId The id of the ServicePlan (optional)
	 * @return The default ComputeZoneFolder
	 */
	Single<ComputeZoneFolder> getDefaultFolderForAccount(Long cloudId, Long accountId, Long siteId, Long servicePlanId);

	/**
	 * Get the default image ComputeZoneFolder
	 * @param cloudId The id of the cloud
	 * @param accountId The id of the account
	 * @return The default image ComputeZoneFolder
	 */
	Single<ComputeZoneFolder> getDefaultImageFolderForAccount(Long cloudId, Long accountId);

	/**
	 * Get a list of ComputeZoneFolder objects from a list of projection ids
	 *
	 * @param ids ComputeZoneFolder ids
	 * @return Observable stream of ComputeZoneFolders
	 */
	Observable<ComputeZoneFolder> listById(Collection<Long> ids);

	/**
	 * Save updates to existing ComputeZoneFolders
	 *
	 * @param folders updated ComputeZoneFolder
	 * @return success
	 */
	Single<Boolean> save(List<ComputeZoneFolder> folders);

	/**
	 * Create new ComputeZoneFolders in Morpheus
	 *
	 * @param folders new ComputeZoneFolders to persist
	 * @return success
	 */
	Single<Boolean> create(List<ComputeZoneFolder> folders);

	/**
	 * Remove persisted ComputeZoneFolder from Morpheus
	 *
	 * @param folders Folders to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeZoneFolderIdentityProjection> folders);

}
