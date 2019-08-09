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
    public NetworkPoolIp networkPoolIp;
    public Long serverId;
    public Long containerId;
    public Long instanceId;
    public String status;
    public String statusMessage;
    public NetworkDomain networkDomain;
}
