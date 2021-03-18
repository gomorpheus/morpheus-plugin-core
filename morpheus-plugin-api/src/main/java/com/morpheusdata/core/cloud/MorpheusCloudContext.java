package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Morpheus Context as it relates to cloud operations. This context contains methods for querying things like {@link ReferenceData}
 * for {@link OptionType} sources managing other resources needed at Cloud initialization.
 * Typically this class is accessed via the primary {@link MorpheusContext}.
 *
 * @author Mike Truso
 */
public interface MorpheusCloudContext {

	MorpheusComputeZonePoolContext getPool();

	/**
	 * Update the status of a Cloud during setup
	 * @param cloud Cloud instance
	 * @param status cloud state status
	 * @param message error or info message
	 * @param syncDate time of update operation
	 */
	void updateZoneStatus(Cloud cloud, Cloud.Status status, String message, Date syncDate);

	/**
	 *	Get the ssh credentials associated with an account
	 * @param account to lookup
	 * @return Morpheus KeyPair
	 */
	Single<KeyPair> findOrGenerateKeyPair(Account account);

	/**
	 *	Update Morpheus with an external reference to the KeyPair in your Cloud API.
	 * @param keyPair that was updated
	 * @param cloud associated with the credentials
	 * @return void
	 */
	Single<Void> updateKeyPair(KeyPair keyPair, Cloud cloud);

	Single<Container> getContainerById(Long id);

	Single<Cloud> getCloudById(Long id);

	Single<Map> buildContainerUserGroups(Account account, VirtualImage virtualImage, List<UserGroup> userGroups, User user, Map opts);

	Single<Boolean> create(List<ReferenceData> referenceData, Cloud cloud, String category);
	Single<Boolean> save(List<ReferenceData> referenceData, Cloud cloud, String category);
	Single<Boolean> remove(List<ReferenceDataSyncProjection> removeItems);
	Observable<ReferenceDataSyncProjection> listReferenceDataByCategory(Cloud cloud, String code);
	Single<ReferenceData> findReferenceDataByExternalId(String externalId);
	Observable<ReferenceData> listReferenceDataById(List<Long> ids);

	Single<Void> updatePowerState(Long id, String state);

	Single<Void> updateInstanceStatus(List<Long> ids, Instance.Status status);

	Single<List<Long>> getStoppedContainerInstanceIds(Long containerId);

	Single<Instance> getInstance(ComputeServer server);

	Single<Container> getContainer(ComputeServer server);
}
