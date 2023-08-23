package com.morpheusdata.model.provisioning;

import com.morpheusdata.model.AccountInvoice;
import com.morpheusdata.model.MorpheusModel;

import java.util.Date;

public class AccountInvoiceItem extends MorpheusModel {
	protected AccountInvoice invoice;
	//linkage
	protected String refType;
	protected Long refId;
	protected String refName;
	protected String refCategory; //discovered / instance / host
	protected String config;
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
	protected String procutName;
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
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getRefCategory() {
		return refCategory;
	}

	public void setRefCategory(String refCategory) {
		this.refCategory = refCategory;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getResourceExternalId() {
		return resourceExternalId;
	}

	public void setResourceExternalId(String resourceExternalId) {
		this.resourceExternalId = resourceExternalId;
	}

	public String getRateExternalId() {
		return rateExternalId;
	}

	public void setRateExternalId(String rateExternalId) {
		this.rateExternalId = rateExternalId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProcutName() {
		return procutName;
	}

	public void setProcutName(String procutName) {
		this.procutName = procutName;
	}

	public String getItemSeller() {
		return itemSeller;
	}

	public void setItemSeller(String itemSeller) {
		this.itemSeller = itemSeller;
	}

	public String getItemAction() {
		return itemAction;
	}

	public void setItemAction(String itemAction) {
		this.itemAction = itemAction;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public String getUsageCategory() {
		return usageCategory;
	}

	public void setUsageCategory(String usageCategory) {
		this.usageCategory = usageCategory;
	}

	public String getUsageService() {
		return usageService;
	}

	public void setUsageService(String usageService) {
		this.usageService = usageService;
	}

	public String getRateId() {
		return rateId;
	}

	public void setRateId(String rateId) {
		this.rateId = rateId;
	}

	public String getRateClass() {
		return rateClass;
	}

	public void setRateClass(String rateClass) {
		this.rateClass = rateClass;
	}

	public String getRateUnit() {
		return rateUnit;
	}

	public void setRateUnit(String rateUnit) {
		this.rateUnit = rateUnit;
	}

	public String getRateTerm() {
		return rateTerm;
	}

	public void setRateTerm(String rateTerm) {
		this.rateTerm = rateTerm;
	}

	public Double getItemUsage() {
		return itemUsage;
	}

	public void setItemUsage(Double itemUsage) {
		this.itemUsage = itemUsage;
	}

	public Double getItemRate() {
		return itemRate;
	}

	public void setItemRate(Double itemRate) {
		this.itemRate = itemRate;
	}

	public Double getItemCost() {
		return itemCost;
	}

	public void setItemCost(Double itemCost) {
		this.itemCost = itemCost;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Double getItemTax() {
		return itemTax;
	}

	public void setItemTax(Double itemTax) {
		this.itemTax = itemTax;
	}

	public Double getAmortizedCost() {
		return amortizedCost;
	}

	public void setAmortizedCost(Double amortizedCost) {
		this.amortizedCost = amortizedCost;
	}

	public Double getOnDemandCost() {
		return onDemandCost;
	}

	public void setOnDemandCost(Double onDemandCost) {
		this.onDemandCost = onDemandCost;
	}

	public String getItemTerm() {
		return itemTerm;
	}

	public void setItemTerm(String itemTerm) {
		this.itemTerm = itemTerm;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getCostProject() {
		return costProject;
	}

	public void setCostProject(String costProject) {
		this.costProject = costProject;
	}

	public String getCostTeam() {
		return costTeam;
	}

	public void setCostTeam(String costTeam) {
		this.costTeam = costTeam;
	}

	public String getCostEnvironment() {
		return costEnvironment;
	}

	public void setCostEnvironment(String costEnvironment) {
		this.costEnvironment = costEnvironment;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getPurchaseOption() {
		return purchaseOption;
	}

	public void setPurchaseOption(String purchaseOption) {
		this.purchaseOption = purchaseOption;
	}

	public String getTenancy() {
		return tenancy;
	}

	public void setTenancy(String tenancy) {
		this.tenancy = tenancy;
	}

	public String getDatabaseEngine() {
		return databaseEngine;
	}

	public void setDatabaseEngine(String databaseEngine) {
		this.databaseEngine = databaseEngine;
	}

	public String getBillingEntity() {
		return billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Long getLastInvoiceSyncTimestamp() {
		return lastInvoiceSyncTimestamp;
	}

	public void setLastInvoiceSyncTimestamp(Long lastInvoiceSyncTimestamp) {
		this.lastInvoiceSyncTimestamp = lastInvoiceSyncTimestamp;
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
}
