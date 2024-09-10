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

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents a {@link StorageServerType} implementation for creating buckets, volumes and file shares.
 * Depending on the capabilities of the referenced storage server one can add implementations for the additional
 * interfaces such as {@link StorageProviderFileShares} or {@link StorageProviderBuckets} or {@link StorageProviderVolumes}.
 * @author David Estes
 * @since 0.15.1
 * @see StorageProviderFileShares
 * @see StorageProviderVolumes
 * @see StorageProviderBuckets
 */
public interface StorageProvider extends PluginProvider {

	/**
	 * Returns the description of the provider type
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the Storage Server Integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provide a {@link StorageServerType} to be added to the morpheus environment
	 * as the type for this {@link StorageServer}. The StorageServerType also defines the
	 * OptionTypes for configuration of a new server and its volume types.
	 * @return StorageServerType
	 */
	StorageServerType getStorageServerType();

	/**
	 * Validation Method used to validate all inputs applied to the integration of a Storage Provider upon save.
	 * If an input fails validation or authentication information cannot be verified, Error messages should be returned
	 * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
	 * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
	 *
	 * @param storageServer The Storage Server object contains all the saved information regarding configuration of the Storage Provider
	 * @param opts an optional map of parameters that could be sent. This may not currently be used and can be assumed blank
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	ServiceResponse verifyStorageServer(StorageServer storageServer, Map opts);

	/**
	 * Called on the first save / update of a storage server integration. Used to do any initialization of a new integration
	 * Often times this calls the periodic refresh method directly.
	 * @param storageServer The Storage Server object contains all the saved information regarding configuration of the Storage Provider.
	 * @param opts an optional map of parameters that could be sent. This may not currently be used and can be assumed blank
	 * @return a ServiceResponse containing the success state of the initialization phase
	 */
	ServiceResponse initializeStorageServer(StorageServer storageServer, Map opts);

	/**
	 * Refresh the provider with the associated data in the external system.
	 * @param storageServer The Storage Server object contains all the saved information regarding configuration of the Storage Provider.
	 * @param opts an optional map of parameters that could be sent. This may not currently be used and can be assumed blank
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * refresh process has failed and will change the storage server status to 'error'
	 */
	ServiceResponse refreshStorageServer(StorageServer storageServer, Map opts);


}
