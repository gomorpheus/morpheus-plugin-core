package com.morpheusdata.core;

import com.morpheusdata.core.synchronous.backup.MorpheusSynchronousBackupRepositoryService;
import com.morpheusdata.core.guidance.MorpheusSynchronousAccountDiscoveryService;
import com.morpheusdata.core.library.MorpheusWorkloadTypeService;
import com.morpheusdata.core.synchronous.*;
import com.morpheusdata.core.synchronous.admin.MorpheusSynchronousAdminService;
import com.morpheusdata.core.synchronous.backup.MorpheusSynchronousBackupJobService;
import com.morpheusdata.core.synchronous.backup.MorpheusSynchronousBackupProviderService;
import com.morpheusdata.core.synchronous.backup.MorpheusSynchronousBackupService;
import com.morpheusdata.core.synchronous.cloud.MorpheusSynchronousCloudService;
import com.morpheusdata.core.synchronous.compute.MorpheusSynchronousComputeServerService;
import com.morpheusdata.core.synchronous.costing.MorpheusSynchronousCostingService;
import com.morpheusdata.core.synchronous.cypher.MorpheusSynchronousCypherService;
import com.morpheusdata.core.synchronous.dashboard.MorpheusSynchronousDashboardService;
import com.morpheusdata.core.synchronous.integration.MorpheusSynchronousAccountInventoryService;
import com.morpheusdata.core.synchronous.integration.MorpheusSynchronousIntegrationService;
import com.morpheusdata.core.synchronous.library.MorpheusSynchronousLibraryService;
import com.morpheusdata.core.synchronous.MorpheusSynchronousComputeTypeSetService;
import com.morpheusdata.core.synchronous.library.MorpheusSynchronousWorkloadTypeService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkSubnetService;
import com.morpheusdata.core.synchronous.MorpheusSynchronousExecuteScheduleTypeService;
import com.morpheusdata.core.synchronous.MorpheusSynchronousVirtualImageService;
import com.morpheusdata.core.synchronous.network.loadbalancer.MorpheusSynchronousLoadBalancerPartitionService;
import com.morpheusdata.core.synchronous.policy.MorpheusSynchronousPolicyService;
import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.core.providers.TaskProvider;
import com.morpheusdata.core.synchronous.provisioning.MorpheusSynchronousProvisionService;
import com.morpheusdata.core.web.MorpheusWebRequestService;
import com.morpheusdata.core.localization.MorpheusLocalizationService;
import com.morpheusdata.model.BackupProvider;

public interface MorpheusServices {

	/**
	 * Returns the Compute Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the Cloud Service to be used typically by {@link com.morpheusdata.core.providers.CloudProvider} implementations.
	 */
	MorpheusSynchronousCloudService getCloud();

	/**
	 * Returns the ComputeSite Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the ComputeSite Service to be used typically by {@link CloudProvider} implementations.
	 */
	MorpheusSynchronousComputeSiteService getComputeSite();

	/**
	 * Returns the Provision Service used for performing provisioning related updates to objects.
	 * @return An Instance of the Provision Service to be used typically by a {@link ProvisionProvider}
	 */
	MorpheusSynchronousProvisionService getProvision();


	/**
	 * Returns the NetworkContext used for performing updates or queries on network related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An Instance of the Network Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkService getNetwork();

	/**
	 * Returns the NetworkSubnetContext used for performing updates or queries on network subnet related assets within Morpheus.
	 * @return An Instance of the NetworkSubnet Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkSubnetService getNetworkSubnet();

	/**
	 * Returns the Task context used for automation tasks on assets within Morpheus.
	 * Typically this would be called by a {@link TaskProvider}.
	 * @return An Instance of the Task Context to be used for calls by various task providers
	 */
	MorpheusSynchronousTaskService getTask();

	/**
	 * Returns the VirtualImage context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Virtual Image Context to be used for calls by various providers
	 */
	MorpheusSynchronousVirtualImageService getVirtualImage();

	/**
	 * Returns the Service Plan context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Service Plan Context to be used for calls by various providers
	 */
	MorpheusSynchronousServicePlanService getServicePlan();

	/**
	 * Returns the Price Plan context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Price Plan Context to be used for calls by various providers
	 */
	MorpheusSynchronousPricePlanService getPricePlan();

	/**
	 * Returns the Compute Server context used for syncing machines within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Compute Server Context to be used for calls by various providers
	 */
	MorpheusSynchronousComputeServerService getComputeServer();

	/**
	 * Returns the workload context used for syncing workloads within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the workload Context to be used for calls by various providers
	 */
	MorpheusSynchronousWorkloadService getWorkload();

	/**
	 * Returns the ComputeTypeSet context
	 * @return
	 */
	MorpheusSynchronousComputeTypeSetService getComputeTypeSet();

	/**
	 * Returns the ContainerType context
	 * @return
	 */
	@Deprecated
	MorpheusSynchronousContainerTypeService getContainerType();

	/**
	 * Returns the WorkloadType context
	 * @return an instance of the Workload Type context
	 */
	MorpheusSynchronousWorkloadTypeService getWorkloadType();

	/**
	 * Returns the Os Type Service
	 *
	 * @return an instance of the Os Type Service
	 */
	MorpheusSynchronousOsTypeService getOsType();

	/**
	 * Returns the Cypher Service
	 *
	 * @return an instance of the Cypher Service
	 */
	MorpheusSynchronousCypherService getCypher();

	/**
	 * Returns the Policy Service for Governance related Policy information.
	 * @return an instance of the Policy Service
	 */
	MorpheusSynchronousPolicyService getPolicy();

	/**
	 * Returns the Costing service and all related subservices for dealing with costing data.
	 * @return an instance of the Costing Service
	 */
	MorpheusSynchronousCostingService getCosting();

	/**
	 * Returns the Operation Notification Service
	 *
	 * @return An instance of the Operation Notification Service
	 */
	MorpheusSynchronousOperationNotificationService getOperationNotification();

	/**
	 * Returns the Tag Service
	 *
	 * @return An instance of the Tag Service
	 */
	MorpheusSynchronousMetadataTagService getMetadataTag();

	/**
	 * Returns the Setting Service
	 *
	 * @return An instance of the Setting Service
	 */
	MorpheusSynchronousSettingService getSetting();

	/**
	 * Returns the Wiki Page Service
	 *
	 * @return An instance of the Wiki Page Service
	 */
	MorpheusSynchronousWikiPageService getWikiPage();

	/**
	 * Returns the StorageVolume Service
	 *
	 * @return An instance of the StorageVolume Service
	 */
	MorpheusSynchronousStorageVolumeService getStorageVolume();

	/**
	 * Returns the StorageController Service
	 *
	 * @return An instance of the StorageController Service
	 */
	MorpheusSynchronousStorageControllerService getStorageController();

	/**
	 * Returns the StorageServer Service
	 *
	 * @return An instance of the StorageServer Service
	 */
	MorpheusSynchronousStorageServerService getStorageServer();

	/**
	 * Returns the StorageBucket Service
	 *
	 * @return An instance of the StorageBucket Service
	 */
	MorpheusSynchronousStorageBucketService getStorageBucket();

	/**
	 * Returns the Instance Service
	 *
	 * @return An instance of the Instance Service
	 */
	MorpheusSynchronousInstanceService getInstance();

	/**
	 * Returns the App Service
	 *
	 * @return An instance of the App Service
	 */
	MorpheusSynchronousAppService getApp();

	/**
	 * Returns the Snapshot service
	 * @return An instance of the Snapshot Service
	 */
	MorpheusSynchronousSnapshotService getSnapshot();

	/**
	 * Returns the Backup Context for sync, executing and restoring backups
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Context
	 */
	MorpheusSynchronousBackupService getBackup();

	/**
	 * Returns the Backup Job Context for sync, executing and restoring backup jobs
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Job Context
	 */
	MorpheusSynchronousBackupJobService getBackupJob();

	/**
	 * Returns the Backup Repository Context for sync of backup repositories.
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Repository Context
	 */
	MorpheusSynchronousBackupRepositoryService getBackupRepository();

	/**
	 * Returns the Permission Service
	 * @return An instance of the MorpheusSynchronousPermissionService
	 */
	MorpheusSynchronousPermissionService getPermission();

	/**
	 * Returns the Resource Permission Service
	 * @return An instance of the MorpheusSynchronousPermissionService
	 */
	MorpheusSynchronousResourcePermissionService getResourcePermission();

	/**
	 * Returns the MorpheusSynchronousAccountCredentialTypeService
	 * @return An instance of the MorpheusSynchronousAccountCredentialTypeService
	 */
	MorpheusSynchronousAccountCredentialTypeService getAccountCredentialType();

	/**
	 * Returns the MorpheusSynchronousAccountCredentialService
	 * @return An instance of the MorpheusSynchronousAccountCredentialService
	 */
	MorpheusSynchronousAccountCredentialService getAccountCredential();

	/**
	 * Returns the MorpheusSynchronousKeyPairService
	 * @return An instance of the MorpheusSynchronousKeyPairService
	 */
	MorpheusSynchronousKeyPairService getKeyPair();

	/**
	 * Returns the SecurityGroup Service
	 *
	 * @return An instance of the SecurityGroup Service
	 */
	MorpheusSynchronousSecurityGroupService getSecurityGroup();

	/**
	 * Returns the MorpheusSynchronousDashboardService
	 * @return An instance of the MorpheusSynchronousDashboardService
	 */
	MorpheusSynchronousDashboardService getDashboard();

	/**
	 * Returns the MorpheusSynchronousLoadBalancerPartitionService
	 * @return An instance of the MorpheusSynchronousLoadBalancerPartitionService
	 */
	MorpheusSynchronousLoadBalancerPartitionService getLoadBalancer();

	/**
	 * Returns the MorpheusSynchronousReferenceDataService
	 * @return an instance of the MorpheusSynchronousReferenceDataService
	 */
	MorpheusSynchronousReferenceDataService getReferenceData();

	/**
	 * Returns the MorpheusSynchronousOperationDataService providing a means to access integration specific custom data
	 * that may have been synced in.
	 * @return an instance of the MorpheusSynchronousOperationDataService
	 */
	MorpheusSynchronousOperationDataService getOperationData();

	/**
	 * Returns the MorpheusSynchronousBackupProviderService
	 * @return an instance of the MorpheusSynchronousBackupProviderService
	 */
	MorpheusSynchronousBackupProviderService getBackupProvider();

	/**
	 * Returns the MorpheusSynchronousAccountPriceSetService
	 * @return an instance of the MorpheusSynchronousAccountPriceSetService
	 */
	MorpheusSynchronousAccountPriceSetService getAccountPriceSet();

	/**
	 * Returns the MorpheusSynchronousAccountPriceService
	 * @return an instance of the MorpheusSynchronousAccountPriceService
	 */
	MorpheusSynchronousAccountPriceService getAccountPrice();

	/**
	 * Returns the MorpheusSynchronousServicePlanPriceSetService
	 * @return an instance of the MorpheusSynchronousServicePlanPriceSetService
	 */
	MorpheusSynchronousServicePlanPriceSetService getServicePlanPriceSet();

	/**
	 * Returns the MorpheusSynchronousPricePlanPriceSetService
	 * @return an instance of the MorpheusSynchronousPricePlanPriceSetService
	 */
	MorpheusSynchronousPricePlanPriceSetService getPricePlanPriceSet();

	/**
	 * Returns the MorpheusSynchronousAccountIntegrationService
	 * @return an instance of the MorpheusSynchronousAccountIntegrationService
	 */
	MorpheusSynchronousAccountIntegrationService getAccountIntegration();

	/**
	 * Returns the MorpheusSynchronousAccountInventoryService
	 * @return an instance of the MorpheusSynchronousAccountInventoryService
	 */
	MorpheusSynchronousAccountInventoryService getAccountInventory();

	/**
	 * Returns the {@link MorpheusSynchronousAdminService} which allows access to {@link MorpheusSynchronousDataService} related services
	 * for administrative objects (i.e. Users,Accounts,etc) in a synchronous manner.
	 * @return an instance of MorpheusSynchronousAdminService
	 */
	MorpheusSynchronousAdminService getAdmin();

	/**
	 * return the {@link MorpheusSynchronousAccountCertificateService} which allows access to {@link com.morpheusdata.model.AccountCertificate}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusSynchronousAccountCertificateService}
	 */
	MorpheusSynchronousAccountCertificateService getCertificate();

	/**
	 * return the {@link MorpheusSynchronousAccountCertificateService} which allows access to {@link com.morpheusdata.model.AccountCertificate}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusSynchronousAccountCertificateService}
	 */
	MorpheusSynchronousWorkloadStateService getWorkloadState();

	/**
	 * return the {@link MorpheusSynchronousAccountResourceTypeService} which allows access to {@link com.morpheusdata.model.AccountResourceType}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusSynchronousAccountResourceTypeService}
	 */
	MorpheusSynchronousAccountResourceTypeService getAccountResourceType();

	/**
	 * return the {@link MorpheusSynchronousResourceSpecService} which allows access to {@link com.morpheusdata.model.ResourceSpec}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusSynchronousResourceSpecService}
	 */
	MorpheusSynchronousResourceSpecService getResourceSpec();

	/**
	 * return the {@link MorpheusSynchronousInstanceTypeLayoutService} which allows access to {@link com.morpheusdata.model.InstanceTypeLayout}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusSynchronousInstanceTypeLayoutService}
	 */
	MorpheusSynchronousInstanceTypeLayoutService getInstanceTypeLayout();

	/**
	 * Returns the {@link MorpheusSynchronousLibraryService } which contains all library services
	 * @return an instance of the MorpheusSynchronousLibraryServices
	 */
	MorpheusSynchronousLibraryService getLibrary();

	/**
	 * Returns the {@link MorpheusSynchronousCodeRepositoryService } which contains all code repository services
	 * @return an instance of the MorpheusSynchronousCodeRepositoryService
	 */
	MorpheusSynchronousCodeRepositoryService getCodeRepository();

	/**
	 * Returns the Integration context used for performing common operations on various integration types Morpheus
	 * has to offer.
	 * @return An instance of the Integration Context to bused for calls by various integration types
	 */
	MorpheusSynchronousIntegrationService getIntegration();

	/**
	 * Returns the Web Request Service. This is used by UI Providers to grab common request attributes
	 *
	 * @return an instance of the web request service
	 */
	MorpheusWebRequestService getWebRequest();

	/**
	 * Returns the localization services. Used by other services to fetch localized strings from
	 * localization codes.
	 * @return an instance of the localization service
	 */
	MorpheusLocalizationService getLocalization();

	/**
	 * Returns the {@link MorpheusSeedService } which allows access to seed data operations
	 * @return an instance of {@link MorpheusSeedService }
	 */
	MorpheusSynchronousSeedService getSeed();

	/**
	 * Returns the {@link MorpheusSynchronousAccountDiscoveryService} which allows access to create guidance
	 * recommendations
	 * @return an instance of {@link MorpheusSynchronousAccountDiscoveryService}
	 */
	MorpheusSynchronousAccountDiscoveryService getDiscovery();

	/**
	 * Returns the {@link MorpheusSynchronousExecuteScheduleTypeService} which allows access to execute schedule types
	 */
	MorpheusSynchronousExecuteScheduleTypeService getExecuteScheduleType();

	/**
	 * Returns the {@link MorpheusExecuteScheduleService } which allows access to execute schedule data operations
	 * @return an instance of {@link MorpheusExecuteScheduleService }
	 */
	MorpheusSynchronousExecuteScheduleService getExecuteSchedule();
}
