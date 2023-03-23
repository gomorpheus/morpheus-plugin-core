package com.morpheusdata.model.provisioning;

import java.util.Map;

import com.morpheusdata.model.Process;
import com.morpheusdata.model.ProxyConfiguration;

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
