package com.morpheusdata.response;

import com.morpheusdata.model.provisioning.UserConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Results of running a {@link com.morpheusdata.model.App}
 */
public class AppProvisionResponse {

	/**
	 * Indicates if the provision was successful
	 */
	public Boolean success = false;
	/**
	 * A list of instance ids to finalize
	 */
	public List<Long> instanceFinalizeList;

	/**
	 * A map of instance ids that failed to provision. Each entry is an instance id (key) and a failure message (value)
	 */
	public Map<Long, String> failedInstances;

	/**
	 * Indicates if the agent should be installed on the server by Morpheus. Setting this to false does not
	 * necessarily mean that the agent will not be installed as it may be installed via cloudinit.
	 */
	public Boolean installAgent;

	/**
	 * Agent will not be installed in any way.. don't wait for it
	 */
	public Boolean noAgent = false;



	/**
	 * Set to the hostname of the ComputeServer (optional)
	 */
	public String hostname;

	/**
	 * When an error occurs, set the error message here
	 */
	public String message;


	public void setError(String message) {
		this.success = false;
		this.message = message;
	}

}
