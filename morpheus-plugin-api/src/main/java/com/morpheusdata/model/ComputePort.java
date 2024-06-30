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

package com.morpheusdata.model;

/**
 * Represents ports available on ComputeServers within a Cloud. Currently utilized when connecting by VNC
 */
public class ComputePort extends MorpheusModel {

	protected Integer port;
	protected String portType;
	protected String parentType;
	protected Long parentId;
	protected String refType;
	protected String regionCode;
	protected Long refId;

	public ComputePort() {
	}

	public ComputePort(Long id, Integer port, String refType, Long refId, String parentType, Long parentId, String portType, String regionCode) {
		this.id = id;
		this.port = port;
		this.refType = refType;
		this.refId = refId;
		this.parentType = parentType;
		this.parentId = parentId;
		this.portType = portType;
		this.regionCode = regionCode;
	}

	/**
	 * Get the port number
	 * @return port number
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * Set the port number
	 * @param port port number
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Get the type of port
	 * @return type of port
	 */
	public String getPortType() {
		return portType;
	}

	/**
	 * Set the type of port. Should be 'vnc' to enable utilization by VNC
	 * @param portType type of port
	 */
	public void setPortType(String portType) {
		this.portType = portType;
	}

	/**
	 * Get the type of parent
	 * @return type of parent
	 */
	public String getParentType() {
		return parentType;
	}

	/**
	 * Set the type of parent. Should be 'ComputeZone' when configured for 'vnc'
	 * @param parentType type of parent
	 */
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	/**
	 * Get the id of the parent
	 * @return id of parent
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * Set the id of the parent. Should be the id of the Cloud when configured for 'vnc'
	 * @param parentId id of parent
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * Get the type of object this is referenced to
	 * @return reference type
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * Set the type of object this is referenced to. Should be 'ComputeServer' when configured for 'vnc'
	 * @param refType reference type
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * Get the region code
	 * @return region code
	 */
	public String getRegionCode() {
		return regionCode;
	}

	/**
	 * Set the region code. Should be the regionCode for the Cloud when configured for 'vnc'
	 * @param regionCode region code
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	/**
	 * Get the id of the referenced object
	 * @return reference id
	 */
	public Long getRefId() {
		return refId;
	}

	/**
	 * Set the id of the referenced object. Should be the id of the ComputeServer when configured for 'vnc'
	 * @param refId reference id
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
