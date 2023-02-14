package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerInstance
import com.morpheusdata.model.NetworkLoadBalancerPolicy
import com.morpheusdata.model.NetworkLoadBalancerProfile
import com.morpheusdata.model.NetworkLoadBalancerRule
import com.morpheusdata.model.projection.LoadBalancerInstanceIdentityProjection
import com.morpheusdata.model.projection.LoadBalancerPolicyIdentityProjection
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
		log.info("Syncing bigip virtual servers")
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
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerInstanceIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerInstanceIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerInstance instance ->
					SyncTask.UpdateItemDto<LoadBalancerInstanceIdentityProjection, Map> matchedItem = updateItemMap[instance.id]
					return new SyncTask.UpdateItem<LoadBalancerInstanceIdentityProjection, Map>(existingItem:instance, masterItem:matchedItem.masterItem)
				}
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
				morpheusContext.loadBalancer.restartLoadBalancerUsage(loadBalancer.id, true)

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
				morpheusContext.loadBalancer.restartLoadBalancerUsage(loadBalancer.id, true)
			}.start()
			log.info('bigip virtual server sync complete')
		}
		catch (Throwable t) {
			log.error("Failed to sync load balancer instance (virtual servers)", t)
		}
	}

	protected syncVirtualServerProfiles(NetworkLoadBalancer loadBalancer, NetworkLoadBalancerInstance existingVip, Map source) {
		def profileSvc = morpheusContext.loadBalancer.profile
		def doUpdate = false

		// Build a sync task to handle the virtual server profiles
		def sourceProfiles
		if (source.profilesReference?.items?.size() > 0)
			sourceProfiles = source.profilesReference.items
		else
			sourceProfiles = []

		SyncTask<NetworkLoadBalancerProfile, Map, NetworkLoadBalancerProfile> syncTask = new SyncTask<>(
			Observable.fromIterable(existingVip.profiles), sourceProfiles
		)
		syncTask.addMatchFunction { NetworkLoadBalancerProfile existingItem, Map syncItem ->
			existingItem.externalId == syncItem.fullPath
		}.onAdd { addItems ->
			for (item in addItems) {
				def profile = profileSvc.findByExternalIdAndLoadBalancer(item.fullPath, loadBalancer).blockingGet()
				if (profile.value.isPresent()) {
					existingVip.addToProfiles(profile.value.get())
					doUpdate = true
				}
			}
		}.onDelete { removeItems ->
			def removeIds = removeItems.collect { return it.externalId }
			existingVip.profiles?.removeAll { profile ->
				return removeIds.contains(profile.externalId)
			}
			doUpdate = true
		}

		return doUpdate
	}

	protected syncVirtualServerPolicies(NetworkLoadBalancer loadBalancer, NetworkLoadBalancerInstance existingVip, Map source) {
		def policySvc = morpheusContext.loadBalancer.policy
		def doUpdate = false

		def sourcePolicies
		if (source.policiesReference?.items?.size())
			sourcePolicies = source.policiesReference.items
		else
			sourcePolicies = []

		SyncTask<NetworkLoadBalancerPolicy, Map, NetworkLoadBalancerPolicy> syncTask = new SyncTask<>(
			Observable.fromIterable(existingVip.policies), sourcePolicies
		)
		syncTask.addMatchFunction { NetworkLoadBalancerPolicy existingItem, Map sourceItem ->
			return existingItem.externalId == sourceItem.fullPath
		}.onAdd { addItems ->
			for (item in addItems) {
				def policy = policySvc.findByExternalIdAndLoadBalancer(item.fullPath, loadBalancer).blockingGet()
				if (policy.value.isPresent()) {
					existingVip.addToPolicies(policy.value.get())
					doUpdate = true
				}
			}
		}.onDelete { removeItems ->
			def removeIds = removeItems.collect { return it.externalId }
			existingVip.policies?.removeAll { policy ->
				return removeIds.contains(policy.externalId)
			}
			doUpdate = true
		}

		return doUpdate
	}
}
