package com.morpheusdata.core;

import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing VirtualImages in Morpheus
 * @author Mike Truso
 */
public interface MorpheusVirtualImageContext {
	/**
	 * Get a list of VirtualImage projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<VirtualImageSyncProjection> listVirtualImageSyncMatch(Long cloudId);

	/**
	 * Get a list of VirtualImage objects from a list of projection ids
	 * @param ids VirtualImage ids
	 * @return Observable stream of VirtualImages
	 */
	Observable<VirtualImage> listVirtualImagesById(Collection<Long> ids);

	/**
	 * Save updates to existing VirtualImages
	 * @param virtualImages updated VirtualImages
	 * @return resulting VirtualImages
	 */
	Single<VirtualImage> save(List<VirtualImage> virtualImages);

	/**
	 * Create new VirtualImages in Morpheus
	 * @param virtualImages new VirtualImages to persist
	 * @return resulting VirtualImages
	 */
	Single<VirtualImage> create(List<VirtualImage> virtualImages);

	/**
	 * Remove persisted VirtualImage from Morpheus
	 * @param virtualImages Images to delete
	 * @return void
	 */
	Single<Void> remove(List<VirtualImageSyncProjection> virtualImages);
}
