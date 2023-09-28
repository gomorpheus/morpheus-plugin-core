package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkDomainRecordService;
import com.morpheusdata.model.NetworkDomain;
import com.morpheusdata.model.NetworkDomainRecord;

public interface MorpheusSynchronousNetworkDomainService extends MorpheusSynchronousDataService<NetworkDomain, NetworkDomain> {

	/**
	 * Returns the context for interacting with {@link NetworkDomainRecord} objects
	 * @return the domain record context for DNS Sync and management
	 */
	MorpheusSynchronousNetworkDomainRecordService getRecord();

}
