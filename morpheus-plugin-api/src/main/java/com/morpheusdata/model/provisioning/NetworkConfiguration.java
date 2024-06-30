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

package com.morpheusdata.model.provisioning;

import java.util.List;
import java.util.Map;

/**
 * Light-weight representation of network configuration for provisioning
 *
 * @author Bob Whiton
 * @since 0.9.0
 */
public class NetworkConfiguration {

	public Map primaryInterface;
	public List<Map> extraInterfaces;
	public Boolean doStatic;
	public Boolean doDhcp;
	public Boolean doCloudInit;
	public Boolean doCustomizations;
	public Boolean havePool;
	public Boolean haveDhcpReservation;
	public Map networkDomain;
}
