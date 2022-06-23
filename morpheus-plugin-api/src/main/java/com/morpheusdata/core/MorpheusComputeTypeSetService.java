package com.morpheusdata.core;

import com.morpheusdata.model.ComputeTypeSet;
import io.reactivex.Single;

/**
 * Context methods for syncing {@link ComputeTypeSet} in Morpheus
 */
public interface MorpheusComputeTypeSetService {

	/**
	 * Get a {@link ComputeTypeSet} by id.
	 * @param id ComputeTypeSet id
	 * @return Single ComputeTypeSet
	 */
	Single<ComputeTypeSet> get(Long id);
}
