package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.MorpheusModel;
import io.reactivex.Observable;
import java.util.Collection;

/**
 * Provides an interface usually used in conjunction with {@link MorpheusDataService} to list
 * identity projection models which are simplified models that contain just a few properties for efficient matching
 * in a sync scenario.
 * @param <I> the Identity Projection class to be retrieved for sync (i.e. {@link com.morpheusdata.model.projection.NetworkIdentityProjection})
 *
 * @author David Estes
 * @see MorpheusDataService
 */
public interface MorpheusIdentityService<I extends MorpheusModel> {

	Observable<I> listIdentityProjections(DataQuery query);

	Collection<String> getIdentityProperties();
  
}
