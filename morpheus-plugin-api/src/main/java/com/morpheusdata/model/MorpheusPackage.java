package com.morpheusdata.model;

import java.util.Date;
import java.util.List;

public class MorpheusPackage extends MorpheusModel {

	protected Account account;
	protected String description;
	protected Date dateCreated;
	protected Date lastUpdated;
    protected String rawData;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

    public String getRawData() {
        return rawData;
    }
}
