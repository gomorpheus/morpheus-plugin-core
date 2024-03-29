package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.OperationNotification;
import com.morpheusdata.model.projection.OperationNotificationIdentityProjection;

public interface MorpheusSynchronousOperationNotificationService extends MorpheusSynchronousDataService<OperationNotification, OperationNotificationIdentityProjection>, MorpheusSynchronousIdentityService<OperationNotificationIdentityProjection> {
}
