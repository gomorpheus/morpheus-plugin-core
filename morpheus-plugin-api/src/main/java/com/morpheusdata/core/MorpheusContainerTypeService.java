package com.morpheusdata.core;

import com.morpheusdata.model.ContainerType;
import io.reactivex.Single;

/**
 * Context methods for syncing {@link ContainerType} in Morpheus
 */
public interface MorpheusContainerTypeService {

	/**
	 * Get a {@link ContainerType} by id.
	 * @param id ContainerType id
	 * @return Single ContainerType
	 */
	Single<ContainerType> get(Long id);
}
