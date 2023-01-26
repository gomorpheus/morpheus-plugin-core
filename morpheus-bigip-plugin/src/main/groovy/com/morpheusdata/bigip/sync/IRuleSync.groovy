package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerProfile
import com.morpheusdata.model.NetworkLoadBalancerScript
import com.morpheusdata.model.projection.LoadBalancerScriptIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class IRuleSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public IRuleSync() {}
	public IRuleSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.plugin = plugin
		this.loadBalancer = loadBalancer
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip iRules")
		try {
			// get the load balancer script service to interact with database
			def svc = morpheusContext.loadBalancer.script

			// grab master items from the bigip api
			def apiItems = plugin.provider.listIRules(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id)
			SyncTask<LoadBalancerScriptIdentityProjection, Map, NetworkLoadBalancerScript> syncTask = new SyncTask<>(domainRecords, apiItems.irules)
			syncTask.addMatchFunction { LoadBalancerScriptIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerScriptIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerScriptIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerScript script ->
					SyncTask.UpdateItemDto<LoadBalancerScriptIdentityProjection, Map> matchedItem = updateItemMap[script.id]
					return new SyncTask.UpdateItem<LoadBalancerScriptIdentityProjection, Map>(existingItem:script, masterItem:matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def objCategory = "f5.irule.${loadBalancer.id}"
				def adds = []
				for (script in addItems) {
					def addConfig = [account:loadBalancer.account, internalId:script.selfLink, externalId:script.fullPath, name:script.name, category:objCategory,
									 enabled:true, loadBalancer:loadBalancer, script:script.apiAnonymous]
					def add = new NetworkLoadBalancerScript(addConfig)
					add.setConfigMap(script)
					adds << add
				}
			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerScript, Map>> updateItems ->
				// NOT IMPLEMENTED
				log.info ("Sync updates not implemented for iRule sync")
			}.onDelete { removeItems ->
				svc.remove(removeItems).blockingGet()
			}.start()
		}
		catch (Throwable t) {
			log.error("Failure in load balancer irule sync: ${t.message}", t)
		}
	}
}
