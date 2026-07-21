package com.morpheusdata.model

import spock.lang.Specification

class ComputeTypeLayoutSpec extends Specification {

	void "getClusterVersion returns null by default"() {
		given:
		def layout = new ComputeTypeLayout()

		expect:
		layout.getClusterVersion() == null
	}

	void "setClusterVersion and getClusterVersion round-trip"() {
		given:
		def layout = new ComputeTypeLayout()

		when:
		layout.setClusterVersion('1.27.3')

		then:
		layout.getClusterVersion() == '1.27.3'
	}
}

