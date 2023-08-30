package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudFolderService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ComputeZoneFolder} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudFolderService
 * @author Bob Whiton
 * @deprecated replaced by {@link CloudFolderIdentity} since 0.15.3
 */
@Deprecated(since="0.15.3", forRemoval=false)
public class ComputeZoneFolderIdentityProjection extends CloudFolderIdentity {

}
