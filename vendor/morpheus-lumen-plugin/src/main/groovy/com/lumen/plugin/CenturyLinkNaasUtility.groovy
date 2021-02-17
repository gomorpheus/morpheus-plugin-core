package com.lumen.plugin

import com.morpheusdata.apiutil.RestApiUtil
import groovy.util.logging.Slf4j

@Slf4j
class CenturyLinkNaasUtility { //extends AbstractComputeUtility

	static waitInterval = 10000l
	static maxWaitAttempts = 60
	static tokenBuffer = 1000l * 60l //60 second buffer
	static naasTimeoutInSeconds = waitInterval * maxWaitAttempts / 1000l

	static testConnection(Map authConfig) {
		def rtn = [success:false, invalidLogin:false]
		try {
			def results = [success:true]
			if(results.success == true && results.error != true)
				rtn.success = true
			else if(results.errorCode == 401)
				rtn.invalidLogin = true
		} catch(e) {
			log.error("testConnection to ${authConfig.apiUrl}: ${e}")
		}
		return rtn
	}

	static reserveIpVpn(Map authConfig, Map ipConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/services/ipvpn'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [
					serviceId:ipConfig.serviceId,
					bandwidth:(ipConfig.bandwidth ?: 50000),
					ipv4AddressBlock:ipConfig.cidr,
					vrf: ipConfig.vrf,
					requestId: ipConfig.requestId,
					customer: [name:ipConfig.accountName],
					timeoutInSeconds: naasTimeoutInSeconds,
					peDevices: []
			]
			for(row in ipConfig.devices)
				body.peDevices << [deviceName:row.deviceName, interfaceName:row.interfaceName, vlan:row.vlan]
			def requestOpts = [headers:headers, query:query, body:body]
			log.info("create vpn request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("reserveIpVpn results: ${results}, ${ipConfig?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static deleteIpVpn(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/services/ipvpn/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			log.info("deleteIpVpn request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deleteIpVpn results: ${results}, ${resourceId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static loadVpnService(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/services/ipvpn/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadInternetService results: ${results}, ${opts?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static reserveIpBlock(Map authConfig, Map ipConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasIPs'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [:]
			body.requestId = ipConfig.requestId
			body.productType = ipConfig.productType ?: 'EDGE_COMPUTE_INTERNET'
			body.clliCode = ipConfig.clliCode
			body.cidr = ipConfig.cidr
			body.customer = [name:ipConfig.accountName]
			body.timeoutInSeconds = naasTimeoutInSeconds
			def requestOpts = [headers:headers, query:query, body:body]
			log.info("reserve ip request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("reserveIpBlock results: ${results}, ${ipConfig?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static createVlan(Map authConfig, Map vlanConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/vlans'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [:]
			body.requestId = vlanConfig.requestId
			body.customer = [name:vlanConfig.accountName]
			body.serverHostnames = vlanConfig.serverHostnames
			body.timeoutInSeconds = naasTimeoutInSeconds
			def requestOpts = [headers:headers, query:query, body:body]
			log.info("vlan request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("createVlan results: ${results}, ${vlanConfig?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static createPort(Map authConfig, Map portConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/leaf/ports'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [:]
			body.requestId = portConfig.requestId
			body.productType = portConfig.productType ?: 'EDGE_COMPUTE_INTERNET'
			body.customer = [name:portConfig.accountName]
			body.serverHostnames = portConfig.serverHostnames
			body.timeoutInSeconds = naasTimeoutInSeconds
			body.serviceId = portConfig.circuitId
			body.vlan = portConfig.vlanId
			def requestOpts = [headers:headers, query:query, body:body]
			log.info("createPort request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("createPort results: ${results}, ${portConfig?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static createPath(Map authConfig, Map pathConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/paths'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [serviceId:pathConfig.serviceId]
			body.requestId = pathConfig.requestId
			body.productType = pathConfig.productType ?: 'EDGE_COMPUTE_INTERNET'
			body.customer = [name:pathConfig.accountName]
			body.serverHostnames = pathConfig.serverHostnames
			body.timeoutInSeconds = naasTimeoutInSeconds
			body.vlan = pathConfig.vlanId
			def requestOpts = [headers:headers, query:query, body:body]
			log.info("createPath request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("createPath results: ${results}, ${pathConfig?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static createInternetService(Map authConfig, Map serviceConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/services/internet'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [
					serviceId:serviceConfig.serviceId,
					bandwidth:(serviceConfig.bandwidth ?: 50000),
					ipv4AddressBlock:serviceConfig.cidr
			]
			body.requestId = serviceConfig.requestId
			body.customer = [name:serviceConfig.accountName]
			body.timeoutInSeconds = naasTimeoutInSeconds
			body.peDevices = []
			for(row in serviceConfig.devices)
				body.peDevices << [deviceName:row.deviceName, interfaceName:row.interfaceName, vlan:row.vlan, vendor:row.vendor]
			def requestOpts = [headers:headers, query:query, body:body]
			log.info("createInternetService request: ${requestOpts}")
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("createInternetService results: ${results}, ${serviceConfig?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.resourceId = results.data.resourceId
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static loadLeafDevices(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/devices'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [machineHostname:resourceId]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadLeafDevices results: ${results}")
			if(results.success == true) {
				//{leafDevices:[{deviceName, interfaceName}]}
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static loadIpBlock(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasIPs/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadIpBlock results: ${results}, ${opts?.requestId}")
			if(results.success == true) {
				//{"resourceId":"IP-07b015ddb2394cf8be3fca5933f71623","requestStatusUrl":"http://naas01-dev.idc1.level3.com:8557/api/v1/ips/IP-07b015ddb2394cf8be3fca5933f71623"}
				//{"resourceId": "IP-9682fba0ac0544b2ace8b6a72f90b741", "status": "SUCCESS", "operation": "BUILD", "requestDate": "2020-09-16T19:23:11.522", "ipBlock": "216.202.192.32/29"}
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static waitForIpBlock(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxWaitAttempts) {
			//load the vm
			def results = loadIpBlock(authConfig, resourceId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 'SUCCESS') {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.status == 'FAILURE') {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static loadVlan(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/vlans/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadVlan results: ${results}, ${opts?.requestId}")
			if(results.success == true) {
				//[resourceId:VLAN-4ebad48c42f242559120d7d53d9a27b1, status:SUCCESS, requestDate:2020-05-19T15:58:55.080, devices:[[deviceName:MINERLABQH001, interfaceName:ae90, subInterface:3, vlan:3], [deviceName:MINERLABQH002, interfaceName:ae90, subInterface:3, vlan:3]]]]
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static waitForVlan(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxWaitAttempts) {
			//load the vm
			def results = loadVlan(authConfig, resourceId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 'SUCCESS') {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.status == 'FAILURE') {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static loadPort(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/leaf/ports/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadPort results: ${results} ${opts.requestId}")
			if(results.success == true) {
				//[resourceId:PORT-bbf9f43c053046f3943ede10807e14f3, status:SUCCESS, requestDate:2020-05-19T16:05:01.266, serviceId:OH/IRXX/305393/LVLC, devices:[[deviceName:MINERLABQH001, interfaceName:ae90, subInterface:3, vlan:3], [deviceName:MINERLABQH002, interfaceName:ae90, subInterface:3, vlan:3]]]]
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static waitForPort(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxWaitAttempts) {
			//load the vm
			def results = loadPort(authConfig, resourceId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 'SUCCESS') {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.status == 'FAILURE') {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static loadPath(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/paths/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadPath results: ${results}")
			if(results.success == true) {
				//[resourceId:PATH-550d1cf511ee4d6a9148f0cbe485e846, status:SUCCESS, requestDate:2020-05-19T16:11:30.705, peDevices:[[deviceName:mnl-ear-1, interfaceName:lag-10, vlan:10], [deviceName:mnl-ear-2, interfaceName:lag-10, vlan:10]]]]
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static waitForPath(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxWaitAttempts) {
			//load the vm
			def results = loadPath(authConfig, resourceId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 'SUCCESS') {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.status == 'FAILURE') {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static loadInternetService(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/services/internet/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadInternetService results: ${results} ${opts.requestId}")
			if(results.success == true) {
				//[resourceId:SERVICE-49f44ddfc9f243cfa50513b1c39e2113, status:SUCCESS, requestDate:2020-05-19T16:59:43.968, peDevices:[[interfaces:[[interface:lag-10, outerVlan:10]], device:mnl-ear-1], [interfaces:[[interface:lag-10, outerVlan:10]], device:mnl-ear-2]]]]
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static waitForInternetService(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxWaitAttempts) {
			def results = opts.productType == 'EDGE_COMPUTE_IPVPN' ? loadVpnService(authConfig, resourceId, opts) : loadInternetService(authConfig, resourceId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 'SUCCESS') {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.status == 'FAILURE') {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static deleteOrder(Map authConfig, String accountNumber, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasAccounts/' + accountNumber + '/orders/' + resourceId //old api endpoing 'naasOrders/' + resourceId //orders/ <- bug on CL side? doens't match docs
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deleteOrder results: ${results}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static deleteIpBlock(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasIPs/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deleteIpBlock results: ${results} ${opts.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static deleteVlan(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/vlans/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deleteVlan results: ${results} ${opts.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static deletePort(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/leaf/ports/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deletePort results: ${results} ${opts?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static deletePath(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/paths/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deletePath results: ${results} ${opts?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static deleteInternetService(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/services/internet/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def requestOpts = [headers:headers]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
			log.info("deleteInternetService results: ${results}, ${opts?.requestId}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static listVrfs(Map authConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + "naasEdgeRequest/vrfs"
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [
					accountId: authConfig.customerNumber,
					accountType: "ENTERPRISE_ID",
					vrfType: "PRIVATE"
			]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("listVrfs results: ${results}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static loadVrf(Map authConfig, String resourceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/vrfs/' + resourceId
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def requestOpts = [headers:headers, query:query]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
			log.info("loadVrf results: ${results}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static createVrf(Map authConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + 'naasEdgeRequest/vrfs/'
		def tokenResults = getToken(authConfig)
		if(tokenResults.success == true) {
			def headers = buildHeaders([:], authConfig.accountId, tokenResults.token)
			def query = [:]
			def body = [:] // TODO
			def requestOpts = [headers:headers, query:query, body: body]
			def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
			log.info("createVrf results: ${results}")
			if(results.success == true) {
				rtn.data = results.data
				rtn.success = true
			} else {
				rtn.data = results.data
				rtn += parseErrorResults(results)
				rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
				rtn.success = false
			}
		}
		return rtn
	}

	static reserveVrfIp(Map authConfig, String resourceId, Map opts) {
		print "reserveVrfIp noop"
	}

	static buildHeaders(Map headers, String accountId, String token) {
		headers = headers ?: ['Content-Type':'application/json']
		if(accountId)
			headers['accountId'] = accountId
		if(token)
			headers['Authorization'] = 'Bearer ' + token
		return headers
	}

	static parseErrorResults(Map results) {
		def rtn = [:]
		rtn.msg = results?.msg ?: 'unknown api error'
		return rtn
	}

	static checkToken(Map tokenConfig, Map authConfig) {
		def rtn = tokenConfig
		def checkDate = new Date()
		if(tokenConfig.expires) {
			if(tokenConfig.expires.time > (checkDate.time - tokenBuffer)) {
				//reissue
				rtn = getToken(authConfig)
			}
		}
		return rtn
	}

	static getToken(Map authConfig) {
		def rtn = [success:false]
		def requestToken = true
		if(authConfig.token) {
			if(authConfig.expires) {
				def checkDate = new Date()
				if(authConfig.expires.time > (checkDate.time - tokenBuffer)) {
					requestToken = true
				} else {
					requestToken = false
					rtn.success = true
					rtn.token = authConfig.token
				}
			} else {
				requestToken = false
				rtn.success = true
				rtn.token = authConfig.token
			}
		}
		//if need a new one
		if(requestToken == true) {
			def apiPath = authConfig.authPath
			def body = ['grant_type':'client_credentials']
			def headers = ['Content-Type':'application/x-www-form-urlencoded']
			def requestOpts = [headers:headers, body:body, bodyType:'form']
			def tokenDate = new Date()
			def results = RestApiUtil.callJsonApi(authConfig.authUrl, apiPath, authConfig.apiKey, authConfig.apiSecret, requestOpts, 'POST')
			rtn.success = results?.success && results?.error != true
			if(rtn.success == true) {
				//authConfig.token = rtn.token
				rtn.token = results.data['access_token']
				def expires = results.data['expires_in']
				if(expires) {
					rtn.expires = new Date(tokenDate.time + (expires.toLong() * 1000l))
				}
				authConfig.token = rtn.token
				authConfig.expires = rtn.expires
			} else {
				rtn.content = results.content
				rtn.data = results.data
				rtn.errorCode = results.errorCode
				rtn.headers = results.headers
			}
		}
		return rtn
	}

	//{"refresh_token_expires_in": "0", "api_product_list": "[ Sample-API-Product1, Sample-API-Product2]",
	//  "api_product_list_json":[], "organization_name": "ext", "developer.email": "John.Smith@mycompany.com",
	//  "token_type": "BearerToken", "issued_at": "1558032536000", "client_id": "v0ehG4dB55QQB7waOV6Gy1zG4kJ1V7",
	//  "access_token": "NNb28MBFtGdsobXZcldAxtrSYp9C", "application_name": "8ce11702-6337-415e-8ef2-438177fe10xy",
	//  "scope": "", "expires_in": "1799", "refresh_count": "0", "status": "approved"

}
