package com.morpheusdata.request

import com.morpheusdata.model.ComputeServer
import spock.lang.Specification

class GetGpuDevicesRequestSpec extends Specification {

	void "server can be assigned"() {
		given:
		def request = new GetGpuDevicesRequest()
		def server = new ComputeServer(id: 7L)

		when:
		request.server = server

		then:
		request.server.is(server)
		request.server.id == 7L
	}
}
