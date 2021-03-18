package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;

public class ReportResult extends MorpheusModel {
	protected Account account;
	protected String name;
	protected ReportType type;
	protected Map configMap;
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

	@Override
	public Map getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
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

	public enum Status {
		requested,
		generating,
		ready,
		failed
	}
}
