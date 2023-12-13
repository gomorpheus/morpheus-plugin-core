package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.ExecuteScheduleType;

public interface MorpheusSynchronousExecuteScheduleTypeService extends MorpheusSynchronousDataService<ExecuteScheduleType, ExecuteScheduleType>, MorpheusSynchronousIdentityService<ExecuteScheduleType> {

	/** Returns the next fire date for a given schedule type
	 * @param executeScheduleType schedule type to calculate next fire date for
	 * @return next fire date
	 * */
	java.util.Date calculateNextFire(ExecuteScheduleType executeScheduleType);
}
