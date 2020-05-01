package com.morpheusdata.model;

import java.util.Map;

public class TaskConfig {
	// type config
	public Long instanceId;
	public Long containerId;
	public Long serverId;
	public String instanceName;
	public String serverName;
	public String containerName;
	public String internalIp;
	public String externalIp;

	public Map customOptions;
	public Long userId;
	public String userDisplayName;
	public String username;
//	public String cypher;
//	public String username;

	public TaskConfig (Map map) {
		instanceId = (Long) map.get("instanceId");
		containerId = (Long) map.get("containerId");
		serverId = (Long) map.get("serverId");
		instanceName = (String) map.get("instanceId");
		serverName = (String) map.get("serverName");
		containerName = (String) map.get("containerName");
		internalIp = (String) map.get("internalIp");
		externalIp = (String) map.get("externalIp");
		userId = (Long) map.get("userId");
		userDisplayName = (String) map.get("userDisplayName");
		username = (String) map.get("username");
		customOptions = (Map) map.get("customOptions");
	}
}
