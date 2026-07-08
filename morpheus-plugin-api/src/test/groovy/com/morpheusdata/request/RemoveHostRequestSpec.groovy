package com.morpheusdata.request

import spock.lang.Specification

class RemoveHostRequestSpec extends Specification {

	def "defaults are set correctly"() {
		when:
		def req = new RemoveHostRequest()

		then:
		req.force == false
		req.removeResources == false
		req.removeInstances == false
		req.userId == null
		req.skipPolicyCheck == false
	}

	def "fields can be populated"() {
		when:
		def req = new RemoveHostRequest(
			force: true,
			removeResources: true,
			removeInstances: true,
			userId: 7L,
			skipPolicyCheck: true
		)

		then:
		req.force == true
		req.removeResources == true
		req.removeInstances == true
		req.userId == 7L
		req.skipPolicyCheck == true
	}
}
