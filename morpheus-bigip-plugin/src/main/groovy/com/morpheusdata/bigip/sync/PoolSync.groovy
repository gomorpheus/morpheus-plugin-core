package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerMember
import com.morpheusdata.model.NetworkLoadBalancerPool
import com.morpheusdata.model.projection.LoadBalancerPoolIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class PoolSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public PoolSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.plugin = plugin
		this.loadBalancer = loadBalancer
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip pools")
		try {
			// get the load balancer pool service to interact with database
			def svc = morpheusContext.loadBalancer.pool

			// grab master items from the bigip api
			def apiItems = plugin.provider.listPools(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id)
			SyncTask<LoadBalancerPoolIdentityProjection, Map, NetworkLoadBalancerPool> syncTask = new SyncTask<>(domainRecords, apiItems.pools)
			syncTask.addMatchFunction { LoadBalancerPoolIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerPoolIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerPoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerPool pool ->
					SyncTask.UpdateItemDto<LoadBalancerPoolIdentityProjection, Map> matchedItem = updateItemMap[pool.id]
					return new SyncTask.UpdateItem<LoadBalancerPoolIdentityProjection, Map>(existingItem:pool, masterItem:matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def adds = []
				for (pool in addItems) {
					def objCategory = BigIpUtility.getObjCategory('pool', loadBalancer.id)
					def addConfig = [internalId:pool.selfLink, externalId:pool.fullPath, name:pool.name, category:objCategory,
									 enabled:true, loadBalancer:loadBalancer, vipBalance: BigIpUtility.decodeLoadBalancingMode(pool.loadBalancingMode),
									 allowNat:pool.allowNat, allowSnat:pool.allowSnat, vipClientIpMode:pool.ipTosToClient,
									 vipServerIpMode:pool.ipTosToServer, minActive:pool.minActiveMembers, minInService:pool.minUpMembers,
									 minUpMonitor:pool.minUpMembersChecking, minUpAction:pool.minUpMembersAction, maxQueueDepth:pool.queueDepthLimit,
									 maxQueueTime:pool.queueTimeLimit, downAction:pool.serviceDownAction, rampTime:pool.slowRampTime, partition:pool.partition]
					def add = new NetworkLoadBalancerPool(addConfig)
					add.setConfigMap(pool)

					// next sync pool members
					def members = plugin.provider.listPoolMembers(add, [authToken:apiItems.authToken])
					syncPoolMembers(add, members.poolMembers)

					adds << add
				}
				svc.create(adds).blockingGet()
			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerPool, Map>> updateItems ->
				List<NetworkLoadBalancerPool> itemsToUpdate = new ArrayList<NetworkLoadBalancerPool>()
				for (update in updateItems) {
					def source = update.masterItem
					NetworkLoadBalancerPool existingPool = update.existingItem
					def doUpdate = false

					// NOTE: currently updates are not implemented
				}
			}.onDelete { removeItems ->
				svc.remove(removeItems).blockingGet()
			}.start()
		}
		catch (Throwable t) {
			log.error("Failure in load balancer pool sync: ${t.message}", t)
		}
	}

	protected syncPoolMembers(NetworkLoadBalancerPool pool, List poolMemberList) {
		log.info("Syncing pool memebers for pool ${pool.name}: ${poolMemberList}")
		def nodeSvc = morpheusContext.loadBalancer.node

		SyncTask<NetworkLoadBalancerMember, Map, NetworkLoadBalancerMember> syncTask = new SyncTask<>(
			Observable.fromIterable(pool.members), poolMemberList
		)
		syncTask.addMatchFunction { NetworkLoadBalancerMember existingItem, Map syncItem ->
			existingItem.externalId == syncItem.fullPath
		}.addMatchFunction { NetworkLoadBalancerMember existingItem, Map syncItem ->
			def portIndex = syncItem.fullPath.lastIndexOf(':')
			def nodeId = syncItem.fullPath.substring(0, portIndex)
			existingItem.node.externalId == nodeId
		}.onAdd { addItems ->
			List<NetworkLoadBalancerMember> adds = new ArrayList<NetworkLoadBalancerMember>()
			for (item in addItems) {
				def portIndex = item.fullPath.lastIndexOf(':')
				def nodeId = item.fullPath.substring(0, portIndex)
				def portNumber = item.fullPath.substring(portIndex + 1).toInteger()
				def node = nodeSvc.findByLoadBalancerAndExternalId(pool.loadBalancer, item.fullPath).blockingGet()
				if (node.value.isPresent()) {
					def nodeModel = node.value.get()
					def addConfig = [externalId: item.fullPath, node:nodeModel, pool:pool, port:portNumber]
					adds << new NetworkLoadBalancerMember(addConfig)
				}
			}
			pool.members = adds
		}.onDelete { removeItems ->
			for (item in removeItems) {
				pool.members.removeAll { member ->
					return member.externalId == item.externalId
				}
			}
		}.start()
	}
}
