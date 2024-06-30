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

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.AccountPriceIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class AccountPrice extends MorpheusModel {

	enum PRICE_TYPE {
		fixed,
		compute,
		memory,
		cores,
		cpu,
		storage,
		platform,
		software_or_service,
		datastore,
		load_balancer,
		load_balancer_virtual_server
	}

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String category;
	protected Boolean active = true;
	protected Boolean crossCloudApply = false;
	protected Boolean systemCreated = false;
	protected Date dateCreated;
	protected Date lastUpdated;
	// @JsonSerialize(using= ModelAsIdOnlySerializer.class)
	// protected AccountPriceHistory currentPriceHistoryCache;
	protected Boolean currentPriceHistoryCached;
	protected String resourceType;
	protected String priceType;
	protected String priceUnit;
	protected BigDecimal price;
	protected BigDecimal cost;
	protected BigDecimal markup;
	protected String markupType;
	protected Double markupPercent;
	protected Double matchValue;
	protected Long priceTier;
	protected String currency;
	protected String platform;
	protected String softwareOrService;
	protected StorageVolumeType volumeType;
	protected Datastore datastore;
	protected BigDecimal customPrice;
	protected String incurCharges;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
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
		markDirty("category", category, this.category);
		this.category = category;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account, this.account);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active, this.active);
	}

	public Boolean getCrossCloudApply() {
		return crossCloudApply;
	}

	public void setCrossCloudApply(Boolean crossCloudApply) {
		this.crossCloudApply = crossCloudApply;
		markDirty("crossCloudApply", crossCloudApply, this.crossCloudApply);
	}

	public Boolean getSystemCreated() {
		return systemCreated;
	}

	public void setSystemCreated(Boolean systemCreated) {
		this.systemCreated = systemCreated;
		markDirty("systemCreated", systemCreated, this.systemCreated);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated, this.dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
	}

	// public AccountPriceHistory getCurrentPriceHistoryCache() {
	// 	return currentPriceHistoryCache;
	// }

	// public void setCurrentPriceHistoryCache(AccountPriceHistory currentPriceHistoryCache) {
	// 	this.currentPriceHistoryCache = currentPriceHistoryCache;
	// 	markDirty("currentPriceHistoryCache", currentPriceHistoryCache, this.currentPriceHistoryCache);
	// }

	public Boolean getCurrentPriceHistoryCached() {
		return currentPriceHistoryCached;
	}

	public void setCurrentPriceHistoryCached(Boolean currentPriceHistoryCached) {
		this.currentPriceHistoryCached = currentPriceHistoryCached;
		markDirty("currentPriceHistoryCached", currentPriceHistoryCached, this.currentPriceHistoryCached);
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
		markDirty("resourceType", resourceType, this.resourceType);
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
		markDirty("priceType", priceType, this.priceType);
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
		markDirty("priceUnit", priceUnit, this.priceUnit);
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
		markDirty("price", price, this.price);
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
		markDirty("cost", cost, this.cost);
	}

	public BigDecimal getMarkup() {
		return markup;
	}

	public void setMarkup(BigDecimal markup) {
		this.markup = markup;
		markDirty("markup", markup, this.markup);
	}

	public String getMarkupType() {
		return markupType;
	}

	public void setMarkupType(String markupType) {
		this.markupType = markupType;
		markDirty("markupType", markupType, this.markupType);
	}

	public Double getMarkupPercent() {
		return markupPercent;
	}

	public void setMarkupPercent(Double markupPercent) {
		this.markupPercent = markupPercent;
		markDirty("markupPercent", markupPercent, this.markupPercent);
	}

	public Double getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(Double matchValue) {
		this.matchValue = matchValue;
		markDirty("matchValue", matchValue, this.matchValue);
	}

	public Long getPriceTier() {
		return priceTier;
	}

	public void setPriceTier(Long priceTier) {
		this.priceTier = priceTier;
		markDirty("priceTier", priceTier, this.priceTier);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		markDirty("currency", currency, this.currency);
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
		markDirty("platform", platform, this.platform);
	}

	public String getSoftwareOrService() {
		return softwareOrService;
	}

	public void setSoftwareOrService(String softwareOrService) {
		this.softwareOrService = softwareOrService;
		markDirty("softwareOrService", softwareOrService, this.softwareOrService);
	}

	public StorageVolumeType getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(StorageVolumeType volumeType) {
		this.volumeType = volumeType;
		markDirty("volumeType", volumeType, this.volumeType);
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
		markDirty("datastore", datastore, this.datastore);
	}

	public BigDecimal getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(BigDecimal customPrice) {
		this.customPrice = customPrice;
		markDirty("customPrice", customPrice, this.customPrice);
	}

	public String getIncurCharges() {
		return incurCharges;
	}

	public void setIncurCharges(String incurCharges) {
		this.incurCharges = incurCharges;
		markDirty("incurCharges", incurCharges, this.incurCharges);
	}

}
