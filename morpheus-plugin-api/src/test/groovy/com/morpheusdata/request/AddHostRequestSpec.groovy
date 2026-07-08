package com.morpheusdata.request

import com.morpheusdata.model.CloudPool
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.StorageVolume
import com.morpheusdata.model.ComputeServerInterface
import spock.lang.Specification

class AddHostRequestSpec extends Specification {

	def "defaults are set correctly"() {
		when:
		def req = new AddHostRequest()

		then:
		req.licenseCheck == true
		req.pool == null
		req.serverType == null
		req.serverName == null
		req.hostname == null
		req.plan == null
		req.config == null
		req.siteId == null
		req.volumes == null
		req.networkInterfaces == null
		req.securityGroupIds == null
	}

	def "fields can be populated"() {
		given:
		def serverType = new ComputeServerType(id: 10L, code: 'host')
		def plan = new ServicePlan(id: 5L, name: 'Small')
		def vol = new StorageVolume(id: 1L)
		def nic = new ComputeServerInterface(id: 1L)
		def pool = new CloudPool(id: 1L)

		when:
		def req = new AddHostRequest(
			serverType: serverType,
			serverName: 'host-1',
			hostname: 'host-1',
			plan: plan,
			config: [resourcePoolId: 42L],
			siteId: 1L,
			volumes: [vol],
			networkInterfaces: [nic],
			securityGroupIds: [200L],
			licenseCheck: false,
			pool: pool
		)

		then:
		req.serverType.code == 'host'
		req.serverName == 'host-1'
		req.hostname == 'host-1'
		req.plan.id == 5L
		req.config.resourcePoolId == 42L
		req.siteId == 1L
		req.volumes.size() == 1
		req.networkInterfaces.size() == 1
		req.securityGroupIds == [200L]
		req.licenseCheck == false
		req.pool != null
		req.pool.id == 1L
	}
}
