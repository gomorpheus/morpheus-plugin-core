package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerMonitor
import com.morpheusdata.model.NetworkLoadBalancerNode
import com.morpheusdata.model.projection.LoadBalancerMonitorIdentityProjection
import com.morpheusdata.model.projection.LoadBalancerNodeIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class HealthMonitorSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public HealthMonitorSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer
		this.plugin = plugin
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip health monitors")

		try {
			// get the load balancer health monitor service to interact with database
			def svc = morpheusContext.loadBalancer.monitor

			// grab master items from the bigip api
			def apiItems = plugin.provider.listHealthMonitors(loadBalancer)

			// Add sync logic for adds/updates/removes
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id)
			SyncTask<LoadBalancerMonitorIdentityProjection, Map, NetworkLoadBalancerMonitor> syncTask = new SyncTask<>(domainRecords, apiItems.monitors)
			syncTask.addMatchFunction { LoadBalancerMonitorIdentityProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<LoadBalancerMonitorIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<LoadBalancerMonitorIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { NetworkLoadBalancerMonitor monitor ->
					SyncTask.UpdateItemDto<LoadBalancerMonitorIdentityProjection, Map> matchedItem = updateItemMap[monitor.id]
					return new SyncTask.UpdateItem<LoadBalancerMonitorIdentityProjection, Map>(existingItem: monitor, masterItem: matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def adds = []
				for (monitor in addItems) {
					def addConfig = [account:loadBalancer.account, internalId:monitor.selfLink, externalId:monitor.fullPath,
						name:monitor.name, loadBalancer:loadBalancer, monitorType:monitor.serviceType, monitorInterval:BigIpUtility.decodeApiNumber(monitor.interval),
						monitorReverse:(monitor.reverse != 'disabled'), monitorTimeout:BigIpUtility.decodeApiNumber(monitor.timeout), sendData:monitor.send,
						monitorUsername:monitor.username, monitorPassword:monitor.password, receiveData:monitor.recv, description:monitor.description,
						monitorTransparent:(monitor.transparent != 'disabled'), monitorAdaptive:(monitor.adaptive != 'disabled'),
						enabled:true, monitorDestination:monitor.destination, partition:monitor.partition
					]
					def add = new NetworkLoadBalancerMonitor(addConfig)
					add.setConfigMap(monitor)

					// handle parent monitors
					if (monitor.defaultsFrom) {
						def parentMonitor = svc.findByExternalId(monitor.defaultsFrom).blockingGet()
						if (parentMonitor.value.isPresent() ) {
							def parent = parentMonitor.value.get()
							add.setConfigProperty('monitor.id', parent.id)
						}
					}

					adds << add
				}
				svc.create(adds).blockingGet()
			}.onUpdate { List<SyncTask.UpdateItem<NetworkLoadBalancerMonitor, Map>> updateItems ->
				List<NetworkLoadBalancerMonitor> itemsToUpdate = new ArrayList<NetworkLoadBalancerMonitor>()
				for (update in updateItems) {
					def source = update.masterItem
					NetworkLoadBalancerMonitor existingMonitor = update.existingItem
					def doUpdate = false

					if (existingMonitor.partition != source.partition) {
						existingMonitor.partition = source.partition
						doUpdate = true
					}
					if (existingMonitor.name != source.name) {
						existingMonitor.name = source.name
						doUpdate = true
					}
					if (existingMonitor.description != source.description) {
						existingMonitor.description = source.description
						doUpdate = true
					}

					// TODO: do we need to test other items for update? we currently don't in the embedded implementation

					if (doUpdate)
						itemsToUpdate << existingMonitor
				}
				svc.save(itemsToUpdate).blockingGet()
			}.onDelete { List<LoadBalancerMonitorIdentityProjection> monitors ->
				svc.remove(monitors).blockingGet()
			}.start()
			log.info('bigip health monitor sync complete')
		}
		catch (Throwable t) {
			log.error("Failure in bigip health monitor sync: ${t.message}", t)
		}
	}
}
