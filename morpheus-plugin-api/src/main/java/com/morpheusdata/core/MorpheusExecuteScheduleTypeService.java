package com.morpheusdata.core;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.ExecuteScheduleType;
import io.reactivex.rxjava3.core.Single;
/**
 * Context methods for dealing with {@link ExecuteScheduleType} in Morpheus
 */
public interface MorpheusExecuteScheduleTypeService extends MorpheusDataService<ExecuteScheduleType, ExecuteScheduleType> {


	/**
	 * Returns the next fire date for a given schedule type
	 * @param executeScheduleType schedule type to calculate next fire date for
	 * @return next fire date
	 */
	Single<java.util.Date> calculateNextFire(ExecuteScheduleType executeScheduleType);
}
