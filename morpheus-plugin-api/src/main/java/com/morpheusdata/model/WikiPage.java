package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.WikiPageIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class WikiPage extends WikiPageIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	Account account;
	String name;
	String urlName;
	String category;
	String refType;
	Long refId;
	String content;
	String contentFormatted;
	String format = "markdown";
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	User createdBy;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	User updatedBy;
	Date dateCreated;
	Date lastUpdated;
	String updatedByUsername;
	String createdByUsername;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
		markDirty("urlName", urlName);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		markDirty("content", content);
	}

	public String getContentFormatted() {
		return contentFormatted;
	}

	public void setContentFormatted(String contentFormatted) {
		this.contentFormatted = contentFormatted;
		markDirty("contentFormatted", contentFormatted);
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
		markDirty("format", format);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
		markDirty("updatedBy", updatedBy);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public String getUpdatedByUsername() {
		return updatedByUsername;
	}

	public void setUpdatedByUsername(String updatedByUsername) {
		this.updatedByUsername = updatedByUsername;
		markDirty("updatedByUsername", updatedByUsername);
	}

	public String getCreatedByUsername() {
		return createdByUsername;
	}

	public void setCreatedByUsername(String createdByUsername) {
		this.createdByUsername = createdByUsername;
		markDirty("createdByUsername", createdByUsername);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	@Override
	public String getRefType() {
		return refType;
	}

	@Override
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	@Override
	public Long getRefId() {
		return refId;
	}

	@Override
	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}
}
