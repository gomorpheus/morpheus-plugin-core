package com.morpheusdata.infoblox

import com.morpheusdata.core.util.HttpApiClient
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
    @Shared HttpApiClient infobloxAPI
    @Shared MorpheusNetworkService networkContext
    @Subject@Shared InfobloxProvider provider

    void setup() {
        context = Mock(MorpheusContextImpl)
        networkContext = Mock(MorpheusNetworkService)
        context.getNetwork() >> networkContext
        plugin = Mock(InfobloxPlugin)
        infobloxAPI = GroovySpy(HttpApiClient, global: true)
		provider = new InfobloxProvider(plugin, context)
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
        def response = provider.listNetworks(infobloxAPI,poolServer, [doPaging: false])

        then:
        response
    }

    void "listNetworks - do paging"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1", "2"]}')

        when:
        def response = provider.listNetworks(infobloxAPI,poolServer, [doPaging:true])// Pagination object?

        then:
        response.success
        response.data.size() == 2
    }

    void "listZones"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1", "2"]}')

        when:
        def response = provider.listZones(infobloxAPI,poolServer, [:])

        then:
        response.success
        response.data.size() == 2
    }

    void "listZones - doPaging false"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1", "2"]}')

        when:
        def response = provider.listZones(infobloxAPI,poolServer, [doPaging: false])

        then:
        response.success
        response.data.size() == 2
    }

    void "testNetworkPoolServer"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1"]}')

        when:
        def response = provider.testNetworkPoolServer(infobloxAPI,poolServer)

        then:
        response.success
    }

    void "getItem"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> new ServiceResponse(success: true, errors: null , content:'{"result": ["1"]}')

        when:
        def response = provider.getItem(infobloxAPI,poolServer, '/path', [:])

        then:
        response.success
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


    // TODO: I'm not quite sure of the actual behavior this should exhibit.
    void "generateExtraAttributes"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost", configMap: [extraAttributes: '{"foo": "bar"}'])

        when:
        def result = provider.generateExtraAttributes(poolServer, [fizz: "buzz"])

        then:
        result.foo.value == "bar"
    }


}
