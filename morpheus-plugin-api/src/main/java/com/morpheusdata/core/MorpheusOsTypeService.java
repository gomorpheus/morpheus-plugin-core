package com.morpheusdata.core;

import com.morpheusdata.model.OsType;
import io.reactivex.rxjava3.core.Observable;

/**
 * @since 0.10.0
 * @author Bob Whiton
 *
 */
public interface MorpheusOsTypeService extends MorpheusDataService<OsType, OsType> {

	/**
	 * Get a list of all the OsType objects
	 * @return Observable stream of OsType
	 */
	@Deprecated(since="0.15.4")
	Observable<OsType> listAll();
}
