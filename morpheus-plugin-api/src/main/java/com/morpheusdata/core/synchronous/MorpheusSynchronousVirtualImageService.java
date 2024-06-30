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

package com.morpheusdata.core.synchronous;

import com.bertramlabs.plugins.karman.CloudFile;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusVirtualImageLocationService;
import com.morpheusdata.core.MorpheusVirtualImageTypeService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.User;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;

public interface MorpheusSynchronousVirtualImageService extends MorpheusSynchronousDataService<VirtualImage, VirtualImageIdentityProjection>, MorpheusSynchronousIdentityService<VirtualImage> {

	/**
	 * The context for dealing with VirtualImageLocations
	 * @return MorpheusVirtualImageLocationService
	 */
	MorpheusSynchronousVirtualImageLocationService getLocation();

	/**
	 * The context for VirtualIMageType
	 */
	MorpheusSynchronousVirtualImageTypeService getType();


	/**
	 * Get a one-off url for an image to upload to it to a cloud
	 * @since 0.15.13
	 * @param virtualImage the image
	 * @param cloudFile the specific file
	 * @param createdBy the user associated with the workload or server
	 * @param cloud the Cloud instance
	 * @return the url of the image file
	 */
	String getCloudFileStreamUrl(VirtualImage virtualImage, CloudFile cloudFile, User createdBy, Cloud cloud);
}
