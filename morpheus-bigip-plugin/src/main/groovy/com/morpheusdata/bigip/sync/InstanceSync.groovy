package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerInstance
import com.morpheusdata.model.NetworkLoadBalancerPool
import com.morpheusdata.model.projection.LoadBalancerInstanceIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class InstanceSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public InstanceSync() {
	}
	public InstanceSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.plugin = plugin
		this.loadBalancer = loadBalancer
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		try {
			// get the load balancer instance service to interact with database
			def svc = morpheusContext.loadBalancer.instance

			// grab master items from the bigip api
			def apiItems = plugin.provider.listVirtualServers(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id)
			SyncTask<LoadBalancerInstanceIdentityProjection, Map, NetworkLoadBalancerInstance> syncTask = new SyncTask<>(domainRecords, apiItems.virtualServers)
			syncTask.addMatchFunction { LoadBalancerInstanceIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.onAdd { addItems ->
				def adds = []
				for (instance in addItems) {
					def addConfig = [internalId:instance.selfLink, externalId:instance.fullPath, vipAddress:instance.vipAddress,
									 active:instance.enabled, vipProtocol:instance.ipProtocol, vipPort:instance.vipPort,
									 vipName:instance.name, loadBalancer:loadBalancer, partition:instance.partition]
					def add = new NetworkLoadBalancerInstance(addConfig)
					add.setConfigMap(instance)

					// add profiles and policies to virtual server
					syncVirtualServerProfiles(loadBalancer, add, instance)
					syncVirtualServerPolicies(loadBalancer, add, instance)

					adds << add
				}

				svc.create(adds).blockingGet()

				// after adds, perform a usage restart on the load balancer
				morpheusContext.loadBalancer.restartLoadBalancerUsage(loadBalancer.id)

			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerInstance, Map>> updateItems ->
				List<NetworkLoadBalancerInstance> itemsToUpdate = new ArrayList<NetworkLoadBalancerInstance>()
				for (update in updateItems) {
					def source = update.masterItem
					NetworkLoadBalancerInstance existingInstance = update.existingItem
					def doUpdate = false

					if (existingInstance.partition != source.partition) {
						existingInstance.partition = source.partition
						doUpdate = true
					}

					// check profiles and policies
					if (syncVirtualServerProfiles(loadBalancer, existingInstance, source))
						doUpdate = true
					if (syncVirtualServerPolicies(loadBalancer, existingInstance, source))
						doUpdate = true

					if (doUpdate)
						itemsToUpdate << existingInstance
				}
				if (itemsToUpdate.size() > 0) {
					svc.save(itemsToUpdate).blockingGet()
				}
			}.onDelete { removeItems ->
				svc.remove(removeItems).blockingGet()

				// after removals, restart load balancer usage tracking
				morpheusContext.loadBalancer.restartLoadBalancerUsage(loadBalancer.id)
			}.start()
		}
		catch (Throwable t) {
			log.error("Failed to sync load balancer instance (virtual servers)", t)
		}
	}

	protected syncVirtualServerProfiles(NetworkLoadBalancer loadBalancer, NetworkLoadBalancerInstance existingVip, Map source) {
		def profileSvc = morpheusContext.loadBalancer.profile
		def doUpdate = false
		// check profiles
		if (existingVip.profiles?.size() != source.profilesReference?.items?.size()) {
			existingVip.profiles?.clear()
			for (sourceProfile in source.profilesReference.items) {
				def profile = profileSvc.findByExternalIdAndLoadBalancer(sourceProfile.fullPath, loadBalancer).blockingGet()
				if (profile) {
					existingVip.addToProfiles(profile)
					doUpdate = true
				}
			}
		}
		return doUpdate
	}

	protected syncVirtualServerPolicies(NetworkLoadBalancer loadBalancer, NetworkLoadBalancerInstance existingVip, Map source) {
		def policySvc = morpheusContext.loadBalancer.policy
		def doUpdate = false

		if (existingVip.policies?.size() != source.policiesReference?.items?.size()) {
			existingVip.policies?.clear()
			for (sourcePolicy in source.policiesReference.items) {
				def policy = policySvc.findByExternalIdAndLoadBalancer(sourcePolicy.fullPath, loadBalancer)
				if (policy) {
					existingVip.addToPolicies(policy)
					doUpdate = true
				}
			}
		}
		return doUpdate
	}
}
