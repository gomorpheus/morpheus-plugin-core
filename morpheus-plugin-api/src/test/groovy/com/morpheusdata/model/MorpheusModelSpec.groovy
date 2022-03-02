package com.morpheusdata.model

import org.apache.groovy.json.internal.LazyMap
import spock.lang.Specification

class MorpheusModelSpec extends Specification {
	void "MorpheusModel.getProperties()"() {
		given:
		def model = new MorpheusModel()

		when:
		def props = model.getProperties()

		then:
		props.size() == 3
	}

	void "NetworkPool.getProperties()"() {
		given:
		def model = new NetworkPool(id: 1)

		when:
		def props = model.getProperties()

		then:
		props.size() == 30
		and: "property of parent is included"
		props['id'] == 1
	}

	void "Instance.getProperties()"() {
		given:
		def model = new Instance(description: 'abc123')

		when:
		def props = model.getProperties()

		then:
		props.size() == 46
		and: "private properties are unavailable"
		props['description'] == null
	}

	void "getConfigMap"() {
		given:
		MorpheusModel model = new MorpheusModel(config: str)

		expect:
		Map configMap = model.getConfigMap()
		configMap == expectedMap

		where:
		str                                                            | expectedMap
		'{"foo":1}'                                                    | [foo: 1]
		'{"foo":2.5}'                                                  | [foo: 2.5]
		'{"foo":"bar"}'                                                | [foo: "bar"]
		'{"foo":true}'                                                 | [foo: true]
		'{"foo":false}'                                                | [foo: false]
		'{"nested":true, "instance":{"id": 1, "name": "My Instance"}}' | [nested: true, instance: [id: 1, name: 'My Instance']]
		'{"alist":["a","b","c"]}'                                      | [alist: ['a', 'b', 'c']]
	}

	void "getConfigMap - malformed json"() {
		given:
		MorpheusModel model = new MorpheusModel(config: '{"foo": "bar" "abc": 123}')

		when:
		Map configMap = model.getConfigMap()
		then:
		noExceptionThrown()
		configMap == [:]
	}

	void "setConfigMap"() {
		given:
		MorpheusModel model = new MorpheusModel()
		LazyMap lazyMap = new LazyMap()
		lazyMap.put('foo', 'bar')

		when:
		model.setConfigMap(lazyMap)

		then:
		model.getConfigMap() == [foo: 'bar']
	}

	void "getConfigProperty"() {
		given:
		MorpheusModel model = new MorpheusModel(config: config)

		expect:
		propertyValue == model.getConfigProperty(prop)

		where:
		prop             | config                                                               || propertyValue
		'foo'            | '{"zoneId":345}'                                                     || null
		'zoneId'         | '{"zoneId":345}'                                                     || 345
		'config.apiKey'  | '{"zoneId":345, "config":{"apiKey": "foobar"}}'                      || 'foobar'
		'config.zone.id' | '{"zoneId":345, "config":{"apiKey": "foobar", "zone": {"id": 567}}}' || 567
		'config.foo.id'  | '{"zoneId":345, "config":{"apiKey": "foobar", "zone": {"id": 567}}}' || null
		'config.alist'   | '{"zoneId":345, "config":{"alist":["a","b","c"]}}'                   || ['a', 'b', 'c']
	}

	void "setConfigMap getConfig"() {
		given:
		MorpheusModel model = new MorpheusModel(config: '{"zoneId":345, "config":{"apiKey": "foobar", "zone": {"id": 567}}}')

		expect:
		model.setConfigMap(configMap)
		expectedConfig == model.getConfig()

		where:
		configMap                                              | expectedConfig
		[foo: 'bar']                                           | '{"foo":"bar"}'
		[zoneId: 345]                                          | '{"zoneId":345}'
		[capacity: 2.5]                                        | '{"capacity":2.5}'
		[nested: true, instance: [id: 1, name: 'My Instance']] | '{"nested":true,"instance":{"id":1,"name":"My Instance"}}'
	}

	void "setConfigProperty"() {
		given:
		MorpheusModel model = new MorpheusModel(config: '{"zoneId":345, "config":{"apiKey": "foobar", "zone": {"id": 567}}}')

		expect:
		model.setConfigProperty(propertyName, propertyValue)
		expectedValue == model.getConfigProperty(propertyName)

		where:
		propertyName          | propertyValue | expectedValue
		'foo'                 | 'bar'         | 'bar'
		'zoneId'              | 345           | 345
		'config.apikey'       | 'someapikey'  | 'someapikey'
		'config.nested.bogus' | 'ignored'     | null
		'somelong'            | 2l            | 2l
		'somebool'            | true          | true
	}
}
