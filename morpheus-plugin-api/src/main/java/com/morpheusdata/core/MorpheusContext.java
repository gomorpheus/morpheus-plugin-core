/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core;

import com.morpheusdata.core.backup.MorpheusBackupProviderService;
import com.morpheusdata.core.cloud.MorpheusCloudService;
import com.morpheusdata.core.costing.MorpheusCostingService;
import com.morpheusdata.core.cypher.MorpheusCypherService;
import com.morpheusdata.core.dashboard.MorpheusDashboardService;
import com.morpheusdata.core.integration.MorpheusIntegrationService;
import com.morpheusdata.core.integration.MorpheusAccountInventoryService;
import com.morpheusdata.core.library.MorpheusWorkloadTypeService;
import com.morpheusdata.core.network.MorpheusNetworkService;
import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerService;
import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.core.providers.TaskProvider;
import com.morpheusdata.core.provisioning.MorpheusProvisionService;
import com.morpheusdata.core.network.MorpheusNetworkSubnetService;
import com.morpheusdata.core.web.MorpheusWebRequestService;
import com.morpheusdata.core.policy.MorpheusPolicyService;
import com.morpheusdata.core.backup.MorpheusBackupService;
import com.morpheusdata.core.backup.MorpheusBackupJobService;
import com.morpheusdata.model.*;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Maybe;
import java.util.Collection;
import java.util.Map;

/**
 * Provides a means to interact or query data from the main Morpheus application back into the various provider extensions
 * It is important to note that most methods in the context are asynchronous and rely on RxJava based interfaces to
 * present the ability for the implementation of the MorpheusContext to be disconnected from the core application.
 *
 * <p>The MorpheusContext typically provides getters for multiple services. These Service interfaces are useful for
 * organizing calls to reduce the size of the individual Context Class</p>
 *
 * (i.e. a Connector app could implement the MorpheusContext and relay communication back to the Morpheus Application itself)
 *
 * @see MorpheusCloudService
 * @see MorpheusNetworkService
 * @see MorpheusTaskService
 * @see MorpheusVirtualImageService
 * @see MorpheusServicePlanService
 * @see MorpheusPolicyService
 * @see MorpheusOperationNotificationService
 *
 * @author David Estes
 */
public interface MorpheusContext {

	/**
	 * Gets references to morpheus service calls for performing operations and/or queries within Morpheus.
	 * These services are the synchronous representations and wrap all services within the `MorpheusAsyncServices`
	 * class. This is useful when writing UI plugins or plugin sections that are probably blocking anyway.
	 * @return references to all synchronous morpheus data services.
	 */
	MorpheusServices getServices();

	/**
	 * Gets references to all rxjava/async morpheus service calls. Most of these services will respond with `Observable`
	 * , `Single`, `Maybe`, or  even `Completable` object types. These are most useful when performing high performance
	 * operations as in sync for various plugin types. These are also the same references as the original services
	 * and can be swapped in as the old service references are deprecated within the morpheusContext.
	 * @return references to all the async morpheus data services.
	 */
	MorpheusAsyncServices getAsync();


	/**
	 * Returns the Compute Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the Cloud Service to be used typically by {@link CloudProvider} implementations.
	 * @deprecated
	 */
	@Deprecated
	MorpheusCloudService getCloud();

	/**
	 * Returns the ComputeSite Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the ComputeSite Service to be used typically by {@link CloudProvider} implementations.
	 * @deprecated
	 */
	@Deprecated
	MorpheusComputeSiteService getComputeSite();

	/**
	 * Returns the Provision Service used for performing provisioning related updates to objects.
	 * @return An Instance of the Provision Service to be used typically by a {@link ProvisionProvider}
	 * @deprecated
	 */
	@Deprecated
	MorpheusProvisionService getProvision();

	/**
	 * Returns the NetworkContext used for performing updates or queries on network related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An Instance of the Network Context to be used for calls by various network providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusNetworkService getNetwork();

	/**
	 * Returns the NetworkSubnetContext used for performing updates or queries on network subnet related assets within Morpheus.
	 * @return An Instance of the NetworkSubnet Context to be used for calls by various network providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusNetworkSubnetService getNetworkSubnet();

	/**
	 * Returns the Task context used for automation tasks on assets within Morpheus.
	 * Typically this would be called by a {@link TaskProvider}.
	 * @return An Instance of the Task Context to be used for calls by various task providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusTaskService getTask();

	/**
	 * Returns the Integration context used for performing common operations on varioues integration types Morpheus
	 * has to offer.
	 * @return An instance of the Integration Context to bused for calls by various integration types
	 * @deprecated
	 */
	@Deprecated
	MorpheusIntegrationService getIntegration();

	/**
	 * Returns the VirtualImage context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the Virtual Image Context to be used for calls by various providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusVirtualImageService getVirtualImage();

	/**
	 * Returns the Service Plan context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the Service Plan Context to be used for calls by various providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusServicePlanService getServicePlan();

	/**
	 * Returns the Compute Server context used for syncing machines within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the Compute Server Context to be used for calls by various providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusComputeServerService getComputeServer();

	/**
	 * Returns the workload context used for syncing workloads within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the workload Context to be used for calls by various providers
	 * @deprecated
	 */
	@Deprecated
	MorpheusWorkloadService getWorkload();

	/**
	 * Returns the ComputeTypeSet context
	 * @return
	 * @deprecated
	 */
	@Deprecated
	MorpheusComputeTypeSetService getComputeTypeSet();

	/**
	 * Returns the ContainerType context
	 * @return
	 * @deprecated
	 */
	@Deprecated
	MorpheusContainerTypeService getContainerType();

	/**
	 * Returns the Custom Report Types Context used for generating custom reports.
	 * Typically this should only ever be used by a report provider as it may not be accessible in all other contexts.
	 *
	 * @return an instance of the Report Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusReportService getReport();

	/**
	 * Returns the Os Type Service
	 *
	 * @return an instance of the Os Type Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusOsTypeService getOsType();

	/**
	 * Returns the Cypher Service
	 *
	 * @return an instance of the Cypher Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusCypherService getCypher();

	/**
	 * Returns the Policy Service for Governance related Policy information.
	 * @return an instance of the Policy Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusPolicyService getPolicy();

	/**
	 * Returns the Costing service and all related subservices for dealing with costing data.
	 * @return an instance of the Costing Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusCostingService getCosting();

	/**
	 * Returns the Web Request Service. This is used by UI Providers to grab common request attributes
	 *
	 * @return an instance of the web request service
	 * @deprecated
	 */
	@Deprecated
	MorpheusWebRequestService getWebRequest();

	/**
	 * Returns the Operation Notification Service
	 *
	 * @return An instance of the Operation Notification Service
	 * @deprecated
	 */
	MorpheusOperationNotificationService getOperationNotification();

	/**
	 * Returns the Tag Service
	 *
	 * @return An instance of the Tag Service
	 * @deprecated
	 */
	MorpheusMetadataTagService getMetadataTag();

	/**
	 * Returns the Wiki Page Service
	 *
	 * @return An instance of the Wiki Page Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusWikiPageService getWikiPage();

	/**
	 * Returns the StorageVolume Service
	 *
	 * @return An instance of the StorageVolume Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusStorageVolumeService getStorageVolume();

	/**
	 * Returns the StorageController Service
	 *
	 * @return An instance of the StorageController Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusStorageControllerService getStorageController();

	/**
	 * Returns the Usage Service
	 *
	 * @return An instance of the Usage Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusUsageService getUsage();

	/**
	 * Returns the Stats Service
	 *
	 * @return An instance of the Stats Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusStatsService getStats();

	/**
	 * Returns a json encoded string of the settings for the plugin requested. The settings are defined
	 * by getSettings() on Plugin
	 * @param plugin the Plugin to fetch the settings for
	 * @return a JSON encoded string representing the settings for the plugin
	 */
	Single<String> getSettings(Plugin plugin);

	/**
	 * Returns the Instance Service
	 *
	 * @return An instance of the Instance Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusInstanceService getInstance();

	/**
	 * Returns the App Service
	 *
	 * @return An instance of the App Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusAppService getApp();

	/**
	 * Returns the Snapshot service
	 * @return An instance of the Snapshot Service
	 * @deprecated
	 */
	@Deprecated
	MorpheusSnapshotService getSnapshot();

	/**
	 * Returns the factory for generating ComputeTypeLayouts
	 * @return An instance of the ComputeTypeLayoutFactory
	 */
	MorpheusComputeTypeLayoutFactoryService getComputeTypeLayoutFactoryService();

	/**
	 * Returns the Backup Context for sync, executing and restoring backups
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Context
	 * @deprecated
	 */
	@Deprecated
	MorpheusBackupService getBackup();

	/**
	 * Returns the Backup Job Context for sync, executing and restoring backup jobs
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Job Context
	 * @deprecated
	 */
	@Deprecated
	MorpheusBackupJobService getBackupJob();

	/**
	 * Returns the Process Service
	 * @return An instance of the MorpheusProcessService
	 * @deprecated
	 */
	@Deprecated
	MorpheusProcessService getProcess();

	/**
	 * Returns the Permission Service
	 * @return An instance of the MorpheusPermissionService
	 * @deprecated
	 */
	@Deprecated
	MorpheusPermissionService getPermission();

	/**
	 * Returns the MorpheusAccountCredentialTypeService
	 * @return An instance of the MorpheusAccountCredentialTypeService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountCredentialTypeService getAccountCredentialType();

	/**
	 * Returns the MorpheusAccountCredentialService
	 * @return An instance of the MorpheusAccountCredentialService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountCredentialService getAccountCredential();

	/**
	 * Returns the MorpheusKeyPairService
	 * @return An instance of the MorpheusKeyPairService
	 * @deprecated
	 */
	@Deprecated
	MorpheusKeyPairService getKeyPair();

	/**
	 * Returns the SecurityGroup Service
	 *
	 * @return An instance of the SecurityGroup Service
	 */
	MorpheusSecurityGroupService getSecurityGroup();

	/**
	 * Returns the MorpheusDashboardService
	 * @return An instance of the MorpheusDashboardService
	 * @deprecated
	 */
	@Deprecated
	MorpheusDashboardService getDashboard();

	/**
	 * Returns the MorpheusLoadBalancerService
	 * @return An instance of the MorpheusLoadBalancerService
	 * @deprecated
	 */
	@Deprecated
	MorpheusLoadBalancerService getLoadBalancer();

	/**
	 * Returns the MorpheusReferenceDataService
	 * @return an instance of the MorpheusReferenceDataService
	 * @deprecated
	 */
	@Deprecated
	MorpheusReferenceDataService getReferenceData();

	/**
	 * Returns the MorpheusBackupProviderService
	 * @return an instance of the MorpheusBackupProviderService
	 * @deprecated
	 */
	@Deprecated
	MorpheusBackupProviderService getBackupProvider();

	/**
	 * Returns the MorpheusAccountPriceSetService
	 * @return an instance of the MorpheusAccountPriceSetService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountPriceSetService getAccountPriceSet();

	/**
	 * Returns the MorpheusAccountPriceService
	 * @return an instance of the MorpheusAccountPriceService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountPriceService getAccountPrice();

	/**
	 * Returns the MorpheusServicePlanPriceSetService
	 * @return an instance of the MorpheusServicePlanPriceSetService
	 * @deprecated
	 */
	@Deprecated
	MorpheusServicePlanPriceSetService getServicePlanPriceSet();

	/**
	 * Returns the MorpheusAccountIntegrationService
	 * @return an instance of the MorpheusAccountIntegrationService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountIntegrationService getAccountIntegration();

	/**
	 * Returns the MorpheusAccountInventoryService
	 * @return an instance of the MorpheusAccountInventoryService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountInventoryService getAccountInventory();

	/**
	 * Returns the MorpheusAccountInventoryService
	 * @return an instance of the MorpheusAccountInventoryService
	 * @deprecated
	 */
	@Deprecated
	MorpheusAccountInventoryTypeService getAccountInventoryType();

	/**
	 * Returns the MorpheusCodeRepositoryService
	 * @return an instance of the MorpheusCodeRepositoryService
	 * @deprecated
	 */
	@Deprecated
	MorpheusCodeRepositoryService getCodeRepository();

	/**
	 * Returns the MorpheusWorkspaceService
	 * @return an instance of the MorpheusWorkspaceService
	 * @deprecated
	 */
	@Deprecated
	MorpheusWorkspaceService getWorkspace();

	/**
	 * Returns the MorpheusWorkspaceTypeService
	 * @return an instance of the MorpheusWorkspaceTypeService
	 * @deprecated
	 */
	@Deprecated
	MorpheusWorkspaceTypeService getWorkspaceType();

	/**
	 * Returns the MorpheusCodeRepositoryTraitService
	 * @return an instance of the MorpheusCodeRepositoryTraitService
	 * @deprecated
	 */
	@Deprecated
	MorpheusCodeRepositoryTraitService getCodeRepositoryTrait();

	/**
	 * Returns the MorpheusCodeRepositoryTraitTypeService
	 * @return an instance of the MorpheusCodeRepositoryTraitTypeService
	 * @deprecated
	 */
	@Deprecated
	MorpheusCodeRepositoryTraitTypeService getCodeRepositoryTraitType();

	//Common methods used across various contexts

	/**
	 * Execute an ssh command
	 * @param address internet address
	 * @param port port number
	 * @param username ssh username
	 * @param password ssh password
	 * @param command the command to be executed
	 * @param publicKey public key as a String
	 * @param privateKey private key as a string
	 * @param passPhrase passphrase for <code>privateKey</code>
	 * @param ignoreExitStatus defaults to false. When enabled, marks the command execution as successful, regardless of exit code
	 * @param logLevel defaults to {@link LogLevel} debug
	 * @param doPty Allocate a Pseudo-Terminal
	 * @param runAsUser specify a user to run the command as
	 * @param sudo execute the command with sudo permissions
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeSshCommand(String address, Integer port, String username, String password, String command, String publicKey, String privateKey, String passPhrase, Boolean ignoreExitStatus, LogLevel logLevel, Boolean doPty, String runAsUser, Boolean sudo);

	/**
	 * Execute a command on a Windows machine
	 *
	 * @param address internet address
	 * @param port port number
	 * @param username connection username
	 * @param password connection password
	 * @param command the command to be executed
	 * @param noProfile add a <code>–noprofile</code> argument to PowerShell
	 * @param elevated use elevated privileges
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeWindowsCommand(String address, Integer port, String username, String password, String command, Boolean noProfile, Boolean elevated);


	/**
	 * Execute a command on a Container or VM using the standard connection details
	 *
	 * @param container resource to execute on which to execute the command
	 * @param command the command to be executed
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeCommandOnWorkload(Container container, String command);

	/**
	 * Execute a command on a Container or VM using custom connection details
	 *
	 * @param container resource to execute on which to execute the command
	 * @param command the command to be executed
	 * @param sshUsername username
	 * @param sshPassword password
	 * @param publicKey public key as a String
	 * @param privateKey private key as a String
	 * @param passPhrase passphrase for <code>privateKey</code>
	 * @param noProfile for Windows VMs, add a <code>–noprofile</code> argument to PowerShell
	 * @param runAsUser run the command as a specific user
	 * @param sudo execute the command with sudo permissions
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeCommandOnWorkload(Container container, String command, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, String runAsUser, Boolean sudo);

	/**
	 *  Execute a command on a server using the default connection details
	 *
	 * @param server server on which to execute the command
	 * @param command the command to be executed
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command);

	/**
	 * Execute a command on a server using custom connection details
	 *
	 * @param server server on which to execute the command
	 * @param command the command to be executed
	 * @param rpc when enabled, override the agent mode and execute over ssh/winrm
	 * @param sshUsername username
	 * @param sshPassword password
	 * @param publicKey public key as a String
	 * @param privateKey private key as a String
	 * @param passPhrase passphrase for <code>privateKey</code>
	 * @param noProfile for Windows VMs, add a <code>–noprofile</code> argument to PowerShell
	 * @param sudo execute the command with sudo permissions
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command, Boolean rpc, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, Boolean sudo);

	/**
	 * Trigger an agent upgrade operation on a server
	 * @since 1.1.6
	 * @param serverId the ID of the {@link ComputeServer} object to initiate an agent upgrade on
	 */
	void queueUpgradeServerAgent(Long serverId);


	/**
	 * Execute a command on a server using custom connection details
	 *
	 * @param server server on which to execute the command
	 * @param command the command to be executed
	 * @param rpc when enabled, override the agent mode and execute over ssh/winrm
	 * @param sshUsername username
	 * @param sshPassword password
	 * @param publicKey public key as a String
	 * @param privateKey private key as a String
	 * @param passPhrase passphrase for <code>privateKey</code>
	 * @param noProfile for Windows VMs, add a <code>–noprofile</code> argument to PowerShell
	 * @param sudo execute the command with sudo permissions
	 * @param guestExec execute the command with guest execution
	 * @return A result object detailing the command execution
	 */
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command, Boolean rpc, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, Boolean sudo, Boolean guestExec);

	Single<TaskConfig> buildInstanceConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts);

	Single<TaskConfig> buildContainerConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts);

	Single<TaskConfig> buildWorkloadConfig(Workload workload, Map baseConfig, Task task, Collection excludes, Map opts);

	Single<TaskConfig> buildComputeServerConfig(ComputeServer container, Map baseConfig, Task task, Collection excludes, Map opts);

	/**
	 * Acquires a distributed lock by key and some additional lock options can be provided
	 * @param name the key name of the lock to acquire
	 * @param opts the acquire wait timeout option via key [timeout:ms] as well as the locks ttl via [ttl:ms] property.
	 * @return a unique lock key id to control concurrent release attempts. send this to releaseLocks opts.lock key
	 *
	 * <p><strong>Example:</strong> </p>
	 * <pre>{@code
	 * String lockId
	 * try {
	 *     lockId = morpheusContext.acquireLock('mylock.key',[ttl:600000L,timeout:600000L]);
	 *     //do stuff
	 * } finally {
	 *     if(lockId) {
	 *         morpheusContext.releaseLock('mylock.key',[lock: lockId]);
	 *     }
	 * }
	 * }</pre>
	 */
	Single<String> acquireLock(String name, Map<String,Object> opts);

	/**
	 * Releases a lock key for other threads or nodes to be able to use it.
	 * It takes an optional set of opts that can be used to scope the lock release to a key hash for concurrency safety
	 * @param name the key name of the lock to release
	 * @param opts the opts map of wait timeouts or [lock:lockId] where the lockId is the return of {@link MorpheusContext#acquireLock(String, Map)}
	 * @return the success state of the release lock attempt
	 */
	Single<Boolean> releaseLock(String name, Map<String,Object> opts);

	/**
	 * Returns the unique lock key id for the specified lock key name if exists else null.
	 * It takes an optional set of opts that can be used to scope the lock release to a key hash for concurrency safety
	 * @param name
	 * @param opts
	 * @return the unique lock key id to control concurrent release attempts. send this to releaseLocks opts.lock key
	 */
	Maybe<String> checkLock(String name, Map<String,Object> opts);
}
