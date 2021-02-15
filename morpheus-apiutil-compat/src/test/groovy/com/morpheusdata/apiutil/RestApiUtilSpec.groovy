package com.morpheusdata.apiutil

import spock.lang.Specification

class RestApiUtilSpec extends Specification {
	def "ExtractCookie"() {
		given:
		def rawCookie = 'ibapauth="ip=10.30.19.110,client=API,group=admin-group,ctime=1568815778,timeout=600,mtime=1568815778,su=1,auth=LOCAL,user=admin,xOdj0yvvEvBpRXesDLHIirxYXyER0m6CrKg"; httponly; Path=/'

		expect:
		RestApiUtil.extractCookie(null) == null
		RestApiUtil.extractCookie('rawCookie="foobar"') == [rawCookie: "foobar"]
		RestApiUtil.extractCookie(rawCookie) == [ibapauth: "ip=10.30.19.110,client=API,group=admin-group,ctime=1568815778,timeout=600,mtime=1568815778,su=1,auth=LOCAL,user=admin,xOdj0yvvEvBpRXesDLHIirxYXyER0m6CrKg"]
	}
}
