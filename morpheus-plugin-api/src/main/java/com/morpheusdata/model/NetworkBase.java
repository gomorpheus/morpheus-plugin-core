package com.morpheusdata.model;

import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Represents the base class of all Network Attachable classes. This is the representation of different
 * network objects that can be assigned to an instance at provisioning. (.i.e. {@link Network}, {@link NetworkGroup}, or {@link NetworkSubnet} ).
 *
 * @see Network
 * @see NetworkGroup
 * @see NetworkSubnet
 *
 * @author David Estes
 * @since 0.15.1
 */
public class NetworkBase extends MorpheusIdentityModel {

}
