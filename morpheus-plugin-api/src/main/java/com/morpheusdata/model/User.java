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

import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.UserIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;
import java.util.Map;

/**
 * Represents the Morpheus User and associated user data when calling into plugins.
 * This can be useful display information or the {@link #getPermissions()} property can be used by some providers
 * to determine what may need to be displayed based on the user access
 *
 * @author David Estes, Mike Truso
 */
public class User extends UserIdentityProjection implements UserIdentity {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
  protected Account account;
  protected String firstName;
  protected String lastName;
	protected String email;
  protected Boolean enabled;
  protected Boolean accountLocked;
  protected Boolean accountExpired;
  protected Date lastLoginDate;
  protected String linuxUsername;
  protected String windowsUsername;
	protected Map<String, String> permissions;
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLinuxUsername() {
		return linuxUsername;
	}

	public void setLinuxUsername(String linuxUsername) {
		this.linuxUsername = linuxUsername;
	}

	public String getWindowsUsername() {
		return windowsUsername;
	}

	public void setWindowsUsername(String windowsUsername) {
		this.windowsUsername = windowsUsername;
	}

	/**
	 * A Map of the user's permissions where the key is the {@link Permission#code} and the value is the highest {@link Permission.AccessType}
	 * @return Map the map of permissions
	 */
	public Map<String, String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Map<String, String> permissions) {
		this.permissions = permissions;
	}

	public String getDisplayName() {
		if(this.firstName != null && lastName != null) {
			return firstName + " " + lastName;
		}
		return username;
	}
	
}
