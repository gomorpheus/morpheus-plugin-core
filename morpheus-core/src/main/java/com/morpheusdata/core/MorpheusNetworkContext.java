package com.morpheusdata.core;

import com.morpheusdata.model.NetworkPoolServer;

public interface MorpheusNetworkContext {

	void updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message);
}
