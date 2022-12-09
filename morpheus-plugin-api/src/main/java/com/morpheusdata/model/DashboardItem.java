package com.morpheusdata.model;

public class DashboardItem extends MorpheusModel {

	
	protected DashboardItemType type;
	protected Integer itemRow;
	protected Integer itemColumn;
	protected String itemGroup;
	protected String config;
	
	//filters
	//ui types
	//endpoints
	//scripts
	
	public DashboardItemType getType() {
		return type;
	}

	public void setDashboardId(DashboardItemType type) {
		markDirty("type", type);
		this.type = type;
	}

	public Integer getItemRow() {
		return itemRow;
	}

	public void setItemRow(Integer itemRow) {
		markDirty("itemRow", itemRow);
		this.itemRow = itemRow;
	}

	public Integer getItemColumn() {
		return itemColumn;
	}

	public void setItemColumn(Integer itemColumn) {
		markDirty("itemColumn", itemColumn);
		this.itemColumn = itemColumn;
	}

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		markDirty("itemGroup", itemGroup);
		this.itemGroup = itemGroup;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		markDirty("config", config);
		this.config = config;
	}

}
