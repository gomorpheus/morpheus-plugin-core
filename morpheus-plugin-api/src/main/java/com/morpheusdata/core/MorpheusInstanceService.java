package com.morpheusdata.core;

import com.morpheusdata.model.Instance;
import com.morpheusdata.request.DeleteInstanceRequest;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for dealing with {@link Instance} in Morpheus
 */
public interface MorpheusInstanceService {

	/**
	 * Get a {@link Instance} by id.
	 * @param id Instance id
	 * @return Observable stream of sync projection
	 */
	Single<Instance> get(Long id);

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

	/**
	 * Delete the existing Instance from Morpheus and the resources from the underlying Cloud.
	 * Use with caution as resources within the Cloud will be deleted.
	 * @param instance Instance to delete
	 * @param deleteRequest Options for the delete
	 * @return ServiceResponse indicating success or failure. This is an async request within Morpheus and the return
	 * will not capture any underlying errors experienced asynchronously.
	 */
	Single<ServiceResponse> delete(Instance instance, DeleteInstanceRequest deleteRequest);

	/**
	 * Returns the Instance Scale Service
	 *
	 * @return An instance of the Instance Scale Service
	 */
	MorpheusInstanceScaleService getScale();
}
