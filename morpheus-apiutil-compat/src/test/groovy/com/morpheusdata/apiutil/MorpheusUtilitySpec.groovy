package com.morpheusdata.apiutil

import com.morpheusdata.model.MorpheusModel
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

	void "getConfigProperty"() {
		expect:
		propertyValue == MorpheusUtility.getConfigProperty(prop, config)

		where:
		prop             | config                                                               || propertyValue
		'foo'            | '{"zoneId":345}'                                                     || null
		'zoneId'         | '{"zoneId":345}'                                                     || 345
		'config.apiKey'  | '{"zoneId":345, "config":{"apiKey": "foobar"}}'                      || 'foobar'
		'config.zone.id' | '{"zoneId":345, "config":{"apiKey": "foobar", "zone": {"id": 567}}}' || 567
		'config.foo.id' | '{"zoneId":345, "config":{"apiKey": "foobar", "zone": {"id": 567}}}' 	|| null
	}
}
