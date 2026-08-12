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

package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.VirtualSwitch;
import com.morpheusdata.model.projection.VirtualSwitchIdentityProjection;

/**
 * Synchronous service interface for interacting with {@link VirtualSwitch} objects in a blocking fashion.
 * Provides standard CRUD and sync identity projection listing.
 *
 * @since 1.5.0
 */
public interface MorpheusSynchronousVirtualSwitchService extends MorpheusSynchronousDataService<VirtualSwitch, VirtualSwitchIdentityProjection>, MorpheusSynchronousIdentityService<VirtualSwitchIdentityProjection> {

	/**
	 * Returns the {@link MorpheusSynchronousVirtualSwitchSegmentService} for managing traffic segments.
	 * @return An instance of the Segment service
	 * @since 1.5.0
	 */
	MorpheusSynchronousVirtualSwitchSegmentService getSegment();

	/**
	 * Returns the {@link MorpheusSynchronousVirtualSwitchUplinkService} for managing per-host uplinks.
	 * @return An instance of the Uplink service
	 * @since 1.5.0
	 */
	MorpheusSynchronousVirtualSwitchUplinkService getUplink();
}
