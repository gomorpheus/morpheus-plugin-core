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

package com.morpheusdata.model.event;

import com.morpheusdata.model.Network;

public class NetworkEvent implements Event<NetworkEvent.NetworkEventType> {
	String message;
	NetworkEventType type;
	Network network;

	@Override
	public String getMessage() {
		return "";
	}

	@Override
	public NetworkEventType getType() {
		return type;
	}

	public enum NetworkEventType implements EventType {
		ALL,
		CREATE,
		DELETE,
		UPDATE
	}
}
