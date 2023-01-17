package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerProfile
import com.morpheusdata.model.projection.LoadBalancerProfileIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class ProfileSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public ProfileSync(){}
	public ProfileSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer
		this.plugin = plugin
		this.morpheusContext = plugin.morpheus
	}
	def execute() {
		try {
			// get the load balancer profile service to interact with database
			def svc = morpheusContext.loadBalancer.profile

			// grab master items from the bigip api
			def apiItems = plugin.provider.listProfiles(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id)
			SyncTask<LoadBalancerProfileIdentityProjection, Map, NetworkLoadBalancerProfile> syncTask = new SyncTask<>(domainRecords, apiItems.profiles)
			syncTask.addMatchFunction { LoadBalancerProfileIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerProfileIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerProfileIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerProfile profile ->
					SyncTask.UpdateItemDto<LoadBalancerProfileIdentityProjection, Map> matchedItem = updateItemMap[profile.id]
					return new SyncTask.UpdateItem<LoadBalancerProfileIdentityProjection, Map>(existingItem:profile, masterItem:matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def adds = []
				for (profile in addItems) {
					def addConfig = [account:loadBalancer.account, internalId:profile.selfLink, externalId:profile.fullPath, name:profile.name, category:objCategory,
									 enabled:true, loadBalancer:loadBalancer, proxyType:profile.proxyType, redirectRewrite:profile.redirectRewrite, serviceType:profile.serviceType, partition:profile.partition]
					def add = new NetworkLoadBalancerProfile(addConfig)
					add.setConfigMap(profile)
					adds << add
				}
				svc.create(adds).blockingGet()
			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerProfile, Map>> updateItems ->
				List<NetworkLoadBalancerProfile> itemsToUpdate = new ArrayList<NetworkLoadBalancerProfile>()
				for (update in updateItems) {
					def source = update.masterItem
					NetworkLoadBalancerProfile existingProfile = update.existingItem
					def doUpdate = false

					if (existingProfile.partition != source.partition) {
						existingProfile.partition = source.partition
						doUpdate = true
					}

					if (doUpdate)
						itemsToUpdate << existingProfile
				}
				if (itemsToUpdate.size() > 0) {
					svc.save(itemsToUpdate).blockingGet()
				}
			}.onDelete { removeItems ->
				svc.remove(removeItems).blockingGet()
			}.start()
		}
		catch (Throwable t) {
			log.error("Failure in load balancer profile sync: ${t.message}", t)
		}
	}
}
