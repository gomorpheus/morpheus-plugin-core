package com.morpheusdata.bigip.util

import com.morpheusdata.model.NetworkLoadBalancer

class BigIpUtility {
	static final String BIGIP_PARTITION = 'Common'
	static final String BIGIP_REFDATA_CATEGORY = 'loadbalancer.bigip'

	static final Map OBJ_CATEGORY_PREFIX = [
		partition:'loadbalancer.f5.partition',
		persistence:'loadbalancer.f5.persistence',
		rule:'f5.rule',
		cert:'f5.ssl.cert',
		profile:'f5.profile',
		policy:'f5.policy',
		pool:'f5.pool'
	]

	static final List CONDITION_OPERATOR = [
		[name:'equals', criteria:[equals:true]],
		[name:'does not equal', criteria:[equals:true, not:true]],
		[name:'contains', criteria:[contains:true]],
		[name:'does not contain', criteria:[contains:true, not:true]],
		[name:'starts with', criteria:[startsWith:true]],
		[name:'does not start with', criteria:[startsWith:true, not:true]],
		[name:'ends with', criteria:[endsWith:true]],
		[name:'does not end with', criteria:[endsWith:true, not:true]]
	]
	static final List CONDITION_PARAM = [
		[name:'http_host', criteria:[host:true, httpHost:true]],
		[name:'http_method', criteria:[httpMethod:true]],
		[name:'http_referer', criteria:[httpReferer:true]],
		[name:'http_user_agent', criteria:[httpUserAgent:true, browserType:true]],
		[name:'path', criteria:[httpUri:true, path:true]]
	]
	static final List POLICY_CONTROLS = ['forwarding']
	static final List POLICY_TYPES = ['http']
	static final List POLICY_STRATEGIES = [
		[id:'/Common/first-match', name:'first'],
		[id:'/Common/all-match', name:'all'],
		[id:'/Common/best-match', name:'best']
	]

	static final VIRTUAL_SERVER_PROTOCOL_LIST = [
		[name:'tcp', value:'tcp'],
		[name:'udp', value:'udp'],
		[name:'sctp', value:'sctp']
	]

	static final BALANCE_MODE = [
		[name:'round robin', value:'roundrobin'],
		[name:'least connections', value:'leastconnections']
	]

	static final STICKY_MODE = [
		[name:'sourceip', value:'sourceip'],
		[name:'cookie', value:'cookie']
	]

	static final HTTP_PROXY_TYPE = [
		[name:'Reverse', value:'reverse'],
		[name:'Explicit', value:'explicit'],
		[name:'Transparent', value:'transparent']
	]

	static def getObjCategory(String type, Long loadBalancerId) {
		return getObjCategory(type, loadBalancerId.toString())
	}

	static def getObjCategory(String type, NetworkLoadBalancer loadBalancer) {
		return getObjCategory(type, loadBalancer.id)
	}

	static def getObjCategory(String type, String loadBalancerId) {
		return "${OBJ_CATEGORY_PREFIX[type]}${loadBalancerId ? '.' + loadBalancerId : ''}".toString()
	}

	static String parseNodeState(String nodeState, String status) {
		def rtn = 'unknown'
		if(nodeState == 'user-enabled')
			rtn = 'enabled'
		else if(nodeState == 'user-disabled') {
			if(status == 'user-down')
				rtn = 'offline'
			else
				rtn = 'disabled'
		}
		return rtn
	}

	static String parseServiceType(String link) {
		def rtn
		def lastSlash = link.lastIndexOf('/')
		if(lastSlash > -1) {
			def lastQuestion = link.lastIndexOf('?')
			if(lastQuestion > -1)
				rtn = link.substring(lastSlash + 1, lastQuestion)
			else
				rtn = link.substring(lastSlash + 1)
		}
		return rtn
	}

	static decodeApiNumber(Object var) {
		def rtn
		if(rtn == 'auto')
			rtn = 0
		else if(rtn instanceof Number)
			rtn = var
		return rtn
	}

	static decodeLoadBalancingMode(String mode) {
		def rtn = 'leastconnections'
		if(mode == 'round-robin')
			rtn = 'roundrobin'
		return rtn
	}

	static String convertExternalId(String externalId) {
		return externalId ? externalId.replaceAll('/', '~') : ''
	}

	static String combineStringLists(List list) {
		def rtn
		if(list?.size() > 0) {
			rtn = list.join(',')
		}
		return rtn
	}

	static String parsePathName(String path) {
		def rtn
		def lastSlash = path.lastIndexOf('/')
		if(lastSlash > -1)
			rtn = path.substring(lastSlash + 1)
		return rtn
	}

	static String extractReferenceId(String link) {
		def rtn
		def lastSlash = link.lastIndexOf('/')
		if(lastSlash > -1) {
			def lastQuestion = link.lastIndexOf('?')
			if (lastQuestion > -1)
				rtn = link.substring(lastSlash + 1, lastQuestion)
			else
				rtn = link.substring(lastSlash + 1)
		}
		return rtn ? rtn.replaceAll('~', '/') : ''
	}

	static String parseDetination(String destination) {
		def rtn = destination
		def lastSlash = destination.lastIndexOf('/')
		if (lastSlash > -1) {
			def lastColon = destination.lastIndexOf(':')
			if(lastColon > -1) {
				rtn = destination.substring(lastSlash + 1, lastColon)
			}
		}
		return rtn
	}

	static Integer parseDetinationPort(String destination) {
		def rtn
		def lastColon = destination.lastIndexOf(':')
		if(lastColon > -1) {
			def portStr = destination.substring(lastColon + 1)
			rtn = portStr.toInteger()
		}
		return rtn
	}

	static String buildPartitionedName(item, delim = '~') {
		def partition = item.partition ?: BIGIP_PARTITION
		return "${delim}${partition}${item instanceof Map && item.draft ? delim + 'Drafts' : ''}${delim}${item.name}".toString()
	}

	static String buildApiPath(String path, String externalId, String name, String type = null) {
		def rtn = path
		if(rtn.endsWith('/') == false)
			rtn = rtn + '/'
		if(externalId) {
			rtn = rtn + (externalId.startsWith('/') ? externalId.substring(1) : externalId)
		} else {
			if(type)
				rtn = rtn + (type.startsWith('/') ? type.substring(1) : type) + (type.endsWith('/') ? '' : '/')
			if(name)
				rtn = rtn + (name.startsWith('/') ? name.substring(1) : name)
		}
		return rtn
	}

	static String getLoadBalancingMode(String mode) {
		def rtn = 'least-connections-member'
		if(mode == 'roundrobin')
			rtn = 'round-robin'
		return rtn
	}
}
