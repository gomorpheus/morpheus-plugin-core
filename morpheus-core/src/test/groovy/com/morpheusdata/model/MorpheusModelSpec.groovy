package com.morpheusdata.model

import spock.lang.Specification

class MorpheusModelSpec extends Specification {
	void "MorpheusModel.getPropertiesMap()" () {
		given:
		def model = new MorpheusModel()

		when:
		def props = model.getPropertiesMap()

		then:
		props.size() == 2
	}

	void "NetworkPool.getPropertiesMap()" () {
		given:
		def model = new NetworkPool(id: 1)

		when:
		def props = model.getPropertiesMap()

		then:
		props.size() == 22
		and: "property of parent is included"
		props['id'] == 1
	}
}
