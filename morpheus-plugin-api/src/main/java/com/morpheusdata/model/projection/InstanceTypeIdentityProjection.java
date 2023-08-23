package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.InstanceType} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see com.morpheusdata.core.library.MorpheusLibraryServices
 * @author Dustin DeYoung
 * @since 0.15.3
 */
public class InstanceTypeIdentityProjection extends MorpheusIdentityModel {
	protected String name;
}
