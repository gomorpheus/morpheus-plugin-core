package com.morpheusdata.model.projection;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ReferenceData} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Eric Helgeson
 * @since 0.8.0
 */
public class ReferenceDataSyncProjection {
	public Long id;
	public String externalId;
	public String name;
}
