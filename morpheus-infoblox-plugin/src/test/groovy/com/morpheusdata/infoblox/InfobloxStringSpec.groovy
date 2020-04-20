package com.morpheusdata.infoblox

import spock.lang.Narrative
import spock.lang.Specification

@Narrative("Some tests around the url parsing methods of the Infoblox provider")
class InfobloxStringSpec extends Specification {
    void "parseNetworkFilter"() {
        expect:
        InfobloxProvider.parseNetworkFilter('') == [:]
        InfobloxProvider.parseNetworkFilter(null) == [:]
        InfobloxProvider.parseNetworkFilter('foo=bar') == ["foo":"bar"]
        InfobloxProvider.parseNetworkFilter('foo=bar&fizz=buzz') == ["foo":"bar", "fizz": "buzz"]
    }

    void "cleanServiceUrl"() {
        expect:
        InfobloxProvider.cleanServiceUrl(null) == null
        InfobloxProvider.cleanServiceUrl('https://foo.com/foo.bar') == 'https://foo.com'
        InfobloxProvider.cleanServiceUrl('http://a.b/b/c.html') == 'http://a.b'
        InfobloxProvider.cleanServiceUrl('gopher://foo.com/foo.bar') == 'gopher://foo.com'
        InfobloxProvider.cleanServiceUrl('http://example-of-a-really-long-domain.a/foo.bar') == 'http://example-of-a-really-long-domain.a'
    }

    void "getServicePath"() {
        expect:
        InfobloxProvider.getServicePath(null) == '/'
        InfobloxProvider.getServicePath('https://foo.com/foo.bar') == '/foo.bar/'
        InfobloxProvider.getServicePath('http://a.b/b/c.html') == '/b/c.html/'
        InfobloxProvider.getServicePath('gopher://foo.com/foo.bar') == '/foo.bar/'
        InfobloxProvider.getServicePath('http://example-of-a-really-long-domain.a/foo.bar') == '/foo.bar/'
    }
}
