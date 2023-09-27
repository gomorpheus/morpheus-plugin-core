package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.EnvironmentVariableType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link EnvironmentVariableType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusEnvironmentVariableTypeService extends MorpheusDataService<EnvironmentVariableType, EnvironmentVariableType> {

	/**
	 * Get a list of EnvironmentVariableType objects from a list of projection ids
	 *
	 * @param ids EnvironmentVariableType ids
	 * @return Observable stream of EnvironmentVariableTypes
	 */
	@Deprecated(since="0.15.4")
	Observable<EnvironmentVariableType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing EnvironmentVariableTypes
	 * @param environmentVariableTypes updated EnvironmentVariableType
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<EnvironmentVariableType> environmentVariableTypes);

	/**
	 * Create new EnvironmentVariableTypes in Morpheus
	 * @param environmentVariableTypes new EnvironmentVariableTypes to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<EnvironmentVariableType> environmentVariableTypes);

	/**
	 * Remove persisted EnvironmentVariableType from Morpheus
	 * @param environmentVariableTypes to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<EnvironmentVariableType> environmentVariableTypes);

}
