package com.morpheusdata.model;

public class ReportResultRow extends MorpheusModel {
	protected ReportResult reportResult;
	protected String section;
	protected Long displayOrder;
	protected String data; //json data map

	public ReportResult getReportResult() {
		return reportResult;
	}

	public void setReportResult(ReportResult reportResult) {
		this.reportResult = reportResult;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
