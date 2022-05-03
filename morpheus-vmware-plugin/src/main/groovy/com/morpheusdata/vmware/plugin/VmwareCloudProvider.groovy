package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.NetworkSubnetType
import com.morpheusdata.model.NetworkProxy
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.StorageControllerType
import com.morpheusdata.model.StorageVolumeType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.vmware.plugin.sync.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.util.ConnectionUtils
import java.security.MessageDigest

@Slf4j
class VmwareCloudProvider implements CloudProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	VmwareCloudProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		OptionType apiUrl = new OptionType(
				name: 'Api Url',
				code: 'vmware-plugin-api-url',
				fieldName: 'serviceUrl',
				displayOrder: 0,
				fieldLabel: 'Api Url',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType username = new OptionType(
				name: 'Username',
				code: 'vmware-plugin-username',
				fieldName: 'serviceUsername',
				displayOrder: 1,
				fieldLabel: 'Username',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType password = new OptionType(
				name: 'Password',
				code: 'vmware-plugin-password',
				fieldName: 'servicePassword',
				displayOrder: 2,
				fieldLabel: 'Password',
				required: true,
				inputType: OptionType.InputType.PASSWORD,
				fieldContext: 'domain'
		)
		OptionType version = new OptionType(
				name: 'Version',
				code: 'vmware-plugin-version',
				fieldName: 'apiVersion',
				displayOrder: 3,
				fieldLabel: 'Version',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				optionSource: 'vmwarePluginVersions'
		)
		OptionType vdc = new OptionType(
				name: 'VDC',
				code: 'vmware-plugin-vdc',
				fieldName: 'datacenter',
				displayOrder: 4,
				fieldLabel: 'VDC',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				dependsOn: 'vmware-plugin-api-url,vmware-plugin-username,vmware-plugin-password',
				optionSource: 'vmwarePluginVDC'
		)

		OptionType cluster = new OptionType(
				name: 'Cluster',
				code: 'vmware-plugin-cluster',
				fieldName: 'cluster',
				displayOrder: 5,
				fieldLabel: 'Cluster',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				dependsOn: 'vmware-plugin-vdc',
				optionSource: 'vmwarePluginCluster'
		)

		OptionType resourcePool = new OptionType(
				name: 'Resource Pool',
				code: 'vmware-plugin-resource-pool',
				fieldName: 'resourcePoolId',
				displayOrder: 6,
				fieldLabel: 'Resource Pool',
				required: false,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				dependsOn: 'vmware-plugin-cluster',
				optionSource: 'vmwarePluginResourcePool'
		)

		OptionType inventoryInstances = new OptionType(
				name: 'Inventory Existing Instances',
				code: 'vmware-plugin-import-existing',
				fieldName: 'importExisting',
				displayOrder: 7,
				fieldLabel: 'Inventory Existing Instances',
				required: false,
				inputType: OptionType.InputType.CHECKBOX,
				fieldContext: 'config'
		)

		[apiUrl, username, password, version, vdc, cluster, resourcePool, inventoryInstances]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		ComputeServerType hypervisorType = new ComputeServerType()
		hypervisorType.name = 'Vmware Plugin Hypervisor'
		hypervisorType.code = 'vmware-plugin-hypervisor'
		hypervisorType.description = 'vmware plugin hypervisor'
		hypervisorType.vmHypervisor = true
		hypervisorType.controlPower = false
		hypervisorType.reconfigureSupported = false
		hypervisorType.externalDelete = false
		hypervisorType.hasAutomation = false
		hypervisorType.agentType = ComputeServerType.AgentType.none
		hypervisorType.platform = PlatformType.esxi
		hypervisorType.managed = false
		hypervisorType.provisionTypeCode = 'vmware-provision-provider-plugin'

		ComputeServerType serverType = new ComputeServerType()
		serverType.name = 'Vmware Plugin Server'
		serverType.code = 'vmware-plugin-server'
		serverType.description = 'vmware plugin server'
		serverType.reconfigureSupported = false
		serverType.hasAutomation = false
		serverType.supportsConsoleKeymap = true
		serverType.platform = PlatformType.none
		serverType.managed = false
		serverType.provisionTypeCode = 'vmware-provision-provider-plugin'

		ComputeServerType vmwareWindows = new ComputeServerType()
		vmwareWindows.name = 'VMware Windows Node'
		vmwareWindows.code = 'vmware-plugin-windows-node'
		vmwareWindows.description = ''
		vmwareWindows.reconfigureSupported = true
		vmwareWindows.hasAutomation = true
		vmwareWindows.supportsConsoleKeymap = true
		vmwareWindows.controlEjectCd = true
		vmwareWindows.guestVm = true
		vmwareWindows.controlSuspend = true
		vmwareWindows.platform = PlatformType.windows
		vmwareWindows.managed = true
		vmwareWindows.provisionTypeCode = 'vmware-provision-provider-plugin'

		ComputeServerType vmwareVm = new ComputeServerType()
		vmwareVm.name = 'Vmware Linux VM'
		vmwareVm.code = 'vmware-plugin-vm'
		vmwareVm.description = ''
		vmwareWindows.controlEjectCd = true
		vmwareWindows.guestVm = true
		vmwareWindows.controlSuspend = true
		vmwareVm.reconfigureSupported = false
		vmwareVm.hasAutomation = true
		vmwareVm.supportsConsoleKeymap = true
		vmwareVm.platform = PlatformType.linux
		vmwareVm.managed = true
		vmwareVm.provisionTypeCode = 'vmware-provision-provider-plugin'

		return [hypervisorType, serverType, vmwareWindows, vmwareVm]
	}

	@Override
	Collection<ProvisioningProvider> getAvailableProvisioningProviders() {
		return plugin.getProvidersByType(ProvisioningProvider) as Collection<ProvisioningProvider>
	}

	@Override
	Collection<BackupProvider> getAvailableBackupProviders() {
		return null
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders().find { it.code == providerCode }
	}

	@Override
	Collection<NetworkType> getNetworkTypes() {
		def vmwareNetworkConfig = [
				code: 'vmware-plugin-network',
				externalType: 'Network',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Network'
		]
		NetworkType vmwareNetwork = new NetworkType(vmwareNetworkConfig)

		def subnetConfig = [
				code: 'esxi-plugin-subnet',
				dhcpServerEditable: true,
				canAssignPool: true,
				cidrRequired: true,
				cidrEditable: true,
				name: 'Esxi Plugin Subnet'
		]
		NetworkSubnetType esxiSubnet = new NetworkSubnetType(subnetConfig)
		vmwareNetwork.setNetworkSubnetTypes([esxiSubnet])

		def vmwareDistributedSwitchConfig = [
				code: 'vmware-plugin-distributed-switch',
				externalType: 'DistributedSwitch',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Distributed Switch'
		]
		NetworkType vmwareDistributedSwitch = new NetworkType(vmwareDistributedSwitchConfig)

		def vmwareDistributedConfig = [
				code: 'vmware-plugin-distributed',
				externalType: 'DistributedVirtualPortgroup',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Distributed Switch Group'
		]
		NetworkType vmwareDistributed = new NetworkType(vmwareDistributedConfig)

		def vmwareOpaqueConfig = [
				code: 'vmware-plugin-opaque',
				externalType: 'OpaqueNetwork',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Opaque Network'
		]
		NetworkType vmwareOpaque = new NetworkType(vmwareOpaqueConfig)

		return [vmwareNetwork, vmwareDistributedSwitch, vmwareDistributed, vmwareOpaque]
	}

	@Override
	Collection<StorageVolumeType> getStorageVolumeTypes() {
		def datastoreVolumeType = new StorageVolumeType([
				code: 'vmware-plugin-datastore',
				name: 'Vmware Datastore'
		])
		def diskVolumeType = new StorageVolumeType([
				code: 'vmware-plugin-disk',
				name: 'Disk'
		])

		return [datastoreVolumeType, diskVolumeType]
	}

	@Override
	Collection<StorageControllerType> getStorageControllerTypes() {
		def standardType = new StorageControllerType([
		        code: 'vmware-plugin-standard',
				name: 'Vwmare Plugin Standard',
		])

		def ideType = new StorageControllerType([
				code: 'vmware-plugin-ide',
				name: 'Vwmare Plugin IDE',
		])

		def busLogicType = new StorageControllerType([
				code: 'vmware-plugin-busLogic',
				name: 'Vwmare Plugin SCSI BusLogic Parallel',
		])

		def lsiType = new StorageControllerType([
				code: 'vmware-plugin-lsiLogic',
				name: 'Vwmare Plugin SCSI LSI Logic Parallel',
		])

		def lsiSasType = new StorageControllerType([
				code: 'vmware-plugin-lsiLogicSas',
				name: 'Vwmare Plugin SCSI LSI Logic SAS',
		])

		def paravirtualType = new StorageControllerType([
				code: 'vmware-plugin-paravirtual',
				name: 'Vwmare Plugin SCSI VMware Paravirtual',
		])

		return [standardType, ideType, busLogicType, lsiType, lsiSasType, paravirtualType]
	}

	@Override
	ServiceResponse validate(Cloud cloudInfo) {
		log.info("validate: {}", cloudInfo)
		try {
			if(cloudInfo) {
				def configMap = cloudInfo.getConfigMap()
				if(configMap.datacenter?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Choose a datacenter')
				} else if(cloudInfo.serviceUsername?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Enter a username')
				} else if(cloudInfo.servicePassword?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Enter a password')
				} else if(cloudInfo.serviceUrl?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Enter an api url')
				} else {
					//test api call
					def apiUrl = VmwareProvisionProvider.getVmwareApiUrl(cloudInfo.serviceUrl)
					//get creds
					def dcList = VmwareComputeUtility.listDatacenters(apiUrl, cloudInfo.serviceUsername, cloudInfo.servicePassword)
					if(dcList.success == true) {
						return ServiceResponse.success()
					} else {
						return new ServiceResponse(success: false, msg: 'Invalid vmware credentials')
					}
				}
			} else {
				return new ServiceResponse(success: false, msg: 'No cloud found')
			}
		} catch(e) {
			log.error('Error validating cloud', e)
			return new ServiceResponse(success: false, msg: 'Error validating cloud')
		}
	}

	@Override
	void refresh(Cloud cloudInfo) {
		initializeCloud(cloudInfo)
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {
		//nothing daily
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return new ServiceResponse(success: true)
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'vmware-plugin-cloud'
	}

	@Override
	String getName() {
		return 'Vmware'
	}

	@Override
	String getDescription() {
		return 'Vmware VCenter plugin'
	}

	@Override
	Boolean hasComputeZonePools() {
		return true
	}

	@Override
	Boolean hasNetworks() {
		return true
	}

	@Override
	Boolean hasFolders() {
		return true
	}

	@Override
	ServiceResponse startServer(ComputeServer computeServer) {
		log.debug("startServer: ${computeServer}")
		def rtn = [success:false]
		try {
			return vmwareProvisionProvider().startServer(computeServer)
		} catch(e) {
			rtn.msg = "Error starting server: ${e.message}"
			log.error("startServer error: ${e}", e)
		}
		return ServiceResponse.create(rtn)
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		log.debug("stopServer: ${computeServer}")
		def rtn = [success:false]
		try {
			return vmwareProvisionProvider().stopServer(computeServer)
		} catch(e) {
			rtn.msg = "Error stoping server: ${e.message}"
			log.error("stopServer error: ${e}", e)
		}
		return ServiceResponse.create(rtn)
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		ServiceResponse rtn = new ServiceResponse(success: false)
		log.info "Initializing Cloud: ${cloud.code}"
		log.info "config: ${cloud.configMap}"

		HttpApiClient client

		try {
			def syncDate = new Date()
			def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
			def apiVersion = cloud.getConfigProperty('apiVersion') ?: '6.7'
			def apiUrlObj = new URL(authConfig.apiUrl)
			def apiHost = apiUrlObj.getHost()
			def apiPort = apiUrlObj.getPort() > 0 ? apiUrlObj.getPort() : (apiUrlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
			NetworkProxy proxySettings = cloud.apiProxy
			def hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, proxySettings)
			log.debug("vmware online: {} - {}", apiHost, hostOnline)
			if(hostOnline) {
				def testResults = VmwareComputeUtility.testConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
				if(testResults.success == true) {
					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.syncing, null, syncDate)

					def doInventory = cloud.getConfigProperty('importExisting')
					Boolean createNew = false
					if(doInventory == 'on' || doInventory == 'true' || doInventory == true) {
						createNew = true
					}

					client = new HttpApiClient()
					client.networkProxy = proxySettings

					checkZoneConfig(cloud)
					checkCluster(cloud)

					(new ResourcePoolsSync(cloud, morpheusContext)).execute()
					(new FoldersSync(cloud, morpheusContext)).execute()
					(new CustomSpecSync(cloud, morpheusContext)).execute()
					(new IPPoolsSync(cloud, morpheusContext)).execute()
					(new AlarmsSync(cloud, morpheusContext)).execute()
					(new DatastoresSync(cloud, morpheusContext)).execute()
					(new StoragePodsSync(cloud, morpheusContext)).execute()
					(new TemplatesSync(cloud, morpheusContext)).execute()
					(new ContentLibrarySync(cloud, morpheusContext, client)).execute()
					(new NetworksSync(cloud, morpheusContext, getNetworkTypes())).execute()
					(new HostsSync(cloud, morpheusContext)).execute()
					(new DatacentersSync(cloud, morpheusContext)).execute()
					if(apiVersion && apiVersion != '6.0') {
						(new CategoriesSync(cloud, morpheusContext, client)).execute()
						(new TagsSync(cloud, morpheusContext, client)).execute()
					}
					(new VirtualMachineSync(cloud, createNew, proxySettings, apiVersion, morpheusContext, vmwareProvisionProvider(), client)).execute()

					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.ok, null, syncDate)
				}
				else {
					if (testResults.invalidLogin == true) {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.error, 'invalid credentials', syncDate)
					} else {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.error, 'error connecting', syncDate)
					}
				}
			} else {
				morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.offline, 'vmware is not reachable', syncDate)
			}
			rtn.success = true
		} catch (e) {
			log.error("refresh cloud error: ${e}", e)
		} finally {
			if(client) {
				client.shutdownClient()
			}
		}
		return rtn
	}

	def checkZoneConfig(Cloud cloud) {
		log.debug "checkZoneConfig"
		def save = false
		//check the datacenter
		def datacenter = cloud.getConfigProperty('datacenter')
		def datacenterId = cloud.getConfigProperty('datacenterId')
		def datacenterResults = listDatacenters(cloud)
		def currentDatacenter = datacenterResults?.datacenters?.find{ it.name == cloud.getConfigProperty('datacenter') }
		//set the id of the datacenter if not set
		if(!datacenterId && datacenter) {
			if(currentDatacenter) {
				cloud.setConfigProperty('datacenterId', currentDatacenter.ref)
				save = true
			}
		}
		//check for datacenter name changes
		if(!currentDatacenter && datacenterId) {
			currentDatacenter = datacenterResults?.datacenters?.find{ it.ref == datacenterId }
			if(currentDatacenter) {
				cloud.setConfigProperty('datacenter', currentDatacenter.name)
				save = true
			}
		}
		//check the resource pool
		if(!cloud.getConfigProperty('resourcePoolId') && cloud.getConfigProperty('resourcePool')) {
			//we need to migrate the data model
			def results = listResourcePools(cloud)
			def currentPool = results?.resourcePools?.find{ it.name == cloud.getConfigProperty('resourcePool') }
			cloud.setConfigProperty('resourcePoolId', currentPool.ref)
			cloud.setConfigProperty('resourcePool', '')
			save = true
		}
		//check the region code
		def regionCode = getRegionCode(cloud)
		if(cloud.regionCode != regionCode) {
			cloud.regionCode = regionCode
			save = true
		}
		if(save) {
			morpheusContext.cloud.save(cloud).blockingGet()
		}
	}

	def checkCluster(Cloud cloud) {
		log.debug "checkCluster: ${cloud}"
		def serviceInstance
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		try {
			def cluster = cloud.getConfigProperty('cluster')
			if(!cluster) {
				serviceInstance = VmwareComputeUtility.getServiceInstance(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
				def resourcePool = cloud.getConfigProperty('resourcePoolId')
				def resourcePoolEntity = resourcePool ? VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', resourcePool) : null
				def clusterEntity = resourcePoolEntity ? resourcePoolEntity.getOwner() : null
				if(clusterEntity) {
					cloud.setConfigProperty('cluster', clusterEntity.getName())
					morpheusContext.cloud.save(cloud).blockingGet()
				}
			}
		} catch(e) {
			log.warn("error checking for cluster on cloud: ${cloud?.id}: ${e}", e)
		} finally {
			if(serviceInstance)
				VmwareComputeUtility.releaseServiceInstance(serviceInstance, authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
		}
	}

	static listHosts(Cloud cloud, String clusterScope) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterScope ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listHosts(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listComputeResources(Cloud cloud) {
		log.debug "listComputeResources: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		rtn = VmwareComputeUtility.listComputeResources(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter])
		return rtn
	}

	static listDatacenters(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listDatacenters(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, getApiOptions(cloud))
		return rtn
	}

	static listDatastores(Cloud cloud, String clusterInternalId=null) {
		log.debug "listDatastores: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterInternalId ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listDatastores(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listStoragePods(Cloud cloud, String clusterInternalId=null) {
		log.debug "listStoragePods: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterInternalId ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listStoragePods(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listResourcePools(Cloud cloud, String clusterName=null) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterName ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listResourcePools(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster: cluster])
		return rtn
	}

	static listVirtualMachines(Cloud cloud, String clusterInternalId=null) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterInternalId ?: cloud?.getConfigProperty('cluster')
		def resourcePool = cloud?.getConfigProperty('resourcePoolId')
		rtn = VmwareComputeUtility.listVirtualMachines(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, resourcePool:resourcePool, cluster:cluster])
		return rtn
	}

	static listTemplates(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		def resourcePool = cloud?.getConfigProperty('resourcePoolId')
		rtn = VmwareComputeUtility.listTemplates(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, resourcePool:resourcePool, cluster:cluster])
		return rtn
	}

	static listIpPools(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listIpPools(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listNetworks(Cloud cloud, String clusterInternalId=null) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterInternalId ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listNetworks(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listCustomizationSpecs(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listCustomizationSpecs(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listAlarms(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listAlarms(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, getApiOptions(cloud))
		return rtn
	}

	static getRegionCode(Cloud cloud) {
		def datacenter = cloud?.getConfigProperty('datacenter')
		def apiUrl = VmwareProvisionProvider.getVmwareApiUrl(cloud.serviceUrl)
		def regionString = "${apiUrl}.${datacenter}"
		MessageDigest md = MessageDigest.getInstance("MD5")
		md.update(regionString.bytes)
		byte[] checksum = md.digest()
		return checksum.encodeHex().toString()
	}

	static getApiOptions(Cloud cloud) {
		def rtn = [:]
		rtn.datacenter = cloud?.getConfigProperty('datacenter')
		rtn.cluster = cloud?.getConfigProperty('cluster')
		rtn.resourcePool = cloud?.getConfigProperty('resourcePoolId')
		rtn.proxySettings = cloud.apiProxy
		return rtn
	}

	VmwareProvisionProvider vmwareProvisionProvider() {
		this.plugin.getProviderByCode('vmware-provision-provider-plugin')
	}
}
