package com.morpheusdata.model

import groovy.json.JsonOutput
import spock.lang.Specification

class CloudSpec extends Specification {

	void "configMap"() {
		setup:
		def cloud = new Cloud()
		cloud.config = JsonOutput.toJson([key1: 'value1', key2: [key3: 'value3']])

		expect:
		cloud.config == '{"key1":"value1","key2":{"key3":"value3"}}'

		when:
		cloud.setConfigMap([key3: 'value3', key4: [key5: 'value5']])

		then:
		cloud.config == '{"key3":"value3","key4":{"key5":"value5"}}'
	}
}
