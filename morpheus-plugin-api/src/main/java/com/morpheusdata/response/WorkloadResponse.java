package com.morpheusdata.response;

import java.util.List;
import com.morpheusdata.model.provisioning.UserConfiguration;

/**
 * Results of running a {@link com.morpheusdata.model.Workload}
 */
public class WorkloadResponse {

	public Boolean unattendCustomized = false;

	/**
	 * The id of the created server
	 */
	public String externalId;

	/**
	 * The public ip address of the created server
	 */
	public String publicIp;

	/**
	 * The private ip address of the created server
	 */
	public String privateIp;
	public String status;

	/**
	 * Indicates if the agent should be installed on the server by Morpheus. Setting this to false does not
	 * necessarily mean that the agent will not be installed as it may be installed via cloudinit.
	 */
	public Boolean installAgent;

	/**
	 * The list of users for Morpheus to create
	 */
	public List<UserConfiguration> createUsers;

	public Object server; // cloud server data

	public Boolean success = false;
	public Boolean customized = false;

	/**
	 * Was the (windows) license applied during customization outside of normal flow. Not Morpheus license.. windows license
	 */
	public Boolean licenseApplied = false;

	/**
	 * Agent will not be installed in any way.. don't wait for it
	 */
	public Boolean noAgent = false;

	// This should be removed
	public Long poolId;

	public String hostname;

	public String message; // error message

	/**
	 * Wait for the network to become active on the ComputeServer before finalizing
	 */
	public Boolean skipNetworkWait = false;

	public void setError(String message) {
		this.success = false;
		this.message = message;
	}

}
