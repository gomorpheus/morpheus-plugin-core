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
	 * @param folders Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeZoneFolderIdentityProjection> folders);

}
