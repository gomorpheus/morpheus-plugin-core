package com.morpheusdata.core.providers;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides methods for interacting with provisioned vms related to power and resizing
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface ComputeProvisionProvider extends ProvisionProvider {

	/**
	 * Stop the server
	 * @param computeServer to stop
	 * @return Response from API
	 */
	ServiceResponse stopServer(ComputeServer computeServer);

	/**
	 * Start the server
	 * @param computeServer to start
	 * @return Response from API
	 */
	ServiceResponse startServer(ComputeServer computeServer);


}
