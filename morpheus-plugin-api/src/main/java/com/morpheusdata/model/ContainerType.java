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

import java.util.Collection;

public class ContainerType extends MorpheusModel {

	protected String code;
	protected String shortName;
	protected String name;
	protected Collection<Integer> ports;
	protected String containerVersion;
	protected String repositoryImage;
	protected String imageCode;
	protected String entryPoint;
	protected String mountLogs;
	protected String statTypeCode;
	protected String logTypeCode;
	protected Boolean showServerLogs;
	protected String category;
	protected String cloneType;
	protected Integer priorityOrder;
	protected String serverType;
	protected String checkTypeCode;
	protected VirtualImage virtualImage;
	protected ProvisionType provisionType;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Integer> getPorts() {
		return ports;
	}

	public void setPorts(Collection<Integer> ports) {
		this.ports = ports;
	}

	public String getContainerVersion() {
		return containerVersion;
	}

	public void setContainerVersion(String containerVersion) {
		this.containerVersion = containerVersion;
	}

	public String getRepositoryImage() {
		return repositoryImage;
	}

	public void setRepositoryImage(String repositoryImage) {
		this.repositoryImage = repositoryImage;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public String getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	public String getMountLogs() {
		return mountLogs;
	}

	public void setMountLogs(String mountLogs) {
		this.mountLogs = mountLogs;
	}

	public String getStatTypeCode() {
		return statTypeCode;
	}

	public void setStatTypeCode(String statTypeCode) {
		this.statTypeCode = statTypeCode;
	}

	public String getLogTypeCode() {
		return logTypeCode;
	}

	public void setLogTypeCode(String logTypeCode) {
		this.logTypeCode = logTypeCode;
	}

	public Boolean getShowServerLogs() {
		return showServerLogs;
	}

	public void setShowServerLogs(Boolean showServerLogs) {
		this.showServerLogs = showServerLogs;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCloneType() {
		return cloneType;
	}

	public void setCloneType(String cloneType) {
		this.cloneType = cloneType;
	}

	public Integer getPriorityOrder() {
		return priorityOrder;
	}

	public void setPriorityOrder(Integer priorityOrder) {
		this.priorityOrder = priorityOrder;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getCheckTypeCode() {
		return checkTypeCode;
	}

	public void setCheckTypeCode(String checkTypeCode) {
		this.checkTypeCode = checkTypeCode;
	}

	public VirtualImage getVirtualImage() {
		return virtualImage;
	}

	public void setVirtualImage(VirtualImage virtualImage) {
		this.virtualImage = virtualImage;
	}

	public ProvisionType getProvisionType() {
		return provisionType;
	}

	public void setProvisionType(ProvisionType provisionType) {
		this.provisionType = provisionType;
	}
}
