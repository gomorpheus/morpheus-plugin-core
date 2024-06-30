/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

	/**
	 * Returns the Instance Threshold Service
	 * @return An instance of the Instance Threshold Service
	 */
	MorpheusInstanceThresholdService getThreshold();
}
