package com.morpheusdata.request

import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Instance
import spock.lang.Specification

class BeforeConvertToManagedRequestSpec extends Specification {

	void "default construction initializes fields to null"() {
		when:
		def request = new BeforeConvertToManagedRequest()

		then:
		request.server == null
		request.instance == null
		request.opts == null
	}

	void "setServer / getServer roundtrip"() {
		given:
		def request = new BeforeConvertToManagedRequest()
		def server = new ComputeServer(id: 7)

		when:
		request.setServer(server)

		then:
		request.getServer().is(server)
		request.getServer().id == 7
	}

	void "setInstance / getInstance roundtrip"() {
		given:
		def request = new BeforeConvertToManagedRequest()
		def instance = new Instance(id: 5)

		when:
		request.setInstance(instance)

		then:
		request.getInstance().is(instance)
		request.getInstance().id == 5
	}

	void "setOpts / getOpts roundtrip"() {
		given:
		def request = new BeforeConvertToManagedRequest()
		def opts = [biosWorkloadProfile: 'Virtualization']

		when:
		request.setOpts(opts)

		then:
		request.getOpts() == [biosWorkloadProfile: 'Virtualization']
	}

	void "field access and setter access are consistent"() {
		given:
		def server = new ComputeServer(id: 1)
		def opts = [biosWorkloadProfile: 'Virtualization']
		def request = new BeforeConvertToManagedRequest()

		when:
		request.server = server
		request.opts = opts

		then:
		request.getServer().is(server)
		request.getOpts() == opts
	}
}
