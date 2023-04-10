package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerPolicy
import com.morpheusdata.model.NetworkLoadBalancerRule
import com.morpheusdata.model.projection.LoadBalancerPolicyIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class PolicySync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public PolicySync(){}
	public PolicySync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer
		this.plugin = plugin
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip policies")

		try {
			// get the load balancer pool service to interact with database
			def svc = morpheusContext.loadBalancer.policy

			// grab master items from the bigip api
			def objCategory = BigIpUtility.getObjCategory('policy', loadBalancer.id)
			def apiItems = plugin.provider.listPolicies(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id, objCategory)
			SyncTask<LoadBalancerPolicyIdentityProjection, Map, NetworkLoadBalancerPolicy> syncTask = new SyncTask<>(domainRecords, apiItems.policies)
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

				for (policy in addItems) {
					def addConfig = [account :loadBalancer.account, internalId:policy.selfLink, externalId:policy.fullPath, name:policy.name, category:objCategory,
						enabled :true, loadBalancer:loadBalancer, draft:policy.status == 'draft', controls:BigIpUtility.combineStringLists(policy.controls), policyType:'policy',
						requires: BigIpUtility.combineStringLists(policy.requires), status:policy.status, strategy:BigIpUtility.parsePathName(policy.strategy), partition:policy.partition
					]
					def add = new NetworkLoadBalancerPolicy(addConfig)
					add.setConfigMap(policy)

					if (policy.rulesReference?.items)
						 syncPolicyRules(add, policy.rulesReference.items)
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
					if (existingPolicy.description != source.description) {
						existingPolicy.description = source.description
						doUpdate = true
					}

					if (source.rulesReference?.items) {
						doUpdate = doUpdate || syncPolicyRules(existingPolicy, source.rulesReference.items)
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
			log.info('bigip policy sync complete')
		}
		catch (Throwable t) {
			log.error("Failure in load balancer policy sync: ${t.message}", t)
		}
	}

	protected syncPolicyRules(NetworkLoadBalancerPolicy policy, List policyRuleList) {
		log.info("Syncing policy rules for policy ${policy.name}: ${policyRuleList}")
		def changes = false
		def poolSvc = morpheusContext.loadBalancer.pool

		SyncTask<NetworkLoadBalancerRule, Map, NetworkLoadBalancerRule> syncTask = new SyncTask<>(
			Observable.fromIterable(policy.rules), policyRuleList
		)
		syncTask.addMatchFunction { NetworkLoadBalancerRule existingItem, Map syncItem ->
			existingItem.externalId == syncItem.fullPath
		}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkLoadBalancerRule, Map>> updateItems ->
			return Observable.fromIterable(updateItems.collect { item -> return item.existingItem })
		}.onAdd { addItems ->
			List<NetworkLoadBalancerRule> adds = new ArrayList<NetworkLoadBalancerRule>()
			for (item in addItems) {
				def addConfig = generateRuleMapForSave(item)
				addConfig.policy = policy
				def add = new NetworkLoadBalancerRule(addConfig)
				add.setConfigMap(item)

				// get pools and add
				//add pools
				for (action in item.actionsReference?.items) {
					if(action.pool) {
						def poolMatch = poolSvc.findByLoadBalancerAndExternalId(loadBalancer, action.pool).blockingGet()
						if(poolMatch.value.isPresent()) {
							add.pools << poolMatch.value.get()
						}
					}
				}
				changes = true
				adds << add
			}
			policy.rules = adds
		}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerRule, Map>> updateItems ->
			// NOT IMPLEMENTED
			log.info("Sync updates not implemented for policy rules sync")
		}.onDelete { removeItems ->
			def removeIds = removeItems.collect { return it.externalId }
	    	policy.rules?.removeAll { rule ->
				return removeIds.contains(rule.externalId)
			}
			changes = true
		}.start()
		return changes
	}

	protected generateRuleMapForSave(Map item) {
		def objCategory = "f5.rule.${loadBalancer.id}"
		def addConfig = [internalId:item.selfLink, externalId:item.fullPath, name:item.name, category:objCategory,
						 enabled:true]
		def firstAction = item.actionsReference?.items?.size() > 0 ? item.actionsReference.items.first() : null
		if(firstAction) {
			addConfig += [displayOrder:firstAction.ordinal, actionCode:firstAction.code, actionName:firstAction.name,
						  actionInternalId:firstAction.selfLink, actionExternalId:firstAction.fullPath, actionForward:firstAction.forward,
						  actionLength:firstAction.length, actionOffset:firstAction.offset, actionPoolId:firstAction.pool,
						  actionPort:firstAction.port, actionRequest:firstAction.request, actionSelect:firstAction.select,
						  actionStatus:firstAction.status, actionTimeout:firstAction.timeout, actionVlan:firstAction.vlanId,
						  actionExpiration:firstAction.expirySecs]
		}

		return addConfig
	}
}
