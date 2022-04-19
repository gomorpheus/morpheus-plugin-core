package com.morpheusdata.test

import com.morpheusdata.core.MorpheusWikiPageService
import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusComputeServerService
import com.morpheusdata.core.costing.MorpheusCostingService
import com.morpheusdata.core.cypher.MorpheusCypherService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusOsTypeService
import com.morpheusdata.core.MorpheusReportService
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.integration.MorpheusIntegrationService
import com.morpheusdata.core.network.MorpheusNetworkService
import com.morpheusdata.core.network.MorpheusNetworkSubnetService
import com.morpheusdata.core.MorpheusServicePlanService
import com.morpheusdata.core.MorpheusTaskService
import com.morpheusdata.core.MorpheusVirtualImageService
import com.morpheusdata.core.MorpheusOperationNotificationService
import com.morpheusdata.core.MorpheusStorageVolumeService
import com.morpheusdata.core.MorpheusMetadataTagService
import com.morpheusdata.core.policy.MorpheusPolicyService
import com.morpheusdata.core.provisioning.MorpheusProvisionService
import com.morpheusdata.core.web.MorpheusWebRequestService
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Instance
import com.morpheusdata.model.LogLevel
import com.morpheusdata.model.Task
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.TaskResult
import com.morpheusdata.test.network.MorpheusNetworkServiceImpl
import com.morpheusdata.test.network.MorpheusNetworkSubnetServiceImpl
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
	protected MorpheusWikiPageService wikiPageService


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
		wikiPageService = new MorpheusWikiPageServiceImpl()
    }

    MorpheusContextImpl(MorpheusCloudService cloudContext, MorpheusNetworkService networkContext, MorpheusTaskService taskContext, MorpheusVirtualImageService virtualImageContext) {
        this.cloudContext = cloudContext
        this.networkContext = networkContext
		this.taskContext = taskContext
		this.virtualImageContext = virtualImageContext
    }

	@Override
	MorpheusStorageVolumeService getStorageVolume() {
		return storageVolumeContext
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
		return wikiPageService
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
}
