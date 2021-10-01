package com.morpheusdata.maas.plugin

import com.morpheusdata.core.util.ComputeUtility
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.OsType
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

@Slf4j
class MaasComputeUtility {

	static waitInterval = 10000l
	static maxWaitAttempts = 100
	static maxReleaseWaitAttempts = 200
	static maxDeployWaitAttempts = 400

	static assignedStatusList = [10, 5, 6, 9, 11, 12, 13, 14, 15]
	static errorStatusList = [2, 3, 7, 8, 13, 15, 16, 17, 18, 19, 20, 22]
	static provisionStatusList = [4]

	static testConnection(Map authConfig, Map opts) {
		def rtn = [success:false, invalidLogin:false]
		try {
			def results = listRegionControllers(authConfig, opts)
			if(results.success == true && results.error != true)
				rtn.success = true
			else if(results.errorCode == 401)
				rtn.invalidLogin = true
		} catch(e) {
			log.error("testConnection to ${authConfig.apiUrl}: ${e}")
		}
		return rtn
	}

	//http://naas01-dev.idc1.level3.com:8557/swagger-ui.html#/ 
	//Use interface ae90 on minerlabqh001, and minerlabqh002 

	static listRegionControllers(Map authConfig, Map opts) {
		return listResources(authConfig, 'regioncontrollers', opts)
	}

	static listRackControllers(Map authConfig, Map opts) {
		return listResources(authConfig, 'rackcontrollers', opts)
	}

	static listSpaces(Map authConfig, Map opts) {
		return listResources(authConfig, 'spaces', opts)
	}

	static listResourcePools(Map authConfig, Map opts) {
		return listResources(authConfig, 'resourcepools', opts)
	}

	static listMachines(Map authConfig, Map opts) {
		return listResources(authConfig, 'machines', opts)
	}

	static listNodes(Map authConfig, Map opts) {
		return listResources(authConfig, 'nodes', opts)
	}

	static listImages(Map authConfig, Map opts) {
		return listResources(authConfig, 'boot-resources', opts)
	}

	static listSubnets(Map authConfig, Map opts) {
		return listResources(authConfig, 'subnets', opts)
	}

	static listStaticRoutes(Map authConfig, Map opts) {
		return listResources(authConfig, 'static-routes', opts)
	}

	static listFabrics(Map authConfig, Map opts) {
		return listResources(authConfig, 'fabrics', opts)
	}

	static listVlans(Map authConfig, String fabricId, Map opts) {
		return listResources(authConfig, 'fabrics/' + fabricId + '/vlans', opts)
	}

	static listIpRanges(Map authConfig, Map opts) {
		return listResources(authConfig, 'ipranges', opts)
	}

	static listIpAddresses(Map authConfig, Map opts) {
		return listResources(authConfig, 'ipaddresses', opts)
	}

	static listScripts(Map authConfig, Map opts) {
		return listResources(authConfig, 'scripts', opts)
	}

	static listNotifications(Map authConfig, Map opts) {
		return listResources(authConfig, 'notifications', opts)
	}

	static listPackageRepositories(Map authConfig, Map opts) {
		return listResources(authConfig, 'package-repositories', opts)
	}

	static listPods(Map authConfig, Map opts) {
		return listResources(authConfig, 'pods', opts)
	}

	static listTags(Map authConfig, Map opts) {
		return listResources(authConfig, 'tags', opts)
	}

	static listUsers(Map authConfig, Map opts) {
		return listResources(authConfig, 'users', opts)
	}

	static listZones(Map authConfig, Map opts) {
		return listResources(authConfig, 'zones', opts)
	}

	static listDnsResources(Map authConfig, Map opts) {
		return listResources(authConfig, 'dnsresources', opts)
	}

	static listBootResources(Map authConfig, Map opts) {
		return listResources(authConfig, 'boot-resources', opts)
	}

	static listBootSources(Map authConfig, Map opts) {
		return listResources(authConfig, 'boot-sources', opts)
	}

	static listComissioningScripts(Map authConfig, Map opts) {
		return listResources(authConfig, 'commissioning-scripts', opts)
	}

	static listDevices(Map authConfig, Map opts) {
		return listResources(authConfig, 'devices', opts)
	}

	static listDomains(Map authConfig, Map opts) {
		return listResources(authConfig, 'domains', opts)
	}


	static listVersion(Map authConfig, Map opts) {
		return listResources(authConfig, 'version', opts)
	}

	static listResources(Map authConfig, String resourceName, Map opts) {
		def rtn = [success:false, error:false, data:null]
		String apiPath = authConfig.basePath + '/' + resourceName + '/'
		def headers = buildHeaders([:])
		def query = opts.query ?: [:]

		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		ServiceResponse results = RestApiUtil.callJsonApi(authConfig.apiUrl as String, apiPath, null, null, requestOpts, 'GET')
		log.info("listResources {} results: {}", resourceName, results)
		if(results.success) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results.toMap()
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	//machines
	static loadMachine(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json'])
		def query = opts.query ?: [:]
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static updateMachine(Map authConfig, String machineId, Map machineConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		def body = [:]
		if(machineConfig.pool)
			body.pool = machineConfig.pool
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType:'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'PUT')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static powerOffMachine(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json'])
		def query = opts.query ?: [:]
		query.op = 'power_off'
		def body = [:]
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static powerOnMachine(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json'])
		def query = opts.query ?: [:]
		query.op = 'power_on'
		def body = [:]
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static loadMachinePowerState(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json'])
		def query = opts.query ?: [:]
		query.op = 'query_power_state'
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'GET')
		if(results.success) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static allocateMachine(Map authConfig, Map machineConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'allocate'
		def body = machineConfig.body ?: [:]
		if(machineConfig.name)
			body.name = machineConfig.name
		if(machineConfig.externalId)
			body.system_id = machineConfig.externalId
		if(machineConfig.arch)
			body.arch = machineConfig.arch
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static releaseMachine(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'release'
		def body = [:]
		body.erase = (opts.erase != null) ? opts.erase : true
		body.quick_erase = (opts.quick_erase != null) ? opts.quick_erase : true
		if(opts.comment)
			body.comment = opts.comment
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static deployMachine(Map authConfig, String machineId, Map machineConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'deploy'
		def body = machineConfig.body ?: [:]
		if(machineConfig.userData)
			body.user_data = machineConfig.userData
		//image
		if(machineConfig.osName)
			body.osystem = machineConfig.osName
		if(machineConfig.osVersion)
			body.distro_series = machineConfig.osVersion
		//hwe_kernel, agent_name, bridge_all, bridge_stp, bridge_fd, comment, install_rackd, install_kvm, ephemeral_deploy, vcenter_registration
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static waitForMachine(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxDeployWaitAttempts) {
			//load the vm
			def results = loadMachine(authConfig, machineId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 6) {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.status == 11) {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					rtn.data = results.data
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				rtn.data = results.data
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static waitForMachineRelease(Map authConfig, String machineId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxReleaseWaitAttempts) {
			//load the vm
			def results = loadMachine(authConfig, machineId, opts)
			if(results.success == true) {
				//check status
				if(results.data.status == 4) {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(errorStatusList.contains(results.data.status)) {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					rtn.data = results.data
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				rtn.data = results.data
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	static waitForMachinePowerState(Map authConfig, String machineId, String powerState, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def attempt = 0
		def keepGoing = true
		while(keepGoing == true && attempt < maxDeployWaitAttempts) {
			//load the vm
			def results = loadMachinePowerState(authConfig, machineId, opts)
			if(results.success == true) {
				//check status
				if(results.data.state == powerState) {
					//deployed
					rtn.success = true
					rtn.data = results.data
					keepGoing = false
				} else if(results.data.state == 'error') {
					//failed
					rtn.success = false
					rtn.data = results.data
					keepGoing = false
				} else {
					//still deploying
					rtn.data = results.data
					attempt++
					sleep(waitInterval)
				}
			} else {
				//keep waiting for it to provision
				rtn.data = results.data
				attempt++
				sleep(waitInterval)
			}
		}
		return rtn
	}

	//networks
	static bondInterfaces(Map authConfig, String machineId, Map bondConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'create_bond'
		def body = [name:bondConfig.name]
		body.bond_mode = bondConfig.mode ?: 'active-backup'
		body.parents = bondConfig.parents
		if(bondConfig.vlanId)
			body.vlan = bondConfig.vlanId
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static updateInterface(Map authConfig, String machineId, String interfaceId, Map interfaceConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/' + interfaceId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		def body = [:]
		if(interfaceConfig.vlanId)
			body.vlan = interfaceConfig.vlanId
		if(interfaceConfig.name)
			body.name = interfaceConfig.name
		if(interfaceConfig.macAddress)
			body.mac_address = interfaceConfig.macAddress
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'PUT')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static createInterfaceVlan(Map authConfig, String machineId, Map vlanConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'create_vlan'
		def body = [parent:vlanConfig.parentId, vlan:vlanConfig.vlanId]
		if(vlanConfig.tags)
			body.tags = vlanConfig.tags
		if(vlanConfig.mtu)
			body.mtu = vlanConfig.mtu
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static createVlan(Map authConfig, String fabricId, Map vlanConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/fabrics/' + fabricId + '/vlans/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		def body = [name:vlanConfig.name]
		body.vid = vlanConfig.vlanId
		if(vlanConfig.description)
			body.description = vlanConfig.description
		if(vlanConfig.mtu)
			body.mtu = vlanConfig.mtu
		if(vlanConfig.space)
			body.space = vlanConfig.space
		//body.vlan
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static createSubnet(Map authConfig, Map subnetConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/subnets/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		def body = [name:subnetConfig.name]
		body.cidr = subnetConfig.cidr
		body.vlan = subnetConfig.vlanId
		if(subnetConfig.fabric)
			body.fabric = subnetConfig.fabric
		if(subnetConfig.gateway)
			body.gateway_ip = subnetConfig.gateway
		body.allow_dns = 0
		body.managed = 0
		//body.vlan
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static assignSubnet(Map authConfig, String machineId, String interfaceId, Map subnetConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/' + interfaceId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'link_subnet'
		def body = [:]
		body.mode = subnetConfig.mode ?: 'STATIC'
		body.subnet = subnetConfig.subnetId
		if(subnetConfig.ipAddress)
			body.ip_address = subnetConfig.ipAddress
		if(subnetConfig.gateway)
			body.default_gateway = subnetConfig.gateway
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static unlinkSubnet(Map authConfig, String machineId, String interfaceId, Map linkConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/' + interfaceId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'unlink_subnet'
		def body = [:]
		if(linkConfig.linkId)
			body.id = linkConfig.linkId
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static assignGateway(Map authConfig, String machineId, String interfaceId, Integer linkId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/' + interfaceId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'set_default_gateway'
		def body = [link_id:linkId]
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static assignStorage(Map authConfig, String machineId, Map storageConfig, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/machines/' + machineId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		def query = opts.query ?: [:]
		query.op = 'set_storage_layout'
		def body = [:]
		body.storage_layout = storageConfig.layout ?: 'flat'
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'POST')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	//deletes
	static deleteInterface(Map authConfig, String machineId, String interfaceId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/nodes/' + machineId + '/interfaces/' + interfaceId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static deleteSubnet(Map authConfig, String subnetId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/subnets/' + subnetId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	static deleteVlan(Map authConfig, String fabricId, String vlanId, Map opts) {
		def rtn = [success:false, error:false, data:null]
		def apiPath = authConfig.basePath + '/fabrics/' + fabricId + '/vlans/' + vlanId + '/'
		def headers = buildHeaders(['Accept':'application/json', 'Content-Type':'multipart/form-data'])
		RestApiUtil.RestOptions requestOpts = new RestApiUtil.RestOptions(headers:headers)
		requestOpts.oauth = authConfig.oauth as RestApiUtil.RestOptions.OauthOptions
		def results = RestApiUtil.callJsonApi(authConfig.apiUrl, apiPath, null, null, requestOpts, 'DELETE')
		if(results.success == true) {
			rtn.data = results.data
			rtn.success = true
		} else {
			rtn.data = results
			rtn += parseErrorResults(results)
			rtn.msg = rtn.msg ?: results?.description ?: 'unknown error'
		}
		return rtn
	}

	///MAAS/api/2.0/account/prefs/sshkeys/
	///MAAS/api/2.0/account/prefs/sslkeys/
	///MAAS/api/2.0/nodes/{system_id}/blockdevices/
	///MAAS/api/2.0/dhcp-snippets/
	///MAAS/api/2.0/dnsresourcerecords/
	///MAAS/api/2.0/discovery/
	///MAAS/api/2.0/fannetworks/
	///MAAS/api/2.0/nodes/{system_id}/interfaces/
	///MAAS/api/2.0/license-keys/
	///MAAS/api/2.0/maas/?op=get_config
	///MAAS/api/2.0/machines/
	///MAAS/api/2.0/installation-results/
	///MAAS/api/2.0/nodes/{system_id}/results/

	static buildHeaders(Map headers) {
		headers = (headers ?: [:])
		return headers
	}

	static parseErrorResults(ServiceResponse results) {
		def rtn = [:]
		try {
			rtn.errorCode = results?.errorCode
			//sameple - see if there are more structured errors
			//Authorization Error: 'Invalid access token: vjATbZ9Q7BwXh3SSj'
			rtn.msg = results?.content ?: 'unknown error'
		} catch(e) {
			log.warn("error parsing error results: ${e}")
		}
		return rtn
	}

	static ComputeServer configureComputeServer(Map machine, ComputeServer existingServer, Cloud cloud, ComputeZonePool resourcePool, List<ServicePlan> typePlans) {
		log.debug "configureComputeServer: ${groovy.json.JsonOutput.prettyPrint(machine.encodeAsJson().toString())}, existingServer: ${existingServer} ${cloud}"

		if(existingServer) {
			existingServer.markClean()
		}

		ComputeServer server = existingServer ?: new ComputeServer()

		if(server.name != machine.hostname) {
			server.name = machine.hostname
		}
		if(server.externalId != machine.system_id) {
			server.externalId = machine.system_id
		}

		if(server.hostname != machine.hostname){
			server.hostname = machine.hostname
		}

		if(server.serverType != 'metal') {
			server.serverType = 'metal'
		}

		if(server.consoleHost != machine?.ip_addresses?.getAt(0)) {
			server.consoleHost = machine?.ip_addresses?.getAt(0) // host console address
		}

		if(server.internalName != server.name) {
			server.internalName = server.name
		}

		if(server.lvmEnabled != false) {
			server.lvmEnabled = false
		}

		def osDevice = machine.boot_disk?.path?.endsWith('sda') ? '/dev/sda' : '/dev/vda'
		if(server.osDevice != osDevice) {
			server.osDevice = osDevice
		}

		if(server.rootVolumeId != machine.boot_disk?.resource_uri) {
			server.rootVolumeId = machine.boot_disk?.resource_uri
		}

		if(server.dataDevice != server.osDevice) {
			server.dataDevice = server.osDevice
		}

		def powerState = (machine.power_state == 'on' ? 'on' : (machine.power_state == 'off' ? 'off' : 'unknown'))
		if(server.powerState?.toString() != powerState) {
			server.powerState = powerState
		}

		def tags = machine.tag_names?.join(',')
		if(server.tags != tags) {
			server.tags = tags
		}

		def maxStorage = Math.round((machine.storage ?: 0) * ComputeUtility.ONE_MEGABYTE)
		if(server.maxStorage != maxStorage) {
			server.maxStorage = maxStorage
		}

		if(server.maxMemory != (machine.memory ?: 0) * ComputeUtility.ONE_MEGABYTE) {
			server.maxMemory = (machine.memory ?: 0) * ComputeUtility.ONE_MEGABYTE
		}

		if(server.maxCores != (machine.cpu_count ?: 1)) {
			server.maxCores = (machine.cpu_count ?: 1)
		}

		if(server.resourcePool?.id != resourcePool?.id) {
			server.resourcePool = resourcePool
		}

		if(server.provision != canProvision(machine.status)) {
			server.provision = canProvision(machine.status)
		}

		if(server.cloud?.id != cloud?.id) {
			server.cloud = cloud
		}

		if(server.account?.id != cloud.account?.id) {
			server.account = cloud.account
		}

		if(server.status != getServerStatus(machine.status)) {
			server.status = getServerStatus(machine.status)
		}

		def plan = findServicePlanMatch(typePlans, server.tags)
		if(server.plan?.id != plan?.id) {
			server.plan = plan
		}

		if(machine.interface_set?.size() > 0) {
			def firstNic = machine.interface_set.first()
			if(server.macAddress != firstNic.mac_address) {
				server.macAddress = firstNic.mac_address
			}
		}

		// Do not modify the hostname and name if provisioning
		if(assignedStatusList.contains(machine.status) && existingServer) {
			if(server.hostname != existingServer.hostname) {
				server.hostname = existingServer.hostname
			}
			if(server.name != existingServer.name) {
				server.name = existingServer.name
			}
		}

		def serverType = existingServer?.computeServerType ?: new ComputeServerType(code: 'maas-metal')
		if(server.computeServerType?.code != serverType?.code) {
			server.computeServerType = serverType
		}

		server
	}

	static VirtualImage bootImageToVirtualImage(Cloud cloud, Map bootImage, List<OsType> osTypes) {
		log.debug "bootImageToVirtualImage ${cloud} ${bootImage}"
		String objCategory = "maas.image.${cloud.id}"
		def addConfig = [
				category:objCategory,
				code:objCategory + ".${bootImage.name}",
				account: cloud.account,
				name:"${bootImage.name} - ${bootImage.architecture}",
				imageType:'pxe',
				externalId:"${bootImage.name}/${bootImage.id}", //remotePath:bootImage.name, refType:'ComputeZone'
		]

		//add extra stuff?
		//parse the os stuff
		addConfig.osType = findOsTypeMatch(osTypes, bootImage.name, (bootImage.architecture == 'amd64' ? 64 : 32))
		addConfig.platform = addConfig.osType?.platform
		new VirtualImage(addConfig)
	}

	static ComputeZonePool resourcePoolToComputeZonePool(Map resourcePool, Cloud cloud, String category, poolMatchId, releaseMatchId) {
		def cloudItemId = "${resourcePool.id}"
		def poolReadOnly = false
		if(poolMatchId != null) {
			poolReadOnly = (cloudItemId?.toString() != poolMatchId?.toString())
		}
		else if(releaseMatchId != null) {
			poolReadOnly = (cloudItemId?.toString() == releaseMatchId?.toString())
		}

		return new ComputeZonePool(name:resourcePool.name, description:resourcePool.description, owner: cloud.owner,
				externalId: resourcePool.id, cloud:cloud, code: category + ".${resourcePool.id}", category: category,
				refType:'ComputeZone', refId:cloud.id, readOnly: poolReadOnly)
	}

	private static getServerStatus(Integer status) {
		def rtn = 'unknown'
		switch(status) {
			case 0: //new
			case 1: //commissioning
				rtn = 'discovered'
				break
			case 2: //failed commisionig
			case 3: //missing
			case 7: //retired
			case 8: //broken
			case 13: //failed releasing
			case 15: //failed disk erase
			case 16: //rescue
			case 17: //entering rescue
			case 18: //rescue failed
			case 19: //exiting rescue
			case 20: //failed exiting rescue
			case 22: //testing failed
				rtn = 'error'
				break
			case 4: //ready
				rtn = 'available'
				break
			case 5: //reserved
			case 10: //allocated
			case 21: //testing
				rtn = 'reserved'
				break
			case 6: //deployed
				rtn = 'provisioned'
				break
			case 9: //deploying
				rtn = 'provisioning'
				break
			case 11: //failed
				rtn = 'failed'
				break
			case 12: //releasing
			case 14: //disk erasing
				rtn = 'removing'
				break
			default:
				rtn = 'unknown'
		}
		return rtn
	}

	private static findServicePlanMatch(Collection servicePlans, String tags) {
		def rtn
		if(servicePlans?.size() > 0) {
			if(tags) {
				def tagList = tags.toLowerCase().split(',')
				rtn = servicePlans.find{ tagList.contains(it.tagMatch.toLowerCase()) }
			}
		}
		return rtn
	}

	private static canProvision(Integer status) {
		return provisionStatusList.contains(status)
	}

	private static findOsTypeMatch(Collection osTypes, String name, Integer bitCount) {
		def rtn
		def nameList = name.tokenize('/')
		//first token should match the osName on an os type
		def osName = nameList[0]
		//second should be version to search codename or version
		def osVersion = nameList[1]
		//find it?
		def nameMatches = osTypes?.findAll{ it.osName == osName && bitCount == bitCount }
		//narrow it down
		rtn = nameMatches?.find{ it.osVersion == osVersion || it.osCodename == osVersion }
		if(!rtn)
			rtn = nameMatches?.find{ osVersion.indexOf(it.osVersion) > -1 }
		return rtn
	}
}
