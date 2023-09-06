package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudPoolService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ComputeZonePool} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudPoolService
 * @author Mike Truso
 * @since 0.8.0
 * @deprecated replaced by {@link CloudPoolIdentity}
 */
@Deprecated(since="0.15.3", forRemoval=false)
public class ComputeZonePoolIdentityProjection extends CloudPoolIdentity {

}
