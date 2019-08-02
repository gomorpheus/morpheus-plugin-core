package com.morpheusdata.infoblox

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.model.NetworkPoolServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class InfobloxProviderTest extends Specification {
    @Shared MorpheusContext context
    @Shared InfobloxPlugin plugin
    @Shared InfobloxAPI infobloxAPI
    @Shared MorpheusNetworkContext networkContext
    @Subject@Shared InfobloxProvider provider

    void setup() {
        context = Mock(MorpheusContextImpl)
        networkContext = Mock(MorpheusNetworkContext)
        context.getNetwork() >> networkContext
        plugin = Mock(InfobloxPlugin)
        infobloxAPI = Mock(InfobloxAPI)
        provider = new InfobloxProvider(plugin, context, infobloxAPI)
    }

    void "Provider valid"() {
        expect:
        provider
    }

    void "updateNetworkPoolStatus"() {
        given:
        def poolServer = new NetworkPoolServer(apiPort: 8080, serviceUrl: "http://localhost")
        infobloxAPI.callApi(_, _, _, _, _, _) >> [success: true, errors: false, content: '{"foo": 1}']

        when:
        provider.refresh(poolServer)

        then:
        1 * networkContext.updateNetworkPoolStatus(_, 'syncing', null)
        1 * networkContext.updateNetworkPoolStatus(_, 'ok', null)

    }
}
