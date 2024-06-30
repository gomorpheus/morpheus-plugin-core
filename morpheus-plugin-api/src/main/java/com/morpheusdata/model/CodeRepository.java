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
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import java.util.Date;
import java.util.List;

public class CodeRepository extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected AccountIntegration integration;
	//protected Deployment deployment;
	protected String name;
	protected String category;
	protected String type;
	protected String description;
	protected String orgId;
	protected String orgName;
	protected String displayName;
	protected String internalId;
	protected String externalId;
	protected String status;
	protected String serviceUrl;
	protected Boolean active;
	protected Boolean enabled;
	protected Boolean privateRepo;
	protected Boolean forkRepo;
	protected Boolean cacheRepo;
	protected Boolean scribeRepo;
	protected String scribeMode;
	protected Long repoSize;
	protected String codeLanguage;
	protected String defaultBranch;
	protected String repoMode;
	protected String readMe;
	protected String uuid;
	protected Date dateCreated;
	protected Date lastUpdated;
	//lists
	//List<CodeRepositoryBranch> branches;
	protected List<CodeRepositoryTrait> traits;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public AccountIntegration getIntegration() {
		return integration;
	}
	
	public void setIntegration(AccountIntegration integration) {
		this.integration = integration;
		markDirty("integration", integration);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
		markDirty("orgId", orgId);
	}

	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
		markDirty("orgName", orgName);
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName);
	}

	public String getInternalId() {
		return internalId;
	}
	
	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getServiceUrl() {
		return serviceUrl;
	}
	
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		markDirty("serviceUrl", serviceUrl);
	}

	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getPrivateRepo() {
		return privateRepo;
	}
	
	public void setPrivateRepo(Boolean privateRepo) {
		this.privateRepo = privateRepo;
		markDirty("privateRepo", privateRepo);
	}

	public Boolean getForkRepo() {
		return forkRepo;
	}
	
	public void setForkRepo(Boolean forkRepo) {
		this.forkRepo = forkRepo;
		markDirty("forkRepo", forkRepo);
	}

	public Boolean getCacheRepo() {
		return cacheRepo;
	}
	
	public void setCacheRepo(Boolean cacheRepo) {
		this.cacheRepo = cacheRepo;
		markDirty("cacheRepo", cacheRepo);
	}

	public Boolean getScribeRepo() {
		return scribeRepo;
	}
	
	public void setScribeRepo(Boolean scribeRepo) {
		this.scribeRepo = scribeRepo;
		markDirty("scribeRepo", scribeRepo);
	}

	public String getScribeMode() {
		return scribeMode;
	}
	
	public void setScribeMode(String scribeMode) {
		this.scribeMode = scribeMode;
		markDirty("scribeMode", scribeMode);
	}

	public Long getRepoSize() {
		return repoSize;
	}
	
	public void setRepoSize(Long repoSize) {
		this.repoSize = repoSize;
		markDirty("repoSize", repoSize);
	}

	public String getCodeLanguage() {
		return codeLanguage;
	}
	
	public void setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
		markDirty("codeLanguage", codeLanguage);
	}

	public String getDefaultBranch() {
		return defaultBranch;
	}
	
	public void setDefaultBranch(String defaultBranch) {
		this.defaultBranch = defaultBranch;
		markDirty("defaultBranch", defaultBranch);
	}

	public String getRepoMode() {
		return repoMode;
	}
	
	public void setRepoMode(String repoMode) {
		this.repoMode = repoMode;
		markDirty("repoMode", repoMode);
	}
	
	public String getReadMe() {
		return readMe;
	}

	public void setReadMe(String readMe) {
		this.readMe = readMe;
		markDirty("readMe", readMe);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
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

	public List<CodeRepositoryTrait> getTraits() {
		return traits;
	}

	public void setTraits(List<CodeRepositoryTrait> traits) {
		this.traits = traits;
		markDirty("traits", traits);
	}

}
