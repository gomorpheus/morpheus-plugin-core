package com.morpheusdata.model

import spock.lang.Specification

class LogLevelSpec extends Specification {
	void "correct values"() {
		expect:
		LogLevel.values().toString() == "[trace, debug, info, warn, error, fatal]"
	}
}
