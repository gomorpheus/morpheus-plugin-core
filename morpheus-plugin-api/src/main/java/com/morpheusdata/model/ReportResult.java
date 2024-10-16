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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.core.providers.ReportProvider;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Map;

/**
 * Represents a report result. A report result is a particular report run containing all filter information, the user and account who
 * executed it as well as the status. This is passed often into the {@link ReportProvider} methods for both
 * generating a report as well as displaying a report.
 *
 * @author David Estes
 * @since 0.8.0
 */
public class ReportResult extends MorpheusModel {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String name;
	protected ReportType type;
	protected Status status = Status.requested;
	protected String reportTitle;
	protected String filterTitle;
	protected Long reportJobId;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public Long getReportJobId() {
		return reportJobId;
	}

	public void setReportJobId(Long reportJobId) {
		this.reportJobId = reportJobId;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public enum Status {
		requested,
		generating,
		ready,
		failed
	}
}
