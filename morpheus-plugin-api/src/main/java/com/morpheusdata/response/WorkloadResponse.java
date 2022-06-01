package com.morpheusdata.response;

import java.util.List;
import com.morpheusdata.model.provisioning.UserConfiguration;

/**
 * Results of running a {@link com.morpheusdata.model.Workload}
 */
public class WorkloadResponse {

	/**
	 * Set to true when a Windows server was deployed and the image is syspreped or the unattend was customized
	 */
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
	 * The list of users for Morpheus to create on the ComputeServer
	 */
	public List<UserConfiguration> createUsers;
	
	/**
	 * Indicates if the provision was successful
	 */
	public Boolean success = false;

	/**
	 * Set to true when network customizations were performed
	 */
	public Boolean customized = false;

	/**
	 * Was the (windows) license applied during customization outside of normal flow. Not Morpheus license.. windows license
	 */
	public Boolean licenseApplied = false;


	// This should be removed
	public Long poolId;

	/**
	 * Set to the hostname of the ComputeServer (optional)
	 */
	public String hostname;

	/**
	 * When an error occurs, set the error message here
	 */
	public String message;

	/**
	 * Wait for the network to become active on the ComputeServer before finalizing
	 */
	public Boolean skipNetworkWait = false;

	public void setError(String message) {
		this.success = false;
		this.message = message;
	}

}
