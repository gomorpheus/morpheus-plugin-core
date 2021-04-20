package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
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
public interface MorpheusVirtualImageService {
	/**
	 * Get a list of VirtualImage projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<VirtualImageIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Get a list of VirtualImage objects from a list of projection ids
	 * @param ids VirtualImage ids
	 * @return Observable stream of VirtualImages
	 */
	Observable<VirtualImage> listById(Collection<Long> ids);

	/**
	 * Save updates to existing VirtualImages
	 * @param virtualImages updated VirtualImages
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<Boolean> save(List<VirtualImage> virtualImages, Cloud cloud);

	/**
	 * Create new VirtualImages in Morpheus
	 * @param virtualImages new VirtualImages to persist
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Single<Boolean> create(List<VirtualImage> virtualImages, Cloud cloud);

	/**
	 * Remove persisted VirtualImage from Morpheus
	 * @param virtualImages Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<VirtualImageIdentityProjection> virtualImages);
}
