package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.core.network.MorpheusNetworkService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.CloudIdentityProjection;
import com.morpheusdata.model.projection.InstanceIdentityProjection;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import com.morpheusdata.model.projection.WorkloadIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Morpheus Context as it relates to cloud operations.
 * for {@link OptionType} sources managing other resources needed at Cloud initialization.
 * Typically, this class is accessed via the primary {@link MorpheusContext}.
 * This service used to contain methods for querying things like {@link ReferenceData} but those have since moved
 * to the {@link com.morpheusdata.core.MorpheusReferenceDataService}
 *
 * @author Mike Truso
 * @since 0.8.0
 */
public interface MorpheusCloudService extends MorpheusDataService<Cloud>, MorpheusIdentityService<CloudIdentityProjection> {

	Observable<WorkloadIdentityProjection> listCloudWorkloadProjections(Long cloudId);

	MorpheusComputeZonePoolService getPool();

	MorpheusComputeZoneFolderService getFolder();

	MorpheusComputeZoneRegionService getRegion();

	MorpheusDatastoreService getDatastore();

	MorpheusNetworkService getNetwork();

	MorpheusAccountResourceService getResource();

	Single<Collection<ComputeServerType>> getComputeServerTypes(Long cloudId);

	/**
	 * Update the status of a Cloud during setup
	 * @param cloud Cloud instance
	 * @param status cloud state status
	 * @param message error or info message
	 * @param syncDate time of update operation
	 * @deprecated replaced by {@link MorpheusCloudService#updateCloudStatus(Cloud, Cloud.Status, String, Date)}
	 */
	@Deprecated
	void updateZoneStatus(Cloud cloud, Cloud.Status status, String message, Date syncDate);


	/**
	 * Updates the costing status of a cloud from a costing daily refresh operation. This is typically only
	 * ever used when implementing a custom {@link com.morpheusdata.core.providers.CloudCostingProvider}
	 * @param cloud Cloud instance we are updating cost status on
	 * @param status cloud costing state status
	 * @param message error or info message
	 * @param syncDate time of cost refresh operation
	 * @see com.morpheusdata.core.providers.CloudCostingProvider
	 * @deprecated replaced by {@link MorpheusCloudService#updateCloudCostStatus(Cloud, Cloud.Status, String, Date)}
	 */
	@Deprecated
	void updateZoneCostStatus(Cloud cloud, Cloud.Status status, String message, Date syncDate);

	/**
	 * Update the status of a Cloud during setup
	 * @param cloud Cloud instance
	 * @param status cloud state status
	 * @param message error or info message
	 * @param syncDate time of update operation
	 */
	void updateCloudStatus(Cloud cloud, Cloud.Status status, String message, Date syncDate);


	/**
	 * Updates the costing status of a cloud from a costing daily refresh operation. This is typically only
	 * ever used when implementing a custom {@link com.morpheusdata.core.providers.CloudCostingProvider}
	 * @param cloud Cloud instance we are updating cost status on
	 * @param status cloud costing state status
	 * @param message error or info message
	 * @param syncDate time of cost refresh operation
	 * @see com.morpheusdata.core.providers.CloudCostingProvider
	 */
	void updateCloudCostStatus(Cloud cloud, Cloud.Status status, String message, Date syncDate);




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

	/**
	 * Returns the AccountCredential for the Cloud if the usage of credentials is
	 * supported by the Cloud and the Cloud is currently configured to use an AccountCredential
	 * @param cloudId
	 * @return AccountCredential
	 */
	Single<AccountCredential> loadCredentials(Long cloudId);

	/**
	 * Saves a workload
	 * @param workload
	 * @return boolean
	 */
	Single<Boolean> saveWorkload(Workload workload);

	Observable<Workload> getWorkload(ComputeServer server);

	Single<Cloud> getCloudById(Long id);

	Single<Map> buildContainerUserGroups(Account account, VirtualImage virtualImage, List<UserGroup> userGroups, User user, Map opts);

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
