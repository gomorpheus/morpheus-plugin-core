package com.morpheusdata.infoblox

import com.morpheusdata.test.MorpheusContextImpl
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.network.MorpheusNetworkService
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.response.ServiceResponse
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class InfobloxProviderSpec extends Specification {
    @Shared MorpheusContext context
    @Shared InfobloxPlugin plugin
    @Shared RestApiUtil infobloxAPI
    @Shared MorpheusNetworkService networkContext
    @Subject@Shared InfobloxProvider provider

    void setup() {
        context = Mock(MorpheusContextImpl)
        networkContext = Mock(MorpheusNetworkService)
        context.getNetwork() >> networkContext
        plugin = Mock(InfobloxPlugin)
        infobloxAPI = GroovySpy(RestApiUtil, global: true)
		provider = new InfobloxProvider(plugin, context, infobloxAPI)
    }

    void "Provider valid"() {
        expect:
        provider
    }

    void "listNetworks"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"foo": 1}')

        when:
        def response = provider.listNetworks(poolServer, [doPaging: false])

        then:
        response
    }

    void "listNetworks - do paging"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1", "2"]}')

        when:
        def response = provider.listNetworks(poolServer, [doPaging:true])// Pagination object?

        then:
        response.success
        response.results.size() == 2
    }

    void "listZones"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1", "2"]}')

        when:
        def response = provider.listZones(poolServer, [:])

        then:
        response.success
        response.results.size() == 2
    }

    void "listZones - doPaging false"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1", "2"]}')

        when:
        def response = provider.listZones(poolServer, [doPaging: false])

        then:
        response.success
        response.results.size() == 2
    }

    void "testNetworkPoolServer"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1"]}')

        when:
        def response = provider.testNetworkPoolServer(poolServer)

        then:
        response.success
    }

    void "getItem"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1"]}')

        when:
        def response = provider.getItem(poolServer, '/path', [:])

        then:
        response.success
    }

    void "releaseIpAddress"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        def ipAddress = new NetworkPoolIp(externalId: "123", internalId: "internalId", ptrId: "ptrId")

        when:
        def result = provider.releaseIpAddress(poolServer, ipAddress, [:])

        then:
        result.success
        3 * infobloxAPI.callApi(_, _, _, _, _, 'DELETE') >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1"]}')
    }

	@Ignore("network context is null, TODO Fix")
    void "createHostRecord"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        def ipAddress = new NetworkPoolIp(hostname: "hostname", ipAddress: "192.168.1.1")
        def domain = new NetworkDomain(name:"name")
        def networkPool = new NetworkPool(name: "pool name")
        and:
        infobloxAPI.callApi(_, _, _, _, _, 'POST') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'{"result": ["1"] ]}')
        infobloxAPI.callApi(_, _, _, _, _, 'GET') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'{"ipv4addrs": [{"ipv4addr": "192.168.1.1"}]}')


        when:
        def result = provider.createHostRecord(poolServer, networkPool, ipAddress, domain, true, true)

        then:
        result
    }

    void "reserveNextIpAddress"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        def networkPool = new NetworkPool(name: "pool name")
        def hostname = 'foobar'
        and:
        infobloxAPI.callApi(_, _, _, _, _, 'GET') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'{"result": ["1"]}')

        and: "Odd response from API that is surrounded in quotes."
        infobloxAPI.callApi(_, _, _, _, _, 'POST') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'"ippath"')
        when:
        def result = provider.reserveNextIpAddress(poolServer, networkPool, hostname, [:])

        then:
        result.success
    }

    // TODO: I'm not quite sure of the actual behavior this should exhibit.
    void "generateExtraAttributes"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost", configMap: [extraAttributes: '{"foo": "bar"}'])

        when:
        def result = provider.generateExtraAttributes(poolServer, [fizz: "buzz"])

        then:
        result.foo.value == "bar"
    }

    void "getNextIpAddress"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost", configMap: [extraAttributes: '{"foo": "bar"}'])
        def networkPool = new NetworkPool(name: "pool name")
        def hostname = 'hostname'

        and:"mock getItem"
        infobloxAPI.callApi(_, _, _, _, _, 'GET') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'"foo"')
        and:
        infobloxAPI.callApi(_, _, _, _, _, 'POST') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'"foo"')
        infobloxAPI.callApi(_, _, _, _, _, 'POST') >> new ServiceResponse(
                success: true,
                errors: null ,
                content:'{"ips": ["1.1.1.1"]}')

        when:
        def result = provider.getNextIpAddress(poolServer, networkPool, hostname, [:])

        then:
        result.success
    }

}
