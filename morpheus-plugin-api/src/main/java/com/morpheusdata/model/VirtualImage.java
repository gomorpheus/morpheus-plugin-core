package com.morpheusdata.model;

import java.util.List;

public class VirtualImage extends MorpheusModel {
	public String code;
	public String name;
	public String description;
	public String imageType;
	public String externalId;
	public String uniqueId;
	public String category;
	public Boolean isPublic;
	public String platform;
	public Long minDisk;
	public List<String> locations;

}
