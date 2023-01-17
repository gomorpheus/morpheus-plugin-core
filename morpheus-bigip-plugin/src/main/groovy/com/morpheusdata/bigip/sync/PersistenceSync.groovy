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
class PersistenceSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public PersistenceSync() {}
	public PersistenceSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.plugin = plugin
		this.loadBalancer = loadBalancer
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		try {
			// grab our service that interacts with the database
			def objCategory = BigIpUtility.getObjCategory('persistence', loadBalancer.id)
			def svc = morpheusContext.loadBalancer.policy

			def apiItems = plugin.provider.listPersistencePolicies(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id, objCategory)
			SyncTask<LoadBalancerPolicyIdentityProjection, Map, NetworkLoadBalancerPolicy> syncTask = new SyncTask<>(domainRecords, apiItems.policies)
			syncTask.addMatchFunction { LoadBalancerPolicyIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
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
		}
		catch (Throwable t) {
			log.error("Failed to sync persistence policies from the LB: ${t.message}", t)
		}
	}
}
