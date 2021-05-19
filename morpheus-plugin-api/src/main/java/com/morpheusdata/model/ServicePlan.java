package com.morpheusdata.model;

import com.morpheusdata.model.projection.ServicePlanIdentityProjection;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Provides a means to set predefined tiers on memory, storage, cores, and cpu.
 */
public class ServicePlan extends ServicePlanIdentityProjection {
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account owner;
	public String category;
	public String description;
	public String visibility = "public"; //['public', 'private'];
	public Boolean active = true;
	public Boolean deleted = false;
	public Boolean upgradeable = false;
	public Date dateCreated;
	public Date lastUpdated;
	public String internalId;
	public String configs;
	public String serverType;
	public String serverClass; //hardware classes on clouds;
	public Integer sortOrder = 0;
	public String provisionTypeCode;
	public Boolean editable;
	public Long maxCores;
	public Long maxMemory;
	public Long maxStorage;
	public Long maxLog;
	public Long maxCpu;
	public Long coresPerSocket;
	public Long maxDataStorage = 0L;
	public Long minDisks = 1L;
	public Long maxDisks;
	public Boolean customCpu = false;
	public Boolean customCores = false;
	public Boolean customMaxStorage = false;
	public Boolean customMaxDataStorage = false;
	public Boolean customMaxMemory = false;
	public Boolean addVolumes = false; // whether multiple volumes are supported;
	public String memoryOptionSource;
	public String cpuOptionSource;
	public String coresOptionSource;
	public Double internalCost;
	public Double externalCost;
	public ProvisionType provisionType;
	public String regionCode;
	public String refType;
	public Long refId;
	public String tagMatch; //to match discovered servers into a service plan;
	public Boolean hidden = false;
	public String instanceFilter;
	public Boolean provisionable = true;
	public Boolean deletable = true;
	public Boolean noDisks = false;
	public BigDecimal price_monthly;
	public BigDecimal price_hourly;
}
