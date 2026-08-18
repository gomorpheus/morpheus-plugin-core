/*
 *  Copyright 2026 Morpheus Data, LLC.
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

import com.morpheusdata.model.projection.AccountCredentialIdentityProjection;

import java.util.Date;

public class AccountCredentialLink extends AccountCredentialIdentityProjection {
	protected AccountCredential credential;

	//ownership
	protected Account account;

	protected User user;
	protected UserGroup group;
	protected Role role;
	protected Boolean defaultCredential = false;

	//ref
	protected String refType;
	protected String refUuid;
	protected String refName;

	//audit
	protected Date dateCreated;
	protected Date lastUpdated;

	public AccountCredential getCredential() {
		return credential;
	}

	public void setCredential(AccountCredential newCredential) {
		credential = newCredential;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account newAccount){
		account = newAccount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User newUser){
		user = newUser;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup newGroup){
		group = newGroup;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role newRole){
		role = newRole;
	}

	public Boolean getDefaultCredential() {
		return defaultCredential;
	}

	public void setDefaultCredential(Boolean newDefaultCredential) {
		defaultCredential = newDefaultCredential;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String newRefType) {
		refType = newRefType;
	}

	public String getRefUuid() {
		return refUuid;
	}

	public void setRefUuid(String newRefUuid) {
		refUuid = newRefUuid;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String newRefName) {
		refName = newRefName;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date newDateCreated) {
		dateCreated = newDateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date newLastUpdated) {
		lastUpdated = newLastUpdated;
	}

}
