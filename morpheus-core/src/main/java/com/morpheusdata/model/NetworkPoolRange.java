package com.morpheusdata.model;

import java.util.Date;
import java.util.List;

public class NetworkPoolRange extends MorpheusModel {
	public String startAddress;
	public String endAddress;
	public String internalId;
	public String externalId;
	public String description;
	public Integer addressCount = 0;
	public Boolean active = true;
	public Date dateCreated;
	public Date lastUpdated;
	public NetworkPool networkPool;

	// transients
	public List<NetworkPoolIp> ips;
	public Integer reservationCount;
}
