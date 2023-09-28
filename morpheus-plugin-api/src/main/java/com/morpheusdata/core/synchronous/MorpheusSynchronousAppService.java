package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.App;
import com.morpheusdata.model.projection.AppIdentityProjection;

public interface MorpheusSynchronousAppService extends MorpheusSynchronousDataService<App, AppIdentityProjection>, MorpheusSynchronousIdentityService<AppIdentityProjection> {
}
