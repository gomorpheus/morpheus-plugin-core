package com.morpheusdata.model

import spock.lang.Specification

class ContentSecurityPolicySpec extends Specification {
	void "src types"() {
		when:
		ContentSecurityPolicy csp = new ContentSecurityPolicy(scriptSrc: 'script.com', imgSrc: 'img.com', frameSrc: 'frame.com', connectSrc: 'connect.com', styleSrc: 'style.com')

		then:
		csp.scriptSrc == 'script.com'
		csp.frameSrc == 'frame.com'
		csp.connectSrc == 'connect.com'
		csp.imgSrc == 'img.com'
		csp.styleSrc == 'style.com'
	}

	void "build policy"() {
		given:
		String basePolicy = "default-src 'self' "
		ContentSecurityPolicy csp1 = new ContentSecurityPolicy(scriptSrc: '*.google.com', styleSrc: 'https:' )
		ContentSecurityPolicy csp2 = new ContentSecurityPolicy(scriptSrc: '*.yahoo.com', frameSrc: 'morpheusdata.com', connectSrc: 'https://example.com')
		List<ContentSecurityPolicy> pluginContentPolicies = [csp1, csp2]

		when:
		List<String> imgSrc = pluginContentPolicies.findAll{it.imgSrc}?.collect{it.imgSrc}
		List<String> frameSrc = pluginContentPolicies.findAll{it.frameSrc}?.collect{it.frameSrc}
		List<String> scriptSrc = pluginContentPolicies.findAll{it.scriptSrc}?.collect{it.scriptSrc}
		List<String> connectSrc = pluginContentPolicies.findAll{it.connectSrc}?.collect{it.connectSrc}
		List<String> styleSrc = pluginContentPolicies.findAll{it.styleSrc}?.collect{it.styleSrc}

		String csp = basePolicy + "; script-src 'self' 'unsafe-inline' ${scriptSrc.join(' ')}"

		if (imgSrc) {
			csp += "; img-src 'self' ${imgSrc.join(' ')}"
		}
		if (frameSrc) {
			csp += "; frame-src 'self' ${frameSrc.join(' ')}"
		}
		if (scriptSrc) {
			csp += "; style-src 'self' 'unsafe-inline' ${styleSrc.join(' ')}"
		}
		if (connectSrc) {
			csp += "; connect-src 'self' ${connectSrc.join(' ')}"
		}

		then:
		csp == "default-src 'self' ; script-src 'self' 'unsafe-inline' *.google.com *.yahoo.com; frame-src 'self' morpheusdata.com; style-src 'self' 'unsafe-inline' https:; connect-src 'self' https://example.com"
	}
}
