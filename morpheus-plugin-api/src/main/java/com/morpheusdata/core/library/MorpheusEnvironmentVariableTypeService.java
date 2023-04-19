package com.morpheusdata.core.library;

import com.morpheusdata.model.EnvironmentVariableType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link EnvironmentVariableType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusEnvironmentVariableTypeService {

	/**
	 * Get a {@link EnvironmentVariableType} by id.
	 * @param id environment variable type id
	 * @return Observable stream of sync projection
	 */
	Single<EnvironmentVariableType> get(Long id);

	/**
	 * Get a list of EnvironmentVariableType objects from a list of projection ids
	 *
	 * @param ids EnvironmentVariableType ids
	 * @return Observable stream of EnvironmentVariableTypes
	 */
	Observable<EnvironmentVariableType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing EnvironmentVariableTypes
	 * @param environmentVariableTypes updated EnvironmentVariableType
	 * @return success
	 */
	Single<Boolean> save(List<EnvironmentVariableType> environmentVariableTypes);

	/**
	 * Create new EnvironmentVariableTypes in Morpheus
	 * @param environmentVariableTypes new EnvironmentVariableTypes to persist
	 * @return success
	 */
	Single<Boolean> create(List<EnvironmentVariableType> environmentVariableTypes);

	/**
	 * Remove persisted EnvironmentVariableType from Morpheus
	 * @param environmentVariableTypes to delete
	 * @return success
	 */
	Single<Boolean> remove(List<EnvironmentVariableType> environmentVariableTypes);

}
