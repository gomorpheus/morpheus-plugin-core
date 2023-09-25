package com.morpheusdata.core;

import com.morpheusdata.model.MetadataTag;
import com.morpheusdata.model.MetadataTagType;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.projection.InstanceIdentityProjection;
import com.morpheusdata.model.projection.MetadataTagIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing MetadataTags in Morpheus
 */
public interface MorpheusMetadataTagService {

	/**
	 * Returns the MetadataTypeType context used for performing updates or queries on {@link MetadataTagType} related assets within Morpheus.
	 * @return An instance of the MetadataTypeType Context
	 */
	MorpheusMetadataTagTypeService getMetadataTagType();

	/**
	 * Get a list of MetadataTag projections based on the refId and refType associated with the MetadataTag
	 * @param refType the refType to match on. Typically 'ComputeZone' for Cloud related tags
	 * @param refId the refId to match on. Typically the id of the Cloud for Cloud related tags
	 * @return Observable stream of sync projection
	 */
	Observable<MetadataTagIdentityProjection> listSyncProjections(String refType, Long refId);

	/**
	 * Get a list of MetadataTag projections based on the MetadataTagType
	 * @param metadataTagType the MetadataTagType to match on
	 * @return Observable stream of sync projection
	 */
	Observable<MetadataTagIdentityProjection> listSyncProjections(MetadataTagType metadataTagType);

	/**
	 * Get a list of MetadataTag objects from a list of projection ids
	 * @param ids MetadataTag ids
	 * @return Observable stream of MetadataTag
	 */
	Observable<MetadataTag> listById(Collection<Long> ids);

	/**
	 * Save updates to existing MetadataTags
	 * @param metadataTags updated MetadataTags
	 * @return success
	 */
	Single<Boolean> save(List<MetadataTag> metadataTags);

	/**
	 * Create new MetadataTags in Morpheus
	 * @param metadataTags new MetadataTags to persist
	 * @return success
	 */
	Single<Boolean> create(List<MetadataTag> metadataTags);

	/**
	 * Create new MetadataTags in Morpheus associated with a ComputeServer
	 * @param addList new MetadataTags to persist
	 * @return success
	 */
	Single<Boolean> create(List<MetadataTag> addList, ComputeServerIdentityProjection computeServerProj);

	/**
	 * Create new MetadataTags in Morpheus associated with an Instance
	 * @param addList new MetadataTags to persist
	 * @return success
	 */
	Single<Boolean> create(List<MetadataTag> addList, InstanceIdentityProjection instanceProj);


	/**
	 * Remove persisted MetadataTags from Morpheus
	 * @param metadataTags MetadataTags to delete
	 * @return success
	 */
	Single<Boolean> remove(List<MetadataTagIdentityProjection> metadataTags);

	/**
	 * Remove persisted MetadataTags associated with a ComputeServer from Morpheus
	 * @param metadataTags MetadataTags to delete
	 * @return success
	 */
	Single<Boolean> remove(List<MetadataTagIdentityProjection> metadataTags, ComputeServerIdentityProjection serverProj);

	/**
	 * Remove persisted MetadataTags associated with an Instance from Morpheus
	 * @param metadataTags MetadataTags to delete
	 * @return success
	 */
	Single<Boolean> remove(List<MetadataTagIdentityProjection> metadataTags, InstanceIdentityProjection instanceProj);

}
