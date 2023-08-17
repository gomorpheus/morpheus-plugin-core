package com.morpheusdata.core.providers;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Snapshot;
import com.morpheusdata.model.provisioning.HostRequest;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.HostResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides methods for interacting with provisioned vms through a hypervisor console
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface HypervisorConsoleProvisioningProvider extends ProvisioningProvider {

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for an xvpVnc console connection to the server
	 */
	default ServiceResponse getXvpVNCConsoleUrl(ComputeServer server) {
		return null;
	}

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for a noVNC console connection to the server
	 */
	default ServiceResponse getNoVNCConsoleUrl(ComputeServer server){
		return null;
	}

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for a wmks console connection to the server
	 */
	default ServiceResponse getWMKSConsoleUrl(ComputeServer server){
		return null;
	}

	/**
	 * Method called before using the console host to ensure it is accurate
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Success or failure
	 */
	default ServiceResponse updateServerHost(ComputeServer server){
		return null;
	}

	/**
	 * Method called before making a hypervisor vnc console connection to a server to ensure that the server settings are correct
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Success or failure
	 */
	default ServiceResponse enableConsoleAccess(ComputeServer server){
		return null;
	}

}
