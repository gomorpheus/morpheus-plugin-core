package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.projection.InstanceIdentityProjection;
import com.morpheusdata.request.DeleteInstanceRequest;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for dealing with {@link Instance} in Morpheus
 */
public interface MorpheusInstanceService extends MorpheusDataService<Instance, InstanceIdentityProjection> {

	/**
	 * Delete the existing Instance from Morpheus and the resources from the underlying Cloud.
	 * Use with caution as resources within the Cloud will be deleted.
	 * @param instance Instance to delete
	 * @param deleteRequest Options for the delete
	 * @return ServiceResponse indicating success or failure. This is an async request within Morpheus and the return
	 * will not capture any underlying errors experienced asynchronously.
	 */
	Single<ServiceResponse> delete(Instance instance, DeleteInstanceRequest deleteRequest);

	Observable<Cloud> getInstanceClouds(Instance instance);

	/**
	 * Returns the Instance Scale Service
	 *
	 * @return An instance of the Instance Scale Service
	 */
	MorpheusInstanceScaleService getScale();
}
