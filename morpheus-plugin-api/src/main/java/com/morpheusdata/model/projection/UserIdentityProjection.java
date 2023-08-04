package com.morpheusdata.model.projection;

public class UserIdentityProjection extends MorpheusIdentityModel {

	protected String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
