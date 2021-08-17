package com.morpheusdata.core;

import com.morpheusdata.model.OsType;
import io.reactivex.Observable;

/**
 * @since 0.10.0
 * @author Bob Whiton
 *
 */
public interface MorpheusOsTypeService {

	/**
	 * Get a list of all the OsType objects
	 * @return Observable stream of OsType
	 */
	Observable<OsType> listAll();
}
