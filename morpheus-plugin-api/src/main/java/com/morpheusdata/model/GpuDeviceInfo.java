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

package com.morpheusdata.model;

/**
 * Live GPU device information reported by a compute server.
 *
 * @since 1.5.1
 */
public class GpuDeviceInfo {
	protected String uniqueId;
	protected GpuDeviceKind kind;
	protected String vendor;
	protected String name;
	protected String uuid;
	protected String pciBusId;
	protected String parentUniqueId;
	protected String vmId;
	protected String vgpuType;
	protected Long vgpuTypeId;
	protected Integer vendorId;
	protected Integer productId;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public GpuDeviceKind getKind() {
		return kind;
	}

	public void setKind(GpuDeviceKind kind) {
		this.kind = kind;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPciBusId() {
		return pciBusId;
	}

	public void setPciBusId(String pciBusId) {
		this.pciBusId = pciBusId;
	}

	public String getParentUniqueId() {
		return parentUniqueId;
	}

	public void setParentUniqueId(String parentUniqueId) {
		this.parentUniqueId = parentUniqueId;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public String getVgpuType() {
		return vgpuType;
	}

	public void setVgpuType(String vgpuType) {
		this.vgpuType = vgpuType;
	}

	public Long getVgpuTypeId() {
		return vgpuTypeId;
	}

	public void setVgpuTypeId(Long vgpuTypeId) {
		this.vgpuTypeId = vgpuTypeId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
}
