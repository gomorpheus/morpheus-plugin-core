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

import com.morpheusdata.core.backup.MorpheusBackupJobService;
import com.morpheusdata.core.backup.MorpheusBackupRepositoryService;
import com.morpheusdata.core.backup.MorpheusBackupProviderService;
import com.morpheusdata.core.backup.MorpheusBackupService;
import com.morpheusdata.core.cloud.MorpheusCloudService;
import com.morpheusdata.core.costing.MorpheusCostingService;
import com.morpheusdata.core.cypher.MorpheusCypherService;
import com.morpheusdata.core.dashboard.MorpheusDashboardService;
import com.morpheusdata.core.guidance.MorpheusAccountDiscoveryService;
import com.morpheusdata.core.integration.MorpheusAccountInventoryService;
import com.morpheusdata.core.integration.MorpheusIntegrationService;
import com.morpheusdata.core.integration.MorpheusLlmService;
import com.morpheusdata.core.library.MorpheusLibraryService;
import com.morpheusdata.core.library.MorpheusWorkloadTypeService;
import com.morpheusdata.core.network.MorpheusNetworkService;
import com.morpheusdata.core.network.MorpheusNetworkSubnetService;
import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerService;
import com.morpheusdata.core.policy.MorpheusPolicyService;
import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.core.providers.TaskProvider;
import com.morpheusdata.core.provisioning.MorpheusProvisionService;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.core.admin.MorpheusAdminService;
import com.morpheusdata.core.MorpheusUpdateDefinitionService;
import com.morpheusdata.core.system.MorpheusSystemService;

public interface MorpheusAsyncServices {
	/**
	 * Returns the Compute Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the Cloud Service to be used typically by {@link com.morpheusdata.core.providers.CloudProvider} implementations.
	 */
	MorpheusCloudService getCloud();

	/**
	 * Returns the ComputeSite Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the ComputeSite Service to be used typically by {@link CloudProvider} implementations.
	 */
	MorpheusComputeSiteService getComputeSite();

	/**
	 * Returns the Provision Service used for performing provisioning related updates to objects.
	 * @return An Instance of the Provision Service to be used typically by a {@link ProvisionProvider}
	 */
	MorpheusProvisionService getProvision();


	/**
	 * Returns the NetworkContext used for performing updates or queries on network related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An Instance of the Network Context to be used for calls by various network providers
	 */
	MorpheusNetworkService getNetwork();

	/**
	 * Returns the NetworkSubnetContext used for performing updates or queries on network subnet related assets within Morpheus.
	 * @return An Instance of the NetworkSubnet Context to be used for calls by various network providers
	 */
	MorpheusNetworkSubnetService getNetworkSubnet();

	/**
	 * Returns the Task context used for automation tasks on assets within Morpheus.
	 * Typically this would be called by a {@link TaskProvider}.
	 * @return An Instance of the Task Context to be used for calls by various task providers
	 */
	MorpheusTaskService getTask();

	/**
	 * Returns the Integration context used for performing common operations on varioues integration types Morpheus
	 * has to offer.
	 * @return An instance of the Integration Context to bused for calls by various integration types
	 */
	MorpheusIntegrationService getIntegration();

	/**
	 * Returns the VirtualImage context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Virtual Image Context to be used for calls by various providers
	 */
	MorpheusVirtualImageService getVirtualImage();

	/**
	 * Returns the Service Plan context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Service Plan Context to be used for calls by various providers
	 */
	MorpheusServicePlanService getServicePlan();

	/**
	 * Returns the Price Plan context used for syncing Cloud images within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Price Plan Context to be used for calls by various providers
	 */
	MorpheusPricePlanService getPricePlan();

	/**
	 * Returns the Compute Server context used for syncing machines within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.providers.CloudProvider}.
	 * @return An instance of the Compute Server Context to be used for calls by various providers
	 */
	MorpheusComputeServerService getComputeServer();

	/**
	 * Returns the Clusters / Compute Server Groups used for syncing clusters within morpheus (k8s or other types)
	 * @return An instance of the Compute Server Group Context to be used for calls by various providers
	 * @see com.morpheusdata.model.ComputeServerGroup
	 */
	MorpheusComputeServerGroupService getCluster();

	/**
	 * Returns the workload context used for syncing workloads within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the workload Context to be used for calls by various providers
	 */
	MorpheusWorkloadService getWorkload();

	/**
	 * Returns the ComputeTypeSet context
	 * @return
	 */
	MorpheusComputeTypeSetService getComputeTypeSet();

	/**
	 * Returns the ComputeTypeLayout context for managing compute type layouts asynchronously.
	 * @return An instance of the ComputeTypeLayout service
	 */
	MorpheusComputeTypeLayoutService getComputeTypeLayout();

	/**
	 * Returns the ContainerType context
	 * @return
	 */
	@Deprecated
	MorpheusContainerTypeService getContainerType();

	/**
	 * Returns the WorkloadType context
	 * @return an instance of the Workload Type context
	 */
	MorpheusWorkloadTypeService getWorkloadType();

	/**
	 * Returns the Custom Report Types Context used for generating custom reports.
	 * Typically this should only ever be used by a report provider as it may not be accessible in all other contexts.
	 *
	 * @return an instance of the Report Service
	 */
	MorpheusReportService getReport();

	/**
	 * Returns the Os Type Service
	 *
	 * @return an instance of the Os Type Service
	 */
	MorpheusOsTypeService getOsType();

	/**
	 * Returns the Cypher Service
	 *
	 * @return an instance of the Cypher Service
	 */
	MorpheusCypherService getCypher();

	/**
	 * Returns the Policy Service for Governance related Policy information.
	 * @return an instance of the Policy Service
	 */
	MorpheusPolicyService getPolicy();

	/**
	 * Returns the Costing service and all related subservices for dealing with costing data.
	 * @return an instance of the Costing Service
	 */
	MorpheusCostingService getCosting();

	/**
	 * Returns the Operation Notification Service
	 *
	 * @return An instance of the Operation Notification Service
	 */
	MorpheusOperationNotificationService getOperationNotification();

	/**
	 * Returns the Operation Event Service
	 *
	 * @return An instance of the Operation Event Service
	 */
	MorpheusOperationEventService getOperationEvent();

	/**
	 * Returns the Tag Service
	 *
	 * @return An instance of the Tag Service
	 */
	MorpheusMetadataTagService getMetadataTag();

	/**
	 * Returns the Setting Service
	 *
	 * @return An instance of the Setting Service
	 */
	MorpheusSettingService getSetting();

	/**
	 * Returns the Wiki Page Service
	 *
	 * @return An instance of the Wiki Page Service
	 */
	MorpheusWikiPageService getWikiPage();

	/**
	 * Returns the StorageVolume Service
	 * This has been moved to the {@link MorpheusStorageService} as of 1.2.5 and can be accessed via the {@link MorpheusAsyncServices#getStorage()}
	 * @return An instance of the StorageVolume Service
	 * @deprecated
	 */
	@Deprecated(since="1.2.5")
	MorpheusStorageVolumeService getStorageVolume();

	/**
	 * Returns the StorageController Service
	 * This has been moved to the {@link MorpheusStorageService} as of 1.2.5 and can be accessed via the {@link MorpheusAsyncServices#getStorage()}
	 * @return An instance of the StorageController Service
	 * @deprecated
	 */
	@Deprecated(since="1.2.5")
	MorpheusStorageControllerService getStorageController();

	/**
	 * Returns the StorageServer Service
	 * This has been moved to the {@link MorpheusStorageService} as of 1.2.5 and can be accessed via the {@link MorpheusAsyncServices#getStorage()}
	 * @return An instance of the StorageServer Service
	 * @deprecated
	 */
	@Deprecated(since="1.2.5")
	MorpheusStorageServerService getStorageServer();

	/**
	 * Returns the StorageBucket Service
	 * This has been moved to the {@link MorpheusStorageService} as of 1.2.5 and can be accessed via the {@link MorpheusAsyncServices#getStorage()}
	 * @see MorpheusStorageService
	 * @return An instance of the StorageBucket Service
	 * @deprecated
	 */
	@Deprecated(since="1.2.5")
	MorpheusStorageBucketService getStorageBucket();

	/**
	 * Returns the Storage Service Context for accessing storage related services
	 * @since 1.2.5
	 * @return An instance of the Storage Service
	 */
	MorpheusStorageService getStorage();

	/**
	 * Returns the StorageHost Service
	 *
	 * @return An instance of the StorageHost Service
	 */
	MorpheusStorageHostService getStorageHost();

	/**
	 * Returns the StorageHostGroup Service
	 *
	 * @return An instance of the StorageHostGroup Service
	 */
	MorpheusStorageHostGroupService getStorageHostGroup();
	/**
	 * Returns the Usage Service
	 *
	 * @return An instance of the Usage Service
	 */
	MorpheusUsageService getUsage();

	/**
	 * Returns the Stats Service
	 *
	 * @return An instance of the Stats Service
	 */
	MorpheusStatsService getStats();

	/**
	 * Returns the Instance Service
	 *
	 * @return An instance of the Instance Service
	 */
	MorpheusInstanceService getInstance();

	/**
	 * Returns the App Service
	 *
	 * @return An instance of the App Service
	 */
	MorpheusAppService getApp();

	/**
	 * Returns the Snapshot service
	 * @return An instance of the Snapshot Service
	 */
	MorpheusSnapshotService getSnapshot();

	/**
	 * Returns the Backup Context for sync, executing and restoring backups
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Context
	 */
	MorpheusBackupService getBackup();

	/**
	 * Returns the Backup Job Context for sync, executing and restoring backup jobs
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Job Context
	 */
	MorpheusBackupJobService getBackupJob();

	/**
	 * Returns the Backup Repository Context for sync of backup repositories.
	 * Typically this would be called by a {@link BackupProvider}.
	 *
	 * @return an instance of the Backup Repository Context
	 */
	MorpheusBackupRepositoryService getBackupRepository();

	/**
	 * Returns the Process Service
	 * @return An instance of the MorpheusProcessService
	 */
	MorpheusProcessService getProcess();

	/**
	 * Returns the Permission Service
	 * @return An instance of the MorpheusPermissionService
	 */
	MorpheusPermissionService getPermission();

	/**
	 * Returns the Resource Permission Service
	 * @return An instance of the MorpheusPermissionService
	 */
	MorpheusResourcePermissionService getResourcePermission();

	/**
	 * Returns the MorpheusAccountCredentialTypeService
	 * @return An instance of the MorpheusAccountCredentialTypeService
	 */
	MorpheusAccountCredentialTypeService getAccountCredentialType();

	/**
	 * Returns the MorpheusAccountCredentialService
	 * @return An instance of the MorpheusAccountCredentialService
	 */
	MorpheusAccountCredentialService getAccountCredential();

	/**
	 * Returns the MorpheusKeyPairService
	 * @return An instance of the MorpheusKeyPairService
	 */
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
	 */
	MorpheusDashboardService getDashboard();

	/**
	 * Returns the MorpheusLoadBalancerService
	 * @return An instance of the MorpheusLoadBalancerService
	 */
	MorpheusLoadBalancerService getLoadBalancer();

	/**
	 * Returns the MorpheusReferenceDataService
	 * @return an instance of the MorpheusReferenceDataService
	 */
	MorpheusReferenceDataService getReferenceData();

	/**
	 * Returns the MorpheusOperationDataService providing a means to access integration specific custom data
	 * that may have been synced in.
	 * @return an instance of the MorpheusOperationDataService
	 */
	MorpheusOperationDataService getOperationData();

	/**
	 * Returns the MorpheusBackupProviderService
	 * @return an instance of the MorpheusBackupProviderService
	 */
	MorpheusBackupProviderService getBackupProvider();

	/**
	 * Returns the MorpheusAccountPriceSetService
	 * @return an instance of the MorpheusAccountPriceSetService
	 */
	MorpheusAccountPriceSetService getAccountPriceSet();

	/**
	 * Returns the MorpheusAccountPriceSetService
	 * @return an instance of the MorpheusAccountPriceSetService
	 */
	MorpheusAccountPriceHistoryService getAccountPriceHistory();

	/**
	 * Returns the MorpheusAccountPriceService
	 * @return an instance of the MorpheusAccountPriceService
	 */
	MorpheusAccountPriceService getAccountPrice();

	/**
	 * Returns the MorpheusServicePlanPriceSetService
	 * @return an instance of the MorpheusServicePlanPriceSetService
	 */
	MorpheusServicePlanPriceSetService getServicePlanPriceSet();

	/**
	 * Returns the MorpheusPricePlanPriceSetService
	 * @return an instance of the MorpheusPricePlanPriceSetService
	 */
	MorpheusPricePlanPriceSetService getPricePlanPriceSet();

	/**
	 * Returns the MorpheusAccountIntegrationService
	 * @return an instance of the MorpheusAccountIntegrationService
	 */
	MorpheusAccountIntegrationService getAccountIntegration();

	/**
	 * Returns the MorpheusAccountInventoryService
	 * @return an instance of the MorpheusAccountInventoryService
	 */
	MorpheusAccountInventoryService getAccountInventory();

	/**
	 * Returns the {@link MorpheusAdminService} which allows access to {@link MorpheusDataService} related services
	 * for administrative objects (i.e. Users,Accounts,etc)
	 * @return an instance of MorpheusAdminService
	 */
	MorpheusAdminService getAdmin();

	/**
	 * Returns the {@link MorpheusSystemService} which allows access to {@link MorpheusDataService} related services
	 * for System objects. Sub-services for SystemType and SystemComponent are accessible via
	 * {@link MorpheusSystemService#getType()} and {@link MorpheusSystemService#getComponent()}.
	 * @return an instance of MorpheusSystemService
	 */
	MorpheusSystemService getSystem();

	/**
	 * return the {@link MorpheusAccountCertificateService} which allows access to {@link com.morpheusdata.model.AccountCertificate}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusAccountCertificateService}
	 */
	MorpheusAccountCertificateService getCertificate();

	/**
	 * return the {@link MorpheusAccountCertificateService} which allows access to {@link com.morpheusdata.model.AccountCertificate}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusAccountCertificateService}
	 */
	MorpheusWorkloadStateService getWorkloadState();

	/**
	 * return the {@link MorpheusAccountResourceTypeService} which allows access to {@link com.morpheusdata.model.AccountResourceType}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusAccountResourceTypeService}
	 */
	MorpheusAccountResourceTypeService getAccountResourceType();

	/**
	 * return the {@link MorpheusResourceSpecService} which allows access to {@link com.morpheusdata.model.ResourceSpec}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusResourceSpecService}
	 */
	MorpheusResourceSpecService getResourceSpec();

	/**
	 * return the {@link MorpheusInstanceTypeLayoutService} which allows access to {@link com.morpheusdata.model.InstanceTypeLayout}
	 * data operations such as create/update/query operations
	 * @return an instance {@link MorpheusInstanceTypeLayoutService}
	 */
	MorpheusInstanceTypeLayoutService getInstanceTypeLayout();

	/**
	 * Returns the {@link MorpheusLibraryService } which contains all library services
	 * @return an instance of the MorpheusLibraryServices
	 */
	MorpheusLibraryService getLibrary();

	/**
	 * Returns the {@link MorpheusCodeRepositoryService } which contains all code repository services
	 * @return an instance of the MorpheusCodeRepositoryService
	 */
	MorpheusCodeRepositoryService getCodeRepository();

	/**
	 * Returns the {@link MorpheusSeedService } which allows access to seed data operations
	 * @return an instance of {@link MorpheusSeedService }
	 */
	MorpheusSeedService getSeed();

	/**
	 * Returns the {@link MorpheusAccountDiscoveryService} which allows access to creating guidance
	 * recommendations
	 * @return an instance of {@link MorpheusAccountDiscoveryService}
	 */
	MorpheusAccountDiscoveryService getDiscovery();

	/**
	 * Returns the {@link MorpheusExecuteScheduleTypeService} which allows access to execute schedule types
	 */
	MorpheusExecuteScheduleTypeService getExecuteScheduleType();

	/**
	 * Returns the {@link MorpheusExecuteScheduleService} which allows access to execute schedule operations
	 * @return on instance of {@link MorpheusExecuteScheduleService}
	 */
	MorpheusExecuteScheduleService getExecuteSchedule();

	/**
	 * Returns the {@link MorpheusOperationService} which allows access to operation related services
	 * @return on instance of {@link MorpheusOperationService}
	 */
	MorpheusOperationService getOperation();

	/**
	 * Returns the {@link MorpheusFileCopyService} which allows access to file copy services
	 * @return an instance of {@link MorpheusFileCopyService}
	 */
	MorpheusFileCopyService getFileCopy();

	/**
	 * Returns the {@link MorpheusHypervisorService} which allows access to hypervisor services
	 * @return an instance of {@link MorpheusHypervisorService}
	 */
	MorpheusHypervisorService getHypervisorService();

	/**
	 * Returns the {@link MorpheusNetworkResourceGroupService} which allows access to network resource group services
	 * @return an instance of {@link MorpheusNetworkResourceGroupService}
	 */
	MorpheusNetworkResourceGroupService getNetworkResourceGroup();

	/**
	 * Returns the {@link MorpheusComputeTypePackageService} which allows access to compute type package services
	 * @return an instance of {@link MorpheusComputeTypePackageService}
	 */
	MorpheusComputeTypePackageService getComputeTypePackage();

	/**
	 * Returns the Currency Conversion Service for handling currency conversion operations.
	 *
	 * @return an instance of the MorpheusCurrencyConversionService
	 */
	MorpheusCurrencyConversionService getCurrency();

	MorpheusAffinityGroupService getAffinityGroup();

	/**
	 * Returns the MorpheusLlmService for managing LLM Engine model and integration contexts.
	 * @return an instance of the MorpheusLlmService
	 */
	MorpheusLlmService getLlm();

	/**
	 * Returns the {@link MorpheusUpdateDefinitionService} for async CRUD access to {@link com.morpheusdata.model.UpdateDefinition} objects.
	 * @return an instance of {@link MorpheusUpdateDefinitionService}
	 */
	MorpheusUpdateDefinitionService getUpdateDefinition();
}
