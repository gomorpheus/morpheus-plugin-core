package com.morpheusdata.core;

import com.morpheusdata.model.Setting;
import com.morpheusdata.model.SettingType;
import com.morpheusdata.model.NetworkProxy;
import io.reactivex.Maybe;

public interface MorpheusSettingService extends MorpheusDataService<Setting, Setting>, MorpheusIdentityService<Setting> {

	/**
	 * Returns the SettingType context used for performing updates or queries on {@link SettingType} related settings within Morpheus.
	 * @return An instance of the SettingType Context
	 */
	MorpheusSettingTypeService getType();

	/**
	 * Returns the Global NetworkProxy if exists else null
	 * @return An instance of the NetworkProxy
	 */
	Maybe<NetworkProxy> getGlobalNetworkProxy();
}
