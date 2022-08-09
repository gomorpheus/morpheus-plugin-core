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
}

