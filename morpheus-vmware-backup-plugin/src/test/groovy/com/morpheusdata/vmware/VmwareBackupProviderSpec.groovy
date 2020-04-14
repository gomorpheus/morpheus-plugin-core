package com.morpheusdata.vmware

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.response.ServiceResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class VmwareBackupProviderSpec extends Specification {
    @Shared MorpheusContext context
    @Shared VmwareBackupPlugin plugin
    @Shared MorpheusNetworkContext networkContext
    @Subject @Shared VmwareBackupProvider provider

    void setup() {
        context = Mock(MorpheusContextImpl)
        networkContext = Mock(MorpheusNetworkContext)
        context.getNetwork() >> networkContext
        plugin = Mock(VmwareBackupPlugin)
        provider = new VmwareBackupProvider(plugin, context)
    }

    void "Provider valid"() {
        expect:
        provider
    }
}
