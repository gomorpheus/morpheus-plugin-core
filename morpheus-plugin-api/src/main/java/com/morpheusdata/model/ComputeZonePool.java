package com.morpheusdata.model;

/**
 * This model represents logical groupings/separations within a cloud for virtualization management
 * for example Vmware Clusters/Resource Pools, or AWS VPCs, Azure Resource Groups, OpenStack Projects
 * @author Eric Helgeson
 * @since 0.8.0
 * @deprecated replaced by {@link CloudPool} for better naming in 0.15.3
 */
@Deprecated(since="0.15.3",forRemoval = false)
public class ComputeZonePool extends CloudPool {

}
