package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerNode
import com.morpheusdata.model.projection.LoadBalancerNodeIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class NodesSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public NodesSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer
		this.plugin = plugin
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Starting bigip node sync")
		try {
			// get the load balancer node service to interact with database
			def svc = morpheusContext.loadBalancer.node

			// grab master items from the bigip api
			def apiItems = plugin.provider.listNodes(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id)
			SyncTask<LoadBalancerNodeIdentityProjection, Map, NetworkLoadBalancerNode> syncTask = new SyncTask<>(domainRecords, apiItems.nodes)
			syncTask.addMatchFunction { LoadBalancerNodeIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerNodeIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerNodeIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerNode node ->
					SyncTask.UpdateItemDto<LoadBalancerNodeIdentityProjection, Map> matchedItem = updateItemMap[node.id]
					return new SyncTask.UpdateItem<LoadBalancerNodeIdentityProjection, Map>(existingItem:node, masterItem:matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def adds = []
				for (node in addItems) {
					def addConfig = [loadBalancer:loadBalancer, internalId:node.selfLink, externalId:node.fullPath,
									 name:node.name, description:node.description, ipAddress:node.address, nodeState: BigIpUtility.parseNodeState(node.session, node.state),
									 enabled:true, partition:node.partition
					]
					adds << new NetworkLoadBalancerNode(addConfig)
				}
				svc.create(adds).blockingGet()
			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerNode, Map>> updateItems ->
				List<NetworkLoadBalancerNode> itemsToUpdate = new ArrayList<NetworkLoadBalancerNode>()
				for (update in updateItems) {
					def source = update.masterItem
					NetworkLoadBalancerNode existingNode = update.existingItem
					def doUpdate = false
					def currentNodeState = BigIpUtility.parseNodeState(source.session, source.state)

					if (existingNode.partition != source.partition) {
						existingNode.partition = source.partition
						doUpdate = true
					}
					if (existingNode.name != source.name) {
						existingNode.name = source.name
						doUpdate = true
					}
					if (existingNode.description != source.description) {
						existingNode.description = source.description
						doUpdate = true
					}
					if (existingNode.ipAddress != source.address) {
						existingNode.ipAddress = source.address
						doUpdate = true
					}
					if (existingNode.nodeState != currentNodeState) {
						existingNode.nodeState = currentNodeState
						doUpdate = true
					}

					if (doUpdate)
						itemsToUpdate << existingNode
				}

				if (itemsToUpdate.size() > 0) {
					svc.save(itemsToUpdate).blockingGet()
				}
			}.onDelete { List<LoadBalancerNodeIdentityProjection> nodes ->
				svc.remove(nodes).blockingGet()
			}.start()
			log.info('bigip node sync complete')
		}
		catch (Throwable t) {
			log.error("Failure in bigip node sync: ${t.message}", t)
		}
	}
}
