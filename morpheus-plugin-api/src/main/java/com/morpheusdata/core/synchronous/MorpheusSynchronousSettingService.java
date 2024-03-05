package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.NetworkProxy;
import com.morpheusdata.model.SettingType;
import com.morpheusdata.model.Setting;

public interface MorpheusSynchronousSettingService extends MorpheusSynchronousDataService<Setting, Setting>, MorpheusSynchronousIdentityService<Setting> {

	/**
	 * Returns the SettingType context used for performing updates or queries on {@link SettingType} related assets within Morpheus.
	 * @return An instance of the SettingType Context
	 */
	MorpheusSynchronousSettingTypeService getType();

	/**
	 * Returns the Global NetworkProxy if exists else null
	 * @return An instance of the NetworkProxy
	 */
	NetworkProxy getGlobalNetworkProxy();
}
