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
import com.morpheusdata.model.AccountCertificate
import com.morpheusdata.model.Icon
import com.morpheusdata.model.Instance
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.NetworkLoadBalancerInstance
import com.morpheusdata.model.NetworkLoadBalancerMonitor
import com.morpheusdata.model.NetworkLoadBalancerNode
import com.morpheusdata.model.NetworkLoadBalancerPolicy
import com.morpheusdata.model.NetworkLoadBalancerPool
import com.morpheusdata.model.NetworkLoadBalancerProfile
import com.morpheusdata.model.NetworkLoadBalancerRule
import com.morpheusdata.model.NetworkLoadBalancerType
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
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
		OptionType credential = new OptionType(
			name:'credentials',
			code:'plugin.bigio.credentials',
			fieldName:'type',
			displayOrder:9,
			defaultValue:'local',
			fieldLabel:'Credentials',
			required:false,
			inputType:OptionType.InputType.CREDENTIAL,
			fieldContext:'credential',
			config:JsonOutput.toJson(credentialTypes:['username-password']).toString()
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

		return [apiHost, apiPort, credential, username, password]
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
			optionSource:'bigIpPluginPolicyControls',
			fieldContext:'domain'
		)
		policyOptions << new OptionType(
			name:'Requires',
			code:'plugin.bigip.policy.requires',
			fieldName:'requires',
			displayOrder:11,
			fieldLabel:'Requires',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyRequires',
			fieldContext:'domain'
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
			defaultValue:'/Common/first-match',
			fieldContext:'domain'
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
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		ruleOptions << new OptionType(
			name:'Field',
			code:'plugin.bigip.policy.rule.field',
			fieldName:'field',
			displayOrder:21,
			fieldLabel:'Field',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyRuleField',
			fieldContext:'domain'
		)
		ruleOptions << new OptionType(
			name:'Operator',
			code:'plugin.bigip.policy.rule.operator',
			fieldName:'operator',
			displayOrder:22,
			fieldLabel:'Operator',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginPolicyRuleOperator',
			fieldContext:'domain'
		)
		ruleOptions << new OptionType(
			name:'Value',
			code:'plugin.bigip.policy.rule.value',
			fieldName:'value',
			displayOrder:23,
			fieldLabel:'Value',
			required:true,
			inputType:OptionType.InputType.TEXT,
			fieldContext:'domain'
		)
		ruleOptions << new OptionType(
			name:'Pool',
			code:'plugin.bigip.policy.rule.pool',
			fieldName:'pool',
			displayOrder:24,
			fieldLabel:'Pool',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginVirtualServerPools',
			fieldContext:'domain'
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
			editable:false,
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
			editable:false,
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
			editable:false,
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
			editable:false,
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

	Collection<OptionType> getInstanceOptionTypes() {
		Collection<OptionType> instanceOptionTypes =  new ArrayList<OptionType>()
		instanceOptionTypes << new OptionType(
			name:'vipName',
			code:'plugin.bigip.instance.vipName',
			fieldName:'vipName',
			fieldContext:'domain',
			displayOrder:1,
			fieldLabel:'Name',
			required:true,
			editable:false,
			inputType:OptionType.InputType.TEXT
		)
		instanceOptionTypes << new OptionType(
			name:'vipHostname',
			code:'plugin.bigip.instance.vipHostname',
			fieldName:'vipHostname',
			fieldContext:'domain',
			displayOrder:2,
			fieldLabel:'VIP Hostname',
			required:false,
			inputType:OptionType.InputType.TEXT
		)
		instanceOptionTypes << new OptionType(
			name:'vipAddress',
			code:'plugin.bigip.instance.vipAddress',
			fieldName:'vipAddress',
			fieldContext:'domain',
			displayOrder:3,
			fieldLabel:'VIP Address',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		instanceOptionTypes << new OptionType(
			name:'vipPort',
			code:'plugin.bigip.instance.vipPort',
			fieldName:'vipPort',
			fieldContext:'domain',
			displayOrder:4,
			fieldLabel:'VIP Port',
			required:true,
			inputType:OptionType.InputType.TEXT
		)
		instanceOptionTypes << new OptionType(
			name:'persistence',
			code:'plugin.bigip.instance.persistence',
			fieldName:'vipSticky',
			fieldContext:'domain',
			displayOrder:5,
			fieldLabel:'Persistence',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginVirtualServerPersistence'
		)
		instanceOptionTypes << new OptionType(
			name:'vipBalance',
			code:'plugin.bigip.instance.balanceMode',
			fieldName:'vipBalance',
			fieldContext:'domain',
			displayOrder:6,
			fieldLabel:'Balance Mode',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginBalanceModes'
		)
		instanceOptionTypes << new OptionType(
			name:'monitor',
			code:'plugin.bigip.instance.monitor',
			fieldName:'monitor',
			fieldContext:'domain',
			displayOrder:7,
			fieldLabel:'Monitor',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'bigIpPluginHealthMonitors'
		)
		instanceOptionTypes << new OptionType(
			name:'sslCert',
			code:'plugin.bigip.instance.sslCert',
			fieldName:'sslCert',
			fieldContext:'domain',
			displayOrder:8,
			fieldLabel:'SSL Certificate',
			required:true,
			inputType:OptionType.InputType.SELECT,
			optionSource:'accountSslCertificate'
		)
		instanceOptionTypes << new OptionType(
			name:'sslRedirectMode',
			code:'plugin.bigip.instance.sslRedirectMode',
			fieldName:'sslRedirectMode',
			fieldContext:'domain',
			displayOrder:9,
			fieldLabel:'SSL Redirect Mode',
			required:false,
			inputType:OptionType.InputType.SELECT,
			optionSource:'sslRedirectMode',
			visibleOnCode:'loadBalancer.sslCert:([^0]),loadBalancerInstance.sslCert:([^0])'
		)
		instanceOptionTypes << getPartitionOptionType()
		return instanceOptionTypes
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
			editable:false,
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
			fieldContext:'config',
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
			editable:false,
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
			editable:false,
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
			editable:false,
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
		ServiceResponse response = ServiceResponse.prepare()
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
				//sync stuff
				//(new PartitionSync(this.plugin, loadBalancer)).execute()
				//(new NodesSync(this.plugin, loadBalancer)).execute()
				//(new HealthMonitorSync(this.plugin, loadBalancer)).execute()
				//(new PoolSync(this.plugin, loadBalancer)).execute()
				(new PolicySync(this.plugin, loadBalancer)).execute()
				//(new ProfileSync(this.plugin, loadBalancer)).execute()
				//(new CertificateSync(this.plugin, loadBalancer)).execute()
				//(new PersistenceSync(this.plugin, loadBalancer)).execute()
				//(new IRuleSync(this.plugin, loadBalancer)).execute()
				//(new InstanceSync(this.plugin, loadBalancer)).execute()

				// update status
				morpheusContext.loadBalancer.updateLoadBalancerStatus(loadBalancer, 'ok', null)
				morpheusContext.loadBalancer.clearLoadBalancerAlarm(loadBalancer)
				rtn.success = true
			}
			else {
				rtn.addError("F5 BigIP is unreachable")
				morpheusContext.loadBalancer.updateLoadBalancerStatus(loadBalancer, 'offline', 'Load Balancer is unreachable.')
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
			code:getLoadBalancerTypeCode(),
			name:'F5 BigIP',
			internal:false,
			enabled:true,
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
			monitorOptionTypes:getHealthMonitorOptionTypes(),
			instanceOptionTypes:getInstanceOptionTypes()
		)

		return [type]
	}

	String getLoadBalancerTypeCode() {
		return PROVIDER_CODE
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

		return rtn
	}

	// service methods for api interaction
	@Override
	ServiceResponse createLoadBalancerProfile(NetworkLoadBalancerProfile loadBalancerProfile) {
		def createRtn = ServiceResponse.prepare()
		try {
			// for SSL profiles determine if we have to install cert or just assign it
			def apiConfig = getConnectionBase(loadBalancerProfile.loadBalancer)
			def createParams = [profileData:[:]]
			createParams.profileData.name = loadBalancerProfile.name
			createParams.profileData.description = loadBalancerProfile.description
			createParams.profileData.partition = loadBalancerProfile.partition
			createParams.serviceType = loadBalancerProfile.serviceType
			if (loadBalancerProfile.serviceType == 'http') {
				createParams.profileData.proxyType = loadBalancerProfile.proxyType
				createRtn.success = true
			}
			if (loadBalancerProfile.serviceType == 'client-ssl') {
				def certSvc = morpheus.loadBalancer.certificate
				// if a morpheus or self signed cert is selected, it will need to be installed
				if (loadBalancerProfile.sslCert.isNumber()) {
					AccountCertificate cert
					// if the cert is self signed one, create the cert first
					if (loadBalancerProfile.sslCert.toLong() == -1) {
					}
					else {
						cert = certSvc.getAccountCertificateById(loadBalancerProfile.sslCert.toLong()).blockingGet().value
					}

					// install the cert onto bigip
					def certName = BigIpUtility.buildSafeCertName(cert.name)
					def token = certSvc.createCertInstallToken(cert, "${certSvc.sslInstallTokenName}.1", BigIpUtility.BIGIP_REFDATA_CATEGORY).blockingGet()
					def rtn = installCert(apiConfig + [certName:certName, token:token, nlbi:1])

					if (rtn.success) {
						rtn = installKey(apiConfig + [keyName:certName, token:token, nlbi:1, authToken:rtn.authToken])

						if (rtn.success) {
							// expire out cert token
							certSvc.expireCertInstallToken(token).blockingGet()
						} else {
							createRtn.success = false
							createRtn.msg = "Unable to install ssl cert: ${rtn.message}"
							return createRtn
						}
					} else {
						log.error("Error Installing Certificate on F5 Server. Perhaps it cannot reach the Morpheus Appliance URL For Cert transfer... ${rtn.msg ?: rtn.message}")
						createRtn.success = false
						createRtn.msg = "Unable to install rsa private key: ${rtn.message}"
						return rtn
					}

					// if the cert installs correctly, add it to profileData
					createParams.certName = certName
					createParams.hostName = cert.domainName
					createParams.authToken = rtn.authToken
				}
				else {
					createRtn.success = true
					createParams.certName = BigIpUtility.parsePathName(loadBalancerProfile.sslCert)
				}
			}
			else {
				createRtn.success = true
			}

			// create the profile
			if(createRtn.success) {
				def profileResponse = createProfile(apiConfig + createParams)
				createRtn.success = profileResponse.success
				createRtn.msg = profileResponse.content?.message
				createRtn.data = [category:"f5.profile.${loadBalancerProfile.loadBalancerId}"]
			}

			return createRtn
		}
		catch (Throwable t) {
			log.error("Unable to create profile: ${t.message}", t)
			rtn.msg = "Unable to create profile: ${t.message}"
		}
	}

	@Override
	ServiceResponse deleteLoadBalancerProfile(NetworkLoadBalancerProfile loadBalancerProfile) {
		ServiceResponse rtn = ServiceResponse.prepare()
		try {
			def loadBalancer = loadBalancerProfile.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def profileConfig = [:] + apiConfig
			profileConfig.externalId = loadBalancerProfile.externalId
			profileConfig.name = loadBalancerProfile.name
			profileConfig.serviceType = loadBalancerProfile.serviceType
			profileConfig.partition = loadBalancerProfile.partition
			def results = deleteProfile(profileConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.data = [found:results.found]
		}
		catch(Throwable t) {
			log.error("error removing profile: ${t.message}", t)
			rtn.msg = 'unknown error removing profile ' + e.message
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return null
	}

	@Override
	ServiceResponse createLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		def monitorSvc = morpheus.loadBalancer.monitor
		ServiceResponse rtn = ServiceResponse.prepare()
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
			monitorConfig.destination = monitor.monitorDestination
			monitorConfig.authToken = apiConfig.authToken
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
		return rtn
	}

	@Override
	ServiceResponse validateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		ServiceResponse rtn = ServiceResponse.prepare()
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
		def rtn = ServiceResponse.prepare()
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
			rtn.data = [found:results.found ?: false, authToken:results.authToken]
		}
		catch(Throwable t) {
			log.error("error removing node: ${t}", t)
			rtn.msg = 'unknown error removing node: ' + t.message
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerNode(NetworkLoadBalancerNode loadBalancerNode) {
		def rtn = ServiceResponse.prepare()
		try {
			//prep up the create call and pass it
			def loadBalancer = loadBalancerNode.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def nodeConfig = [:] + apiConfig

			//fill in config
			nodeConfig.name = loadBalancerNode.name
			nodeConfig.port = loadBalancerNode.port
			nodeConfig.description = loadBalancerNode.description
			nodeConfig.ipAddress = loadBalancerNode.ipAddress
			nodeConfig.healthMonitor = loadBalancerNode.monitor?.externalId
			nodeConfig.partition = loadBalancerNode.partition
			//create it
			nodeConfig.externalId = BigIpUtility.convertExternalId(loadBalancerNode.externalId)
			nodeConfig.authToken = apiConfig.authToken
			log.debug("node config: ${nodeConfig}")
			def results = updateServer(nodeConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.data = [authToken:results.authToken]
			if(rtn.success != true) {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results.content?.message ?: results.message
			}
		} catch(e) {
			log.error("error updating node: ${e}", e)
			rtn.msg = 'unknown error updating node ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse validateLoadBalancerNode(NetworkLoadBalancerNode node) {
		ServiceResponse rtn = ServiceResponse.prepare()
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
			rtn.data = [authToken:results.authToken, pool:pool]
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
			log.error("error removing pool: ${e.message}", e)
			rtn.msg = "unknown error removing pool ${e.message}"
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerPool(NetworkLoadBalancerPool loadBalancerPool) {
		def rtn = ServiceResponse.prepare()
		try {
			//prep up the create call and pass it
			def loadBalancer = loadBalancerPool.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def poolConfig = [:] + apiConfig
			def itemConfig = loadBalancerPool.getConfigMap()
			//fill in config
			poolConfig.name = loadBalancerPool.name
			poolConfig.port = loadBalancerPool.port
			poolConfig.description = loadBalancerPool.description
			poolConfig.loadBalancingMode = BigIpUtility.getLoadBalancingMode(loadBalancerPool.vipBalance)
			poolConfig.partition = loadBalancerPool.partition
			// poolConfig.loadBalancingMode = decodeLoadBalancingMode(loadBalancerPool.vipBalance)
			if(loadBalancerPool.monitors?.size() > 0)
				poolConfig.monitorName = loadBalancerPool.monitors.collect{ it.externalId }?.join(' and ')
			//create it
			poolConfig.externalId = BigIpUtility.convertExternalId(loadBalancerPool.externalId)
			log.debug("pool config: ${poolConfig}")
			def results = updatePool(poolConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			if(rtn.success == true) {
				//add pool memebers
				if(loadBalancerPool.members?.size() > 0) {
					def memberConfig = apiConfig + [name:loadBalancerPool.name, externalId:poolConfig.externalId, partition:loadBalancerPool.partition]
					memberConfig.members = loadBalancerPool.members.collect{ member -> return [name:member.node.name, port:(member.port ?: loadBalancerPool.port), partition:loadBalancerPool.partition] }
					def memberResults = updatePoolMembers(memberConfig)
					log.debug("memberResults: {}", memberResults)
					if(memberResults.success != true) {
						rtn.msg = "failed to assign pool members: ${memberResults.data?.message ?: memberResults.msg}"
					}
				}
			} else {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results?.data?.message ?: results.msg
			}
		} catch(e) {
			log.error("error updating pool: ${e}", e)
			rtn.msg = 'unknown error updating pool ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse validateLoadBalancerPool(NetworkLoadBalancerPool pool) {
		def rtn = ServiceResponse.prepare()
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

	// Crud for policies
	@Override
	ServiceResponse createLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy) {
		def rtn = ServiceResponse.prepare()
		try {
			//prep up the create call and pass it
			def loadBalancer = loadBalancerPolicy.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def policyConfig = [:] + apiConfig
			//fill in config
			policyConfig.policyName = loadBalancerPolicy.name
			policyConfig.description = loadBalancerPolicy.description
			policyConfig.strategy = loadBalancerPolicy.strategy
			policyConfig.requires = loadBalancerPolicy.requires
			policyConfig.controls = loadBalancerPolicy.controls
			policyConfig.partition = loadBalancerPolicy.partition

			//create it
			def results = createBigIpPolicy(policyConfig)

			if (results.success) {
				rtn.data = [authToken:results.authToken, policy:loadBalancerPolicy]
				loadBalancerPolicy.externalId = "/${policyConfig.partition}/${policyConfig.policyName}".toString()
				loadBalancerPolicy.configMap = results.policy
				loadBalancerPolicy.strategy = policyConfig.strategy.split('/').last()

				// publish the policy
				def publishResults = publishPolicy(policyConfig)
				rtn.data = [policy:loadBalancerPolicy]
				rtn.success = publishResults.success
			}
			else {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results.msg
			}
		} catch(e) {
			log.error("error creating policy: ${e}", e)
			rtn.msg = 'unknown error creating policy ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse deleteLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy) {
		def rtn = ServiceResponse.prepare()
		try {
			def loadBalancer = loadBalancerPolicy.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def policyConfig = [:] + apiConfig
			policyConfig.policyName = loadBalancerPolicy.name
			policyConfig.partition = loadBalancerPolicy.partition
			def results = deleteBigIpPolicy(policyConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.data = [authToken:apiConfig.authToken]
		} catch(e) {
			log.error("error removing policy: ${e}", e)
			rtn.msg = 'unknown error removing policy ' + e.message
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse validateLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy) {
		def rtn = ServiceResponse.prepare()
		try {
			if(!loadBalancerPolicy.name) {
				rtn.addError('name', 'Name is required')
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating policy: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse validateLoadBalancerRule(NetworkLoadBalancerRule rule) {
		ServiceResponse rtn = ServiceResponse.prepare()
		try {
			def config = rule.configMap
			if(!rule.name) {
				rtn.addError('name', 'Name is required')
			}
			if(!config.value) {
				rtn.addError('value', 'Value is required')
			}
			if(!config.pool) {
				rtn.addError('pool', 'Forward to pool is required')
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating node: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse createLoadBalancerRule(NetworkLoadBalancerRule rule) {
		def rtn = ServiceResponse.prepare()
		try {
			// get our policy
			//def policy = NetworkLoadBalancerPolicy.get(opts.policy.id.toLong())
			def policy = rule.policy

			// create the rule and add it to our policy
			def createRule = addPolicyRule(policy, rule)

			if (createRule.success) {
				// publish policy
				def publishPolicy = publishPolicy([policyName:policy.name, partition:policy.partition] + createRule.auth)
				if (publishPolicy.success) {
					// build out the domain shit
					def addRule = buildPolicyRuleFromMap(policy, createRule.data)
					rtn.success = true
					rtn.data = addRule
				}
				else {
					rtn.errors = createRule.errors
					rtn.msg = createRule.content?.message ?: results.message
				}
			}
			else {
				rtn.errors = createRule.errors
				rtn.msg = createRule.content?.message ?: results.message
			}
		}
		catch(e) {
			log.error("error creating policy rule: ${e}", e)
			rtn.msg = "unknown error creating policy rule  ${e.message}"
		}
		return rtn
	}

	@Override
	ServiceResponse deleteLoadBalancerRule(NetworkLoadBalancerRule rule) {
		ServiceResponse rtn = ServiceResponse.prepare()
		try {
			def policy = rule.policy
			def loadBalancer = policy.loadBalancer
			def auth = getConnectionBase(policy.loadBalancer)
			def policyNamingMap = [name:policy.name, partition:policy.partition, draft:true]
			def existingRules = getPolicyRules(auth + policyNamingMap)
			if (existingRules == null) {
				def out = createPolicyDraft(policy, [auth:auth])
				if (!out.success)
					return out
			}

			// after policy draft has been created, remove our rule
			def endpointPath = "${auth.path}/tm/ltm/policy/${BigIpUtility.buildPartitionedName(policyNamingMap)}/rules/${rule.name}".toString()
			def params = [
				uri:auth.url,
				path:endpointPath,
				username:auth.username,
				password:auth.password,
				authToken:auth.authToken
			]
			def out = callApi(params, 'DELETE')

			if (!out.success) {
				rtn.msg = out.data?.message ?: out.errors?.error ?: out.msg
			}
			else {
				// if rule deleted successfully, publish the policy draft again
				def publishPolicy = publishPolicy([policyName:policy.name, partition:policy.partition] + auth)
				if (!publishPolicy.success) {
					rtn.msg = publishPolicy.message ?: publishPolicy.msg
				}
				else {
					rtn.success = true
				}
			}
		}
		catch(e) {
			log.error("error removing policy rule: ${e}", e)
			rtn.msg = "unknown error removing policy rule: ${e.message}"
		}
		log.debug("rtn: ${rtn}")
		return rtn
	}

	// Crud for virtual servers/instance
	@Override
	ServiceResponse createLoadBalancerVirtualServer(NetworkLoadBalancerInstance loadBalancerInstance) {
		def rtn = ServiceResponse.prepare()
		try {
			//prep up the create call and pass it
			def loadBalancer = loadBalancerInstance.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def virtualServerConfig = [:] + apiConfig
			def instanceConfig = loadBalancerInstance.getConfigMap()
			//fill in config
			virtualServerConfig.name = loadBalancerInstance.vipName
			virtualServerConfig.vipProtocol = loadBalancerInstance.vipProtocol
			virtualServerConfig.description = loadBalancerInstance.description
			virtualServerConfig.partition = loadBalancerInstance.partition
			//virtualServerConfig.type ?
			virtualServerConfig.active = loadBalancerInstance.active
			//destination
			virtualServerConfig.vipAddress = loadBalancerInstance.vipAddress
			virtualServerConfig.vipPort = loadBalancerInstance.vipPort
			virtualServerConfig.destination = "/${virtualServerConfig.partition}/" + loadBalancerInstance.vipAddress + ':' + (loadBalancerInstance.vipPort ?: '80')
			//source
			virtualServerConfig.sourceAddress = loadBalancerInstance.sourceAddress
			//pool
			virtualServerConfig.defaultPool = instanceConfig.defaultPool
			if(instanceConfig.defaultPool) {
				virtualServerConfig.pool = morpheus.loadBalancer.pool.listById([instanceConfig.defaultPool.toLong()]).blockingFirst()
			}
			//get policies
			virtualServerConfig.defaultPolicy = instanceConfig.defaultPolicy
			virtualServerConfig.policies = []
			if(instanceConfig.defaultPolicy) {
				def policy = morpheus.loadBalancer.policy.listById([instanceConfig.defaultPolicy.toLong()]).blockingFirst()
				if(policy)
					virtualServerConfig.policies << [name:policy.name]
			}
			loadBalancerInstance.policies?.each { policy ->
				virtualServerConfig.policies << [name:policy.name]
			}
			//get profiles
			virtualServerConfig.clientProfile = instanceConfig.clientProfile
			virtualServerConfig.serverProfile = instanceConfig.serverProfile
			virtualServerConfig.httpProfile = instanceConfig.httpProfile
			virtualServerConfig.profiles = []
			if(instanceConfig.httpProfile) {
				def profile = morpheus.loadBalancer.profile.listById([instanceConfig.httpProfile.toLong()]).blockingFirst()
				if(profile)
					virtualServerConfig.profiles << [name:profile.name]
			}
			if(instanceConfig.clientProfile) {
				def profile = morpheus.loadBalancer.profile.listById([instanceConfig.clientProfile.toLong()]).blockingFirst()
				if(profile)
					virtualServerConfig.profiles << [name:profile.name]
			}
			if(instanceConfig.serverProfile) {
				def profile = morpheus.loadBalancer.profile.listById([instanceConfig.serverProfile.toLong()]).blockingFirst()
				if(profile)
					virtualServerConfig.profiles << [name:profile.name]
			}
			loadBalancerInstance.profiles?.each { profile ->
				virtualServerConfig.profiles << [name:profile.name]
			}
			//get scripts
			virtualServerConfig.scripts = []
			loadBalancerInstance.scripts?.each { script ->
				virtualServerConfig.scripts << script.externalId
			}
			//persistenace
			if(loadBalancerInstance.vipSticky) {
				virtualServerConfig.persist = []
				virtualServerConfig.persist << [name:loadBalancerInstance.vipSticky]
			}
			//create it
			log.debug("vsconfig: {}", virtualServerConfig)
			def results = createVirtualServer(virtualServerConfig)
			log.debug("api results: {}", results)
			rtn.success = results.success
			if(rtn.success == true) {
				loadBalancerInstance.externalId = results.virtualServer?.fullPath
				rtn.data = [instance:loadBalancerInstance]
			} else {
				//fill in errors
				rtn.errors = results.errors
				rtn.msg = results.virtualServer?.message ?: results.message
			}
		} catch(e) {
			log.error("error creating virtual server: ${e}", e)
			rtn.msg = 'unknown error creating virtual server ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse deleteLoadBalancerVirtualServer(NetworkLoadBalancerInstance loadBalancerInstance) {
		def rtn = ServiceResponse.prepare()
		try {
			def loadBalancer = loadBalancerInstance.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def virtualServerConfig = [:] + apiConfig
			virtualServerConfig.externalId = loadBalancerInstance.externalId
			def results = deleteVirtualServer(virtualServerConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			rtn.data = [found:results.found]
		} catch(e) {
			log.error("error removing virtual server: ${e}", e)
			rtn.msg = 'unknown error removing virtual server ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse updateLoadBalancerVirtualServer(NetworkLoadBalancerInstance loadBalancerInstance) {
		def rtn = ServiceResponse.prepare()
		try {
			//prep up the create call and pass it
			def loadBalancer = loadBalancerInstance.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def virtualServerConfig = [:] + apiConfig
			def instanceConfig = loadBalancerInstance.getConfigMap()
			//fill in config
			virtualServerConfig.name = loadBalancerInstance.vipName
			virtualServerConfig.vipProtocol = loadBalancerInstance.vipProtocol
			virtualServerConfig.description = loadBalancerInstance.description
			//virtualServerConfig.type ?
			virtualServerConfig.active = loadBalancerInstance.active
			virtualServerConfig.partition = loadBalancerInstance.partition
			//destination
			virtualServerConfig.vipAddress = loadBalancerInstance.vipAddress
			virtualServerConfig.vipPort = loadBalancerInstance.vipPort
			virtualServerConfig.destination = "/${loadBalancerInstance.partition}/" + loadBalancerInstance.vipAddress + ':' + (loadBalancerInstance.vipPort ?: '80')
			//source
			virtualServerConfig.sourceAddress = loadBalancerInstance.sourceAddress
			//pool
			virtualServerConfig.defaultPool = instanceConfig.defaultPool
			if(instanceConfig.defaultPool) {
				virtualServerConfig.pool = morpheus.loadBalancer.pool.listById([instanceConfig.defaultPool.toLong()]).blockingFirst()
			}
			//get policies
			virtualServerConfig.defaultPolicy = instanceConfig.defaultPolicy
			virtualServerConfig.policies = []
			if(instanceConfig.defaultPolicy) {
				def policy = morpheus.loadBalancer.policy.listById([instanceConfig.defaultPolicy.toLong()]).blockingFirst()
				if(policy)
					virtualServerConfig.policies << [name:policy.name, partition:policy.partition]
			}
			// add other policies
			for (policy in loadBalancerInstance.policies) {
				virtualServerConfig.policies << [name:policy.name, partition:policy.partition]
			}

			//get profiles
			virtualServerConfig.clientProfile = instanceConfig.clientProfile
			virtualServerConfig.serverProfile = instanceConfig.serverProfile
			virtualServerConfig.httpProfile = instanceConfig.httpProfile
			virtualServerConfig.profiles = []
			if(instanceConfig.httpProfile) {
				def profile = morpheus.loadBalancer.profile.listById([instanceConfig.httpProfile.toLong()]).blockingFirst()
				if(profile)
					virtualServerConfig.profiles << [name:profile.name]
			}
			if(instanceConfig.clientProfile) {
				def profile = morpheus.loadBalancer.profile.listById([instanceConfig.clientProfile.toLong()]).blockingFirst()
				if(profile)
					virtualServerConfig.profiles << [name:profile.name]
			}
			if(instanceConfig.serverProfile) {
				def profile = morpheus.loadBalancer.profile.listById([instanceConfig.serverProfile.toLong()]).blockingFirst()
				if(profile)
					virtualServerConfig.profiles << [name:profile.name]
			}

			// add profiles
			for (profile in loadBalancerInstance.profiles) {
				virtualServerConfig.profiles << [name:profile.name, partition:profile.partition]
			}

			//get scripts
			virtualServerConfig.scripts = []
			loadBalancerInstance.scripts?.each { script ->
				virtualServerConfig.scripts << script.externalId
			}
			//persistenace
			if(loadBalancerInstance.vipSticky) {
				virtualServerConfig.persist = []
				virtualServerConfig.persist << [name:loadBalancerInstance.vipSticky]
			}
			//update it
			virtualServerConfig.externalId = BigIpUtility.convertExternalId(loadBalancerInstance.externalId)
			log.debug("vsconfig: ${virtualServerConfig}")
			def results = updateVirtualServer(virtualServerConfig)
			log.debug("api results: ${results}")
			rtn.success = results.success
			if(rtn.success != true) {
				rtn.errors = results.errors
				rtn.msg = results?.virtualServer?.message ?: results.message
			}
		} catch(e) {
			log.error("error updating virtual server: ${e}", e)
			rtn.msg = 'unknown error updating virtual server ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse validateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		def policySvc = morpheus.loadBalancer.policy
		def rtn = ServiceResponse.prepare()
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

			//need either a policy or a pool
			def configMap = instance.configMap
			if(configMap?.mode != 'instance' && !configMap?.defaultPool && instance.policies?.size() == 0) {
				rtn.errors.defaultPool = 'Pool or a policy is required'
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error("error validating virtual server: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse addInstance(NetworkLoadBalancerInstance loadBalancerInstance) {
		def rtn = ServiceResponse.prepare()
		try {
			def opts = loadBalancerInstance.configMap.options
			def loadBalancer = loadBalancerInstance.loadBalancer
			def lbSvc = morpheus.loadBalancer
			def instance = loadBalancerInstance.instance
			//vip details
			def vipAddress = loadBalancerInstance.vipAddress
			def vipHostname = loadBalancerInstance.vipHostname
			def vipProtocol = loadBalancerInstance.vipProtocol
			def vipMode = loadBalancerInstance.vipMode
			def vipPort = loadBalancerInstance.vipPort
			def vipBalance = loadBalancerInstance.vipBalance
			def servicePort = loadBalancerInstance.servicePort
			def backendPort = loadBalancerInstance.backendPort
			def vipShared = loadBalancerInstance.vipShared
			def vipDirectAddress = loadBalancerInstance.vipDirectAddress
			def vipSticky = loadBalancerInstance.vipSticky
			//ssl config
			def sslCert = loadBalancerInstance.sslCert
			def sslRedirectMode = loadBalancerInstance.sslRedirectMode
			def sslMode = loadBalancerInstance.sslMode
			def sslEnabled = sslCert != null
			//service types
			def vipServiceMode = BigIpUtility.getVipServiceMode(loadBalancerInstance)
			def backendServiceMode = BigIpUtility.getBackendServiceMode(loadBalancerInstance)
			def contentSwitchResults
			def contentSwitchServerName
			def vsVipAddress = vipAddress
			def partition = loadBalancerInstance.partition
			//api config
			def apiConfig = getConnectionBase(loadBalancer)
			def serverNodes = []
			def healthMonitorName = BigIpUtility.buildHealthMonitorName(loadBalancerInstance.id, servicePort, sslEnabled)
			def keepGoing = true
			//naming
			def firstContainer = instance.containers?.size() > 0 ? instance.containers.first() : null
			def namingConfig = lbSvc.buildNamingConfig(firstContainer, opts, null)
			//results
			def createResults
			//health monitor
			if(loadBalancerInstance.monitor) {
				healthMonitorName = loadBalancerInstance.monitor.name
				createResults = [success:true]
			} else {
				//create healthmon
				def healthMonitorConfig = apiConfig +
					[name:healthMonitorName, port:servicePort, destination:"*:${servicePort}", partition:partition]
				createResults = createHealthMonitor(healthMonitorConfig)
				if(createResults.success != true) {
					rtn.success = false
					keepGoing = false
					rtn.msg = 'failed to create health monitor: ' + createResults.message
					rtn.data = createResults.healthMonitor
					rtn.results = createResults
				}
			}
			// create nodes
			if(keepGoing == true) {
				loadBalancerInstance.instance.containers.each { container ->
					namingConfig = lbSvc.buildNamingConfig(container, opts, null)
					def serverName = lbSvc.buildServerName(loadBalancer.serverName, container.server.id, namingConfig)
					def serverIp = lbSvc.getContainerIp(container, true) // prefer external address of container
					def serverMonitor = '/Common/icmp'
					def serverConfig = apiConfig +
						[name:serverName, ipAddress:serverIp, port:servicePort, healthMonitor:serverMonitor, authToken:createResults.authToken, partition:partition]
					createResults = createServer(serverConfig)
					if(createResults.success == true) {
						serverNodes << serverName
					} else {
						rtn.success = false
						keepGoing = false
						rtn.msg = 'failed to create server: ' + createResults.message
						rtn.data = createResults.node
						rtn.results = createResults
					}
				}
			}
			//create the pool
			def poolName = lbSvc.buildPoolName(loadBalancer.poolName, loadBalancerInstance.id, sslEnabled, namingConfig)
			if(keepGoing == true) {
				def poolConfig = apiConfig +
					[name:poolName, loadBalancingMode:BigIpUtility.getLoadBalancingMode(vipBalance), authToken:createResults.authToken, partition:partition]
				createResults = createPool(poolConfig)
				if(createResults.success != true) {
					rtn.success = false
					keepGoing = false
					rtn.msg = 'failed to create pool: ' + createResults.msg
					rtn.data = createResults.pool
					rtn.results = createResults
				}
			}
			//add pool members
			if(keepGoing == true) {
				def memberConfig = apiConfig +
					[name:poolName, monitorName:"/${partition}/${healthMonitorName}", port:servicePort,
					 members:serverNodes.collect{ node -> return [name:node, partition:partition] },
					 authToken:createResults.authToken, partition:partition]
				createResults = addPoolMembers(memberConfig)
				log.debug("add pool memeber results: {}", createResults)
				if(createResults.success != true) {
					rtn.success = false
					keepGoing = false
					rtn.msg = 'failed to assign pool members: ' + createResults.msg
					rtn.data = createResults.members
					rtn.results = createResults
				}
			}
			//policies
			//virtual server
			// TODO: continue with profiles
			def virtualServerName = lbSvc.buildVirtualServerName(loadBalancer.virtualServiceName, loadBalancerInstance.id, loadBalancerInstance.vipName, sslEnabled, namingConfig)
			if(keepGoing == true) {
				def virtualServerConfig = apiConfig + [name:virtualServerName, vipAddress:vipAddress,
													   vipPort:vipPort, persist:vipSticky, poolName:poolName, profiles:[], partition:partition]
				//ssl
				if(sslEnabled) {
					def res = installSslCert(loadBalancerInstance, loadBalancerInstance.sslCert)
					if(res.success) {
						res = createProfile(
							apiConfig + [
								certName:BigIpUtility.buildSslCertName(loadBalancerInstance.sslCert),
								profileName:BigIpUtility.generateSslProfileName(loadBalancerInstance),
								hostName:vipHostname,
								authToken:createResults.authToken,
								partition:partition
							]
						)
						if(res.success) {
							virtualServerConfig.profiles << [name:BigIpUtility.generateSslProfileName(loadBalancerInstance), partition:partition]
						}
						else {
							rtn.success = false
							rtn.msg = res.message
							rtn.error = rtn.message ?: rtn.msg
							keepGoing = false
						}
					}
				}
				// create policy
				if (keepGoing) {
					def res = createBigIpPolicy(apiConfig + [policyName:BigIpUtility.generatePolicyName(loadBalancerInstance), partition:partition])
					if(res.success) {
						// add policy rules
						res = addPolicyRules(apiConfig + [
							policyName:BigIpUtility.generatePolicyName(loadBalancerInstance),
							hostName:vipHostname,
							poolName:poolName,
							partition:partition
						])
						if(!res.success) {
							rtn.success = false
							keepGoing = false
							rtn.message = "Failed to add policy rules: ${res.message}"
							rtn.error = rtn.message
						}
						res = publishPolicy(apiConfig + [policyName:BigIpUtility.generatePolicyName(loadBalancerInstance), partition:partition])
						if(!res.success) {
							rtn.success = false
							keepGoing = false
							rtn.message = "Failed to publish policy: ${res.message}"
							rtn.error = rtn.message
						}
						// add the policy to virtual server config
						virtualServerConfig.policies = [[name:BigIpUtility.generatePolicyName(loadBalancerInstance), partition:partition]]
					} else {
						rtn.success = false
						rtn.message = "Unable to create vip policy: ${res.content?.message}"
						rtn.error = rtn.message
						keepGoing = false
						return rtn
					}
				}
				//create vip
				if (keepGoing) {
					createResults = createVirtualServer(virtualServerConfig)
					if (createResults.success != true) {
						rtn.success = false
						keepGoing = false
						rtn.msg = 'failed to create virtual server: ' + createResults.virtualServer?.message ?: createResults.errors?.error
						rtn.data = createResults.virtualServer
						rtn.results = createResults
					} else {
						rtn.success = true
					}
				}
			}
			//other stuff
			//handle removes
			//handle any removed containers
			if(keepGoing == true && opts.removeContainers?.size > 0) {
				def serverIdList = []
				lbSvc.getLoadBalancerServerIds(loadBalancer, loadBalancerInstance.id).blockingSubscribe { id ->
					serverIdList << id
				}
				opts.removeContainers?.each { container ->
					namingConfig = lbSvc.buildNamingConfig(container, opts, null)
					def serverMatch = serverIdList.find { it == container.server.id }
					if(!serverMatch) {
						def serverName = BigIpUtility.buildServerName(loadBalancer.serverName, container.server.id, namingConfig)
						def serverConfig = apiConfig + [name:serverName, authToken:createResults.authToken, partition:partition]
						def deleteResult = deleteServer(serverConfig)
						log.debug("delete server results: ${deleteResult}")
					}
				}
			}
			//create policies?
		} catch(e) {
			log.error("Unable to add add instance to load balancer: ${e.message}", e)
			rtn.error = 'failed to create load balancer instance ' + e.message
			rtn.success = false
		}
		log.debug("addInstance: ${rtn}")
		return rtn
	}

	@Override
	ServiceResponse updateInstance(NetworkLoadBalancerInstance instance) {
		def rtn = ServiceResponse.prepare()
		try {
			def lbSvc = morpheus.loadBalancer
			def changeResults = instance.holder.changeResults
			def opts = instance.holder.options
			if(changeResults.changed == true) {
				opts.activeConfig.loadBalancerInstance = instance
				def removeResults = removeInstance(opts.activeConfig, instance.instance)
				if(removeResults.success == true) {
					def addResults = addInstance(instance, opts)
					rtn.success = removeResults.success
				}
			} else if(changeResults.addNodes == true) {
				def updateResults = updateInstanceMembers(instance, opts)
				rtn.success = updateResults.success
			} else if(changeResults.removeNodes == true) {
				def updateResults = updateInstanceMembers(instance, opts)
				if (updateResults.success) {
					rtn.success = updateResults.success
					def loadBalancer = instance.loadBalancer
					def config = getConnectionBase(loadBalancer)
					// remove our nodes from the f5
					for (container in opts.removeContainers) {
						def namingConfig = lbSvc.buildNamingConfig(container, opts, null)
						def serverName = lbSvc.buildServerName(loadBalancer.serverName, container.server.id, namingConfig)
						def nodeConfig = [name:serverName, partition:instance.partition] + config
						def removeResult = deleteServer(nodeConfig)
						if (!removeResult.success) {
							log.warn("Unable to delete node from load balancer: ${removeResult.message}")
							rtn.success = false
							rtn.msg = "Unable to delete node from load balancer: ${removeResult.message}"
						}
					}
				}
			} else {
				rtn.success = true
				rtn.msg = 'no changes detected'
			}
		}
		catch (Throwable t) {
			log.error("Unable to updateInstance ${instance.vipName}: ${t.message}", t)
		}

		return rtn
	}

	def updateInstanceMembers(NetworkLoadBalancerInstance loadBalancerInstance, Map opts = [:]) {
		def rtn = [success:false]
		try {
			//prep up the create call and pass it
			def lbSvc = morpheus.loadBalancer
			def loadBalancer = loadBalancerInstance.loadBalancer
			def apiConfig = getConnectionBase(loadBalancer)
			def poolConfig = [:] + apiConfig
			//ssl
			def sslCert = loadBalancerInstance.sslCert
			def sslEnabled = sslCert != null
			//pool config
			def firstContainer = loadBalancerInstance.instance.containers?.size() > 0 ? loadBalancerInstance.instance.containers.first() : null
			def namingConfig = lbSvc.buildNamingConfig(firstContainer, opts, null)
			def poolName = lbSvc.buildPoolName(loadBalancer.poolName, loadBalancerInstance.id, sslEnabled, namingConfig)
			def servicePort = loadBalancerInstance.servicePort
			def partition = loadBalancerInstance.partition
			//fill in config
			poolConfig.name = poolName
			poolConfig.port = loadBalancerInstance.servicePort
			poolConfig.partition = partition
			//figure otu what containers should be in the pool
			def containerList = []
			loadBalancerInstance.instance?.containers?.each { container ->
				//check if its a removed contianer
				def removeMatch = opts.removeContainers?.find{ it.id == container.id }
				if(removeMatch == null) {
					containerList << container
				}
			}
			//add any add containers
			opts.addContainers?.each { container ->
				def existingMatch = loadBalancerInstance.instance?.containers?.find{ it.id == container.id }
				if(existingMatch == null)
					containerList << container
			}
			//build up the member list
			poolConfig.members = []
			if(containerList?.size() > 0) {
				containerList?.each { container ->
					namingConfig = lbSvc.buildNamingConfig(container, opts, null)
					def serverName = lbSvc.buildServerName(loadBalancer.serverName, container.server.id, namingConfig)
					def serverIp = lbSvc.getContainerIp(container, true)
					//if this is a new server create it
					def addMatch = opts.addContainers?.find{ it.id == container.id }
					if(addMatch != null) {
						//new - create it
						def serverMonitor = '/Common/icmp'
						def serverConfig = apiConfig + [name:serverName, ipAddress:serverIp, port:servicePort, healthMonitor:serverMonitor, partition:partition]
						def createResults = createServer(serverConfig)
					}
					//add to the member list
					poolConfig.members << [name:serverName, partition:partition]
				}
			}
			//update it
			def memberResults = updatePoolMembers(poolConfig)
			println("memberResults: ${memberResults}")
			rtn.success = memberResults.success
			if(rtn.success == false) {
				//fill in errors
				rtn.errors = memberResults.errors
				rtn.msg = memberResults?.content?.message ?: memberResults.message
			}
		} catch(e) {
			log.error("error updating pool: ${e}", e)
			rtn.msg = 'unknown error updating pool ' + e.message
		}
		return rtn
	}

	@Override
	ServiceResponse removeInstance(NetworkLoadBalancerInstance loadBalancerInstance) {
		def rtn = ServiceResponse.prepare() //deleted is if the gorm objects were removed - not the vip
		try {
			def activeConfig = [id:loadBalancerInstance.id, loadBalancer:loadBalancerInstance.loadBalancer,
								vipAddress:loadBalancerInstance.vipAddress, vipPort:loadBalancerInstance.vipPort, vipBalance:loadBalancerInstance.vipBalance,
								vipHostname:loadBalancerInstance.vipHostname, vipName:loadBalancerInstance.vipName,
								vipProtocol:loadBalancerInstance.vipProtocol, vipSticky:loadBalancerInstance.vipSticky,
								vipShared:loadBalancerInstance.vipShared, sslCert:loadBalancerInstance.sslCert,
								vipMode:loadBalancerInstance.vipMode, sslRedirectMode:loadBalancerInstance.sslRedirectMode,
								vipDirectAddress:loadBalancerInstance.vipDirectAddress, externalAddress:loadBalancerInstance.externalAddress,
								servicePort:loadBalancerInstance.servicePort, backendPort:loadBalancerInstance.backendPort,
								serverName:loadBalancerInstance.serverName, poolName:loadBalancerInstance.poolName,
								serviceName:loadBalancerInstance.serviceName, virtualServiceName:loadBalancerInstance.virtualServiceName,
								loadBalancerInstance:loadBalancerInstance, monitor:loadBalancerInstance.monitor,
								partition:loadBalancerInstance.partition
			]
			def removeResults = removeInstance(activeConfig, loadBalancerInstance.instance)
			rtn.success = removeResults.success

		} catch(e) {
			log.error("removeInstance error: ${e}", e)
		}
		return rtn
	}

    // The operations that deal with the bigip api
	def removeInstance(Map instanceConfig, Instance instance) {
		def rtn = [success:false, deleted:false]
		try {
			def loadBalancer = instanceConfig.loadBalancer
			def lbSvc = morpheus.loadBalancer
			log.info("Removing VIP from LoadBalancer: {}", instanceConfig.loadBalancer?.name)
			//vip details
			def vipAddress = instanceConfig.vipAddress
			def vipHostname = instanceConfig.vipHostname
			def vipProtocol = instanceConfig.vipProtocol
			def vipMode = instanceConfig.vipMode
			def vipPort = instanceConfig.vipPort
			def servicePort = instanceConfig.servicePort
			def backendPort = instanceConfig.backendPort
			def vipShared = instanceConfig.vipShared
			def vipDirectAddress = instanceConfig.vipDirectAddress
			def loadBalancerInstance = instanceConfig.loadBalancerInstance
			//ssl config
			def sslCert = instanceConfig.sslCert
			def sslEnabled = sslCert != null
			def partition = instanceConfig.partition
			//base api config
			def apiConfig = getConnectionBase(loadBalancer)
			//do the deletes
			def keepGoing = true
			//naming
			def removeOpts = [:]
			def firstContainer = instance.containers?.size() > 0 ? instance.containers.first() : null
			def namingConfig = lbSvc.buildNamingConfig(firstContainer, removeOpts, null)
			//results
			def deleteResults
			//remove the virtual server - todo handle shared vips
			def virtualServerName = lbSvc.buildVirtualServerName(instanceConfig.virtualServiceName, instanceConfig.id, instanceConfig.vipName, sslEnabled, namingConfig)
			def virtualServerConfig = apiConfig + [name:virtualServerName, vipAddress:vipAddress, vipPort:vipPort, partition:partition]
			//load the virtual server
			def policyName = BigIpUtility.generatePolicyName(loadBalancerInstance)
			def virtualServerResults = vipExists(virtualServerConfig)
			virtualServerConfig.authToken = virtualServerResults.authToken
			def virtualServer = virtualServerResults.vip
			//remove the vip if it only has one policy
			log.debug("virtualServerResults: {}", virtualServerResults)
			if(virtualServer) {
				def policyMatch = virtualServer.policiesReference?.items?.find{ it.name == policyName }
				def policyCount = virtualServer.policiesReference?.items?.size() ?: 0
				log.debug("policyMatch: {} count: {}", policyMatch, policyCount)
				if(policyCount == 0 || (policyMatch && policyCount == 1)) {
					//no more policies - can remove the vip - have to look at other types of policies
					deleteResults = deleteVirtualServer(virtualServerConfig)
					if(deleteResults.success != true) {
						rtn.success = false
						keepGoing = false
						rtn.status = deleteResults.status
						rtn.msg = 'failed to delete virtual server: ' + deleteResults.message
						rtn.message = deleteResults.message
						rtn.content = deleteResults.content
						rtn.results = deleteResults.results
					}
				}
			}
			//remove policy from virtual server
			def out = removeVipPolicy(virtualServerConfig + [policyName:BigIpUtility.generatePolicyName(loadBalancerInstance)])
			if(!out.success) {
				log.warn("Error Removing Policy from Load Balancer Instance")
				rtn.success = false
				keepGoing = false
				rtn.message = out.data.message ?: out.errors?.error ?: out.message
			}
			//delete policy
			out = deleteBigIpPolicy(virtualServerConfig + [policyName:policyName])
			if(!out.success) {
				log.warn("Error Removing BigIP Policy from Load Balancer Instance")
				rtn.success = false
				keepGoing = false
				rtn.message = out.content
			}
			if(sslEnabled) {
				out = removeVipProfile(virtualServerConfig + [profileName:BigIpUtility.generateSslProfileName(loadBalancerInstance)])
				if(!out.success) {
					log.warn("Error Removing Profiles from Load Balancer Instance")
					rtn.success = false
					keepGoing = false
					rtn.message = out.msg
				}
				out = deleteSslProfile(virtualServerConfig + [profileName:BigIpUtility.generateSslProfileName(loadBalancerInstance)])
				if(!out.success) {
					log.warn("Error Removing SSL Profiles from Load Balancer Instance")
					rtn.success = false
					keepGoing = false
					rtn.message = out.msg
				}
			}
			//remove the pool
			def poolName = lbSvc.buildPoolName(loadBalancer.poolName, loadBalancerInstance.id, sslEnabled, namingConfig)
			if(keepGoing == true) {
				def poolConfig = apiConfig + [name:poolName, authToken:virtualServerConfig.authToken, partition:partition]
				// remove pool from virtual server
				removeVipPool(poolConfig + [vipAddress:vipAddress, vipPort:vipPort])
				deleteResults = deletePool(poolConfig)
				if(deleteResults.success != true) {
					rtn.success = false
					keepGoing = false
					rtn.msg = 'failed to delete server pool: ' + deleteResults.msg
					rtn.message = deleteResults.msg
					rtn.data = deleteResults.data
					rtn.results = deleteResults
				}
			}
			//remove servers if nothing aimed at them
			if(keepGoing == true) {
				def serverIdList = lbSvc.getLoadBalancerServerIds(loadBalancer, instanceConfig.id).blockingSubscribe()
				instance.containers?.findAll{ctr -> ctr.inService}?.each { container ->
					namingConfig = lbSvc.buildNamingConfig(container, removeOpts, null)
					def serverMatch = serverIdList.find { it == container.server.id }
					if(!serverMatch) {
						def serverName = lbSvc.buildServerName(instanceConfig.serverName, container.server.id, namingConfig)
						def serverConfig = apiConfig + [name:serverName, authToken:virtualServerConfig.authToken, partition:partition]
						def deleteResult = deleteServer(serverConfig)
						log.debug("delete server results: {}", deleteResult)
					} else {
						//remove monitor?
					}
				}
			}
			//remove the health monitor
			if(instanceConfig.monitor == null) {
				def healthMonitorName = BigIpUtility.buildHealthMonitorName(instanceConfig.id, servicePort, sslEnabled)
				if(keepGoing == true) {
					def healthMonitorConfig = apiConfig + [name:healthMonitorName, authToken:virtualServerConfig.authToken, partition:partition]
					deleteResults = deleteHealthMonitor(healthMonitorConfig)
					if(deleteResults.success != true) {
						rtn.success = false
						keepGoing = false
						rtn.status = deleteResults.status
						rtn.msg = 'failed to delete health monitor: ' + deleteResults.message
						rtn.message = deleteResults.message
						rtn.content = deleteResults.data
						rtn.results = deleteResults
					}
				}
			}
			//all done
			if(keepGoing == true) {
				rtn.success = true
			}
		} catch(e) {
			log.error("removeInstance error: ${e}", e)
		}
		log.debug("returning: {}", rtn)
		return rtn
	}

	def deleteVirtualServer(Map opts) {
		def rtn = [success:false, found:false]
		def virtualServer
		if(opts.externalId)
			virtualServer = loadVirtualServer(opts)
		else
			virtualServer = findVirtualServer(opts)
		if(virtualServer.found == true) {
			rtn.found = true
			def endpointPath
			if(opts.externalId)
				endpointPath = "${opts.path}/tm/ltm/virtual/${BigIpUtility.convertExternalId(opts.externalId)}"
			else if(opts.name)
				endpointPath = "${opts.path}/tm/ltm/virtual/${BigIpUtility.buildPartitionedName(opts)}"
			def params = [
				uri:opts.url,
				path:endpointPath,
				username:opts.username,
				password:opts.password,
				authToken:opts.authToken
			]
			def results = callApi(params, 'DELETE')
			rtn.success = results.success
			log.debug("deleteResults: {}", rtn)
		} else {
			rtn.success = true
		}
		return rtn
	}

	def loadVirtualServer(Map opts) {
		def rtn = [success:false, found:false]
		def itemId = BigIpUtility.convertExternalId(opts.externalId)
		def endpointPath = "${opts.path}/tm/ltm/virtual/${itemId}"
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
			rtn.externalId = opts.externalId
			rtn.virtualServer = results.data
			rtn.authToken = opts.authToken
		}
		return rtn
	}

	def updateVirtualServer(Map opts) {
		def rtn = [success:false]
		def virtualServer = loadVirtualServer(opts)
		if(virtualServer.found != true) {
			rtn.success = false
			rtn.found = false
		} else {
			def vipName = BigIpUtility.buildPartitionedName(opts)
			def endpointPath = "${opts.path}/tm/ltm/virtual/${vipName}"
			def profiles = []
			if(opts.profiles)
				opts.profiles.each { profile -> profiles << profile }
			else
				profiles << [name:'http']
			//json data
			def data = [
				name:opts.name,
				description:opts.description ?: '',
				partition:opts.partition,
				enabled:(opts.active == false ? false : true),
				profiles:profiles ?: [],
				policies:opts.policies ?: [],
				persist:opts.persist ?: [],
				rules:opts.scripts ?: []
			]
			//add config
			if(opts.pool)
				data.pool = opts.pool.externalId
			else if(opts.poolName)
				data.pool = "/${opts.poolPartition ?: 'Common'}/${opts.poolName}"
			else
				data.pool = ''
			//protocol
			data.ipProtocol = opts.vipProtocol ?: 'tcp'
			//destination
			data.destination = opts.destination ?: "/${opts.partition ?: 'Common'}/${opts.vipAddress}:${opts.vipPort ?: 80}"
			//source
			if(opts.sourceAddress)
				data.source = opts.sourceAddress
			//source translation
			data.sourceAddressTranslation = [type:(opts.sourceAddressTranslation ?: 'automap')]
			log.debug("virtual server body: {}", data)
			//make the api call
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password,
				authToken:opts.authToken
			]

			def results = callApi(params, 'PATCH')
			log.debug("results: {}", results)
			rtn = [success:results.success, virtualServer:results.data, errors:results.errors, message:results.msg]
		}
		return rtn
	}

	Map vipExists(Map opts) {
		def rtn = [exists:false]
		def endpointPath = "${opts.path}/tm/ltm/virtual"
		def params = [uri:opts.url,
					  path:endpointPath,
					  username:opts.username,
					  password:opts.password,
					  urlParams:['expandSubcollections':'true'],
					  authToken:opts.authToken
		]
		def out = callApi(params, 'GET')
		def vip = out?.data?.items?.find { item -> item.destination.contains("${opts.vipAddress}:${opts.vipPort ?: 80}") }
		rtn.exists = vip ? true : false
		rtn.name = vip?.name
		rtn.details = vip
		rtn.vip = vip
		rtn.authToken = opts.authToken
		return rtn
	}

	def deleteSslProfile(Map params) {
		if (!params.authToken) {
			params.authToken = getAuthToken(params)
		}
		if(sslProfileExists(params)) {
			def profileName = BigIpUtility.buildPartitionedName([name:params.profileName, partition:params.partition])
			def endpointPath = "${params.path}/tm/ltm/profile/client-ssl/${profileName}"
			// delete ssl profile
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]
			def out = callApi(reqParams, 'DELETE')
			return [success:out.success, data:out.data, errors:out.errors, msg:out.data?.message ?: out.errors?.error ?: out.msg]
		} else {
			return [success:true, authToken:params.authToken]
		}
	}

	def removeVipPool(Map opts) {
		def rtn = [success:true]
		def vipResults
		if(opts.virtualServerId)
			vipResults = loadVirtualServer(opts + [externalId:opts.virtualServerId])
		else
			vipResults = findVirtualServer(opts)
		if(vipResults.found == true) {
			def virtualServer = vipResults.virtualServer
			//if we found it - remove pool
			if(virtualServer && virtualServer.pool == BigIpUtility.buildPartitionedName(opts, '/')) {
				def endpointPath = "${opts.path}/tm/ltm/virtual/${BigIpUtility.buildPartitionedName(virtualServer)}"
				def data = [pool:'']
				def params = [
					uri:opts.url,
					path:endpointPath,
					body:data,
					username:opts.username,
					password:opts.password,
					authToken:opts.authToken
				]
				def out = callApi(params, 'PATCH')
				rtn.success = out.success
				rtn.data = out.data
				rtn.authToken = opts.authToken
				rtn.message = out.data?.message ?: out.errors?.error ?: out.msg
			}
		}
		return rtn
	}

	def removeVipProfile(Map params) {
		def vip = vipExists(params)
		if(vip.exists) {
			// get vip profiles
			def vipName = BigIpUtility.buildPartitionedName(params)
			def endpointPath = "${params.path}/tm/ltm/virtual/${vipName}/profiles"
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]
			def out = callApi(reqParams, 'GET')

			// set vip profiles minus this profile
			def profiles = []
			for (item in out.data.items) {
				if(item.name != params.profileName)
					profiles << [name:item.name, partition:item.partition]
			}

			endpointPath = "${params.path}/tm/ltm/virtual/${vipName}"
			def data = [profiles:profiles]
			reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken,
			]
			out = callApi(reqParams, 'PATCH')
			return [success:out.success, data:out.data, errors:out.errors, msg:out.data?.message ?: out.errors?.error ?: out.msg]
		}
		else {
			return [success:true, msg:'No operation', authToken:params.authToken]
		}
	}

	def removeVipPolicy(Map params) {
		def vip = vipExists(params)
		if(vip.exists) {
			// get vip policies
			def vipName = BigIpUtility.buildPartitionedName(params)
			def endpointPath = "${params.path}/tm/ltm/virtual/${vipName}/policies"
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				username:params.username,
				password:params.password,
				authToken:params.authToken,
			]
			def out = callApi(reqParams, 'GET')

			// set vip policies minus this policy
			def policies = []
			for (item in out.data.items) {
				if(item.name != params.policyName)
					policies << [name:item.name, partition:item.partition]
			}
			endpointPath = "${params.path}/tm/ltm/virtual/${vipName}"
			def data = [policies:policies]
			reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]
			out = callApi(reqParams, 'PATCH')
			return [success:out.success, data:out.data, authToken:params.authToken]
		} else {
			return [success:true, msg:'No operation', authToken:params.authToken]
		}
	}

	def createVirtualServer(Map opts) {
		def rtn = [success:false]
		def virtualServer = findVirtualServer(opts)
		if(virtualServer.found == true) {
			rtn.success = true
			rtn.virtualServer = virtualServer
			// update virtual server with new profiles and policies
			if(opts.profiles || opts.policies) {
				def out = updateVirtualServerProfiles(opts)
			}
		} else {
			def endpointPath = "${opts.path}/tm/ltm/virtual"
			def profiles = []
			if(opts.profiles) {
				def needsHttpProfile = false
				opts.profiles.each { profile ->
					profiles << profile
					if (profile.name?.contains('ssl')) {
						needsHttpProfile = true
					}
				}
				if (needsHttpProfile)
					profiles << [name:'http']
			}
			else {
				profiles << [name: 'http']
			}
			//json data
			def data = [
				name:opts.name,
				enabled:(opts.active == false ? false : true),
				profiles:profiles,
				policies:opts.policies ?: [],
				partition:opts.partition
			]
			//add config
			if(opts.description)
				data.description = opts.description
			if(opts.pool)
				data.pool = opts.pool.externalId
			else if(opts.poolName)
				data.pool = "/${opts.poolPartition ?: opts.partition ?: BigIpUtility.BIGIP_PARTITION}/${opts.poolName}"
			//scripts
			if(opts.scripts)
				data.rules = opts.scripts
			//persistence
			if(opts.persist)
				data.persist = opts.persist
			//protocol
			data.ipProtocol = opts.vipProtocol ?: 'tcp'
			//destination
			data.destination = opts.destination ?: "/${opts.partition ?: BigIpUtility.BIGIP_PARTITION}/${opts.vipAddress}:${opts.vipPort ?: 80}"
			//source
			if(opts.sourceAddress)
				data.source = opts.sourceAddress
			//source translation
			data.sourceAddressTranslation = [type:(opts.sourceAddressTranslation ?: 'automap')]
			log.debug("virtual server body: {}", data)
			//make the api call
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password
			]

			def out = callApi(params, 'POST')
			log.debug("${endpointPath} results: ${out.data}")
			rtn = [success:out.success, virtualServer:out.data, errors:out.errors]
		}
		return rtn
	}

	Map findVirtualServer(Map opts) {
		def rtn = [success:false, found:false]
		def endpointPath = "${opts.path}/tm/ltm/virtual"
		def params = [uri:opts.url,
					  path:endpointPath,
					  username:opts.username,
					  password:opts.password,
					  urlParams:['expandSubcollections':'true'],
					  authToken:opts.authToken
		]
		def results = callApi(params, 'GET')
		def virtualServer = results.data.items?.find { item -> item.destination.contains("${opts.vipAddress}:${opts.vipPort ?: 80}") }
		if(virtualServer) {
			rtn.success = true
			rtn.found = true
			rtn.virtualServer = virtualServer
			rtn.authToken = opts.authToken
		}
		return rtn
	}

	def getVipProfiles(Map params) {
		def virtualName = BigIpUtility.buildPartitionedName(params)
		def endpointPath = "${params.path}/tm/ltm/virtual/${virtualName}/profiles"
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		def out = callApi(reqParams, 'GET')

		return out.data.items
	}

	def updateVirtualServerProfiles(Map params) {
		def virtualServer = findVirtualServer(params).virtualServer
		def vipProfiles = getVipProfiles(
			[path:params.path, name:virtualServer.name, url:params.url, username:params.username, password:params.password, authToken:params.authToken, partition:virtualServer.partition]
		)
		def sslProfile = vipProfiles.find {
			if(it.name.size() >= 4)
				return it.name[-4..-1] == '-ssl'
			else
				return false
		}

		def profiles = vipProfiles.collect { return [name:it.name, fullPath:it.fullPath, partition:it.partition] }
		if(!sslProfile && params.profiles) {
			setSslProfileSni(params)
			params.profiles.each { profiles << it }
		}

		def vipPolicies = getVipPolicies(
			[path:params.path, name:virtualServer.name, url:params.url, username:params.username, password:params.password, partition:virtualServer.partition]
		)
		def policies = vipPolicies.collect { return [name:it.name, fullPath:it.fullPath, partition:it.partition] }
		params.policies.each { policies << it }

		def serverName = BigIpUtility.buildPartitionedName(virtualServer)
		def endpointPath = "${params.path}/tm/ltm/virtual/${serverName}"
		def data = [
			profiles:profiles,
			policies:policies
		]

		if (!virtualServer.pool) {
			data.pool = "/${params.partition ?: BigIpUtility.BIGIP_PARTITION}/${params.poolName}"
		}

		def reqParams = [
			uri:params.url,
			path:endpointPath,
			body:data,
			username:params.username,
			password:params.password
		]

		def out = callApi(reqParams, 'PATCH')

		return out
	}

	def getVipPolicies(Map params) {
		def virtualName = BigIpUtility.buildPartitionedName(params)
		def endpointPath = "${params.path}/tm/ltm/virtual/${virtualName}/policies"
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		def out = callApi(reqParams, 'GET')

		return out.data.items
	}

	def setSslProfileSni(Map params) {
		if(sslProfileExists(params)) {
			def profileName = BigIpUtility.buildPartitionedName([name:params.profileName, partition:params.partition])
			def endpointPath = "${params.path}/tm/ltm/profile/client-ssl/${profileName}"
			def data = [
				sniDefault:true
			]
			// create the profile
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]

			def out = callApi(reqParams, 'PUT')
			return out
		} else {
			return [success:true, authToken:params.authToken]
		}
	}

	def deleteProfile(Map opts) {
		def rtn = [success:false]
		def profileId = opts.externalId ?: opts.name
		def profile = profileId ? loadProfile(opts) : [found:false]
		if(profile.found == true) {
			def endpointPath = "${opts.path}/tm/ltm/profile/${opts.serviceType}/${BigIpUtility.buildPartitionedName(opts)}"
			def params = [
				uri:opts.url,
				path:endpointPath,
				username:opts.username,
				password:opts.password,
				authToken:profile.authToken
			]
			def results = callApi(params, 'DELETE')

			if (results.success)
				rtn.success = true
			else
				rtn.msg = results.data?.message ?: results.errors?.error ?: results.msg
		}
		else {
			rtn.found = false
			rtn.success = true
		}
		return rtn
	}

	def loadProfile(Map opts) {
		def rtn = [success:false, found:false]

		if (!opts.externalId)
			return rtn

		def endpointPath = "${opts.path}/tm/ltm/profile/${opts.serviceType}/${BigIpUtility.buildPartitionedName(opts)}"
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
			rtn.profile = results.data ?: [:]
			rtn.authToken = opts.authToken
		}
		return rtn
	}

	def createProfile(Map params) {
		def serviceType = params.serviceType ?: 'client-ssl'
		if(!sslProfileExists(params)) {
			def endpointPath = "${params.path}/tm/ltm/profile/${serviceType}"
			def data = params.profileData ?: [name:params.profileName, partition:params.partition ?: BigIpUtility.BIGIP_PARTITION]

			if (serviceType == 'client-ssl') {
				log.debug("profile params: ${params}")
				def certInfo = getCertInfo(params)
				def certRootName = params.certName
				def certKeyName = params.certName
				if (certKeyName.endsWith('.crt')) {
					certKeyName = certKeyName.replaceAll('.crt', '.key')
				}
				data.certKeyChain = [[name:certRootName, cert:"/${certInfo.partition}/${certRootName}".toString(), key:"/${certInfo.partition}/${certKeyName}".toString()]]
				if (params.hostName) {
					data.serverName = params.hostName ?: certInfo.subject.substring(3)
				}
				else {
					data.serverName = (certInfo.subject.split(',').find { it.startsWith('CN=') }).substring(3)
					if (data.serverName.startsWith('*')) {
						data.serverName = data.serverName.substring(2)
					}
				}
			}

			// create the profile
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]
			def out = callApi(reqParams, 'POST')

			if (out.success)
				return [success:true, profile:out.data, authToken:params.authToken]
			else
				return [success:false, msg:out.data?.message ?: out.errors?.error ?: out.msg, authToken:params.authToken]
		}
		else {
			return [success:true, authToken:params.authToken]
		}
	}

	Boolean sslProfileExists(Map params) {
		def profileName = BigIpUtility.buildPartitionedName([name:params.profileName, partition:params.partition])
		def endpointPath = "${params.path}/tm/ltm/profile/client-ssl/${profileName}"
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]

		def out = callApi(reqParams, 'GET')

		return out.success
	}

	def installKey(Map params) {
		if (!keyExists(params)) {
			def endpointPath = "${params.path}/tm/sys/crypto/key"
			def data = [
				fromUrl:"${morpheus.loadBalancer.applianceUrl}/certificate/info/key/${params.token.value}/${params.nlbi}",
				command:'install',
				name:params.keyName
			]

			// install the rsa private key
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]
			def out = callApi(reqParams, 'POST')

			if (out.success)
				return [success:true, authToken:params.authToken] + out.data
			else
				return [success:false, msg:out.data?.message ?: out.errors?.error ?: out.msg]
		}
		return [success:true, message:'key already exists']
	}

	def installSslCert(NetworkLoadBalancerInstance nlbi, AccountCertificate cert) {
		def certSvc = morpheus.loadBalancer.certificate
		def output = [success:true]
		def token = certSvc.createCertInstallToken(cert, "${certSvc.sslInstallTokenName}.${nlbi.id}").blockingGet()
		def apiConfig = getConnectionBase(nlbi.loadBalancer)
		def rtn = installCert(apiConfig + [certName:BigIpUtility.buildSslCertName(cert), token:token, nlbi:nlbi.id])

		if (rtn.success) {
			rtn = installKey(apiConfig + [keyName:BigIpUtility.buildSslCertName(cert), token:token, nlbi:nlbi.id])

			if (rtn.success) {
				// expire out cert token
				certSvc.expireCertInstallToken(token).blockingGet()
			}
			else {
				output.success = false
				output.message = "Unable to install ssl cert: ${rtn.message}"
			}
		}
		else {
			output.success = false
			output.message = "Unable to install rsa private key: ${rtn.message}"
		}
		return rtn
	}

	def installCert(Map params) {
		if (!certExists(params)) {
			def endpointPath = "${params.path}/tm/sys/crypto/cert".toString()
			def url = morpheus.loadBalancer.applianceUrl
			def data = [
				fromUrl:"${url}${url.endsWith('/') ? '' : '/'}certificate/info/cert/${params.token.value}/${params.nlbi}".toString(),
				command:'install',
				name:params.certName
			]

			// install the cert
			def reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]
			def out = callApi(reqParams, 'POST')

			if (out.success)
				return [success:true, authToken:params.authToken] + out.data
			else
				return [success:false, msg:out.data?.message ?: out.errors?.error ?: out.msg]
		}
		return [success:true, message:'certificate already exists', authToken:params.authToken]
	}

	def getCertInfo(Map params) {
		def endpointPath = "${params.path}/tm/sys/file/ssl-cert"

		// create the health monitor
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		def out = callApi(reqParams, 'GET')

		def name = params.certName
		def certInfo = out.data.items.find { item -> item.name == name }
		if (!certInfo) {
			name = "${params.certName}${params.certName[-4..-1] == '.crt' ? '' : '.crt'}".toString()
			certInfo = out.data.items.find { item -> item.name == name }
		}
		return certInfo
	}

	def getKeyInfo(Map params) {
		def endpointPath = "${params.path}/tm/sys/file/ssl-key"

		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]

		def out = callApi(reqParams, 'GET')

		def name = "${params.keyName}${params.keyName[-4..-1] == '.key' ? '' : '.key'}".toString()
		def certInfo = out.data.items.find { item -> item.name == name }
		return certInfo
	}

	Boolean certExists(Map params) {
		def cert = getCertInfo(params)
		if (cert)
			return true
		else
			return false
	}

	Boolean keyExists(Map params) {
		def key = getKeyInfo(params)
		if (key)
			return true
		else
			return false

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

	def updateServer(Map opts) {
		def rtn = [success:false]
		def server = loadServer(opts)
		if(server.found != true) {
			rtn.success = false
			rtn.found = false
		}
		else {
			def endpointPath = "${opts.path}/tm/ltm/node/${opts.externalId}"
			def data = [
				name:opts.name,
				address:opts.ipAddress,
				description:opts.description,
				monitor:opts.healthMonitor
			]
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
				rtn.node = results.data
			}
			else {
				rtn.msg = results.data?.message ?: results.errors?.error ?: results.msg
			}
		}
		return rtn
	}

	Boolean policyExists(Map params) {
		def endpointPath = "${params.path}/tm/ltm/policy/~${params.partition ?: BigIpUtility.BIGIP_PARTITION}~${params.policyName}"
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		def out = callApi(reqParams, 'GET')

		return out.success
	}

	def createBigIpPolicy(Map params) {
		def rtn = [success:false, authToken:params.authToken]
		if(!policyExists(params)) {
			def endpointPath = "${params.path}/tm/ltm/policy"
			def data = [
				name:params.policyName,
				controls:[params.controls ?: 'forwarding'],
				requires:[params.requires ?: 'http'],
				strategy:params.strategy ?: '/Common/first-match',
				subPath:'Drafts',
				partition:params.partition
			]

			def reqParams = [
				uri:params.url,
				path:endpointPath,
				body:data,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]

			def out = callApi(reqParams, 'POST')

			if (out.success) {
				rtn.success = true
				rtn.policy = out.data
			}
			else {
				rtn.msg = out.data?.message ?: out.errors?.error ?: out.msg
			}
		}
		else {
			rtn.success = true
		}
		return rtn
	}

	def deleteBigIpPolicy(Map params) {
		def rtn = [success:false, authToken:params.authToken]
		if(policyExists(params)) {
			def endpointPath = "${params.path}/tm/ltm/policy/~${params.partition ?: BigIpUtility.BIGIP_PARTITION}~${params.policyName}"

			def reqParams = [
				uri:params.url,
				path:endpointPath,
				username:params.username,
				password:params.password,
				authToken:params.authToken
			]

			def out = callApi(reqParams, 'DELETE')

			if (out.success) {
				rtn.success = true
			}
			else {
				rtn.msg = out.data?.message ?: out.errors?.error ?: out.msg
			}
			return rtn
		}
		else {
			return [success:true]
		}
	}

	def publishPolicy(Map params) {
		def rtn = [success:false, authToken:params.authToken]
		def endpointPath = "${params.path}/tm/ltm/policy"
		def data = [
			name:"/${params.partition ?: BigIpUtility.BIGIP_PARTITION}/Drafts/${params.policyName}",
			command:'publish'
		]
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			body:data,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		def out = callApi(reqParams, 'POST')
		if (out.success) {
			rtn.success = true
			rtn.data = out.data
		}
		else {
			rtn.msg = out.data?.message ?: out.errors?.error ?: out.msg
		}

		return rtn
	}

	def addPolicyRules(Map params) {
		// add the rules
		def rtn = [success:true]
		def endpointPath = "${params.path}/tm/ltm/policy/~${params.partition ?: BigIpUtility.BIGIP_PARTITION}~Drafts~${params.policyName}/rules"
		def data = [
			name:"rule_${params.hostName}",
			ordinal:1
		]
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			body:data,
			username:params.username,
			password:params.password,
			authToken:params.authToken,
		]
		def out = callApi(reqParams, 'POST')
		if(!out.success) {
			return [success:false, msg:out.data?.message ?: out.msg, errors:out.errors]
		}
		// add conditions to rule
		endpointPath = "${params.path}/tm/ltm/policy/~${params.partition ?: BigIpUtility.BIGIP_PARTITION}~Drafts~${params.policyName}/rules/rule_${params.hostName}"
		data = [
			actions:[
				[name:'0', forward:true, pool:"/${params.partition ?: BigIpUtility.BIGIP_PARTITION}/${params.poolName}", request:true, select:true]
			],
			conditions:[
				[name:'0', caseInsensitive:true, equals:true, external:true, host:true, httpHost:true, present:true, remote:true, request:true, values:["${params.hostName}"]]
			]
		]
		reqParams = [
			uri:params.url,
			path:endpointPath,
			body:data,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		out = callApi(reqParams, 'PUT')

		if(!out.success) {
			return [success:false, messsage:out.data?.message ?: out.msg, errors:out.errors]
		}
		return rtn
	}

	def addPolicyRule(NetworkLoadBalancerPolicy policy, NetworkLoadBalancerRule rule) {
		def rtn = [success:true]
		def	auth = getConnectionBase(policy.loadBalancer)
		rtn.auth = auth

		// get policy draft, or create if it doesn't exist
		def policyNamingMap = [name:policy.name, partition:policy.partition, draft:true]
		def existingRules = getPolicyRules(auth + policyNamingMap)
		def config = rule.configMap

		// we have to create a draft of the policy if it doesn't have one yet
		if (existingRules == null) {
			def out = createPolicyDraft(policy, [auth:auth])
			if (!out.success)
				return out
			else
				existingRules = getPolicyRules(auth + policyNamingMap)
		}

		def endpointPath = "${auth.path}/tm/ltm/policy/${BigIpUtility.buildPartitionedName(policyNamingMap)}/rules"
		def data = [
			name:rule.name,
			ordinal:config.ordinal ?: existingRules.size()
		]
		def params = [
			uri:auth.url,
			path:endpointPath,
			body:data,
			username:auth.username,
			password:auth.password,
			authToken:auth.authToken
		]
		def out = callApi(params, 'POST')
		if(!out.success) {
			return out
		}
		// add conditions to rule
		def pool = morpheus.loadBalancer.pool.listById([config.pool.toLong()]).blockingSingle()
		endpointPath = "${auth.path}/tm/ltm/policy/~${policy.partition ?: BigIpUtility.BIGIP_PARTITION}~Drafts~${policy.name}/rules/${rule.name}"
		// add action section to body
		data = [
			actions:[
				[name:'0', forward:true, pool:BigIpUtility.buildPartitionedName(pool, '/'), request:true, select:true]
			]
		]
		// build condition map
		def condition = [name:'0', caseInsensitive:true, external:true, present:true, remote:true, request:true, values:["${config.value}".toString()]]

		// add field and operator to condition
		condition += (BigIpUtility.CONDITION_PARAM.find { it.name == config.field }).criteria
		condition += (BigIpUtility.CONDITION_OPERATOR.find { it.name == config.operator }).criteria
		data.conditions = [condition]

		params = [
			uri:auth.url,
			path:endpointPath,
			body:data,
			username:auth.username,
			password:auth.password,
			urlParams:['expandSubcollections':'true'],
			authToken:auth.authToken
		]
		out = callApi(params, 'PUT')

		if(!out.success) {
			return out
		}
		else {
			// TODO: check to see if need to make a call to get rule details
			rtn.success = true
			rtn.data = out.data
		}
		return rtn
	}

	def createPolicyDraft(NetworkLoadBalancerPolicy policy, Map opts = [:]) {
		def rtn = [success:false]
		def auth = opts.auth
		if (!auth) {
			auth = getConnectionBase(policy.loadBalancer)
		}
		def endpointPath = "${auth.path}/tm/ltm/policy/${BigIpUtility.buildPartitionedName([name:policy.name, partition:policy.partition])}"
		def params = [
			uri:auth.url,
			path:endpointPath,
			body:[test:''],
			username:auth.username,
			password:auth.password,
			urlParams:['options':'create-draft'],
			authToken:auth.authToken
		]
		def out = callApi(params, 'PATCH')
		rtn.success = out.success
		rtn.auth = auth
		return out
	}

	def getPolicyRules(Map params) {
		def policyName = BigIpUtility.buildPartitionedName(params)
		def endpointPath = "${params.path}/tm/ltm/policy/${policyName}/rules"
		def reqParams = [
			uri:params.url,
			path:endpointPath,
			username:params.username,
			password:params.password,
			authToken:params.authToken
		]
		def out = callApi(reqParams, 'GET')

		if (!out.success)
			return null
		else
			return out.data.items
	}

	def buildPolicyRuleFromMap(NetworkLoadBalancerPolicy policy, Map item) {
		def loadBalancer = policy.loadBalancer
		def objCategory = BigIpUtility.getObjCategory('rule', loadBalancer)
		def addConfig = [internalId:item.selfLink, externalId:item.fullPath, name:item.name, category:objCategory,
						 enabled:true, policy:policy]
		def firstAction = item.actionsReference?.items?.size() > 0 ? item.actionsReference.items.first() : null
		if(firstAction) {
			addConfig += [displayOrder:firstAction.ordinal, actionCode:firstAction.code, actionName:firstAction.name,
						  actionInternalId:firstAction.selfLink, actionExternalId:firstAction.fullPath, actionForward:firstAction.forward,
						  actionLength:firstAction.length, actionOffset:firstAction.offset, actionPoolId:firstAction.pool,
						  actionPort:firstAction.port, actionRequest:firstAction.request, actionSelect:firstAction.select,
						  actionStatus:firstAction.status, actionTimeout:firstAction.timeout, actionVlan:firstAction.vlanId,
						  actionExpiration:firstAction.expirySecs]
		}
		//lookup pools and add
		def add = new NetworkLoadBalancerRule(addConfig)
		add.setConfigMap(item)
		//add pools
		for (action in item.actionsReference?.items) {
			if(action.pool) {
				def poolMatch = morpheus.loadBalancer.pool.findByLoadBalancerAndExternalId(loadBalancer, action.pool)
				if(poolMatch) {
					add.pools.add(poolMatch)
				}
			}
		}
		return add
	}

	def createServer(Map opts) {
		def rtn = [success:false, authToken:opts.authToken]
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
				rtn.msg = results.data?.message ?: results.errors?.error ?: results.msg
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
			rtn.data = results.data
			if (results.success) {
				rtn.success = true
				rtn.found = true
				rtn.authToken = healthMonitor.authToken
			}
			else {
				rtn.success = false
				rtn.found = true
				rtn.message = results.data?.message ?: results.errors?.error ?: results.msg
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

	def updatePool(Map opts) {
		def rtn = [success:false]
		def pool = loadPool(opts)
		if(pool.found != true) {
			rtn.success = false
			rtn.found = false
		} else {
			def endpointPath = "${opts.path}/tm/ltm/pool/${opts.externalId}"
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
				authToken:opts.authToken
			]
			def out = callApi(params, 'PATCH')
			if (out.success) {
				rtn.success = true
				rtn.pool = out.data
			}
			else {
				log.warn("Unable to update bigip pool: ${out.data?.message ?: out.errors?.error ?: out.msg}")
				rtn.msg = out.data?.message ?: out.errors?.error ?: out.msg
			}
		}
		return rtn
	}

	def loadPool(Map opts) {
		if (!opts.authToken) {
			opts.authToken = getAuthToken(opts)
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
		if (results.success) {
			rtn.success = true
			rtn.members = results.data
		}
		else {
			rtn.msg = results.data?.message ?: results.errors?.error ?: results.msg
		}
		return rtn
	}

	def updatePoolMembers(Map opts) {
		def rtn = [success:false]
		def loadResults = loadPool(opts)
		if(loadResults.found != true) {
			rtn.success = false
			rtn.found = false
		} else {
			def poolUri = BigIpUtility.buildPartitionedName(loadResults.pool)
			def endpointPath = "${opts.path}/tm/ltm/pool/${poolUri}"
			def members = []
			opts.members.each { member ->
				def nodeName = "${member.externalId ?: BigIpUtility.buildPartitionedName(member, '/')}"
				members << [name:"${nodeName}:${member.port ?: opts.port}"]
			}
			def data = [members:members]
			//add monitor
			if(opts.monitorName)
				data.monitor = BigIpUtility.buildPartitionedName([name:opts.monitorName, partition:opts.partition], '/')
			def params = [
				uri:opts.url,
				path:endpointPath,
				body:data,
				username:opts.username,
				password:opts.password,
				authToken:loadResults.authToken
			]
			def out = callApi(params, 'PATCH')
			if (out.success) {
				rtn.success = true
				rtn.members = out.data
			}
			else {
				log.warn("Unable to update bigip pool members: ${out.data?.message ?: out.errors?.error ?: out.msg}")
				rtn.msg = out.data?.message ?: out.errors?.error ?: out.msg
			}
		}
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
			rtn.success = results.success
			rtn.data = results.data
			if (!rtn.success) {
				log.warn("Failed to remove load balancer pool ${poolName}: ${results.data?.message}")
				rtn.errors = results.errors
				rtn.msg = results.data?.message
			}
		} else {
			rtn.found = false
			rtn.success = true
		}
		return rtn
	}

	protected getConnectionBase(NetworkLoadBalancer lb, Map opts = null) {
		if (!lb.credentialLoaded)
			morpheus.loadBalancer.loadLoadBalancerCredentials(lb)

		def connectionBase = [
			url:"https://${lb.sshHost}:${lb.apiPort}",
			path:'/mgmt',
			username:lb.credentialData?.username ?: lb.sshUsername,
			password:lb.credentialData?.password ?: lb.sshPassword
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
			throw new RuntimeException("Failed to authenticate to bigip: ${response.data?.message ?: response.errors['error']}")
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
			log.error("Failure in call to F5 api: ${response.data?.message ?: response.errors?.error ?: response.msg}")
		}
		return response
	}

	// constants
	public static final String PROVIDER_CODE = 'bigip'
}
