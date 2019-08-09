package com.morpheusdata

import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolServer

class MorpheusNetworkContextImpl implements MorpheusNetworkContext {
    @Override
    void updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message) {

    }

    @Override
    void removePoolIp(NetworkPoolIp poolServer) {

    }

    @Override
    NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord) {
        return null
    }

    @Override
    NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp) {
        return null
    }
}
