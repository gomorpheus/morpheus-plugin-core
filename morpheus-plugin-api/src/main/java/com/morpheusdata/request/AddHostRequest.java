/*
 *  Copyright 2026 Morpheus Data, LLC.
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

package com.morpheusdata.request;

import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.ComputeServerInterface;
import com.morpheusdata.model.ComputeServerType;
import com.morpheusdata.model.ServicePlan;
import com.morpheusdata.model.StorageVolume;

import java.util.List;
import java.util.Map;

/**
 * Request object for adding a single host to a cloud via
 * {@link com.morpheusdata.core.MorpheusComputeServerService#addHost}.
 *
 * @since 1.4.2
 */
public class AddHostRequest {

	/**
	 * The type of host to provision. Resolved by {@code id} to a persisted
	 * {@link ComputeServerType}; pass a reference carrying at least the id. Required.
	 */
	public ComputeServerType serverType;

	/**
	 * Optional display name for the new host. If not provided, a name is auto-generated.
	 */
	public String serverName;

	/**
	 * Optional hostname for the new host. Defaults to {@link #serverName} if not provided.
	 */
	public String hostname;

	/**
	 * The service plan (sizing) to provision the host with. Passed through by {@code id} as a
	 * reference to a persisted {@link ServicePlan}.
	 */
	public ServicePlan plan;

	/**
	 * Additional provider-specific configuration passed straight through to the provisioner.
	 * May include settings such as {@code customOptions}, or a nested {@code server} map whose
	 * entries are applied directly onto the server config. Free-form; not schema-validated.
	 */
	public Map<String, Object> config;

	/**
	 * Optional id of the group/site to provision the host into. {@code null} lets the cloud
	 * choose its default placement.
	 */
	public Long siteId;

	/**
	 * Storage volumes to provision and attach to the new host.
	 * <p>
	 * These are <em>typed configuration templates</em>, not references to persisted
	 * {@link StorageVolume} rows: only the descriptive fields are read ({@code name},
	 * {@code rootVolume}, {@code maxStorage}, and the {@code type}/{@code datastore} ids) to
	 * describe the volumes the provisioner should create. No existing StorageVolume record is
	 * looked up or required for the volume itself.
	 */
	public List<StorageVolume> volumes;

	/**
	 * Network interfaces to provision on the new host.
	 * <p>
	 * Like {@link #volumes}, these are <em>typed configuration templates</em> rather than persisted
	 * {@link ComputeServerInterface} rows: only the descriptive fields are read ({@code name},
	 * {@code ipMode}, any static {@code addresses}, and the {@code network}/{@code networkGroup}/
	 * {@code networkPool} ids) to describe the NICs the provisioner should create. No existing
	 * ComputeServerInterface record is looked up or required for the interface itself.
	 */
	public List<ComputeServerInterface> networkInterfaces;

	/**
	 * Ids of security groups to apply to the new host. Each id is passed through to the provisioner
	 * as a reference to a persisted security group.
	 */
	public List<Long> securityGroupIds;

	/**
	 * Whether to perform license and connectivity checks before provisioning. Set to {@code false}
	 * to skip these checks. Defaults to {@code true}.
	 */
	public boolean licenseCheck = true;

	/**
	 * Optional resource pool/cluster to provision the host into. Passed through by {@code id} as a
	 * reference to a persisted {@link CloudPool}. {@code null} is valid for clouds that do not scope
	 * hosts to a resource pool/cluster.
	 */
	public CloudPool pool;
}
