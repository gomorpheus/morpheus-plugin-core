package com.morpheusdata.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Light-weight representation of network configuration for provisioning
 *
 * @author Bob Whiton
 * @since 0.9.0
 */
public class NetworkConfiguration {

	public Map primaryInterface;
	public List<Map> extraInterfaces;
	public Boolean doStatic;
	public Boolean doDhcp;
	public Boolean doCloudInit;
	public Boolean doCustomizations;
	public Boolean havePool;
	public Boolean haveDhcpReservation;
	public Map networkDomain;
}
