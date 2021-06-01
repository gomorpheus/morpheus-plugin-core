package com.morpheusdata.core.util

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

		void "addRequiredHeader"() {
		expect:
		finalHeaders == RestApiUtil.addRequiredHeader(headers, name, value)

		where:
		headers      | name  | value  || finalHeaders
		null         | 'foo' | 'bar'  || [foo: 'bar']
		[foo: 'bar'] | 'foo' | 'buzz' || [foo: 'bar']
		[foo: 'bar'] | 'abc' | 'def'  || [foo: 'bar', abc: 'def']
	}

	void "callApi - fail"() {
		given:
		String url = 'morpheusdata.com'
		String path = 'oauth'
		String username = 'user'
		String password = 'pazz'
		String method = 'POST'
		RestApiUtil.RestOptions opts = new RestApiUtil.RestOptions()

		when:
		def resp = RestApiUtil.callApi(url, path, username, password, opts, method)

		then:
		!resp.success
		resp.errors.error == "Error occurred processing the response for morpheusdata.com/oauth : null"
	}

	void "callApi - not found"() {
		given:
		String url = 'https://morpheusdata.com'
		String path = 'oauth'
		String username = 'user'
		String password = 'pazz'
		String method = 'POST'
		RestApiUtil.RestOptions opts = new RestApiUtil.RestOptions()

		when:
		def resp = RestApiUtil.callApi(url, path, username, password, opts, method)

		then:
		!resp.success
		resp.errors == [:]
		resp.errorCode == "404"
	}

//	void "callApi - post multipart/form-data MAAS"() {
//		given:
//		String url = 'http://127.0.0.1:2223'
//		String path = '/MAAS/api/2.0/machines/'
//		String method = 'POST'
//		def headers = [Accept:'application/json', 'Content-Type':'multipart/form-data']
//		def query = [op: 'allocate']
//		def body = [pool: 'default']
//		def oauth = new RestApiUtil.RestOptions.OauthOptions(version:'1',
//				consumerKey:'rUN5ZaXnXgCZAyswWB',
//				consumerSecret:'',
//				apiKey:'fmWPuzB9safxjKSD6r',
//				apiSecret:'WfdjMwjdaMxQBvQ8G8Y6L9NE4xQ76vPc')
//
//		RestApiUtil.RestOptions opts = new RestApiUtil.RestOptions(headers: headers, queryParams: query as Map<String,String>, body:body, contentType: 'multi-part-form')
//		opts.oauth = oauth as RestApiUtil.RestOptions.OauthOptions
//
//		when:
//		def resp = RestApiUtil.callApi(url, path, null, null, opts, method)
//
//		then:
//		resp.success == true
//	}
}
