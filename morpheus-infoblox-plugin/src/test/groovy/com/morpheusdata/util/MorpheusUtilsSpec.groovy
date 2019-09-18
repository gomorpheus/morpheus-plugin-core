package com.morpheusdata.util

import spock.lang.Specification

class MorpheusUtilsSpec extends Specification {
	def "ExtractCookie"() {
		given:
		def rawCookie = 'ibapauth="ip=10.30.19.110,client=API,group=admin-group,ctime=1568815778,timeout=600,mtime=1568815778,su=1,auth=LOCAL,user=admin,xOdj0yvvEvBpRXesDLHIirxYXyER0m6CrKg"; httponly; Path=/'

		expect:
		MorpheusUtils.extractCookie(null) == null
		MorpheusUtils.extractCookie('rawCookie="foobar"') == [rawCookie: "foobar"]
		MorpheusUtils.extractCookie(rawCookie) == [ibapauth: "ip=10.30.19.110,client=API,group=admin-group,ctime=1568815778,timeout=600,mtime=1568815778,su=1,auth=LOCAL,user=admin,xOdj0yvvEvBpRXesDLHIirxYXyER0m6CrKg"]
	}
}
