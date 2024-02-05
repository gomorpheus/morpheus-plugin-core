package com.morpheusdata.core;

import com.morpheusdata.model.ExecuteSchedule;

/**
 * Context methods for dealing with {@link } in Morpheus
 */
public interface MorpheusExecuteScheduleService extends MorpheusDataService<ExecuteSchedule, ExecuteSchedule> {

	/**
	 * Returns the Instance Scale Type Service
	 *
	 * @return An instance of the Instance Scale Type Service
	 */
	MorpheusExecuteScheduleTypeService getType();
}
