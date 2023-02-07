package com.morpheusdata.bigip

import com.morpheusdata.bigip.sync.CertificateSync
import com.morpheusdata.bigip.sync.HealthMonitorSync
import com.morpheusdata.bigip.sync.IRuleSync
import com.morpheusdata.bigip.sync.InstanceSync
import com.morpheusdata.bigip.sync.NodesSync
import com.morpheusdata.bigip.sync.PartitionSync
import com.morpheusdata.bigip.sync.PersistenceSync
import com.morpheusdata.bigip.sync.PolicySync
import com.morpheusdata.bigip.sync.PoolSync
import com.morpheusdata.bigip.sync.ProfileSync
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.network.loadbalancer.LoadBalancerProvider
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.core.util.MorpheusUtils
import com.morpheusdata.model.Icon
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerInstance
import com.morpheusdata.model.NetworkLoadBalancerMonitor
import com.morpheusdata.model.NetworkLoadBalancerNode
import com.morpheusdata.model.NetworkLoadBalancerPool
import com.morpheusdata.model.NetworkLoadBalancerProfile
import com.morpheusdata.model.NetworkLoadBalancerType
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

@Slf4j
class BigIpProvider implements LoadBalancerProvider {
	MorpheusContext morpheusContext
	Plugin plugin

	private java.lang.Object maxResults

	BigIpProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
	}

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	/**
	 * Returns the instance of the Plugin class that this provider is loaded from
	 * @return Plugin class contains references to other providers
	 */
	@Override
	Plugin getPlugin() {
		return plugin
	}

	/**
	 * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
	 * that is seeded or generated related to this provider will reference it by this code.
	 * @return short code string that should be unique across all other plugin implementations.
	 */
	@Override
	String getCode() {
		return PROVIDER_CODE
	}

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	@Override
	String getName() {
		return 'BigIp'
	}

	@Override
	String getDescription() {
		return "An F5 BigIp load balancer";
	}

	@Override
	Icon getIcon() {
		return null
	}

	/**
	 * Gets a collection of option types for creating a load balancer integration
	 * @return
	 */
	@Override
	Collection<OptionType> getOptionTypes() {
		OptionType apiHost = new OptionType(
			name:'Host',
			code:'plugin.bigip.host',
			fieldName:'sshHost',
			displayOrder:0,
			fieldLabel:'Api Host',
			required:true,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		OptionType apiPort = new OptionType(
			name:'Port',
			code:'plugin.bigip.port',
			fieldName:'apiPort',
			displayOrder:1,
			defaultValue:'443',
			fieldLabel:'Api Port',
			required:false,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		OptionType username = new OptionType(
			name:'Username',
			code:'plugin.bigip.username',
			fieldName:'sshUsername',
			displayOrder:10,
			fieldLabel:'Username',
			required:true,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		OptionType password = new OptionType(
			name:'Password',
			code:'plugin.bigip.password',
			fieldName:'sshPassword',
			displayOrder:11,
			fieldLabel:'Password',
			required:true,
			inputType:OptionType.InputType.PASSWORD,
			fieldContext:'domain'
		)

		return [apiHost, apiPort, username, password]
	}

	/**
	 * Gets a collection of input option types for creating/editing a load balancer policy
	 * @return
	 */
	Collection<OptionType> getPolicyOptionTypes() {
		Collection<OptionType> policyOptions = new ArrayList<OptionType>()
		policyOptions <<  new OptionType(
			name:'Name',
			code:'plugin.bigip.policy.name',
			fieldName:'name',
			displayOrder:0,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		policyOptions << new OptionType(
			name:'Description',
			code:'plugin.bigip.policy.description',
			fieldName:'description',
			displayOrder:1,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		policyOptions << new OptionType(
			name:'Controls',
			code:'plugin.bigip.policy.controls',
			fieldName:'controls',
			displayOrder:10,
			fieldLabel:'Controls',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyControls'
		)
		policyOptions << new OptionType(
			name:'Requires',
			code:'plugin.bigip.policy.requires',
			fieldName:'requires',
			displayOrder:11,
			fieldLabel:'Requires',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyRequires'
		)
		policyOptions << new OptionType(
			name:'Strategy',
			code:'plugin.bigip.policy.strategy',
			fieldName:'strategy',
			displayOrder:12,
			fieldLabel:'Strategy',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyStrategies',
			defaultValue:'/Common/first-match'
		)
		policyOptions << getPartitionOptionType()

		return policyOptions
	}

	/**
	 * Gets a collection of input option types for creating/editing a load balancer policy rule
	 * @return
	 */
	Collection<OptionType> getPolicyRuleOptionTypes() {
		Collection<OptionType> ruleOptions = new ArrayList<OptionType>()
		ruleOptions << new OptionType(
			name:'Name',
			code:'plugin.bigip.policy.rule.name',
			fieldName:'name',
			displayOrder:20,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		ruleOptions << new OptionType(
			name:'Field',
			code:'plugin.bigip.policy.rule.field',
			fieldName:'field',
			displayOrder:21,
			fieldLabel:'Field',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyRuleField'
		)
		ruleOptions << new OptionType(
			name:'Operator',
			code:'plugin.bigip.policy.rule.operator',
			fieldName:'operator',
			displayOrder:22,
			fieldLabel:'Operator',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyRuleOperator'
		)
		ruleOptions << new OptionType(
			name:'Value',
			code:'plugin.bigip.policy.rule.value',
			fieldName:'value',
			displayOrder:23,
			fieldLabel:'Value',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		ruleOptions << new OptionType(
			name:'Pool',
			code:'plugin.bigip.policy.rule.pool',
			fieldName:'pool',
			displayOrder:24,
			fieldLabel:'Pool',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginVirtualServerPools'
		)

		return ruleOptions
	}

	/**
	 * Gets a collection of input option types for creating/editing an SSL profile
	 * @return
	 */
	Collection<OptionType> getProfileOptionTypes() {
		Collection<OptionType> profileOptions = new ArrayList<OptionType>()
		profileOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.profile.name',
			fieldName:'name',
			fieldContext:'domain',
			displayOrder:0,
			fieldLabel:'name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		profileOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.profile.description',
			fieldName:'description',
			fieldContext:'domain',
			displayOrder:1,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		profileOptions << new OptionType(
			name:'serviceType',
			code:'plugin.bigip.profile.serviceType',
			fieldName:'serviceType',
			fieldContext:'domain',
			displayOrder:2,
			fieldLabel:'Service Type',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginProfileServiceTypes'
		)
		profileOptions << new OptionType(
			name:'proxyType',
			code:'plugin.bigip.profile.proxyType',
			fieldName:'proxyType',
			fieldContext:'domain',
			displayOrder:3,
			fieldLabel:'Proxy Type',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginHttpProxies',
			visibleOnCode:'loadBalancerProfile.serviceType:http'
		)
		profileOptions << new OptionType(
			name:'sslCert',
			code:'plugin.bigip.profile.sslCert',
			fieldName:'sslCert',
			fieldContext:'domain',
			displayOrder:4,
			fieldLabel:'SSL Cert',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpCertSelect',
			visibleOnCode:'loadBalancerProfile.serviceType:client-ssl'
		)
		profileOptions << getPartitionOptionType()

		return profileOptions
	}

	Collection<OptionType> getPoolOptionTypes() {
		Collection<OptionType> poolOptions = new ArrayList<OptionType>()
		poolOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.pool.name',
			fieldName:'name',
			fieldContext:'domain',
			displayOrder:10,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		poolOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.pool.description',
			fieldName:'description',
			fieldContext:'domain',
			displayOrder:11,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		poolOptions << new OptionType(
			name:'balanceMode',
			code:'plugin.bigip.pool.balanceMode',
			fieldName:'vipBalance',
			fieldContext:'domain',
			displayOrder:12,
			fieldLabel:'Balance Mode',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginBalanceModes'
		)
		poolOptions << new OptionType(
			name:'port',
			code:'plugin.bigip.pool.port',
			fieldName:'port',
			fieldContext:'domain',
			displayOrder:13,
			fieldLabel:'Service Port',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		poolOptions << new OptionType(
			name:'members',
			code:'plugin.bigip.pool.members',
			fieldName:'members.id',
			fieldContext:'domain',
			displayOrder:14,
			fieldLabel:'Members',
			required:false,
			inputType:OptionType.InputType.MULTI_TYPEAHEAD,
			optionSource:'bigIpPluginNodes',
			editTemplate:'/templates/poolMemberRow',
			idTemplate:'/templates/poolMemberId'
		)
		poolOptions << new OptionType(
			name:'monitors',
			code:'plugin.bigip.pool.monitors',
			fieldName:'monitors.id',
			fieldContext:'domain',
			displayOrder:15,
			fieldLabel:'Health Monitors',
			required:false,
			inputType:OptionType.InputType.MULTI_TYPEAHEAD,
			optionSource:'bigIpPluginHealthMonitors'
		)
		/* poolOptions << new OptionType(
			name:'persistence',
			code:'plugin.bigip.pool.persistence',
			fieldName:'persistence',
			fieldContext:'domain',
			displayOrder:16,
			fieldLabel:'Persistence',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPoolPersistenceModes'
		) */
		poolOptions << getPartitionOptionType()

		return poolOptions
	}

	Collection<OptionType> getHealthMonitorOptionTypes() {
		Collection<OptionType> monitorOptions = new ArrayList<OptionType>()
		monitorOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.monitor.name',
			fieldName:'name',
			fieldContext:'domain',
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		monitorOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.monitor.description',
			fieldName:'description',
			fieldContext:'domain',
			displayOrder:2,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		monitorOptions << new OptionType(
			name:'monitor',
			code:'plugin.bigip.monitor.monitor',
			fieldName:'monitor.id',
			displayOrder:4,
			fieldLabel:'Parent Monitor',
			required:true,
			inputType:OptionType.InputType.SELECT,
			fieldContext:'config',
			optionSource:'bigIpPluginHealthMonitors'
		)
		/*monitorOptions << new OptionType(
			name:'monitorType',
			code:'plugin.bigip.monitor.monitorType',
			fieldName:'monitorType',
			displayOrder:5,
			fieldLabel:'Monitor Type',
			fieldContext:'domain',
			required:false,
			inputType:OptionType.InputType.TEXT
		)*/
		monitorOptions << new OptionType(
			name:'destination',
			code:'plugin.bigip.monitor.destination',
			fieldName:'monitorDestination',
			fieldContext:'domain',
			displayOrder:6,
			fieldLabel:'Destination',
			required:true,
			inputType:OptionType.InputType.TEXT,
			defaultValue:'*:*'
		)
		monitorOptions << new OptionType(
			name:'interval',
			code:'plugin.bigip.monitor.interval',
			fieldName:'monitorInterval',
			fieldContext:'domain',
			displayOrder:7,
			fieldLabel:'Interval',
			required:true,
			inputType:OptionType.InputType.TEXT,
			defaultValue:'5'
		)
		monitorOptions << new OptionType(
			name:'timeout',
			code:'plugin.bigip.monitor.timeout',
			fieldName:'monitorTimeout',
			fieldContext:'domain',
			displayOrder:8,
			fieldLabel:'Timeout',
			required:true,
			inputType:OptionType.InputType.TEXT,
			defaultValue:'15'
		)
		monitorOptions << new OptionType(
			name:'monitorConfig',
			code:'plugin.bigip.monitor.config',
			fieldName:'monitorConfig',
			displayOrder:9,
			fieldLabel:'Config',
			required:false,
			inputType:OptionType.InputType.CODE_EDITOR,
			fieldCondition:'config'
		)
		/* monitorOptions << new OptionType(
			name:'maxRetries',
			code:'plugin.bigip.monitor.maxRetries',
			fieldName:'maxRetries',
			fieldContext:'domain',
			displayOrder:10,
			fieldLabel:'Max Retries',
			required:false,
			inputType:OptionType.InputType.TEXT,
			defaultValue:5
		)*/
		monitorOptions << getPartitionOptionType()

		return monitorOptions
	}

	Collection<OptionType> getVirtualServerOptionTypes() {
		Collection<OptionType> virtualServerOptions = new ArrayList<OptionType>()
		virtualServerOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.virtualService.name',
			fieldName:'vipName',
			fieldContext:'domain',
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'vipAddress',
			code:'plugin.bigip.virtualService.vipHostname',
			fieldName:'vipHostname',
			fieldContext:'domain',
			displayOrder:2,
			fieldLabel:'VIP Hostname',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'vipAddress',
			code:'plugin.bigip.virtualService.vipAddress',
			fieldName:'vipAddress',
			fieldContext:'domain',
			displayOrder:3,
			fieldLabel:'VIP Address',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'vipPort',
			code:'plugin.bigip.virtualService.vipPort',
			fieldName:'vipPort',
			fieldContext:'domain',
			displayOrder:4,
			fieldLabel:'VIP Port',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'persistence',
			code:'plugin.bigip.virtualService.persistence',
			fieldName:'vipSticky',
			fieldContext:'domain',
			displayOrder:5,
			fieldLabel:'Persistence',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginVirtualServerPersistence'
		)
		virtualServerOptions << new OptionType(
			name:'balanceMode',
			code:'plugin.bigip.virtualService.balanceMode',
			fieldName:'vipBalance',
			fieldContext:'domain',
			displayOrder:6,
			fieldLabel:'Balance Mode',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginBalanceModes'
		)
		virtualServerOptions << new OptionType(
			name:'defaultPool',
			code:'plugin.bigip.virtualService.defaultPool',
			fieldName:'defaultPool',
			fieldContext:'domain',
			displayOrder:11,
			fieldLabel:'Default Pool',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginVirtualServerPools'
		)
		virtualServerOptions << getPartitionOptionType()
	}

	Collection<OptionType> getNodeOptionTypes() {
		List<OptionType> nodeOptions = new ArrayList<OptionType>()
		nodeOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.node.name',
			fieldName:'name',
			fieldContext:'domain',
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.node.description',
			fieldName:'description',
			fieldContext:'domain',
			displayOrder:2,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << new OptionType(
			name:'ipAddress',
			code:'plugin.bigip.node.ipAddress',
			fieldName:'ipAddress',
			fieldContext:'domain',
			displayOrder:3,
			fieldLabel:'IP Address',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << new OptionType(
			name:'monitor',
			code:'plugin.bigip.node.monitor',
			fieldName:'monitor.id',
			fieldContext:'domain',
			displayOrder:4,
			fieldLabel:'Health Monitor',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginHealthMonitors'
		)
		nodeOptions << new OptionType(
			name:'port',
			code:'plugin.bigip.node.port',
			fieldName:'port',
			fieldContext:'domain',
			displayOrder:5,
			fieldLabel:'Port',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << getPartitionOptionType()
	}

	OptionType getPartitionOptionType() {
		return new OptionType(
			name:'partition',
			code:'plugin.bigip.monitor.partition',
			fieldName:'partition',
			fieldContext:'domain',
			displayOrder:99,
			fieldLabel:'Partition',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPartitions',
			defaultValue:'Common'
		)
	}

	/**
	 * This method is called to validate connectivity to the BigIP load balancer integration.  It verifies that the host
	 * info and credentials are valid and the plugin can
	 * @param loadBalancer
	 * @param opts
	 * @return
	 */
	@Override
	ServiceResponse validate(NetworkLoadBalancer loadBalancer, Map opts) {
		ServiceResponse response = ServiceResponse.error()
		response.data = loadBalancer
		def apiUrl = getApiUrl(loadBalancer)
		boolean hostOnline = false
		// attempt a network connection to the bigip integration first
		try {
			def apiUrlObj = new URL(apiUrl)
			def apiHost = apiUrlObj.host
			def apiPort = apiUrlObj.port > 0 ? apiUrlObj.port : (apiUrlObj?.protocol?.toLowerCase() == 'https' ? 443 : 80)
			hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)
		}
		catch (Throwable t) {
			log.error("Cannot connect to ${apiUrl}: ${t.message}.message", t)
			response.addError("Cannot connect to ${apiUrl}: ${t.message}.message")
		}

		// if host is online, attempt to authenticate to it
		if (hostOnline) {
			try {
				def base = getConnectionBase(loadBalancer)
				if (base.authToken) {
					response.success = true
				}
			}
			catch (Throwable t) {
				log.error(t.message, t)
				response.addError("Failured to connect to load balancer: ${t}")
			}
		}

		return response
	}

	@Override
	ServiceResponse initializeLoadBalancer(NetworkLoadBalancer loadBalancer, Map opts) {
		return null
	}

	@Override
	ServiceResponse refresh(NetworkLoadBalancer loadBalancer) {
		ServiceResponse rtn = new ServiceResponse(success: false)
		log.info "syncing load balancer: ${loadBalancer.name}"

		try {
			def apiUrl = getApiUrl(loadBalancer)
			boolean hostOnline = false
			def apiUrlObj = new URL(apiUrl)
			def apiHost = apiUrlObj.host
			def apiPort = apiUrlObj.port > 0 ? apiUrlObj.port : (apiUrlObj?.protocol?.toLowerCase() == 'https' ? 443 : 80)
			hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)

			if (hostOnline) {
				//cache stuff
				(new PartitionSync(this.plugin, loadBalancer)).execute()
				(new NodesSync(this.plugin, loadBalancer)).execute()
				(new HealthMonitorSync(this.plugin, loadBalancer)).execute()
				(new PoolSync(this.plugin, loadBalancer)).execute()
				(new PolicySync(this.plugin, loadBalancer)).execute()
				(new ProfileSync(this.plugin, loadBalancer)).execute()
				(new CertificateSync(this.plugin, loadBalancer)).execute()
				(new PersistenceSync(this.plugin, loadBalancer)).execute()
				(new IRuleSync(this.plugin, loadBalancer)).execute()
				(new InstanceSync(this.plugin, loadBalancer)).execute()

				// update status
				morpheusContext.loadBalancer.updateLoadBalancerStatus(loadBalancer, 'ok', null)
				morpheusContext.loadBalancer.clearLoadBalancerAlarm(loadBalancer)
				rtn.success = true
			}
			else {
				rtn.addError("F5 BigIP is unreachable")
			}
		}
		catch (e) {
			log.error("refresh load balancer error: ${e}", e)
			rtn.addError(e)
		}
		return rtn
	}

	@Override
	Collection<NetworkLoadBalancerType> getLoadBalancerTypes() {
		NetworkLoadBalancerType type = new NetworkLoadBalancerType(
			code:code,
			name:'F5 BigIP',
			internal:false,
			enabled:true,
			networkService:'pluginLoadBalancerService',
			initializeQueue:'pluginInitializeLoadBalancerQueue',
			createType:'multi',
			supportsCerts:true,
			creatable:true,
			viewSet:'bigip',
			supportsHostname:true,
			supportsSticky:true,
			supportsBalancing:true,
			supportsVip:true,
			format:'external',
			sharedVipMode:'none',
			hasVirtualServers:true,
			hasMonitors:true,
			hasNodes:true,
			hasNodeMonitors:true,
			hasPolicies:true,
			editable: true,
			removable: true,
			hasProfiles:true,
			hasRules:true,
			hasScripts:true,
			hasServices:false,
			hasPools:true,
			createVirtualServers:true,
			createMonitors:true,
			createNodes:true,
			createPolicies:true,
			createProfiles:true,
			createRules:false,
			createScripts:false,
			createServices:false,
			createPools:true,
			nameEditable: true,
			createPricePlans: true,
			optionTypes:getOptionTypes(),
			policyOptionTypes:getPolicyOptionTypes(),
			policyRuleOptionTypes:getPolicyRuleOptionTypes(),
			vipOptionTypes:getVirtualServerOptionTypes(),
			poolOptionTypes:getPoolOptionTypes(),
			profileOptionTypes:getProfileOptionTypes(),
			nodeOptionTypes:getNodeOptionTypes(),
			monitorOptionTypes:getHealthMonitorOptionTypes()
		)

		return [type]
	}

	// methods used for entity syncing in the f5 world
	def listPartitions(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/auth/partition"
		def params = [uri:apiConfig.url,
					  path:endpointPath,
					  username:apiConfig.username,
					  password:apiConfig.password,
					  authToken:apiConfig.authToken
		]
		//add paging for large lists
		def results = callApi(params, 'GET')
		if (results.success) {
			rtn.success = results.success
			rtn.partitions = results.data.items
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get partitions results: ${results}")
		return rtn
	}

	/**
	 * This method will grab a list of nodes from the bigip api
	 * @param loadBalancer
	 */
	def listNodes(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/ltm/node"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			rtn.success = true
			rtn.nodes = results.data.items
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get nodes results: ${results}")
		return rtn
	}

	def listHealthMonitors(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false, monitors:[]]
		def endpointPath = "${apiConfig.path}/tm/ltm/monitor"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			urlParams:['expandSubcollections':'true'],
			authToken:apiConfig.authToken
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			rtn.success = true
			rtn.authToken = apiConfig.authToken

			for (item in results.data.items) {
				if(item.reference?.link) {
					def serviceType = BigIpUtility.parseServiceType(item.reference.link)
					//load types
					if(serviceType) {
						params.path = endpointPath + '/' + serviceType
						def subResults = callApi(params, 'GET')
						if(subResults.success) {
							for (subItem in subResults.data?.items) {
								def row = subItem
								row.serviceType = serviceType
								rtn.monitors << row
							}
						}
					}
				}
			}
		}

		log.debug("get health monitors results: ${results}")
		return rtn
	}

	def listPools(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/ltm/pool"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken
			//,urlParams:['expandSubcollections':'true']
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			rtn.success = true
			rtn.pools = results.data.items
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer pools results: ${results}")
		return rtn
	}

	def listPoolMembers(NetworkLoadBalancerPool pool, Map opts) {
		def apiConfig = getConnectionBase(pool.loadBalancer, [authToken:opts?.authToken])
		def rtn = [success:false]
		def externalId = BigIpUtility.convertExternalId(pool.externalId)
		def endpointPath = "${apiConfig.path}/tm/ltm/pool/${externalId}/members"
		def params = [uri:apiConfig.url,
					  path:endpointPath,
					  username:apiConfig.username,
					  password:apiConfig.password,
					  authToken:apiConfig.authToken
					  //,urlParams:['expandSubcollections':'true']
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			rtn.success = true
			rtn.poolMembers = results.data.items
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer pool members results: ${results}")
		return rtn
	}

	def listPolicies(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/ltm/policy"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken,
			urlParams:['expandSubcollections':'true']
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			def policies = results.data.items.findAll { policy ->
				return policy.status == 'published'
			}
			rtn.success = true
			rtn.policies = policies
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer policies: ${results}")
		return rtn
	}

	def listPersistencePolicies(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false, persistencePolicies:[]]
		def endpointPath = "${apiConfig.path}/tm/ltm/persistence"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken,
			urlParams:[expandSubcollections:'true']
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			for (policy in results.data.items) {
				params.path = "${endpointPath}/${BigIpUtility.extractReferenceId(policy.reference?.link)}"
				params.urlParams = ['expandSubcollections':'true']
				def detailResults = callApi(params, 'GET')
				if (detailResults.success && detailResults.data?.items)
					rtn.persistencePolicies.addAll(detailResults.data.items)
			}
			rtn.success = true
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer persistence policies: ${results}")
		return rtn
	}

	def listProfiles(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false, profiles:[]]
		def endpointPath = "${apiConfig.path}/tm/ltm/profile"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken,
			urlParams:['expandSubcollections':'true']
		]

		// first grab service types
		def results = callApi(params, 'GET')
		if (results.success) {
			for (item in results.data.items) {
				if(item.reference?.link) {
					def serviceType = BigIpUtility.parseServiceType(item.reference.link)
					params.path = new URL(item.reference.link).path
					def subResults = callApi(params, 'GET')
					if (subResults.success) {
						for (subItem in subResults.data.items) {
							def row = subItem
							row.serviceType = serviceType
							rtn.profiles << row
						}
					}
				}
			}
			rtn.success = true
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer profiles: ${results}")

		return rtn
	}

	def listCertificates(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/sys/file/ssl-cert"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken
		]
		def results = callApi(params, 'GET')

		if (results.success) {
			rtn.success = true
			rtn.certificates = results.data.items
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer certificates: ${results}")

		return rtn
	}

	def listIRules(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/ltm/rule"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken
		]
		def results = callApi(params, 'GET')

		if (results.success) {
			rtn.success = true
			rtn.irules = results.data.items
			rtn.authToken = apiConfig.authToken
		}

		log.debug("get load balancer iRules: ${results}")

		return rtn
	}

	def listVirtualServers(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false, virtualServers:[]]
		def endpointPath = "${apiConfig.path}/tm/ltm/virtual"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken,
			urlParams:['expandSubcollections':'true']
		]
		def results = callApi(params, 'GET')

		if (results.success) {
			for (item in results.data.items) {
				def row = item
				row.vipAddress = BigIpUtility.parseDetination(item.destination)
				row.vipPort = BigIpUtility.parseDetinationPort(item.destination)
				rtn.virtualServers << row
			}
			rtn.authToken = apiConfig.authToken
			rtn.success = true
		}

		log.debug("get load balancer virtual servers: ${results}")

		return rtn
	}

	def listProfileServiceTypes(NetworkLoadBalancer loadBalancer) {
		def apiConfig = getConnectionBase(loadBalancer)
		def rtn = [success:false, serviceTypes:[]]
		def endpointPath = "${apiConfig.path}/tm/ltm/profile"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			authToken:apiConfig.authToken,
			urlParams:['expandSubcollections':'true']
		]
		def results = callApi(params, 'GET')

		if (results.success) {
			for (item in results.data.items) {
				if(item.reference?.link) {
					def serviceType = BigIpUtility.parseServiceType(item.reference.link)
					rtn.serviceTypes << [name:serviceType, href:item.reference.link]
				}
			}
			rtn.authToken = apiConfig
			rtn.success = true
		}

	}

	// service methods for api interaction
	@Override
	ServiceResponse createLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return null
	}

	@Override
	ServiceResponse deleteLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return null
	}

	@Override
	ServiceResponse updateLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return null
	}

	@Override
	ServiceResponse createLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		def monitorSvc = morpheus.loadBalancer.monitor
		ServiceResponse rtn = ServiceResponse.error()
		try {
			//prep up the create call and pass it
			def loadBalancer = monitor.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def monitorConfig = apiConfig.clone()
			def itemConfig = monitor.getConfigMap()
			//fill in config
			monitorConfig.name = monitor.name
			monitorConfig.serviceType = monitor.monitorType
			monitorConfig.description = monitor.description
			monitorConfig.timeout = monitor.monitorTimeout
			monitorConfig.interval = monitor.monitorInterval
			monitorConfig.destination = monitor.monitorDestination
			monitorConfig.partition = monitor.partition
			if(itemConfig['monitor.id']) {
				def parentMonitor = monitorSvc.findById(itemConfig['monitor.id'].toLong()).blockingGet()
				if(parentMonitor.value.isPresent())
					monitorConfig.defaultsFrom = parentMonitor.value.get().name
			}
			if(itemConfig.monitorConfig) {
				//additional json config
				def extraConfig = new groovy.json.JsonSlurper().parseText(itemConfig.monitorConfig) ?: [:]
				monitorConfig.monitorConfig = extraConfig
			}
			//create it
			log.debug("monitor config: {}", monitorConfig)
			def results = createHealthMonitor(monitorConfig)
			log.debug("api results: {}", results)
			rtn.success = results.success
			if (rtn.success == true) {
				monitor.externalId = results.healthMonitor?.fullPath
				rtn.data = monitor
			}
			else {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results.message ?: results.msg
			}
			return rtn
		}
		catch (Throwable t) {
			log.error("Unable to create health monitor: ${t.message}", t)
		}
	}

	@Override
	ServiceResponse deleteLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		ServiceResponse rtn = ServiceResponse.error()
		try {
			def loadBalancer = monitor.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def monitorConfig = apiConfig.clone()
			monitorConfig.name = monitor.name
			monitorConfig.serviceType = monitor.monitorType
			monitorConfig.partition = monitor.partition
			monitorConfig.externalId = monitorConfig.serviceType + '/' + BigIpUtility.convertExternalId(monitor.externalId)
			monitorConfig.authToken = apiConfig.authToken
			log.debug "monitor config: ${monitorConfig}"
			def results = deleteHealthMonitor(monitorConfig)
			log.info("api results: {}", results)
			rtn.success = results.success
			rtn.data = [found:results.found, authToken:apiConfig.authToken]
		}
		catch(Throwable t) {
			log.error("error removing monitor: ${t}", t)
			rtn.msg = 'unknown error removing monitor ' + t.message
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		def monitorSvc = morpheus.loadBalancer.monitor
		ServiceResponse rtn = ServiceResponse.error()
		try {
			//prep up the create call and pass it
			def loadBalancer = monitor.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def monitorConfig = apiConfig.clone()
			def itemConfig = monitor.getConfigMap()
			//fill in config
			monitorConfig.name = monitor.name
			monitorConfig.serviceType = monitor.monitorType
			monitorConfig.description = monitor.description
			monitorConfig.timeout = monitor.monitorTimeout
			monitorConfig.interval = monitor.monitorInterval
			monitorConfig.authToken = apiConfig.authToken
			if(itemConfig['monitor.id']) {
				def parentMonitor = monitorSvc.findById(itemConfig['monitor.id'].toLong())
				if(parentMonitor.value.isPresent())
					monitorConfig.defaultsFrom = parentMonitor.value.get().name
			}
			if(itemConfig.monitorConfig) {
				//additional json config
				def extraConfig = new groovy.json.JsonSlurper().parseText(itemConfig.monitorConfig) ?: [:]
				monitorConfig.monitorConfig = extraConfig
			}
			//update it
			monitorConfig.externalId = monitorConfig.serviceType + '/' + BigIpUtility.convertExternalId(monitor.externalId)
			log.debug("monitor config: {}", monitorConfig)
			def results = updateHealthMonitor(monitorConfig)
			log.debug("api results: {}", results)
			rtn.success = results.success
			rtn.authToken = results.authToken
			if(rtn.success != true) {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results.content?.message ?: results.message
			}
		}
		catch (Throwable t) {
			log.error("Unable to update health monitor: ${t.message}", t)
		}
	}

	@Override
	ServiceResponse validateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		ServiceResponse rtn = ServiceResponse.error()
		try {
			if(!monitor.name) {
				rtn.errors.name = 'Name is required'
			}

			//extra config
			if(monitor.config) {
				log.debug("validating monitor config: {}", monitor.config)
				//try to parse it - show error if bad
				try {
					new groovy.json.JsonSlurper().parseText(monitor.config)
				} catch(e2) {
					//bad
					rtn.errors.monitorConfig = 'monitor config value is invalid json'
				}
			}
			//results
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating monitor: ${e}", e)
		}
		return rtn
	}

	// Nodes crud
	@Override
	ServiceResponse createLoadBalancerNode(NetworkLoadBalancerNode loadBalancerNode) {
		ServiceResponse rtn = ServiceResponse.error()
		try {
			def loadBalancer = loadBalancerNode.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def nodeConfig = [:] + apiConfig
			nodeConfig.name = loadBalancerNode.name
			nodeConfig.port = loadBalancerNode.port
			nodeConfig.description = loadBalancerNode.description
			nodeConfig.ipAddress = loadBalancerNode.ipAddress
			nodeConfig.healthMonitor = loadBalancerNode.monitor?.externalId
			nodeConfig.partition = loadBalancerNode.partition
			//create it
			log.debug("node config: ${nodeConfig}")
			def results = createServer(nodeConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.data = [authToken:apiConfig.authToken]
			// if successful make sure the new id of the node is set before returning to caller so it may be persisted
			if(rtn.success == true) {
				loadBalancerNode.externalId = results.node?.fullPath
				rtn.data.node = loadBalancerNode
				rtn.success = true
			}
			else {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results.data?.message ?: results.msg
			}
		} catch(Throwable t) {
			log.error("error creating node: ${t}", t)
			rtn.msg = 'unknown error creating node: ' + t.message
		}
		return rtn
	}

	@Override
	ServiceResponse deleteLoadBalancerNode(NetworkLoadBalancerNode loadBalancerNode) {
		def rtn = [success:false]
		try {
			def loadBalancer = loadBalancerNode.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def nodeConfig = [:] + apiConfig
			nodeConfig.externalId = loadBalancerNode.externalId
			nodeConfig.name = loadBalancerNode.name
			nodeConfig.authToken = apiConfig.authToken
			nodeConfig.partition = loadBalancerNode.partition
			def results = deleteServer(nodeConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.found = results.found ?: false
			rtn.authToken = results.authToken
		}
		catch(Throwable t) {
			log.error("error removing node: ${t}", t)
			rtn.msg = 'unknown error removing node: ' + t.message
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerNode(NetworkLoadBalancerNode node) {
		return null
	}

	@Override
	ServiceResponse validateLoadBalancerNode(NetworkLoadBalancerNode node) {
		ServiceResponse rtn = ServiceResponse.error()
		try {
			if(!node.name) {
				rtn.errors.name = 'Name is required'
			}
			if(!node.ipAddress) {
				rtn.errors.ipAddress = 'Address is required'
			}
			if (!node.port) {
				rtn.errors.port = 'Port is required'
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating node: ${e}", e)
		}
		return rtn
	}

	// Crud for lb pools
	@Override
	ServiceResponse createLoadBalancerPool(NetworkLoadBalancerPool pool) {
		ServiceResponse rtn = ServiceResponse.error()
		try {
			//prep up the create call and pass it
			def loadBalancer = pool.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def poolConfig = [:] + apiConfig
			def itemConfig = pool.getConfigMap()
			//fill in config
			poolConfig.name = pool.name
			poolConfig.port = pool.port
			poolConfig.description = pool.description
			poolConfig.partition = pool.partition
			poolConfig.loadBalancingMode = BigIpUtility.getLoadBalancingMode(pool.vipBalance)
			poolConfig.authToken = apiConfig.authToken
			if(pool.monitors?.size() > 0)
				poolConfig.monitorName = pool.monitors.collect{ it.externalId }?.join(' and ')
			//create it
			log.debug("pool config: ${poolConfig}")
			def results = createPool(poolConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.data.authToken = results.authToken
			if(rtn.success == true) {
				pool.externalId = results.pool?.fullPath
				//add pool memebers
				if(pool.members?.size() > 0) {
					def memberConfig = apiConfig + [name:pool.name, partition:pool.partition, authToken:results.authToken]
					memberConfig.members = pool.members.collect{ member -> return [name:member.node.name, partition:member.node.partition, port:(member.port ?: pool.port)] }
					def memberResults = addPoolMembers(memberConfig)
					log.debug("memberResults: {}", memberResults)
					if(memberResults.success != true) {
						rtn.msg = 'failed to assign pool members: ' + memberResults.msg
					}
				}
			} else {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results?.msg ?: results.message
			}
		} catch(e) {
			log.error("error creating pool: ${e}", e)
			rtn.msg = 'unknown error creating pool ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse deleteLoadBalancerPool(NetworkLoadBalancerPool pool) {
		ServiceResponse rtn = ServiceResponse.error()
		try {
			def loadBalancer = pool.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def poolConfig = [:] + apiConfig
			poolConfig.externalId = pool.externalId
			poolConfig.name = pool.name
			poolConfig.partition = pool.partition
			def results = deletePool(poolConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.msg = results.message
		} catch(e) {
			log.error("error removing pool: ${e}", e)
			rtn.msg = 'unknown error removing pool ' + e.message
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerPool(NetworkLoadBalancerPool pool) {
		return null
	}

	@Override
	ServiceResponse validateLoadBalancerPool(NetworkLoadBalancerPool pool) {
		def rtn = ServiceResponse.error()
		try {
			if(!pool.name) {
				rtn.errors.name = 'Name is required'
			}
			if (!pool.port) {
				rtn.errors.port = 'Port is required'
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating pool: ${e}", e)
		}
		return rtn
	}

	// Crud for virtual servers/instance
	@Override
	ServiceResponse createLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return null
	}

	@Override
	ServiceResponse deleteLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return null
	}

	@Override
	ServiceResponse updateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return null
	}

	@Override
	ServiceResponse validateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		def policySvc = morpheus.loadBalancer.policy
		def rtn = [success:false, errors:[:], msg:null]
		try {
			//need a name
			if(!instance.vipName) {
				rtn.errors.vipName = 'Name is required'
			}
			if(!instance.vipProtocol) {
				rtn.errors.vipProtocol = 'Protocol is required'
			}
			if(!instance.vipAddress) {
				rtn.errors.vipAddress = 'Vip Address is required'
			}
			if(!instance.vipPort) {
				rtn.errors.vipPort = 'Vip Port is required'
			}

			def policies = []
			if(instanceConfig.policies?.id) {
				if(instanceConfig.policies.id instanceof CharSequence) {
					def policy = policySvc.findById(instanceConfig.policies.id.toLong()).blockingGet()
					if(policy.value.isPresent())
						policies << policy.value.get()
				} else { //if(poolConfig.nodes.id instanceof Collection) {
					instanceConfig.policies.id?.each { rowId ->
						def rowLongId = rowId ? rowId.toLong() : null
						def policy = rowLongId ? policySvc.findById(rowLongId).blockingGet() : null
						if(policy.value.isPresent())
							policies << policy.value.get()
					}
				}
			}
			//need either a policy or a pool
			if(opts.mode != 'instance' && !opts.config?.defaultPool && policies?.size() == 0) {
				rtn.errors.defaultPool = 'Pool or a policy is required'
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating virtual server: ${e}", e)
		}
		return rtn
	}

	def deleteServer(Map opts) {
		def rtn = [success:false]
		def server = loadServer(opts)
		if(server.found == true) {
			def nodeName = BigIpUtility.buildPartitionedName(opts)
			def endpointPath = "${opts.path}/tm/ltm/node/${nodeName}"
			def params = [
				uri:opts.url,
				path:endpointPath,
				username:opts.username,
				password:opts.password,
				authToken:opts.authToken
			]
			def results = callApi(params, 'DELETE')
			rtn.success = results.success
		}
		else {
			rtn.found = false
			rtn.success = true
		}
		return rtn
	}

	def createServer(Map opts) {
		def rtn = [success:false]
		def server = loadServer(opts)
		if(server.found == true) {
			rtn.success = true
			rtn.found = true
			rtn.serverName = server.node.name
			rtn.node = server.node
		}
		else {
			def endpointPath = "${opts.path}/tm/ltm/node"
			def data = [
				name:opts.name,
				address:opts.ipAddress,
				description:opts.description,
				monitor:opts.healthMonitor,
				partition:opts.partition
			]
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password,
				authToken:server.authToken
			]
			def results = callApi(params, 'POST')
			if (results.success) {
				rtn.success = true
				rtn.serverName = results.data.name
				rtn.node = results.data
			}
			else {
				rtn.msg = results.msg
			}
		}
		return rtn
	}

	def loadServer(Map opts) {
		def rtn = [success:false, found:false]
		def nodeName = BigIpUtility.buildPartitionedName(opts)
		def endpointPath = "${opts.path}/tm/ltm/node/${nodeName}"
		def params = [
			uri:opts.url,
			path:endpointPath,
			username:opts.username,
			password:opts.password,
			authToken:opts.authToken
		]
		def results = callApi(params, 'GET')
		log.debug("results: ${results}")
		if(results.success) {
			rtn.success = true
			rtn.found = true
			rtn.name = opts.name
			rtn.node = results.data
			rtn.authToken = opts.authToken
		}
		else {
			rtn.msg = results.msg
		}
		return rtn
	}

	def updateHealthMonitor(Map opts) {
		def rtn = [success:false]
		def monitor = loadHealthMonitor(opts)
		if(monitor.found != true) {
			rtn.success = false
			rtn.found = false
			rtn.healthMonitor = monitor.healthMonitor
		}
		else {
			def endpointPath = BigIpUtility.buildApiPath("${opts.path}/tm/ltm/monitor/", opts.externalId, opts.name, opts.serviceType ?: 'http')
			def data = [
				name:opts.name,
				description:opts.description
			]
			if(opts.destination)
				data.destination = opts.destination
			if(opts.interval)
				data.interval = opts.interval.toInteger()
			if(opts.timeout)
				data.timeout = opts.timeout.toInteger()
			if(opts.defaultsFrom)
				data.defaultsFrom = opts.defaultsFrom
			if(opts.monitorConfig)
				data += opts.monitorConfig
			// call api
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password,
				authToken:opts.authToken
			]
			def results = callApi(params, 'PATCH')
			if (results.success) {
				rtn.success = true
				rtn.healthMonitor = results.data
			}
			else {
				rtn.msg = results.msg
			}
			log.debug("updateHealthMonitor: {}", results)
		}
		return rtn
	}

	def createHealthMonitor(Map opts) {
		// only create this monitor if it does not exist
		def rtn = [success:false]
		def monitor = loadHealthMonitor(opts)
		if(monitor.found == true) {
			rtn.success = true
			rtn.found = true
			rtn.healthMonitor = monitor.healthMonitor
		}
		else {
			def serviceType = opts.serviceType ?: 'http'
			def serviceDetination = opts.destination ?: '*:*'
			def endpointPath = BigIpUtility.buildApiPath("${opts.path}/tm/ltm/monitor/", null, null, serviceType)
			def data = [
				name:opts.name,
				destination:serviceDetination,
				description:opts.description,
				partition:opts.partition
			]
			if(opts.interval)
				data.interval = opts.interval.toInteger()
			if(opts.timeout)
				data.timeout = opts.timeout.toInteger()
			if(opts.defaultsFrom)
				data.defaultsFrom = opts.defaultsFrom
			if(opts.monitorConfig)
				data += opts.monitorConfig
			// create the health monitor
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password,
				authToken:opts.authToken
			]
			def resp = callApi(params, 'POST')
			if (resp.success) {
				rtn.healthMonitor = resp.data
				rtn.success = true
			}
		}
		return rtn
	}

	def loadHealthMonitor(Map opts) {
		def rtn = [success:false, found:false]
		def monitorName = BigIpUtility.buildPartitionedName(opts)
		def endpointPath = BigIpUtility.buildApiPath("${opts.path}/tm/ltm/monitor/", opts.externalId, monitorName, opts.serviceType ?: 'http')
		def params = [
			uri:opts.url,
			path:endpointPath,
			username:opts.username,
			password:opts.password,
			authToken:opts.authToken
		]
		def results = callApi(params, 'GET')
		log.debug("loadHealthMonitor: {}", results)
		if(results.success) {
			rtn.success = true
			rtn.found = true
			rtn.healthMonitor = results.data
			rtn.authToken = params.authToken
		}
		return rtn
	}

	def deleteHealthMonitor(Map opts) {
		def rtn = [success:false]
		def healthMonitor = loadHealthMonitor(opts)
		if(healthMonitor.found == true) {
			def itemName = BigIpUtility.buildPartitionedName(opts)
			def endpointPath = BigIpUtility.buildApiPath("${opts.path}/tm/ltm/monitor/", null, itemName, opts.serviceType ?: 'http')
			def params = [
				uri:opts.url,
				path:endpointPath,
				username:opts.username,
				password:opts.password,
				authToken:healthMonitor.authToken
			]
			def results = callApi(params, 'DELETE')
			if (results.success) {
				rtn.success = true
				rtn.found = true
				rtn.authToken = healthMonitor.authToken
			}
		}
		else {
			rtn.found = false
			rtn.success = true
		}
		return rtn
	}

	def createPool(Map opts) {
		def rtn = [success:false]
		def pool = loadPool(opts)
		if(pool.found == true) {
			rtn.success = true
			rtn.found = true
			rtn.authToken = pool.authToken
			rtn.pool = pool.pool
		} else {
			def endpointPath = "${opts.path}/tm/ltm/pool"
			def data = [
				name:opts.name,
				description:opts.description ?: 'created by morpheus',
				loadBalancingMode:opts.loadBalancingMode ?: 'least-connections-member',
				partition:opts.partition
			]
			//add monitor
			if(opts.monitorName)
				data.monitor = opts.monitorName
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password,
				authToken:pool.authToken
			]
			def results = callApi(params, 'POST')
			if (results.success) {
				rtn.success = results.success
				rtn.pool = results.data
				rtn.authToken = pool.authToken
			}
			else {
				rtn.errors = results.errors
				rtn.msg = results.msg
			}
		}
		return rtn
	}

	def loadPool(Map opts) {
		if (!opts.authToken) {
			opts.authToken = getAuthToken(opts).data.token.token
		}
		def rtn = [success:false, found:false]
		def poolName = BigIpUtility.buildPartitionedName(opts)
		def endpointPath = "${opts.path}/tm/ltm/pool/${poolName}"
		def params = [
			uri:opts.url,
			path:endpointPath,
			username:opts.username,
			password:opts.password,
			authToken:opts.authToken
		]
		def results = callApi(params, 'GET')
		log.debug("results: ${results}")
		if(results.success) {
			rtn.success = true
			rtn.found = true
			rtn.pool = results.data ?: [:]
			rtn.authToken = opts.authToken
		}
		return rtn
	}

	def addPoolMembers(Map opts) {
		def rtn = [success:false]
		def poolName = BigIpUtility.buildPartitionedName(opts)
		def endpointPath = "${opts.path}/tm/ltm/pool/${poolName}"
		def members = []
		opts.members.each { member ->
			members << [name:"${member.externalId ?: "/${member.partition}/${member.name}"}:${member.port ?: opts.port}"]
		}
		def data = [members:members]
		//add monitor
		if(opts.monitorName)
			data.monitor = opts.monitorName

		def params = [
			uri:opts.url,
			path:endpointPath,
			body:data,
			username:opts.username,
			password:opts.password,
			authToken:opts.authToken
		]
		def results = callApi(params, 'PATCH')
		rtn = results
		return rtn
	}

	def deletePool(Map opts) {
		def rtn = [success:false]
		def poolId = opts.externalId ?: opts.name
		def pool = poolId ? loadPool(opts) : [found:false]
		if(pool.found == true) {
			def poolName = BigIpUtility.buildPartitionedName(opts)
			def endpointPath = "${opts.path}/tm/ltm/pool/${poolName}"
			def params = [
				uri:opts.url,
				path:endpointPath,
				username:opts.username,
				password:opts.password,
				authToken:pool.authToken
			]
			def results = callApi(params, 'DELETE')
			rtn = results
		} else {
			rtn.found = false
			rtn.success = true
		}
		return rtn
	}

	protected getConnectionBase(NetworkLoadBalancer lb, Map opts = null) {
		def connectionBase = [
			url:"https://${lb.sshHost}:${lb.apiPort}",
			path:'/mgmt',
			username:lb.sshUsername,
			password:lb.sshPassword
		]

		connectionBase.authToken = opts?.authToken ?: getAuthToken(connectionBase)
		return connectionBase
	}

	protected getApiUrl(NetworkLoadBalancer loadBalancer) {
		return 'https://' + loadBalancer.sshHost + (loadBalancer.apiPort ? ':' + loadBalancer.apiPort : '') + '/mgmt'
	}

	protected getAuthToken(Map params) {
		def authParams = params.clone()
		authParams.body = [
			username:authParams.remove('username'),
			password:authParams.remove('password'),
			loginProviderName:'tmos'
		]
		authParams.uri = authParams.url ?: authParams.uri
		authParams.path = '/mgmt/shared/authn/login'
		HttpApiClient httpClient = new HttpApiClient()
		HttpApiClient.RequestOptions reqOpts = new HttpApiClient.RequestOptions(body:authParams.body, ignoreSSL:true)
		def response = httpClient.callJsonApi(
			authParams.uri, authParams.path, reqOpts, 'POST'
		)
		if (response.success) {
			return response.data.token?.token
		}
		else {
			throw new RuntimeException("Failed to authenticate to bigip: ${response.msg}")
		}
	}

	protected callApi(Map params, String method = 'GET', skipAuth = false) {
		if (!params.authToken && !false) {
			params.authToken = getAuthToken(params)
		}

		// set all request headers
		def headers
		if (params.headers)
			headers = params.headers
		else
			headers = [:]

		// set the authentication
		if (params.authToken) {
			headers['X-F5-Auth-Token'] = params.authToken
		}

		HttpApiClient httpClient = new HttpApiClient()
		HttpApiClient.RequestOptions reqOpts = new HttpApiClient.RequestOptions(ignoreSSL:true)
		reqOpts.headers = headers

		if (params.body)
			reqOpts.body = params.body
		if (params.urlParams)
			reqOpts.queryParams = params.urlParams

		def response = httpClient.callJsonApi(
			params.uri, params.path, reqOpts, method
		)

		if (!response.success) {
			log.error("Failure in call to F5 api: ${response.msg}")
		}
		return response
	}

	// constants
	public static final String PROVIDER_CODE = 'morpheus-bigip-provider'
}
