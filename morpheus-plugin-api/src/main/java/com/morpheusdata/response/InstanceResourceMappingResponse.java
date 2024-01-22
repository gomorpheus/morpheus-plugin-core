package com.morpheusdata.response;

/**
 * Results of running resolving the Iac resource mapping for a {@link com.morpheusdata.model.Workload}
 * @since 0.15.10
 */
public class InstanceResourceMappingResponse {

	/**
	 * Indicates if the lookup and mapping was successful
	 */
	public Boolean success = false;

	/**
	 * The public ip address of the created server
	 */
	public String publicIp;

	/**
	 * The private ip address of the created server
	 */
	public String privateIp;

	/**
	 * Indicates if the agent should be installed on the server by Morpheus. Setting this to false does not
	 * necessarily mean that the agent will not be installed as it may be installed via cloudinit.
	 */
	public Boolean installAgent;

	/**
	 * Agent will not be installed, don't wait for it
	 */
	public Boolean noAgent = false;

	/**
	 * Morpheus id of the server to update
	 */
	public Long serverId = null;

	/**
	 * When an error occurs, set the error message here
	 */
	public String message;

	public void setError(String message) {
		this.success = false;
		this.message = message;
	}

}
