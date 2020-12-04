package com.morpheusdata.model;

import java.util.Map;

public class Cloud extends MorpheusModel {
	public Account account;
	public String name;
	public String code;
	public String description;
	public CloudType cloudType;
	public Map configMap;
}
