package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.Datastore;
import com.morpheusdata.model.projection.CloudRegionIdentity;
import com.morpheusdata.model.projection.DatastoreIdentity;
import com.morpheusdata.model.projection.DatastoreIdentityProjection;

public interface MorpheusSynchronousDatastoreService  extends MorpheusSynchronousDataService<Datastore,DatastoreIdentity>, MorpheusSynchronousIdentityService<DatastoreIdentity> {
}
