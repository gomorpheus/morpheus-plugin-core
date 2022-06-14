package com.morpheusdata.model.provisioning;

import com.morpheusdata.model.NetworkConfiguration;
import com.morpheusdata.model.ProxyConfiguration;
import com.morpheusdata.model.Process;

import java.util.Map;

public class WorkloadRequest {

	public Process process;
	public NetworkConfiguration networkConfiguration;
	public ProxyConfiguration proxyConfiguration;
	public UsersConfiguration usersConfiguration;

	public Map cloudConfigOpts;
	public String cloudConfigUser;
	public String cloudConfigMeta;
	public String cloudConfigNetwork;

}
