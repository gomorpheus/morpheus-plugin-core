package com.morpheusdata.response;

import java.util.List;
import com.morpheusdata.model.UserConfiguration;

/**
 * Results of running a {@link com.morpheusdata.model.Workload}
 */
public class WorkloadResponse {
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

}
