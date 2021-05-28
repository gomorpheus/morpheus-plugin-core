package com.morpheusdata.cloud

import com.morpheusdata.response.WorkloadResponse
import spock.lang.Specification
import spock.lang.Subject

class DigitalOceanApiServiceSpec extends Specification {

	@Subject
	DigitalOceanApiService service

	def setup() {
		service = new DigitalOceanApiService()
	}

	void "dropletToWorkloadResponse"() {
		given:
		def droplet = [
				id      : '1111',
				networks: [
						v4: [
								[ip_address: '10.10.10.10', type: 'public'],
								[ip_address: '192.168.0.10', type: 'private'],
						]
				]
		]
		WorkloadResponse expected = new WorkloadResponse(externalId: '1111', publicIp: '10.10.10.10', privateIp: '192.168.0.10')

		when:
		def resp = service.dropletToWorkloadResponse(droplet)

		then:
		resp.externalId == expected.externalId
		resp.publicIp == expected.publicIp
		resp.privateIp == expected.privateIp
		resp.status == expected.status
	}
}
