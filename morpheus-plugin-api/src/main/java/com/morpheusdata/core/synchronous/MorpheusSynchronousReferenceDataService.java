package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;

public interface MorpheusSynchronousReferenceDataService extends MorpheusSynchronousDataService<ReferenceData, ReferenceDataSyncProjection>, MorpheusSynchronousIdentityService<ReferenceDataSyncProjection> {
}
