package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * This provides a way to query a list of regions and sync those regions for particular {@link com.morpheusdata.model.Cloud} objects.
 * Some clouds have dynamic region lists and this provides a mechanism to grab those regions and sync by them
 * @author David Estes
 * @since 0.14.0
 * @see com.morpheusdata.model.ComputeZoneRegion
 * @deprecated replaced by {@link CloudRegionIdentity} in 0.15.3
 */
@Deprecated(since="0.15.3", forRemoval=true)
public class ComputeZoneRegionIdentityProjection  extends CloudRegionIdentity {

}
