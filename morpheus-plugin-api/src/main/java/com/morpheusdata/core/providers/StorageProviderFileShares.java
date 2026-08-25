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
 * {@link com.morpheusdata.model.StorageServerType} that can create and delete file share storage
 * buckets. These could be like Samba file shares or NFS file shares.
 *
 * @since 0.15.1
 * @see StorageProvider
 */
public interface StorageProviderFileShares {

	/**
	 * Validates the submitted information when saving a file share.
	 * This is invoked before both {@link #createFileShare} and {@link #updateFileShare}.
	 * @param storageShare Storage Bucket (file share) information
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. The errors field of the ServiceResponse is used to send validation
	 * results back to the interface in the format of {@code errors['fieldName'] = 'validation message' }. The msg
	 * property can be used to send generic validation text that is not related to a specific field on the model.
	 * A ServiceResponse with a success value of 'false' will halt the create/update process.
	 * Defaults to a no-op success response so existing implementations do not need to override it.
	 */
	default ServiceResponse validateFileShare(StorageBucket storageShare, Map opts) {
		return ServiceResponse.success();
	}

	ServiceResponse createFileShare(StorageBucket storageShare, Map opts);

	ServiceResponse updateFileShare(StorageBucket storageShare, Map opts);

	ServiceResponse deleteFileShare(StorageBucket storageShare, Map opts);

	Collection<String> getFileShareProviderTypes();
}
