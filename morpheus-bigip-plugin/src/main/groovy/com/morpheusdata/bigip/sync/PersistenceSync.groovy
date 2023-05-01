package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerPolicy
import com.morpheusdata.model.projection.LoadBalancerPolicyIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class PersistenceSync extends BigIPEntitySync {
	public PersistenceSync() {}
	public PersistenceSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.plugin = plugin
		this.loadBalancer = loadBalancer
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip persistence policies")
		if (!shouldExecute()) {
			log.info('Skipping bigip persistence policy sync')
			return
		}

		try {
			// grab our service that interacts with the database
			def objCategory = BigIpUtility.getObjCategory('persistence', loadBalancer.id)
			def svc = morpheusContext.loadBalancer.policy

			def apiItems = plugin.provider.listPersistencePolicies(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id, objCategory)
			SyncTask<LoadBalancerPolicyIdentityProjection, Map, NetworkLoadBalancerPolicy> syncTask = new SyncTask<>(domainRecords, apiItems.persistencePolicies)
			syncTask.addMatchFunction { LoadBalancerPolicyIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerPolicyIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerPolicyIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerPolicy policy ->
					SyncTask.UpdateItemDto<LoadBalancerPolicyIdentityProjection, Map> matchedItem = updateItemMap[policy.id]
					return new SyncTask.UpdateItem<LoadBalancerPolicyIdentityProjection, Map>(existingItem:policy, masterItem:matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def adds = []
				for (item in addItems) {
					log.debug("item: {}", item)
					def addConfig = [
						account:loadBalancer.account, externalId:item.fullPath, name:item.name, category:objCategory,
						enabled:true, loadBalancer:loadBalancer, policyType:'persistence', partition:item.partition
					]
					def add = new NetworkLoadBalancerPolicy(addConfig)
					add.setConfigMap(item)
					adds << add
				}
				svc.create(adds).blockingGet()
			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerPolicy, Map>> updateItems ->
				List<NetworkLoadBalancerPolicy> itemsToUpdate = new ArrayList<NetworkLoadBalancerPolicy>()
				for (update in updateItems) {
					def source = update.masterItem
					NetworkLoadBalancerPolicy existingPolicy = update.existingItem
					def doUpdate = false

					// NOTE: currently updates are not implemented
					if (existingPolicy.partition != source.partition) {
						existingPolicy.partition = source.partition
						doUpdate = true
					}
					if (existingPolicy.name != source.name) {
						existingPolicy.name = source.name
						doUpdate = true
					}
					if (doUpdate)
						itemsToUpdate << existingPolicy
				}
				if (itemsToUpdate.size() > 0) {
					svc.save(itemsToUpdate).blockingGet()
				}
			}.onDelete { removeItems->
				svc.remove(removeItems).blockingGet()
			}.start()
			log.info('big ip persistence policy sync complete')
		}
		catch (Throwable t) {
			log.error("Failed to sync persistence policies from the LB: ${t.message}", t)
		}
	}
}
