package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.MetadataTagType;
import com.morpheusdata.model.projection.MetadataTagTypeIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing MetadataTagTypes in Morpheus
 */
public interface MorpheusMetadataTagTypeService {

	/**
	 * Get a list of MetadataTagType projections based on the refId and refType associated with the MetadataTagType
	 * @param refType the refType to match on. Typically 'ComputeZone' for Cloud related tags
	 * @param refId the refId to match on. Typically the id of the Cloud for Cloud related tags
	 * @return Observable stream of sync projection
	 */
	Observable<MetadataTagTypeIdentityProjection> listSyncProjections(String refType, Long refId);
	
	/**
	 * Get a list of MetadataTagType objects from a list of projection ids
	 * @param ids MetadataTagType ids
	 * @return Observable stream of MetadataTagType
	 */
	Observable<MetadataTagType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing MetadataTagTypes
	 * @param metadataTagTypes updated MetadataTagTypes
	 * @return success
	 */
	Single<Boolean> save(List<MetadataTagType> metadataTagTypes);

	/**
	 * Create new MetadataTagTypes in Morpheus
	 * @param metadataTagTypes new MetadataTagType to persist
	 * @return success
	 */
	Single<Boolean> create(List<MetadataTagType> metadataTagTypes);
	
	/**
	 * Remove persisted MetadataTagTypes from Morpheus
	 * @param metadataTagTypes MetadataTagTypes to delete
	 * @return success
	 */
	Single<Boolean> remove(List<MetadataTagTypeIdentityProjection> metadataTagTypes);
}