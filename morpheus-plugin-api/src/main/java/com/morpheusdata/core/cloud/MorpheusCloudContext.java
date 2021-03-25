package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.InstanceIdentityProjection;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import com.morpheusdata.model.projection.WorkloadIdentityProjection;
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

	Single<Workload> getWorkloadById(Long id);
	Observable<Workload> getWorkload(ComputeServer server);

	Single<Cloud> getCloudById(Long id);

	Single<Map> buildContainerUserGroups(Account account, VirtualImage virtualImage, List<UserGroup> userGroups, User user, Map opts);

	Single<Boolean> create(List<ReferenceData> referenceData, Cloud cloud, String category);
	Single<Boolean> save(List<ReferenceData> referenceData, Cloud cloud, String category);
	Single<Boolean> remove(List<ReferenceDataSyncProjection> removeItems);
	Observable<ReferenceDataSyncProjection> listReferenceDataByCategory(Cloud cloud, String code);
	Single<ReferenceData> findReferenceDataByExternalId(String externalId);
	Observable<ReferenceData> listReferenceDataById(List<Long> ids);

	/**
	 * Update a collection of Instances to a particular status.
	 *
	 * @param ids {@link Instance} id list
	 * @param status instance status
	 * @return null
	 */
	Single<Void> updateInstanceStatus(List<Long> ids, Instance.Status status);

	Observable<InstanceIdentityProjection> getStoppedContainerInstanceIds(Long computeServerId);

	Single<Instance> getInstance(ComputeServer server);

	Single<String> buildUserData(PlatformType platformType, Map userConfig, Map cloudConfig);

}
