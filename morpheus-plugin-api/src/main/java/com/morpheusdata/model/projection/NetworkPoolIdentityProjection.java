package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.MorpheusNetworkPoolService;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.NetworkPoolType;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkPool} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusNetworkPoolService
 * @author David Estes
 */
public class NetworkPoolIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String typeCode;
	protected String category;
	protected Long accountId;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param category the category of the object
	 * @param accountId the account id of the object
	 */
	public NetworkPoolIdentityProjection(Long id, String externalId, String category, Long accountId) {
		this.id = id;
		this.externalId = externalId;
		this.category = category;
		this.accountId = accountId;
	}

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param typeCode the type code from the {@link NetworkPoolType}
	 */
	public NetworkPoolIdentityProjection(Long id, String externalId,String typeCode) {
		this.id = id;
		this.externalId = externalId;
		this.typeCode = typeCode;
	}

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param category the category of the object
	 * @param accountId the account id of the object
	 */
	public NetworkPoolIdentityProjection(Long id, String externalId, String typeCode, String category, Long accountId) {
		this.id = id;
		this.externalId = externalId;
		this.typeCode = typeCode;
		this.category = category;
		this.accountId = accountId;
	}

	public NetworkPoolIdentityProjection() {
		//
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the network pool. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId",externalId);
	}

	/**
	 * Gets the unique code correlating to the {@link NetworkPoolType} this pool belongs to. Pool type codes are globally unique.
	 *
	 * @return the code correlating to the {@link NetworkPoolType} this record belongs to.
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * Sets the unique code correlating to the {@link NetworkPoolType} this pool belongs to. Pool type codes are globally unique.
	 *
	 * @param typeCode the code correlating to the {@link NetworkPoolType} this record belongs to.
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
		markDirty("typeCode", typeCode);
	}

	/*
	 * Returns the category of the equivalent object.
	 * @return the category of the current record
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category of the network pool. In this class this should not be called directly
	 * @param category of the current record
	 */
	public void setCategory(String category) {
		this.category = category;
		markDirty("category",category);
	}

	/**
	 * returns the account ID of the equivalent object.
	 * @return the account ID of the current record
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * Sets the account ID of the network pool. In this class this should not be called directly
	 * @param accountId account ID of the current record
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
		markDirty("accountId",accountId);
	}
}
