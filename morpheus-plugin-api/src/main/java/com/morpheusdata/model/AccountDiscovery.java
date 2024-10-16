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

import java.util.Date;

public class AccountDiscovery extends MorpheusModel {
	protected AccountDiscoveryType type;
	protected Long accountId;
	protected Long userId;
	protected Long zoneId;
	protected Long siteId;
	protected Long planId;
	protected String refType;
	protected Long refId;
	protected String refName;
	protected Date refDate;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Date endDate;
	protected Long actionPlanId;
	protected String actionTitle;
	protected String beforeValue;
	protected String actionValue;
	protected String actionValueType;
	protected String actionCategory;
	protected String actionType;
	protected String actionMessage;
	protected String statusMessage;
	protected String resolvedMessage;
	protected String processedMessage;
	protected Severity severity = Severity.info; //low, info, warning, critical
	protected Double savings;
	protected Date processDate;
	protected Boolean resolved = false;
	protected Boolean ignored = false;
	protected Boolean processed = false;
	protected Long lookBackDays;

	public AccountDiscoveryType getType() {
		return type;
	}

	public void setType(AccountDiscoveryType type) {
		this.type = type;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public Date getRefDate() {
		return refDate;
	}

	public void setRefDate(Date refDate) {
		this.refDate = refDate;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getActionPlanId() {
		return actionPlanId;
	}

	public void setActionPlanId(Long actionPlanId) {
		this.actionPlanId = actionPlanId;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
	}

	public String getBeforeValue() {
		return beforeValue;
	}

	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}

	public String getActionValue() {
		return actionValue;
	}

	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}

	public String getActionValueType() {
		return actionValueType;
	}

	public void setActionValueType(String actionValueType) {
		this.actionValueType = actionValueType;
	}

	public String getActionCategory() {
		return actionCategory;
	}

	public void setActionCategory(String actionCategory) {
		this.actionCategory = actionCategory;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getResolvedMessage() {
		return resolvedMessage;
	}

	public void setResolvedMessage(String resolvedMessage) {
		this.resolvedMessage = resolvedMessage;
	}

	public String getProcessedMessage() {
		return processedMessage;
	}

	public void setProcessedMessage(String processedMessage) {
		this.processedMessage = processedMessage;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public Double getSavings() {
		return savings;
	}

	public void setSavings(Double savings) {
		this.savings = savings;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public Boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(Boolean ignored) {
		this.ignored = ignored;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public Long getLookBackDays() {
		return lookBackDays;
	}

	public void setLookBackDays(Long lookBackDays) {
		this.lookBackDays = lookBackDays;
	}

	public enum Severity {
		low, //affects cluster details for the object
		info, //affects cluster view
		warning,
		critical
	}
}
