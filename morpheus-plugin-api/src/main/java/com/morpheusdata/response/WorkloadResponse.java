package com.morpheusdata.response;

import java.util.List;
import com.morpheusdata.model.UserConfiguration;
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
	 * Indicates if the agent should be installed on the server by Morpheus
	 */
	public Boolean installAgent;

	/**
	 * The list of users for Morpheus to create
	 */
	public List<UserConfiguration> createUsers;

	public Object server; // cloud server data

	public Boolean success = false;
	public Boolean customized = false;

	public Boolean licenseApplied = false;

	public Boolean noAgent = false;

	public Long poolId;

	public String message; // error message

	public void setError(String message) {
		this.success = false;
		this.message = message;
	}

}
