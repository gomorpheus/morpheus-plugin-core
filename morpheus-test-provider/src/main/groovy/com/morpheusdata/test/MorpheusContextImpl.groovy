package com.morpheusdata.test

import com.morpheusdata.core.MorpheusAccountCredentialService
import com.morpheusdata.core.MorpheusAccountCredentialTypeService
import com.morpheusdata.core.MorpheusAccountIntegrationService
import com.morpheusdata.core.MorpheusAccountPriceService
import com.morpheusdata.core.MorpheusAccountPriceSetService
import com.morpheusdata.core.MorpheusAsyncServices
import com.morpheusdata.core.MorpheusComputeTypeLayoutFactoryService
import com.morpheusdata.core.MorpheusComputeTypeSetService
import com.morpheusdata.core.MorpheusContainerTypeService
import com.morpheusdata.core.MorpheusInstanceService
import com.morpheusdata.core.MorpheusKeyPairService
import com.morpheusdata.core.MorpheusPermissionService
import com.morpheusdata.core.MorpheusProcessService
import com.morpheusdata.core.MorpheusReferenceDataService
import com.morpheusdata.core.MorpheusSecurityGroupService
import com.morpheusdata.core.MorpheusServicePlanPriceSetService
import com.morpheusdata.core.MorpheusServices
import com.morpheusdata.core.MorpheusSnapshotService
import com.morpheusdata.core.MorpheusStatsService
import com.morpheusdata.core.MorpheusStorageControllerService
import com.morpheusdata.core.MorpheusUsageService
import com.morpheusdata.core.MorpheusWikiPageService
import com.morpheusdata.core.MorpheusWorkloadService
import com.morpheusdata.core.backup.MorpheusBackupJobService
import com.morpheusdata.core.backup.MorpheusBackupProviderService
import com.morpheusdata.core.backup.MorpheusBackupService
import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusComputeServerService
import com.morpheusdata.core.costing.MorpheusCostingService
import com.morpheusdata.core.cypher.MorpheusCypherService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusOsTypeService
import com.morpheusdata.core.MorpheusReportService
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.integration.MorpheusAccountInventoryService
import com.morpheusdata.core.integration.MorpheusIntegrationService
import com.morpheusdata.core.network.MorpheusNetworkService
import com.morpheusdata.core.network.MorpheusNetworkSubnetService
import com.morpheusdata.core.MorpheusServicePlanService
import com.morpheusdata.core.MorpheusTaskService
import com.morpheusdata.core.MorpheusVirtualImageService
import com.morpheusdata.core.MorpheusOperationNotificationService
import com.morpheusdata.core.MorpheusStorageVolumeService
import com.morpheusdata.core.MorpheusMetadataTagService
import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerService
import com.morpheusdata.core.policy.MorpheusPolicyService
import com.morpheusdata.core.provisioning.MorpheusProvisionService
import com.morpheusdata.core.web.MorpheusWebRequestService
import com.morpheusdata.core.dashboard.MorpheusDashboardService
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Instance
import com.morpheusdata.model.LogLevel
import com.morpheusdata.model.Task
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.TaskResult
import com.morpheusdata.test.network.MorpheusNetworkServiceImpl
import com.morpheusdata.test.network.MorpheusNetworkSubnetServiceImpl
import com.morpheusdata.test.dashboard.MorpheusDashboardServiceImpl
import io.reactivex.Single

/**
 * Testing Implementation of the Morpehus Context
 */
class MorpheusContextImpl implements MorpheusContext {

    protected MorpheusCloudService cloudContext
    protected MorpheusNetworkService networkContext
    protected MorpheusTaskService taskContext
	protected MorpheusVirtualImageService virtualImageContext
	protected MorpheusServicePlanService servicePlanContext
	protected MorpheusComputeServerService computeServerContext
	protected MorpheusOsTypeService osTypeContext
	protected MorpheusNetworkSubnetService subnetContext
	protected MorpheusOperationNotificationService operationNotification
	protected MorpheusMetadataTagService tagService
	protected MorpheusStorageVolumeService storageVolumeContext
	protected MorpheusStorageControllerService storageControllerContext
	protected MorpheusWikiPageService wikiPageContext
	protected MorpheusUsageService usageContext
	protected MorpheusStatsService statsContext
	protected MorpheusInstanceService instanceService
	protected MorpheusSnapshotService snapshotService
	protected MorpheusBackupServiceImpl backupService
	protected MorpheusDashboardService dashboardService
	protected MorpheusLoadBalancerService loadBalancerService
	protected MorpheusReferenceDataService referenceDataService


    MorpheusContextImpl() {
        cloudContext = new MorpheusCloudServiceImpl()
        networkContext = new MorpheusNetworkServiceImpl()
		taskContext = new MorpheusTaskContextImpl()
		virtualImageContext = new MorpheusVirtualImageContextImpl()
		servicePlanContext = new MorpheusServicePlanServiceImpl()
		computeServerContext = new MorpheusComputeServerServiceImpl()
	    osTypeContext = new MorpheusOsTypeServiceImpl()
	    subnetContext = new MorpheusNetworkSubnetServiceImpl()
	    operationNotification = new MorpheusOperationNotificationServiceImpl()
	    tagService = new MorpheusMetadataTagServiceImpl()
		storageVolumeContext = new MorpheusStorageVolumeServiceImpl()
		storageControllerContext = new MorpheusStorageControllerServiceImpl()
		wikiPageContext = new MorpheusWikiPageServiceImpl()
	    usageContext = new MorpheusUsageServiceImpl()
	    statsContext = new MorpheusStatsServiceImpl()
	    instanceService = new MorpheusInstanceServiceImpl()
	    snapshotService = new MorpheusSnapshotServiceImpl()
		backupService = new MorpheusBackupServiceImpl()
		dashboardService = new MorpheusDashboardServiceImpl()
		loadBalancerService = new MorpheusLoadBalancerServiceImpl()
    }

    MorpheusContextImpl(MorpheusCloudService cloudContext, MorpheusNetworkService networkContext, MorpheusTaskService taskContext, MorpheusVirtualImageService virtualImageContext) {
        this.cloudContext = cloudContext
        this.networkContext = networkContext
		this.taskContext = taskContext
		this.virtualImageContext = virtualImageContext
    }

	/**
	 * Gets references to morpheus service calls for performing operations and/or queries within Morpheus.
	 * These services are the synchronous representations and wrap all services within the `MorpheusAsyncServices`
	 * class. This is useful when writing UI plugins or plugin sections that are probably blocking anyway.
	 * @return references to all synchronous morpheus data services.
	 */
	MorpheusServices getServices() {
		return null;
	}

	/**
	 * Gets references to all rxjava/async morpheus service calls. Most of these services will respond with `Observable`
	 * , `Single`, `Maybe`, or  even `Completable` object types. These are most useful when performing high performance
	 * operations as in sync for various plugin types. These are also the same references as the original services
	 * and can be swapped in as the old service references are deprecated within the morpheusContext.
	 * @return references to all the async morpheus data services.
	 */
	MorpheusAsyncServices getAsync() {
		return null;
	}

	@Override
	MorpheusStorageVolumeService getStorageVolume() {
		return storageVolumeContext
	}

	@Override
	MorpheusStorageControllerService getStorageController() {
		return storageControllerContext
	}

	@Override
	MorpheusUsageService getUsage() {
		return usageContext
	}

	@Override
	MorpheusStatsService getStats() {
		return statsContext
	}

	@Override
	MorpheusOperationNotificationService getOperationNotification() {
		return operationNotification
	}

	@Override
	MorpheusMetadataTagService getMetadataTag() {
		return tagService
	}

	@Override
	MorpheusWikiPageService getWikiPage() {
		return wikiPageContext
	}

    @Override
    MorpheusCloudService getCloud() {
        return cloudContext
    }

	/**
	 * Returns the Provision Service used for performing provisioning related updates to objects.
	 * @return An Instance of the Provision Service to be used typically by a {@link ProvisioningProvider}
	 */
	@Override
	MorpheusProvisionService getProvision() {
		return null
	}

	@Override
    MorpheusNetworkService getNetwork() {
        return networkContext
    }

	@Override
	MorpheusNetworkSubnetService getNetworkSubnet() {
		return subnetContext
	}

	@Override
	MorpheusTaskService getTask() {
		return taskContext
	}

	@Override
	MorpheusDashboardService getDashboard() {
		return dashboardService
	}

	@Override
	MorpheusReferenceDataService getReferenceData() {
		return null
	}

	@Override
	MorpheusBackupProviderService getBackupProvider() {
		return null
	}

	@Override
	MorpheusAccountPriceSetService getAccountPriceSet() {
		return null
	}

	@Override
	MorpheusAccountPriceService getAccountPrice() {
		return null
	}

	@Override
	MorpheusServicePlanPriceSetService getServicePlanPriceSet() {
		return null
	}

	@Override
	MorpheusAccountIntegrationService getAccountIntegration() {
		return null
	}

	@Override
	MorpheusAccountInventoryService getAccountInventory() {
		return null
	}

	@Override
	MorpheusOsTypeService getOsType() {
		return osTypeContext
	}

	/**
	 * Returns the Integration context used for performing common operations on varioues integration types Morpheus
	 * has to offer.
	 * @return An instance of the Integration Context to bused for calls by various integration types
	 */
	@Override
	MorpheusIntegrationService getIntegration() {
		return null
	}

	@Override
	MorpheusVirtualImageService getVirtualImage() {
		return virtualImageContext
	}

	@Override
	MorpheusServicePlanService getServicePlan() {
		return servicePlanContext
	}

	@Override
	MorpheusComputeServerService getComputeServer() {
		return computeServerContext
	}

	@Override
	MorpheusWorkloadService getWorkload() {
		return null
	}

	@Override
	MorpheusComputeTypeSetService getComputeTypeSet() {
		return null
	}

	@Override
	MorpheusContainerTypeService getContainerType() {
		return null
	}
/**
	 * Returns the Custom Report Types Context used for generating custom reports.
	 * Typically this should only ever be used by a report provider as it may not be accessible in all other contexts.
	 *
	 * @return an instance of the Report Context
	 */
	@Override
	MorpheusReportService getReport() {
		return null
	}

	@Override
	MorpheusCypherService getCypher() {
		return null
	}

	@Override
	MorpheusPolicyService getPolicy() {
		return null
	}

	@Override
	MorpheusCostingService getCosting() {
		return null;
	}


	@Override
	MorpheusBackupService getBackup() {

	}

	/**
	* Returns the Backup Job Context for sync, executing and restoring backup jobs
	* Typically this would be called by a {@link BackupProvider}.
	*
	* @return an instance of the Backup Job Context
	*/
	@Override
	MorpheusBackupJobService getBackupJob() {
		return
	}


/**
	 * Returns the Web Request Service. This is used by UI Providers to grab common request attributes
	 *
	 * @return an instance of the web request service
	 */
	@Override
	MorpheusWebRequestService getWebRequest() {
		return null
	}

	@Override
	MorpheusLoadBalancerService getLoadBalancer() {
		return this.loadBalancerService
	}

	@Override
	Single<TaskResult> executeSshCommand(String address, Integer port, String username, String password, String command, String publicKey, String privateKey, String passPhrase, Boolean ignoreExitStatus, LogLevel logLevel, Boolean doPty, String runAsUser, Boolean sudo) {
		return null
	}

	@Override
	Single<TaskResult> executeWindowsCommand(String address, Integer port, String username, String password, String command, Boolean noProfile, Boolean elevated) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnWorkload(Container container, String command) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnWorkload(Container container, String command, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, String runAsUser, Boolean sudo) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command, Boolean rpc, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, Boolean sudo) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command, Boolean rpc, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, Boolean sudo, Boolean guestExec) {
		return null
	}

	@Override
	Single<TaskConfig> buildInstanceConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildContainerConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildComputeServerConfig(ComputeServer container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	/**
	 * Acquires a distributed lock by key and some additional lock options can be provided
	 * @param name the key name of the lock to acquire
	 * @param opts the acquire wait timeout option via key [timeout:ms] as well as the locks ttl via [ttl:ms] property.
	 * @return a unique lock key id to control concurrent release attempts. send this to releaseLocks opts.lock key
	 *
	 * <p><strong>Example:</strong> </p>
	 * <pre>{@code
	 * String lockId
	 * try {*     lockId = morpheusContext.acquireLock('mylock.key',[ttl:600000L,timeout:600000L]);
	 *     //do stuff
	 *} finally {*     if(lockId) {*         morpheusContext.releaseLock('mylock.key',[lock: lockId]);
	 *}*}*}</pre>
	 */
	@Override
	Single<String> acquireLock(String name, Map opts) {
		return null
	}

	/**
	 * Releases a lock key for other threads or nodes to be able to use it.
	 * It takes an optional set of opts that can be used to scope the lock release to a key hash for concurrency safety
	 * @param name the key name of the lock to release
	 * @param opts the opts map of wait timeouts or [lock:lockId] where the lockId is the return of {@link MorpheusContext#acquireLock(String, Map)}
	 * @return the success state of the release lock attempt
	 */
	@Override
	Single<Boolean> releaseLock(String name, Map opts) {
		return null
	}

	@Override
	Single<String> getSettings(Plugin plugin) {
		return null
	}

	@Override
	MorpheusInstanceService getInstance() {
		return instanceService
	}

	@Override
	MorpheusSnapshotService getSnapshot() {
		return snapshotService
	}

	@Override
	MorpheusComputeTypeLayoutFactoryService getComputeTypeLayoutFactoryService() {
		return null
	}

	@Override
	MorpheusProcessService getProcess() {
		return null
	}

	@Override
	MorpheusPermissionService getPermission() {
		return null
	}

	@Override
	MorpheusAccountCredentialTypeService getAccountCredentialType() {
		return null
	}

	@Override
	MorpheusAccountCredentialService getAccountCredential() {
		return null
	}

	@Override
	MorpheusKeyPairService getKeyPair() {
		return null
	}

	@Override
	MorpheusSecurityGroupService getSecurityGroup() {
		return null
	}
}
