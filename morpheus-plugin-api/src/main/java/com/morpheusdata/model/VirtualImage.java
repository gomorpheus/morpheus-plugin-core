package com.morpheusdata.model;

import java.util.List;

/**
 * Describes a pre-built system image. The {@link com.morpheusdata.core.CloudProvider} can be configured to sync
 * existing images between your cloud provider and Morpheus.
 */
public class VirtualImage extends MorpheusModel {
	public String code;
	public String name;
	public String description;
	public ImageType imageType;
	public String externalId;
	public String uniqueId;
	public String category;
	public Boolean isPublic;
	public String platform;
	public Long minDisk;
	public List<String> locations;

}
