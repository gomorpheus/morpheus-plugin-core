package com.morpheusdata.bigip

import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.OptionSourceProvider
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.util.MorpheusUtils
import groovy.util.logging.Slf4j

@Slf4j
class BigIpOptionSourceProvider implements OptionSourceProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	BigIpOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'bigip-option-source-provider'
	}

	@Override
	String getName() {
		return 'BigIP Option Source Provider'
	}

	@Override
	List<String> getMethodNames() {
		return [
			'bigIpPluginPolicyControls', 'bigIpPluginPolicyRequires', 'bigIpPluginPolicyStrategies', 'bigIpPluginPolicyRuleField', 'bigIpPolicyRuleOperator',
			'bigIpPluginPartitions', 'bigIpPluginVirtualServerPools', 'bigIpPluginVirtualServerProfiles', 'bigIpPluginVirtualServerPolicies',
			'bigIpPluginVirtualServerScripts', 'bigIpPluginVirtualServerPersistence', 'bigIpPluginBalanceModes', 'bigIpPluginNodes', 'bigIpPluginHealthMonitors',
			'bigIpPluginPoolPersistenceModes', 'bigIpPluginProfileServiceTypes', 'bigIpPluginHttpProxies'
		]
	}

	def bigIpPluginPolicyControls(params) {
		return BigIpUtility.POLICY_CONTROLS.collect { [name: it, value: it] }
	}

	def bigIpPluginPolicyRequires(params) {
		return BigIpUtility.POLICY_TYPES.collect { [name:it, value:it] }
	}

	def bigIpPluginPolicyStrategies(params) {
		return BigIpUtility.POLICY_STRATEGIES.collect { [name:it.name, value:it.id] }
	}

	def bigIpPluginPolicyRuleField(params) {
		return BigIpUtility.CONDITION_PARAM.collect {
			return [name:it.name, value:it.name]
		}
	}

	def bigIpPluginPolicyRuleOperator(params) {
		return BigIpUtility.CONDITION_OPERATOR.collect {
			return [name:it.name, value:it.name]
		}
	}

	def bigIpPluginBalanceModes(params) {
		return BigIpUtility.BALANCE_MODE
	}

	def bigIpPluginPartitions(params) {
		def loadBalancerId
		if (params.domain?.loadBalancerId) {
			loadBalancerId = params.domain.loadBalancerId
		}
		else if (params.loadBalancer?.id) {
			if (params.loadBalancer.id.getClass().isArray())
				loadBalancerId = params.loadBalancer.id[0]
			else
				loadBalancerId = params.loadBalancer.id
		}
		def partitionSvc = morpheusContext.loadBalancer.partition
		def options = []
		partitionSvc.listById([loadBalancerId]).blockingSubscribe {
			options << [name: it.name, value: it.name]
		}
		return options
	}

	def bigIpPluginVirtualServerPools(params) {
		def rtn = []
		def lbInstance = params.domain
		def loadBalancer = lbInstance?.loadBalancer
		def loadBalancerId = loadBalancer ? loadBalancer.id : params.loadBalancerId
		def pools = []
		if(loadBalancerId) {
			morpheusContext.loadBalancer.pool.listById([loadBalancerId]).blockingSubscribe { pool ->
				pools << [name: pool.name, value: pool.id]
			}
		}
		return rtn
	}

	def bigIpPluginVirtualServerProtocols(params) {
		return BigIpUtility.VIRTUAL_SERVER_PROTOCOL_LIST
	}

	def bigIpPluginVirtualServerProfiles(params) {
		def optionType = params.optionType
		def account = morpheusContext.loadBalancer.getAccountById(params.accountId.toLong()).blockingGet()
		def query = params.phrase ?: params.q
		def profiles = []
		def queryObject = [:]
		if(params.parentId || params.parentid) {
			def parentId = params.parentId ?: params.parentid
			def loadBalancer = morpheusContext.loadBalancer.getLoadBalancerById(parentId.toLong()).blockingGet()
			if(loadBalancer) {
				def queryArgs = [
				    [name:'account.id', value:account.id, operator:'eq'],
					[name:'loadBalancer.id', value:loadBalancer.id, operator:'eq']
				]
				if (optionType?.fieldCondition)
					queryArgs << [name:'serviceType', value:optionType.fieldCondition, operator:'eq']

				queryArgs << [operator:'or', args:[
				    [name:'name', value:"%${query}%", operator:'ilike'],
					[name:'description', value:"%${query}%", operator:'ilike'],
					[name:'serviceType', value:"%${query}%", operator:'ilike']
				]]
				queryObject.criteria = queryArgs

				morpheusContext.loadBalancer.profile.queryProfiles(queryObject).subscribe { profile ->
					profiles << profile
				}
			}
		}
		else if(params.domain) {
			def lbInstance = params.domain
			if(optionType && lbInstance) {
				def queryArgs = [
					[name:'account.id', value:account.id, operator:'eq'],
					[name:'loadBalancer.id', value:lbInstance.loadBalancer.id, operator:'eq']
				]
				if (optionType?.fieldCondition)
					queryArgs << [name:'serviceType', value:optionType.fieldCondition, operator:'eq']

				queryObject.criteria = queryArgs
			}
		}
		queryObject.sort = [[name:'name', direction:'asc']]

		morpheusContext.loadBalancer.profile.queryProfiles(queryObject).subscribe { profile ->
			profiles << profile
		}

		return profiles.collect { profile ->
			return [id:profile.id, name:profile.name, value:profile.id, type:profile.serviceType]
		}
	}

	def bigIpPluginVirtualServerPolicies(params) {
		def rtn = []
		def account = morpheusContext.loadBalancer.getAccountById(params.accountId.toLong()).blockingGet()
		def query = params.phrase ?: params.q
		def policies = []
		def queryObject = [:]
		def includeDraft = MorpheusUtils.parseBooleanConfig(params.includeDraft)
		if(params.parentId || params.parentid) {
			def parentId = params.parentId ?: params.parentid
			def loadBalancer = morpheusContext.loadBalancer.getLoadBalancerById(parentId.toLong()).blockingGet()
			if(loadBalancer) {
				def queryArgs = [
					[name:'account.id', value:account.id, operator:'eq'],
					[name:'loadBalancer.id', value:loadBalancer.id, operator:'eq'],
					[name:'policyType', value:params.policyType ?: 'policy', operator:'eq']
				]
				if (includeDraft)
					queryArgs << [name:'draft', value:false, operator:'eq']

				queryArgs << [operator:'or', args:[
					[name:'name', value:"%${query}%", operator:'ilike'],
					[name:'description', value:"%${query}%", operator:'ilike']
				]]
				queryObject.criteria = queryArgs
			}
		}
		else if(params.domain) {
			def lbInstance = params.domain
			if(lbInstance) {
				def queryArgs = [
					[name:'account.id', value:account.id, operator:'eq'],
					[name:'loadBalancer.id', value:lbInstance.loadBalancer.id, operator:'eq']
				]

				queryObject.criteria = queryArgs
			}
		}
		queryObject.sort = [[name:'name', direction:'asc']]

		morpheusContext.loadBalancer.policy.queryPolicies(queryObject).subscribe { policy ->
			policies << policy
		}
		return policies.collect { policy ->
			return [id:policy.id, name:policy.name, value:policy.id]
		}
	}

	def bigIpPluginVirtualServerScripts(params) {
		def rtn = []
		def loadBalancerId = params.parentId ?: params.parentid
		if(!loadBalancerId) {
			loadBalancerId = params.find { it.key.startsWith("loadBalancer") && it.key.endsWith('id')}?.value
		}
		def loadBalancer = loadBalancerId ? morpheusContext.loadBalancer.getLoadBalancerById(loadBalancerId.toLong()).blockingGet() : null
		if(!loadBalancer && params.domain && params.domain.respondsTo('loadBalancer')) {
			loadBalancer = params.domain.loadBalancer
		}
		def query = params.phrase ?: params.q
		def scripts = []
		def queryObject = [:]
		if(loadBalancer) {
			def queryArgs = [
				[name:'account.id', value:params.accountId.toLong(), operator:'eq'],
				[name:'loadBalancer.id', value:loadBalancer.id, operator:'eq']
			]
			queryArgs << [operator:'or', args:[
				[name:'name', value:"%${query}%", operator:'ilike'],
				[name:'description', value:"%${query}%", operator:'ilike']
			]]
			queryObject.sort = [[name:'name', direction:'asc']]

			morpheusContext.loadBalancer.script.queryScripts(queryObject).subscribe { script ->
				scripts << script
			}
			rtn = scripts.collect{ script -> [id:script.id, name:script.name, value:script.id] }
		}
		return rtn
	}

	def bigIpPluginVirtualServerPersistence(params) {
		return bigIpPluginVirtualServerPolicies(params + [policyType:'persistence'])
	}

	def bigIpPluginNodes(params) {
		def rtn = []
		def parentId = params.parentId ?: params.parentid
		def nodes = []
		def queryObject = [:]
		if(parentId) {
			def loadBalancer = morpheusContext.loadBalancer.getLoadBalancerById(parentId.toLong()).blockingGet()
			if(loadBalancer) {
				def query = params.phrase ?: params.q
				def queryArgs = [
					[name:'loadBalancer.id', value:loadBalancer.id, operator:'eq']
				]
				queryArgs << [operator:'or', args:[
					[name:'name', value:"%${query}%", operator:'ilike'],
					[name:'ipAddress', value:"%${query}%", operator:'ilike']
				]]
				queryObject.sort = [[name:'name', direction:'asc']]

				morpheusContext.loadBalancer.node.queryNodes(queryObject).subscribe { node ->
					nodes << node
				}
				if(nodes?.size() > 0) {
					rtn = nodes.collect{ node -> [id:node.id, name:node.name, ipAddress:node.ipAddress, value:node.id] }
				}
			}
		}
		return rtn
	}

	def bigIpPluginHealthMonitors(params) {
		def rtn = []
		def monitors = []
		def queryObject = [:]
		def loadBalancer
		def loadBalancerId = params.parentId ?: params.parentid ?: params.loadBalancer?.id
		if(loadBalancerId)
			loadBalancer = morpheusContext.loadBalancer.getLoadBalancerById(loadBalancerId.toLong()).blockingGet()
		if(!loadBalancer) {
			def domain = params.domain
			if(domain) {
				loadBalancer = domain.loadBalancer
			}
		}
		if(loadBalancer) {
			def query = params.phrase ?: params.q
			def queryArgs = [
				[name:'loadBalancer.id', value:loadBalancer.id, operator:'eq']
			]
			if(query) {
				queryArgs << [operator:'or', args:[
					[name:'name', value:"%${query}%", operator:'ilike'],
					[name:'monitorType', value:"%${query}%", operator:'ilike']
				]]
			}
			queryObject.sort = [[name:'name', direction:'asc']]

			morpheusContext.loadBalancer.monitor.queryHealthMonitors(queryObject).subscribe { monitor ->
				monitors << monitor
			}
			rtn = monitors.collect{ monitor -> [id:monitor.id, name:monitor.name, type:monitor.monitorType, value:monitor.id] }
		}
		return rtn
	}

	def bigIpPluginPoolPersistenceModes(params) {
		return BigIpUtility.STICKY_MODE
	}

	def bigIpPluginProfileServiceTypes(params) {
		// need to grab the provider that has the bigip api call helpers
		BigIpProvider provider = (BigIpProvider)plugin.getProviderByCode(BigIpProvider.PROVIDER_CODE)
		def lb = morpheusContext.loadBalancer.getLoadBalancerById(params.domain.loadBalancer.id.toLong()).blockingGet()
		def serviceTypes = provider.listProfileServiceTypes(lb)
		return serviceTypes.serviceTypes.collect { type -> [name:type.name, value:type.name] }
	}

	def bigIpPluginHttpProxies(params) {
		return BigIpUtility.HTTP_PROXY_TYPE
	}

	def bigIpCertSelect(params) {
		def objCategory = BigIpUtility.getObjCategory('cert', params.domain.loadBalancerId.toLong())

		// TODO: implement this last.... pain in the friggin ass
		params.certRequired = true
		params.noSelfSigned = true
		def accountCerts = accountSslCertificate(params)
		def refData = ReferenceData.where { category == objCategory }
		accountCerts += refData.collect { [name:it.name, value:it.value] }
		return accountCerts
	}
}
