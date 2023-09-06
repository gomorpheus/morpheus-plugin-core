package com.morpheusdata.model;

import com.morpheusdata.model.projection.ComputeZoneFolderIdentityProjection;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * Represents folders like in Vmware Folders
 * @author David Estes
 * @deprecated replaced by {@link CloudFolder} since 0.15.3
 */
@Deprecated(since="0.15.3", forRemoval=false)
public class ComputeZoneFolder extends CloudFolder {

}
