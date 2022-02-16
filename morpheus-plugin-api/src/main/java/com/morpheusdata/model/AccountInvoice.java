package com.morpheusdata.model;

import java.util.Date;

public class AccountInvoice extends MorpheusModel {
	protected Long ownerId;
	protected String ownerName;
	protected Long accountId;
	protected String accountName;
	protected Long userId;
	protected String userName;
	protected Long zoneId;
	protected String zoneUUID;
	protected String zoneName;
	protected String zoneRegion;
	protected Long siteId;
	protected String siteName;
	protected Long resourceId;
	protected String resourceUuid;
	protected String resourceType;
	protected String resourceName;
	protected String resourceExternalId;
	protected String resourceInternalId;
	protected Long instanceId;
	protected String instanceName;
	protected Long planId;
	protected String planName;
	protected Long layoutId;
	protected String layoutName;
	protected Long serverId;
	protected String serverName;
	protected Long serverGroupId;
	protected String serverGroupName;
	protected Long projectId;
	protected String projectName;
	protected String projectTags;
	protected String refType;
	protected Long refId;
	protected String refUUID;
	protected String refName;
	protected String refCategory; //discovered / instance / host / vm / container
	protected String config;
	protected String rawData;
	//date range
	protected String interval = "month"; //hour, month, year etc
	protected String period; //month year day hour or whatever for easy filtering
	protected Date startDate; //start of the period
	protected Date refStart; //start of the period or time in the period the resource was created if after
	protected Date endDate; //end of the period
	protected Date refEnd; //end of the period or time in the period the resource was delete if before
	protected Boolean estimate = true; //this will be no longer needed - adding actuals to this table now
	protected Boolean active = true;
	//price and cost - estimates
	protected Double intervalPrice = 0.0d; //charge for this for the entire interval
	protected Double intervalCost = 0.0d; //cost for this for the entire interval
	protected Double computePrice = 0.0d;
	protected Double computeCost = 0.0d;
	protected Double memoryPrice = 0.0d;
	protected Double memoryCost = 0.0d;
	protected Double storagePrice = 0.0d;
	protected Double storageCost = 0.0d;
	protected Double networkPrice = 0.0d;
	protected Double networkCost = 0.0d;
	protected Double licensePrice = 0.0d;
	protected Double licenseCost = 0.0d;
	protected Double extraPrice = 0.0d;
	protected Double extraCost = 0.0d;
	protected Double totalPrice = 0.0d;
	protected Double totalCost = 0.0d; //total if whole period is complete
	protected Double runningPrice = 0.0d; //total to current time this was captured
	protected Double runningCost = 0.0d; //total to current time this was captured
	protected String currency = "USD";
	protected Double conversionRate; //rate from currency -> USD
	//price and cost - actuals
	protected Double actualComputePrice = 0.0d;
	protected Double actualComputeCost = 0.0d;
	protected Double actualMemoryPrice = 0.0d;
	protected Double actualMemoryCost = 0.0d;
	protected Double actualStoragePrice = 0.0d;
	protected Double actualStorageCost = 0.0d;
	protected Double actualNetworkPrice = 0.0d;
	protected Double actualNetworkCost = 0.0d;
	protected Double actualLicensePrice = 0.0d;
	protected Double actualLicenseCost = 0.0d;
	protected Double actualExtraPrice = 0.0d;
	protected Double actualExtraCost = 0.0d;
	protected Double actualTotalPrice = 0.0d;
	protected Double actualTotalCost = 0.0d;
	protected Double actualRunningPrice = 0.0d;
	protected Double actualRunningCost = 0.0d;
	protected String actualCurrency = "USD";
	protected Double actualConversionRate; //rate from actualCurrency -> USD
	//price and cost - actuals
	protected Double invoiceComputePrice = 0.0d;
	protected Double invoiceComputeCost = 0.0d;
	protected Double invoiceMemoryPrice = 0.0d;
	protected Double invoiceMemoryCost = 0.0d;
	protected Double invoiceStoragePrice = 0.0d;
	protected Double invoiceStorageCost = 0.0d;
	protected Double invoiceNetworkPrice = 0.0d;
	protected Double invoiceNetworkCost = 0.0d;
	protected Double invoiceLicensePrice = 0.0d;
	protected Double invoiceLicenseCost = 0.0d;
	protected Double invoiceExtraPrice = 0.0d;
	protected Double invoiceExtraCost = 0.0d;
	protected Double invoiceTotalPrice = 0.0d;
	protected Double invoiceTotalCost = 0.0d;
	protected Double invoiceRunningPrice = 0.0d;
	protected Double invoiceRunningCost = 0.0d;
	//on / off - power
	protected Long offTime = 0L;
	protected String powerState;
	protected Date powerDate;
	protected Double runningMultiplier = 0.0d;
	//tracking
	protected Date lastCostDate; //date the job was calculated for
	protected Date lastActualDate; //data the actual data was last improted
	protected Date lastInvoiceSyncDate;
	//audit
	protected Date dateCreated;
	protected Date lastUpdated;
	//usage
	protected String usageType;
	protected String usageCategory;
	protected String usageService;
	//formulas
	protected Double runningCostRatio; //cost - running vs estimated total
	protected Double actualRunningCostRatio; //cost - actual running vs total
	protected Double runningPriceRatio; //price - running vs estimated total
	protected Double actualRunningPriceRatio; //price - actual running vs total

	protected Double totalCostEstimateRatio;
	protected Double totalPriceEstimateRatio;
	protected Boolean summaryInvoice = false;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneUUID() {
		return zoneUUID;
	}

	public void setZoneUUID(String zoneUUID) {
		this.zoneUUID = zoneUUID;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneRegion() {
		return zoneRegion;
	}

	public void setZoneRegion(String zoneRegion) {
		this.zoneRegion = zoneRegion;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceUuid() {
		return resourceUuid;
	}

	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceExternalId() {
		return resourceExternalId;
	}

	public void setResourceExternalId(String resourceExternalId) {
		this.resourceExternalId = resourceExternalId;
	}

	public String getResourceInternalId() {
		return resourceInternalId;
	}

	public void setResourceInternalId(String resourceInternalId) {
		this.resourceInternalId = resourceInternalId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Long getServerGroupId() {
		return serverGroupId;
	}

	public void setServerGroupId(Long serverGroupId) {
		this.serverGroupId = serverGroupId;
	}

	public String getServerGroupName() {
		return serverGroupName;
	}

	public void setServerGroupName(String serverGroupName) {
		this.serverGroupName = serverGroupName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectTags() {
		return projectTags;
	}

	public void setProjectTags(String projectTags) {
		this.projectTags = projectTags;
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

	public String getRefUUID() {
		return refUUID;
	}

	public void setRefUUID(String refUUID) {
		this.refUUID = refUUID;
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

	@Override
	public String getConfig() {
		return config;
	}

	@Override
	public void setConfig(String config) {
		this.config = config;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getRefStart() {
		return refStart;
	}

	public void setRefStart(Date refStart) {
		this.refStart = refStart;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getRefEnd() {
		return refEnd;
	}

	public void setRefEnd(Date refEnd) {
		this.refEnd = refEnd;
	}

	public Boolean getEstimate() {
		return estimate;
	}

	public void setEstimate(Boolean estimate) {
		this.estimate = estimate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Double getIntervalPrice() {
		return intervalPrice;
	}

	public void setIntervalPrice(Double intervalPrice) {
		this.intervalPrice = intervalPrice;
	}

	public Double getIntervalCost() {
		return intervalCost;
	}

	public void setIntervalCost(Double intervalCost) {
		this.intervalCost = intervalCost;
	}

	public Double getComputePrice() {
		return computePrice;
	}

	public void setComputePrice(Double computePrice) {
		this.computePrice = computePrice;
	}

	public Double getComputeCost() {
		return computeCost;
	}

	public void setComputeCost(Double computeCost) {
		this.computeCost = computeCost;
	}

	public Double getMemoryPrice() {
		return memoryPrice;
	}

	public void setMemoryPrice(Double memoryPrice) {
		this.memoryPrice = memoryPrice;
	}

	public Double getMemoryCost() {
		return memoryCost;
	}

	public void setMemoryCost(Double memoryCost) {
		this.memoryCost = memoryCost;
	}

	public Double getStoragePrice() {
		return storagePrice;
	}

	public void setStoragePrice(Double storagePrice) {
		this.storagePrice = storagePrice;
	}

	public Double getStorageCost() {
		return storageCost;
	}

	public void setStorageCost(Double storageCost) {
		this.storageCost = storageCost;
	}

	public Double getNetworkPrice() {
		return networkPrice;
	}

	public void setNetworkPrice(Double networkPrice) {
		this.networkPrice = networkPrice;
	}

	public Double getNetworkCost() {
		return networkCost;
	}

	public void setNetworkCost(Double networkCost) {
		this.networkCost = networkCost;
	}

	public Double getLicensePrice() {
		return licensePrice;
	}

	public void setLicensePrice(Double licensePrice) {
		this.licensePrice = licensePrice;
	}

	public Double getLicenseCost() {
		return licenseCost;
	}

	public void setLicenseCost(Double licenseCost) {
		this.licenseCost = licenseCost;
	}

	public Double getExtraPrice() {
		return extraPrice;
	}

	public void setExtraPrice(Double extraPrice) {
		this.extraPrice = extraPrice;
	}

	public Double getExtraCost() {
		return extraCost;
	}

	public void setExtraCost(Double extraCost) {
		this.extraCost = extraCost;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getRunningPrice() {
		return runningPrice;
	}

	public void setRunningPrice(Double runningPrice) {
		this.runningPrice = runningPrice;
	}

	public Double getRunningCost() {
		return runningCost;
	}

	public void setRunningCost(Double runningCost) {
		this.runningCost = runningCost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}

	public Double getActualComputePrice() {
		return actualComputePrice;
	}

	public void setActualComputePrice(Double actualComputePrice) {
		this.actualComputePrice = actualComputePrice;
	}

	public Double getActualComputeCost() {
		return actualComputeCost;
	}

	public void setActualComputeCost(Double actualComputeCost) {
		this.actualComputeCost = actualComputeCost;
	}

	public Double getActualMemoryPrice() {
		return actualMemoryPrice;
	}

	public void setActualMemoryPrice(Double actualMemoryPrice) {
		this.actualMemoryPrice = actualMemoryPrice;
	}

	public Double getActualMemoryCost() {
		return actualMemoryCost;
	}

	public void setActualMemoryCost(Double actualMemoryCost) {
		this.actualMemoryCost = actualMemoryCost;
	}

	public Double getActualStoragePrice() {
		return actualStoragePrice;
	}

	public void setActualStoragePrice(Double actualStoragePrice) {
		this.actualStoragePrice = actualStoragePrice;
	}

	public Double getActualStorageCost() {
		return actualStorageCost;
	}

	public void setActualStorageCost(Double actualStorageCost) {
		this.actualStorageCost = actualStorageCost;
	}

	public Double getActualNetworkPrice() {
		return actualNetworkPrice;
	}

	public void setActualNetworkPrice(Double actualNetworkPrice) {
		this.actualNetworkPrice = actualNetworkPrice;
	}

	public Double getActualNetworkCost() {
		return actualNetworkCost;
	}

	public void setActualNetworkCost(Double actualNetworkCost) {
		this.actualNetworkCost = actualNetworkCost;
	}

	public Double getActualLicensePrice() {
		return actualLicensePrice;
	}

	public void setActualLicensePrice(Double actualLicensePrice) {
		this.actualLicensePrice = actualLicensePrice;
	}

	public Double getActualLicenseCost() {
		return actualLicenseCost;
	}

	public void setActualLicenseCost(Double actualLicenseCost) {
		this.actualLicenseCost = actualLicenseCost;
	}

	public Double getActualExtraPrice() {
		return actualExtraPrice;
	}

	public void setActualExtraPrice(Double actualExtraPrice) {
		this.actualExtraPrice = actualExtraPrice;
	}

	public Double getActualExtraCost() {
		return actualExtraCost;
	}

	public void setActualExtraCost(Double actualExtraCost) {
		this.actualExtraCost = actualExtraCost;
	}

	public Double getActualTotalPrice() {
		return actualTotalPrice;
	}

	public void setActualTotalPrice(Double actualTotalPrice) {
		this.actualTotalPrice = actualTotalPrice;
	}

	public Double getActualTotalCost() {
		return actualTotalCost;
	}

	public void setActualTotalCost(Double actualTotalCost) {
		this.actualTotalCost = actualTotalCost;
	}

	public Double getActualRunningPrice() {
		return actualRunningPrice;
	}

	public void setActualRunningPrice(Double actualRunningPrice) {
		this.actualRunningPrice = actualRunningPrice;
	}

	public Double getActualRunningCost() {
		return actualRunningCost;
	}

	public void setActualRunningCost(Double actualRunningCost) {
		this.actualRunningCost = actualRunningCost;
	}

	public String getActualCurrency() {
		return actualCurrency;
	}

	public void setActualCurrency(String actualCurrency) {
		this.actualCurrency = actualCurrency;
	}

	public Double getActualConversionRate() {
		return actualConversionRate;
	}

	public void setActualConversionRate(Double actualConversionRate) {
		this.actualConversionRate = actualConversionRate;
	}

	public Double getInvoiceComputePrice() {
		return invoiceComputePrice;
	}

	public void setInvoiceComputePrice(Double invoiceComputePrice) {
		this.invoiceComputePrice = invoiceComputePrice;
	}

	public Double getInvoiceComputeCost() {
		return invoiceComputeCost;
	}

	public void setInvoiceComputeCost(Double invoiceComputeCost) {
		this.invoiceComputeCost = invoiceComputeCost;
	}

	public Double getInvoiceMemoryPrice() {
		return invoiceMemoryPrice;
	}

	public void setInvoiceMemoryPrice(Double invoiceMemoryPrice) {
		this.invoiceMemoryPrice = invoiceMemoryPrice;
	}

	public Double getInvoiceMemoryCost() {
		return invoiceMemoryCost;
	}

	public void setInvoiceMemoryCost(Double invoiceMemoryCost) {
		this.invoiceMemoryCost = invoiceMemoryCost;
	}

	public Double getInvoiceStoragePrice() {
		return invoiceStoragePrice;
	}

	public void setInvoiceStoragePrice(Double invoiceStoragePrice) {
		this.invoiceStoragePrice = invoiceStoragePrice;
	}

	public Double getInvoiceStorageCost() {
		return invoiceStorageCost;
	}

	public void setInvoiceStorageCost(Double invoiceStorageCost) {
		this.invoiceStorageCost = invoiceStorageCost;
	}

	public Double getInvoiceNetworkPrice() {
		return invoiceNetworkPrice;
	}

	public void setInvoiceNetworkPrice(Double invoiceNetworkPrice) {
		this.invoiceNetworkPrice = invoiceNetworkPrice;
	}

	public Double getInvoiceNetworkCost() {
		return invoiceNetworkCost;
	}

	public void setInvoiceNetworkCost(Double invoiceNetworkCost) {
		this.invoiceNetworkCost = invoiceNetworkCost;
	}

	public Double getInvoiceLicensePrice() {
		return invoiceLicensePrice;
	}

	public void setInvoiceLicensePrice(Double invoiceLicensePrice) {
		this.invoiceLicensePrice = invoiceLicensePrice;
	}

	public Double getInvoiceLicenseCost() {
		return invoiceLicenseCost;
	}

	public void setInvoiceLicenseCost(Double invoiceLicenseCost) {
		this.invoiceLicenseCost = invoiceLicenseCost;
	}

	public Double getInvoiceExtraPrice() {
		return invoiceExtraPrice;
	}

	public void setInvoiceExtraPrice(Double invoiceExtraPrice) {
		this.invoiceExtraPrice = invoiceExtraPrice;
	}

	public Double getInvoiceExtraCost() {
		return invoiceExtraCost;
	}

	public void setInvoiceExtraCost(Double invoiceExtraCost) {
		this.invoiceExtraCost = invoiceExtraCost;
	}

	public Double getInvoiceTotalPrice() {
		return invoiceTotalPrice;
	}

	public void setInvoiceTotalPrice(Double invoiceTotalPrice) {
		this.invoiceTotalPrice = invoiceTotalPrice;
	}

	public Double getInvoiceTotalCost() {
		return invoiceTotalCost;
	}

	public void setInvoiceTotalCost(Double invoiceTotalCost) {
		this.invoiceTotalCost = invoiceTotalCost;
	}

	public Double getInvoiceRunningPrice() {
		return invoiceRunningPrice;
	}

	public void setInvoiceRunningPrice(Double invoiceRunningPrice) {
		this.invoiceRunningPrice = invoiceRunningPrice;
	}

	public Double getInvoiceRunningCost() {
		return invoiceRunningCost;
	}

	public void setInvoiceRunningCost(Double invoiceRunningCost) {
		this.invoiceRunningCost = invoiceRunningCost;
	}

	public Long getOffTime() {
		return offTime;
	}

	public void setOffTime(Long offTime) {
		this.offTime = offTime;
	}

	public String getPowerState() {
		return powerState;
	}

	public void setPowerState(String powerState) {
		this.powerState = powerState;
	}

	public Date getPowerDate() {
		return powerDate;
	}

	public void setPowerDate(Date powerDate) {
		this.powerDate = powerDate;
	}

	public Double getRunningMultiplier() {
		return runningMultiplier;
	}

	public void setRunningMultiplier(Double runningMultiplier) {
		this.runningMultiplier = runningMultiplier;
	}

	public Date getLastCostDate() {
		return lastCostDate;
	}

	public void setLastCostDate(Date lastCostDate) {
		this.lastCostDate = lastCostDate;
	}

	public Date getLastActualDate() {
		return lastActualDate;
	}

	public void setLastActualDate(Date lastActualDate) {
		this.lastActualDate = lastActualDate;
	}

	public Date getLastInvoiceSyncDate() {
		return lastInvoiceSyncDate;
	}

	public void setLastInvoiceSyncDate(Date lastInvoiceSyncDate) {
		this.lastInvoiceSyncDate = lastInvoiceSyncDate;
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

	public Double getRunningCostRatio() {
		return runningCostRatio;
	}

	public void setRunningCostRatio(Double runningCostRatio) {
		this.runningCostRatio = runningCostRatio;
	}

	public Double getActualRunningCostRatio() {
		return actualRunningCostRatio;
	}

	public void setActualRunningCostRatio(Double actualRunningCostRatio) {
		this.actualRunningCostRatio = actualRunningCostRatio;
	}

	public Double getRunningPriceRatio() {
		return runningPriceRatio;
	}

	public void setRunningPriceRatio(Double runningPriceRatio) {
		this.runningPriceRatio = runningPriceRatio;
	}

	public Double getActualRunningPriceRatio() {
		return actualRunningPriceRatio;
	}

	public void setActualRunningPriceRatio(Double actualRunningPriceRatio) {
		this.actualRunningPriceRatio = actualRunningPriceRatio;
	}

	public Double getTotalCostEstimateRatio() {
		return totalCostEstimateRatio;
	}

	public void setTotalCostEstimateRatio(Double totalCostEstimateRatio) {
		this.totalCostEstimateRatio = totalCostEstimateRatio;
	}

	public Double getTotalPriceEstimateRatio() {
		return totalPriceEstimateRatio;
	}

	public void setTotalPriceEstimateRatio(Double totalPriceEstimateRatio) {
		this.totalPriceEstimateRatio = totalPriceEstimateRatio;
	}

	public Boolean getSummaryInvoice() {
		return summaryInvoice;
	}

	public void setSummaryInvoice(Boolean summaryInvoice) {
		this.summaryInvoice = summaryInvoice;
	}
}
