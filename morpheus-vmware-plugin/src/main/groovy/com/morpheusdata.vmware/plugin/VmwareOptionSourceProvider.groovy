package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.OptionSourceProvider

@Slf4j
class VmwareOptionSourceProvider implements OptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	VmwareOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
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
		return 'vmware-option-source-plugin'
	}

	@Override
	String getName() {
		return 'Vmware Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['vmwarePluginVersions', 'vmwarePluginVDC', 'vmwarePluginCluster', 'vmwarePluginResourcePool'])
	}

	def vmwarePluginVersions(args) {
		log.debug "vmwarePluginVersions: ${args}"
		return [[name: '7.0+',value:'7.0'],[name: '6.7+',value:'6.7'],[name:'6.5+',value:'6.5'],[name: '6.0+',value:'6.0']]
	}

	def vmwarePluginVDC(args) {
		log.debug "vmwarePluginVDC: ${args}"
		Cloud cloud = loadLookupZone(args)
		def datacenters = VmwareCloudProvider.listDatacenters(cloud)?.datacenters
		log.debug "datacenters: ${datacenters}"
		datacenters?.collect {  [name: it.name, value: it.name] }
	}

	def vmwarePluginCluster(args) {
		log.debug "vmwarePluginCluster: ${args}"
		Cloud cloud = loadLookupZone(args)
		def clusters = VmwareCloudProvider.listComputeResources(cloud)?.computeResorces
		log.debug "clusters: ${clusters}"
		clusters.collect {  [name: it.name, value: it.name] }
	}

	def vmwarePluginResourcePool(args) {
		log.debug "vmwarePluginResourcePool: ${args}"
		//prepare a lookup zone
		def configInput = args.config
		if(!configInput.cluster) {
			def rtn = [success:true, resourcePools:[]]
			return []
		} else {
			Cloud cloud = loadLookupZone(args)
			def resourcePools = VmwareCloudProvider.listResourcePools(cloud)?.resourcePools
			def resourcePoolList = buildPoolTree(resourcePools)
			def sortedPools = resourcePoolList.sort{ a, b -> a.name <=> b.name }
			sortedPools.collect {  [name: it.name, value: it.ref] }
		}
	}

	private Cloud loadLookupZone(args) {
		log.debug "loadLookupZone: $args"
		def cloudArgs = args?.size() > 0 ? args.getAt(0) : null
		Cloud tmpCloud = new Cloud()
		if(cloudArgs?.zone) {
			tmpCloud.serviceUrl = cloudArgs.zone.serviceUrl
			tmpCloud.serviceUsername = cloudArgs.zone.serviceUsername
			tmpCloud.servicePassword = cloudArgs.zone.servicePassword
			tmpCloud.setConfigProperty('datacenter', cloudArgs.zone.config?.datacenter)
			tmpCloud.setConfigProperty('cluster', cloudArgs.zone.config?.cluster)
		} else {
			def zoneId = cloudArgs?.zoneId?.toLong()
			if (zoneId) {
				log.debug "using zoneId: ${zoneId}"
				tmpCloud = morpheusContext.cloud.getCloudById(zoneId).blockingGet()

				if(cloudArgs.zone?.serviceUrl)
					tmpCloud.serviceUrl = cloudArgs.zone?.serviceUrl

				if(cloudArgs.zone?.serviceUsername)
					tmpCloud.serviceUsername = cloudArgs.zone?.serviceUsername

				if(cloudArgs.zone?.password && cloudArgs.zone.password != MorpheusUtils.passwordHidden)
					tmpCloud.servicePassword = cloudArgs.zone.servicePassword

				if(cloudArgs.zone?.config?.datacenter)
					tmpCloud.setConfigProperty('datacenter', cloudArgs.zone.config?.datacenter)

				if(cloudArgs.zone?.config?.cluster)
					tmpCloud.setConfigProperty('cluster', cloudArgs.zone.config?.cluster)
			}
		}
		tmpCloud
	}

	private buildPoolTree(pools) {
		def poolsById = pools?.collectEntries{[(it.ref.toString()):it]}
		def resourcePoolsList = pools.collect { pt ->
			def map = [name:nameForPool(pt,poolsById), type:pt.type, ref:pt.ref]
			return map
		}
	}

	protected nameForPool(pool,poolsById=null) {
		def nameElements = [pool.name]
		def currentPool = pool
		if(poolsById) {
			while(true) {
				def parent = currentPool.parentId ? poolsById[currentPool.parentId.toString()] : null

				if(!parent || parent.type != 'ResourcePool') {

					break
				} else {
					nameElements.add(0,parent.name)
					currentPool = parent
				}
			}
		} else {
			while(currentPool.parent?.getMOR()?.getType() == 'ResourcePool') {
				nameElements.add(0,currentPool.parent.getName())
				currentPool = currentPool.parent
			}
		}
		return nameElements.join(' / ')
	}
}
