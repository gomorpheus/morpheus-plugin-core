package com.morpheusdata.apiutil

import spock.lang.Specification

class MorpheusUtilitySpec extends Specification {
	void "getConfigMap"() {
		expect:
		Map configMap = MorpheusUtility.getConfigMap(str)
		configMap == expectedMap

		where:
		str                                                            | expectedMap
		'{"foo":1}'                                                    | [foo: 1]
		'{"foo":2.5}'                                                  | [foo: 2.5]
		'{"foo":"bar"}'                                                | [foo: "bar"]
		'{"foo":true}'                                                 | [foo: true]
		'{"foo":false}'                                                | [foo: false]
		'{"nested":true, "instance":{"id": 1, "name": "My Instance"}}' | [nested: true, instance: [id: 1, name: 'My Instance']]
	}

	void "getConfigMap - malformed json"() {
		when:
		Map configMap = MorpheusUtility.getConfigMap('{"foo": "bar" "abc": 123}')
		then:
		noExceptionThrown()
		configMap == [:]
	}
}
