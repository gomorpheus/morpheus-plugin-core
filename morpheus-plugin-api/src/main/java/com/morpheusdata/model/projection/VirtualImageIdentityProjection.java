package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.VirtualImage} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see com.morpheusdata.core.MorpheusVirtualImageContext
 * @author Mike Truso
 */
public class VirtualImageIdentityProjection extends MorpheusModel {
	public String externalId;
	public String name;
}
