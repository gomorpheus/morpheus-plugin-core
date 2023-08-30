package com.morpheusdata.model;
/**
 * A model representing a region in a cloud. This can be useful for syncing a list of dynamic region sets and also for
 * iterating sync across multiple regions
 *
 * @author David Estes
 * @since 0.14.0
 * @see com.morpheusdata.model.projection.ComputeZoneRegionIdentityProjection
 * @deprecated this has been replaced by {@link CloudRegion}
 */
@Deprecated(since="0.15.3",forRemoval=false)
public class ComputeZoneRegion extends CloudRegion {

}
