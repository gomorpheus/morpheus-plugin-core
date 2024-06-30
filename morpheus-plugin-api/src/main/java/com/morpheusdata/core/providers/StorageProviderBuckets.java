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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * This Provider interface is used in combination with {@link StorageProvider} to define a
 * {@link com.morpheusdata.model.StorageServerType} that can create and delete object storage
 * buckets.
 *
 * @since 0.15.1
 * @see StorageProvider
 */
public interface StorageProviderBuckets {

	/**
	 * Validates the submitted information when saving a bucket.
	 * This is invoked before both {@link #createBucket} and {@link #updateBucket}.
	 * @param storageBucket Storage Bucket information
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. The errors field of the ServiceResponse is used to send validation
	 * results back to the interface in the format of {@code errors['fieldName'] = 'validation message' }. The msg
	 * property can be used to send generic validation text that is not related to a specific field on the model.
	 * A ServiceResponse with a success value of 'false' will halt the create/update process.
	 */
	ServiceResponse validateBucket(StorageBucket storageBucket, Map opts);
	
	/**
	 * Create the {@link StorageBucket} resources on the external provider system.
	 * @param storageBucket the fully configured and validated bucket to be created
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * creation on the external system failed and will halt any further bucket creation processes in morpheus.
	 */
	ServiceResponse createBucket(StorageBucket storageBucket, Map opts);

	/**
	 * Called during update of an existing {@link StorageBucket}. This allows for any custom operations that need
	 * to be performed outside of the standard operations.
	 * @param storageBucket the storage bucket to be updated
	 * @param opts additional options
	 * @return A response is returned depending on if the operation was a success or not.
	 */
	ServiceResponse updateBucket(StorageBucket storageBucket, Map opts);

	/**
	 * Delete the {@link StorageBucket} resources on the external provider system.
	 * @param storageBucket the storage bucket details
	 * @param opts additional options
	 * @return a {@link ServiceResponse} indicating the results of the deletion on the external provider system.
	 * A ServiceResponse object with a success value of 'false' will halt the deletion process and the local refernce
	 * will be retained.
	 */
	ServiceResponse deleteBucket(StorageBucket storageBucket, Map opts);

	/**
	 * Provides a list of supported storage provider types.
	 * The available storage provider types are: ['alibaba','azure','cifs','google',local','nfs','openstack','s3']
	 * @return Collection of provide type codes
	 */
	Collection<String> getStorageBucketProviderTypes();
}
