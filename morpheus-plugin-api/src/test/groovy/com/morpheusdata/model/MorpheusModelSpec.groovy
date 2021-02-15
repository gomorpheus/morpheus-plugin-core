package com.morpheusdata.model

import spock.lang.Specification

class MorpheusModelSpec extends Specification {
	void "MorpheusModel.getProperties()" () {
		given:
		def model = new MorpheusModel()

		when:
		def props = model.getProperties()

		then:
		props.size() == 2
	}

	void "NetworkPool.getProperties()" () {
		given:
		def model = new NetworkPool(id: 1)

		when:
		def props = model.getProperties()

		then:
		props.size() == 29
		and: "property of parent is included"
		props['id'] == 1
	}

	void "Instance.getProperties()"() {
		given:
		def model = new Instance(description: 'abc123')

		when:
		def props = model.getProperties()

		then:
		props.size() == 37
		and: "private properties are unavailable"
		props['description'] == null
	}
}
