package com.morpheusdata.model;

import com.morpheusdata.model.projection.NetworkDomainRecordIdentityProjection;

public class NetworkDomainRecord extends NetworkDomainRecordIdentityProjection {
    protected String name;
    protected String fqdn;
    protected String type = "A";
    protected String comments;
    protected Integer ttl;
    protected String recordData;
    protected String content;
    protected String refType;
    protected Long refId;
    protected String internalId;
    protected String source;
    protected Long serverId;
    protected Long containerId;
    protected Long instanceId;
    protected String status;
    protected String statusMessage;
    protected NetworkPoolIp networkPoolIp;
    protected NetworkDomain networkDomain;

    public void setNetworkPoolIpId(Long id) {
    	this.setNetworkPoolIp(new NetworkPoolIp());
    	this.getNetworkPoolIp().id = id;
	}

	public void setNetworkDomainId(Long id) {
		this.setNetworkDomain(new NetworkDomain());
		this.getNetworkDomain().id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getTtl() {
		return ttl;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}

	public String getRecordData() {
		return recordData;
	}

	public void setRecordData(String recordData) {
		this.recordData = recordData;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public NetworkPoolIp getNetworkPoolIp() {
		return networkPoolIp;
	}

	public void setNetworkPoolIp(NetworkPoolIp networkPoolIp) {
		this.networkPoolIp = networkPoolIp;
	}

	public NetworkDomain getNetworkDomain() {
		return networkDomain;
	}

	public void setNetworkDomain(NetworkDomain networkDomain) {
		this.networkDomain = networkDomain;
	}
}
