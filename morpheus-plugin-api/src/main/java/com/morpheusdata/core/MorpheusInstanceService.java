package com.morpheusdata.core;

import com.morpheusdata.model.Instance;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for dealing with {@link Instance} in Morpheus
 */
public interface MorpheusInstanceService {

	/**
	 * Get a list of Instance objects from a list of ids
	 * @param ids Instance ids
	 * @return Observable stream of Instance
	 */
	Observable<Instance> listById(Collection<Long> ids);

	/**
	 * Save updates to existing Instance
	 * @param instances updated Instances
	 * @return success
	 */
	Single<Boolean> save(List<Instance> instances);
}
