package com.morpheusdata.core;

import com.morpheusdata.model.NetworkDomainRecord;
import com.morpheusdata.model.NetworkPoolIp;
import com.morpheusdata.model.NetworkPoolServer;

public interface MorpheusNetworkContext {

	void updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message);

	void removePoolIp(NetworkPoolIp poolServer);

    NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord);

	NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp);
}
