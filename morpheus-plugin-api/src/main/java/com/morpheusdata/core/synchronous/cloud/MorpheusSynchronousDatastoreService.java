package com.morpheusdata.core.synchronous.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.Datastore;
import com.morpheusdata.model.projection.DatastoreIdentity;

public interface MorpheusSynchronousDatastoreService  extends MorpheusSynchronousDataService<Datastore,DatastoreIdentity>, MorpheusSynchronousIdentityService<DatastoreIdentity> {
}
