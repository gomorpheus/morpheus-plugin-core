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
import com.morpheusdata.model.Icon
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerPool
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
			code:'bigip-plugin-host',
			fieldName:'sshHost',
			displayOrder:0,
			fieldLabel:'Api Host',
			required:true,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		OptionType apiPort = new OptionType(
			name:'Port',
			code:'bigip-plugin-port',
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
			code:'bigip-plugin-username',
			fieldName:'sshUsername',
			displayOrder:10,
			fieldLabel:'Username',
			required:true,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		OptionType password = new OptionType(
			name:'Password',
			code:'bigip-plugin-password',
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
			displayOrder:0,
			fieldLabel:'name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		profileOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.profile.description',
			fieldName:'description',
			displayOrder:1,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		profileOptions << new OptionType(
			name:'serviceType',
			code:'plugin.bigip.profile.serviceType',
			fieldName:'serviceType',
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
			displayOrder:10,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		poolOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.pool.description',
			fieldName:'description',
			displayOrder:11,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		poolOptions << new OptionType(
			name:'balanceMode',
			code:'plugin.bigip.pool.balanceMode',
			fieldName:'balanceMode',
			displayOrder:12,
			fieldLabel:'Balance Mode',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'balanceModes'
		)
		poolOptions << new OptionType(
			name:'port',
			code:'plugin.bigip.pool.port',
			fieldName:'port',
			displayOrder:13,
			fieldLabel:'Service Port',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		poolOptions << new OptionType(
			name:'members',
			code:'plugin.bigip.pool.members',
			fieldName:'members',
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
			fieldName:'monitors',
			displayOrder:15,
			fieldLabel:'Health Monitors',
			required:false,
			inputType:OptionType.InputType.MULTI_TYPEAHEAD,
			optionSource:'bigIpPluginHealthMonitors'
		)
		poolOptions << new OptionType(
			name:'persistence',
			code:'plugin.bigip.pool.persistence',
			fieldName:'persistence',
			displayOrder:16,
			fieldLabel:'Persistence',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPoolPersistenceModes'
		)
		poolOptions << getPartitionOptionType()

		return poolOptions
	}

	Collection<OptionType> getHealthMonitorOptionTypes() {
		Collection<OptionType> monitorOptions = new ArrayList<OptionType>()
		monitorOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.monitor.name',
			fieldName:'name',
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		monitorOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.monitor.description',
			fieldName:'description',
			displayOrder:2,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		monitorOptions << new OptionType(
			name:'enabled',
			code:'plugin.bigip.monitor.enabled',
			fieldName:'enabled',
			displayOrder:3,
			fieldLabel:'Enabled',
			required:true,
			inputType:OptionType.InputType.CHECKBOX,
			defaultValue:'on'
		)
		monitorOptions << new OptionType(
			name:'monitor',
			code:'plugin.bigip.monitor.monitor',
			fieldName:'monitor',
			displayOrder:4,
			fieldLabel:'Parent Monitor',
			required:true,
			inputType:OptionType.InputType.SELECT,
			fieldContext:'config',
			optionSource:'bigIpPluginHealthMonitors'
		)
		monitorOptions << new OptionType(
			name:'monitorType',
			code:'plugin.bigip.monitor.monitorType',
			fieldName:'monitorType',
			displayOrder:5,
			fieldLabel:'Monitor Type',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		monitorOptions << new OptionType(
			name:'destination',
			code:'plugin.bigip.monitor.destination',
			fieldName:'destination',
			displayOrder:6,
			fieldLabel:'Destination',
			required:true,
			inputType:OptionType.InputType.TEXT,
			defaultValue:'*:*'
		)
		monitorOptions << new OptionType(
			name:'interval',
			code:'plugin.bigip.monitor.interval',
			fieldName:'interval',
			displayOrder:7,
			fieldLabel:'Interval',
			required:true,
			inputType:OptionType.InputType.TEXT,
			defaultValue:'5'
		)
		monitorOptions << new OptionType(
			name:'timeout',
			code:'plugin.bigip.monitor.timeout',
			fieldName:'timeout',
			displayOrder:8,
			fieldLabel:'Timeout',
			required:true,
			inputType:OptionType.InputType.TEXT,
			defaultValue:'15'
		)
		monitorOptions << new OptionType(
			name:'config',
			code:'plugin.bigip.monitor.config',
			fieldName:'config',
			displayOrder:9,
			fieldLabel:'Config',
			required:false,
			inputType:OptionType.InputType.CODE_EDITOR,
			fieldCondition:'config'
		)
		monitorOptions << new OptionType(
			name:'maxRetries',
			code:'plugin.bigip.monitor.maxRetries',
			fieldName:'maxRetries',
			displayOrder:10,
			fieldLabel:'Max Retries',
			required:false,
			inputType:OptionType.InputType.TEXT,
			defaultValue:5
		)
		monitorOptions << getPartitionOptionType()

		return monitorOptions
	}

	Collection<OptionType> getVirtualServerOptionTypes() {
		Collection<OptionType> virtualServerOptions = new ArrayList<OptionType>()
		virtualServerOptions << new OptionType(
			name:'name',
			code:'plugin.bigip.virtualService.name',
			fieldName:'name',
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.virtualService.description',
			fieldName:'description',
			displayOrder:2,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'vipAddress',
			code:'plugin.virtualService.vipAddress',
			fieldName:'vipAddress',
			displayOrder:3,
			fieldLabel:'VIP Address',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'vipPort',
			code:'plugin.virtualService.vipPort',
			fieldName:'vipPort',
			displayOrder:4,
			fieldLabel:'VIP Port',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		virtualServerOptions << new OptionType(
			name:'active',
			code:'plugin.bigip.virtualService.active',
			fieldName:'active',
			displayOrder:5,
			fieldLabel:'Enabled',
			required:true,
			inputType:OptionType.InputType.CHECKBOX,
			defaultValue:'on'
		)
		virtualServerOptions << new OptionType(
			name:'protocol',
			code:'plugin.bigip.virtualService.protocol',
			fieldName:'protocol',
			displayOrder:6,
			fieldLabel:'Protocol',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'virtualServerProtocols'
		)
		virtualServerOptions << new OptionType(
			name:'profiles',
			code:'plugin.bigip.virtualService.profiles',
			fieldName:'profiles',
			displayOrder:7,
			fieldLabel:'Profiles',
			required:false,
			inputType:OptionType.InputType.MULTI_TYPEAHEAD,
			optionSource:'bigIpPluginVirtualServerProfiles'
		)
		virtualServerOptions << new OptionType(
			name:'policies',
			code:'plugin.bigip.virtualService.policies',
			fieldName:'policies',
			displayOrder:8,
			fieldLabel:'Policies',
			required:false,
			inputType:OptionType.InputType.MULTI_TYPEAHEAD,
			optionSource:'bigIpPluginVirtualServerPolicies'
		)
		virtualServerOptions << new OptionType(
			name:'scripts',
			code:'plugin.bigip.virtualService.scripts',
			fieldName:'scripts',
			displayOrder:9,
			fieldLabel:'iRules',
			required:false,
			inputType:OptionType.InputType.MULTI_TYPEAHEAD,
			optionSource:'bigIpPluginVirtualServerScripts',
		)
		virtualServerOptions << new OptionType(
			name:'persistence',
			code:'plugin.bigip.virtualService.persistence',
			fieldName:'persistence',
			displayOrder:10,
			fieldLabel:'Persistence',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginVirtualServerPersistence'
		)
		virtualServerOptions << new OptionType(
			name:'defaultPool',
			code:'plugin.bigip.virtualService.defaultPool',
			fieldName:'defaultPool',
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
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << new OptionType(
			name:'description',
			code:'plugin.bigip.node.description',
			fieldName:'description',
			displayOrder:2,
			fieldLabel:'Description',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << new OptionType(
			name:'ipAddress',
			code:'plugin.bigip.node.ipAddress',
			fieldName:'ipAddress',
			displayOrder:3,
			fieldLabel:'IP Address',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		nodeOptions << new OptionType(
			name:'monitor',
			code:'plugin.bigip.node.monitor',
			fieldName:'monitor',
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
		def rtn = [success:false]
		def endpointPath = "${apiConfig.path}/tm/ltm/monitor"
		def params = [
			uri:apiConfig.url,
			path:endpointPath,
			username:apiConfig.username,
			password:apiConfig.password,
			//urlParams:['expandSubcollections':'true'],
			authToken:apiConfig.authToken
		]
		def results = callApi(params, 'GET')
		if (results.success) {
			rtn.success = true
			rtn.monitors = results.data.items
			rtn.authToken = apiConfig.authToken
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
			authToken:apiConfig.authToken
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
		def results = callApi(params, 'GET')
		if (results.success) {
			for (serviceType in results.data.items) {
				params.path = new URL(serviceType.href).path
				def subResults = callApi(params, 'GET')
				if(subResults.success) {
					for (subItem in subResults.data.items) {
						def row = subItem
						row.serviceType = BigIpUtility.parseServiceType(serviceType.reference.link)
						rtn.profiles << row
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
			throw new RuntimeException("Failure in call to F5 api: ${response.msg}")
		}
		return response
	}

	// constants
	public static final String PROVIDER_CODE = 'morpheus-bigip-provider'
}
