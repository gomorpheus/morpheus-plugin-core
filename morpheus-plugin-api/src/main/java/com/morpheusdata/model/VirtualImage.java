package com.morpheusdata.model;

import com.morpheusdata.model.projection.VirtualImageIdentityProjection;

import java.util.List;

/**
 * Describes a pre-built system image. The {@link com.morpheusdata.core.CloudProvider} can be configured to sync
 * existing images between your cloud provider and Morpheus.
 */
public class VirtualImage extends VirtualImageIdentityProjection {
	public String code;
	public String description;
	public ImageType imageType;
	public String uniqueId;
	public String category;
	public Boolean isPublic;
	public String platform;
	public Long minDisk;
	public List<String> locations;

}
