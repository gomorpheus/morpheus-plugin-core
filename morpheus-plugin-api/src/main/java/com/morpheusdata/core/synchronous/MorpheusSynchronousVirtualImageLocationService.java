package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection;

import java.util.List;

public interface MorpheusSynchronousVirtualImageLocationService extends MorpheusSynchronousDataService<VirtualImageLocation, VirtualImageLocationIdentityProjection>, MorpheusSynchronousIdentityService<VirtualImageLocationIdentityProjection> {

	/**
	 * Get a list of VirtualImageLocation projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link CloudRegion} to optionally filter by
	 * @return Observable stream of sync projection
	 */
	List<VirtualImageLocationIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Save updates to existing VirtualImageLocations
	 * @param virtualImageLocations updated VirtualImageLocations
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Boolean save(List<VirtualImageLocation> virtualImageLocations, Cloud cloud);

	/**
	 * Create new VirtualImageLocations in Morpheus
	 * @param virtualImageLocations new VirtualImageLocations to persist
	 * @param cloud the Cloud instance
	 * @return success
	 */
	Boolean create(List<VirtualImageLocation> virtualImageLocations, Cloud cloud);

	/**
	 * Create a new VirtualImageLocation in Morpheus
	 * @param virtualImageLocation a new VirtualImageLocation to persist
	 * @param cloud the Cloud instance
	 * @return success
	 */
	VirtualImageLocation create(VirtualImageLocation virtualImageLocation, Cloud cloud);

}
