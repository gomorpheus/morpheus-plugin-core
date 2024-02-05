package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.ExecuteSchedule;
import com.morpheusdata.model.SettingType;

public interface MorpheusSynchronousExecuteScheduleService extends MorpheusSynchronousDataService<ExecuteSchedule, ExecuteSchedule> {
	/**
	 * Returns the SettingType context used for performing updates or queries on {@link SettingType} related assets within Morpheus.
	 * @return An instance of the SettingType Context
	 */
	MorpheusSynchronousExecuteScheduleTypeService getType();
}
