package com.morpheusdata.model;

public class NetworkDomainRecord extends MorpheusModel {
    public String name;
    public String fqdn;
    public String type = "A";
    public String comments;
    public Integer ttl;
    public String recordData;
    public String content;
    public String refType;
    public Long refId;
    public String internalId;
    public String externalId;
    public String source;
    public Long serverId;
    public Long containerId;
    public Long instanceId;
    public String status;
    public String statusMessage;
    public NetworkPoolIp networkPoolIp;
    public NetworkDomain networkDomain;

    public void setNetworkPoolIpId(Long id) {
    	this.networkPoolIp = new NetworkPoolIp();
    	this.networkPoolIp.id = id;
	}

	public void setNetworkDomainId(Long id) {
		this.networkDomain = new NetworkDomain();
		this.networkDomain.id = id;
	}
}
