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

import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class AccountInvoiceItem extends MorpheusModel {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountInvoice invoice;
	//linkage
	protected String refType;
	protected Long refId;
	protected String refName;
	protected String refCategory; //discovered / instance / host
	protected String rawData;
	protected String externalId;
	protected String resourceExternalId;
	protected String rateExternalId;
	protected String uniqueId; //usageService_usageType_resource_id:rate
	//time
	protected Date startDate; //start of the usage
	protected Date endDate; //end of the usage
	//item type
	protected String itemId;
	protected String itemType;
	protected String itemName;
	protected String itemDescription;
	protected String productId;
	protected String productCode;
	protected String productName;
	protected String itemSeller;
	protected String itemAction;
	//usage
	protected String usageType;
	protected String usageCategory;
	protected String usageService;
	//pricing
	protected String rateId;
	protected String rateClass;
	protected String rateUnit;
	protected String rateTerm;
	//costing
	protected Double itemUsage = 0.0d; //used quantity
	protected Double itemRate = 0.0d; //rate per quantity
	protected Double itemCost = 0.0d; //total cost
	protected Double itemPrice = 0.0d; //total price
	protected Double itemTax = 0.0d; //tax

	protected Double amortizedCost = 0.0d;
	protected Double onDemandCost = 0.0d;
	protected String itemTerm;
	protected String taxType;
	protected String costProject;
	protected String costTeam;
	protected String costEnvironment;
	protected String availabilityZone;
	protected String operatingSystem;
	protected String purchaseOption;
	protected String tenancy;
	protected String databaseEngine;
	protected String billingEntity;
	protected String regionCode;
	protected Long lastInvoiceSyncTimestamp;

	protected String dateCheckHash;
	//audit
	protected Date dateCreated;
	protected Date lastUpdated;

	public AccountInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(AccountInvoice invoice) {
		this.invoice = invoice;
		markDirty("invoice", invoice, this.invoice);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType, this.refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId, this.refId);
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
		markDirty("refName", refName, this.refName);
	}

	public String getRefCategory() {
		return refCategory;
	}

	public void setRefCategory(String refCategory) {
		this.refCategory = refCategory;
		markDirty("refCategory", refCategory, this.refCategory);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData, this.rawData);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId, this.externalId);
	}

	public String getResourceExternalId() {
		return resourceExternalId;
	}

	public void setResourceExternalId(String resourceExternalId) {
		this.resourceExternalId = resourceExternalId;
		markDirty("resourceExternalId", resourceExternalId, this.resourceExternalId);
	}

	public String getRateExternalId() {
		return rateExternalId;
	}

	public void setRateExternalId(String rateExternalId) {
		this.rateExternalId = rateExternalId;
		markDirty("rateExternalId", rateExternalId, this.rateExternalId);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId, this.uniqueId);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		markDirty("startDate", startDate, this.startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		markDirty("endDate", endDate, this.endDate);
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
		markDirty("itemId", itemId, this.itemId);
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
		markDirty("itemType", itemType, this.itemType);
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
		markDirty("itemName", itemName, this.itemName);
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
		markDirty("itemDescription", itemDescription, this.itemDescription);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
		markDirty("productId", productId, this.productId);
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
		markDirty("productCode", productCode, this.productCode);
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
		markDirty("productName", productName, this.productName);
	}

	public String getItemSeller() {
		return itemSeller;
	}

	public void setItemSeller(String itemSeller) {
		this.itemSeller = itemSeller;
		markDirty("itemSeller", itemSeller, this.itemSeller);
	}

	public String getItemAction() {
		return itemAction;
	}

	public void setItemAction(String itemAction) {
		this.itemAction = itemAction;
		markDirty("itemAction", itemAction, this.itemAction);
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
		markDirty("usageType", usageType, this.usageType);
	}

	public String getUsageCategory() {
		return usageCategory;
	}

	public void setUsageCategory(String usageCategory) {
		this.usageCategory = usageCategory;
		markDirty("usageCategory", usageCategory, this.usageCategory);
	}

	public String getUsageService() {
		return usageService;
	}

	public void setUsageService(String usageService) {
		this.usageService = usageService;
		markDirty("usageService", usageService, this.usageService);
	}

	public String getRateId() {
		return rateId;
	}

	public void setRateId(String rateId) {
		this.rateId = rateId;
		markDirty("rateId", rateId, this.rateId);
	}

	public String getRateClass() {
		return rateClass;
	}

	public void setRateClass(String rateClass) {
		this.rateClass = rateClass;
		markDirty("rateClass", rateClass, this.rateClass);
	}

	public String getRateUnit() {
		return rateUnit;
	}

	public void setRateUnit(String rateUnit) {
		this.rateUnit = rateUnit;
		markDirty("rateUnit", rateUnit, this.rateUnit);
	}

	public String getRateTerm() {
		return rateTerm;
	}

	public void setRateTerm(String rateTerm) {
		this.rateTerm = rateTerm;
		markDirty("rateTerm", rateTerm, this.rateTerm);
	}

	public Double getItemUsage() {
		return itemUsage;
	}

	public void setItemUsage(Double itemUsage) {
		this.itemUsage = itemUsage;
		markDirty("itemUsage", itemUsage, this.itemUsage);
	}

	public Double getItemRate() {
		return itemRate;
	}

	public void setItemRate(Double itemRate) {
		this.itemRate = itemRate;
		markDirty("itemRate", itemRate, this.itemRate);
	}

	public Double getItemCost() {
		return itemCost;
	}

	public void setItemCost(Double itemCost) {
		this.itemCost = itemCost;
		markDirty("itemCost", itemCost, this.itemCost);
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
		markDirty("itemPrice", itemPrice, this.itemPrice);
	}

	public Double getItemTax() {
		return itemTax;
	}

	public void setItemTax(Double itemTax) {
		this.itemTax = itemTax;
		markDirty("itemTax", itemTax, this.itemTax);
	}

	public Double getAmortizedCost() {
		return amortizedCost;
	}

	public void setAmortizedCost(Double amortizedCost) {
		this.amortizedCost = amortizedCost;
		markDirty("amortizedCost", amortizedCost, this.amortizedCost);
	}

	public Double getOnDemandCost() {
		return onDemandCost;
	}

	public void setOnDemandCost(Double onDemandCost) {
		this.onDemandCost = onDemandCost;
		markDirty("onDemandCost", onDemandCost, this.onDemandCost);
	}

	public String getItemTerm() {
		return itemTerm;
	}

	public void setItemTerm(String itemTerm) {
		this.itemTerm = itemTerm;
		markDirty("itemTerm", itemTerm, this.itemTerm);
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
		markDirty("taxType", taxType, this.taxType);
	}

	public String getCostProject() {
		return costProject;
	}

	public void setCostProject(String costProject) {
		this.costProject = costProject;
		markDirty("costProject", costProject, this.costProject);
	}

	public String getCostTeam() {
		return costTeam;
	}

	public void setCostTeam(String costTeam) {
		this.costTeam = costTeam;
		markDirty("costTeam", costTeam, this.costTeam);
	}

	public String getCostEnvironment() {
		return costEnvironment;
	}

	public void setCostEnvironment(String costEnvironment) {
		this.costEnvironment = costEnvironment;
		markDirty("costEnvironment", costEnvironment, this.costEnvironment);
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
		markDirty("availabilityZone", availabilityZone, this.availabilityZone);
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
		markDirty("operatingSystem", operatingSystem, this.operatingSystem);
	}

	public String getPurchaseOption() {
		return purchaseOption;
	}

	public void setPurchaseOption(String purchaseOption) {
		this.purchaseOption = purchaseOption;
		markDirty("purchaseOption", purchaseOption, this.purchaseOption);
	}

	public String getTenancy() {
		return tenancy;
	}

	public void setTenancy(String tenancy) {
		this.tenancy = tenancy;
		markDirty("tenancy", tenancy, this.tenancy);
	}

	public String getDatabaseEngine() {
		return databaseEngine;
	}

	public void setDatabaseEngine(String databaseEngine) {
		this.databaseEngine = databaseEngine;
		markDirty("databaseEngine", databaseEngine, this.databaseEngine);
	}

	public String getBillingEntity() {
		return billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
		markDirty("billingEntity", billingEntity, this.billingEntity);
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
		markDirty("regionCode", regionCode, this.regionCode);
	}

	public Long getLastInvoiceSyncTimestamp() {
		return lastInvoiceSyncTimestamp;
	}

	public void setLastInvoiceSyncTimestamp(Long lastInvoiceSyncTimestamp) {
		this.lastInvoiceSyncTimestamp = lastInvoiceSyncTimestamp;
		markDirty("lastInvoiceSyncTimestamp", lastInvoiceSyncTimestamp, this.lastInvoiceSyncTimestamp);
	}

	/**
	 * Provides a HEX Encoded Byte array for each hour of a month to determine if that line item has already been processed or not
	 * LSB Byte array
	 */
	public String getDateCheckHash() {
		return dateCheckHash;
	}

	public void setDateCheckHash(String dateCheckHash) {
		this.dateCheckHash = dateCheckHash;
		markDirty("dateCheckHash", dateCheckHash, this.dateCheckHash);
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
}
