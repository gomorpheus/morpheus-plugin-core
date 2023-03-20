package com.morpheusdata.core.util

import spock.lang.Specification

class MorpheusUtilsSpec extends Specification {
	void "parseDate"() {
		expect:
		rtn == MorpheusUtils.parseDate(dateStr).toGMTString()

		where:
		dateStr                | rtn
		"2018-03-23T16:53:27Z" | "23 Mar 2018 16:53:27 GMT"
	}

	void "compareVersions"() {
		expect:
		rtn == MorpheusUtils.compareVersions(a, b)

		where:
		a       | b        | rtn
		"1"     | null     | 1
		"1.2.3" | "2.1"    | -1
		"2.0"   | "1.0"    | 1
		"1.0"   | "1.0"    | 0
		"1.0"   | "2.0"    | -1
		""      | ""       | 0
		"2.0.1" | "2.0.1"  | 0
		"2.0.1" | "2"      | 1
		"2"     | "2.0.1"  | -1
		"1.5.6" | "16.6.2" | -1
		"15.1"  | "2.3.4"  | 1
	}
}

