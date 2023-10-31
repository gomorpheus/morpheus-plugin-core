package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import java.util.Date;
import java.util.List;

public class Dashboard extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected User user;

	protected String uuid;
	protected String name;
	protected String dashboardId; //dashboard id this set goes to
	protected String code;
	protected String category;
	protected String title;
	protected String description;
	protected Boolean defaultDashboard = false;
	protected Boolean enabled = true;
	protected String sourceType = "system";
	protected String scriptPath;
	protected String templatePath;
	protected Date dateCreated;
	protected Date lastUpdated;

	//items
	protected List<DashboardItem> dashboardItems;

	//getters and setters
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		markDirty("dashboardId", dashboardId);
		this.user = user;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		markDirty("uuid", uuid);
		this.uuid = uuid;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
		markDirty("dashboardId", dashboardId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name);
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code);
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		markDirty("category", category);
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		markDirty("title", title);
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty("description", description);
		this.description = description;
	}

	public Boolean getDefaultDashboard() {
		return defaultDashboard;
	}

	public void setDefaultDashboard(Boolean defaultDashboard) {
		markDirty("defaultDashboard", defaultDashboard);
		this.defaultDashboard = defaultDashboard;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled);
		this.enabled = enabled;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		markDirty("sourceType", sourceType);
		this.sourceType = sourceType;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		markDirty("scriptPath", scriptPath);
		this.scriptPath = scriptPath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		markDirty("templatePath", templatePath);
		this.templatePath = templatePath;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		markDirty("dateCreated", dateCreated, this.dateCreated);
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
		this.lastUpdated = lastUpdated;
	}

	public List<DashboardItem> getDashboardItems() {
		return dashboardItems;
	}

	public void setDashboardItems(List<DashboardItem> dashboardItems) {
		this.dashboardItems = dashboardItems;
		markDirty("dashboardItems", dashboardItems);
	}
}
