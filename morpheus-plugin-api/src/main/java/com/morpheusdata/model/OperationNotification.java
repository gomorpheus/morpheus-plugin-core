package com.morpheusdata.model;

import java.util.Date;
import com.morpheusdata.model.projection.OperationNotificationIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class OperationNotification extends OperationNotificationIdentityProjection {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String category;
	protected String eventKey;
	protected Boolean acknowledged;
	protected Date acknowledgedDate;
	protected String acknowledgedByUser;
	protected Date startDate;
	protected String resourceName;
	protected String status; // unknown, ok, warning, error
	protected String statusMessage;
	protected String uniqueId;
	protected String refType;
	protected Long refId;

	/**
	 * The account related to this OperationNotification
	 * @return the Account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Sets the account related to this OperationNotification
	 * @param account Account
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * The category related to this OperationNotification
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category for this OperationNotification
	 * @param category Category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * The key for this OperationNotification
	 * @return eventKey
	 */
	public String getEventKey() {
		return eventKey;
	}

	/**
	 * Sets a key for this OperationNotification. For example, the VMware alarm key
	 * @param eventKey the event key
	 */
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	/**
	 * Returns if this OperationNotification has been acknowledged
	 * @return acknowledged
	 */
	public Boolean getAcknowledged() {
		return acknowledged;
	}

	/**
	 * Sets whether the OperationNotification has been acknowledged
	 * @param acknowledged acknowledged
	 */
	public void setAcknowledged(Boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	/**
	 * Returns the date that this OperationNotification was acknowledged
	 * @return acknowledgedDate
	 */
	public Date getAcknowledgedDate() {
		return acknowledgedDate;
	}

	/**
	 * Sets the date that this OperationNotification was acknowledged.
	 * @param acknowledgedDate acknowledgedDate
	 */
	public void setAcknowledgedDate(Date acknowledgedDate) {
		this.acknowledgedDate = acknowledgedDate;
	}

	/**
	 * Returns the user that acknowledged this OperationNotification
	 * @return acknowledgedByUser
	 */
	public String getAcknowledgedByUser() {
		return acknowledgedByUser;
	}

	/**
	 * Sets the user that acknowledged this this OperationNotification.
	 * @param acknowledgedByUser acknowledgedByUser
	 */
	public void setAcknowledgedByUser(String acknowledgedByUser) {
		this.acknowledgedByUser = acknowledgedByUser;
	}

	/**
	 * Returns the date that this OperationNotification started
	 * @return startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the date that this OperationNotification started
	 * @param startDate startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns the underlying resource name for this OperationNotification
	 * @return resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Sets the underlying resource name for this OperationNotification.
	 * @param resourceName resourceName
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * Returns the status for this OperationNotification
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status for this OperationNotification. Should be ok, unknown, warning, or error.
	 * @param status status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the status message for this OperationNotification
	 * @return statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Sets the status message for this OperationNotification.
	 * @param statusMessage statusMessage
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * Returns the uniqueId for this OperationNotification
	 * @return uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Sets uniqueId for the OperationNotification. Morpheus ignores this value.
	 * @param uniqueId uniqueId
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Returns the refType for this this OperationNotification.
	 * @return refType
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * Sets the refType for this OperationNotification. Set to the camelcase class name related to this OperationNotification.
	 * For example, 'computeZone', 'networkLoadBalancer', 'computeServer', 'datastore', etc
	 * @param refType refType
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * Returns the refId for this OperationNotification
	 * @return refId
	 */
	public Long getRefId() {
		return refId;
	}

	/**
	 * Sets the refId for this OperationNotification. Set to the id of the object related to this OperationNotification.
	 * @param refId refId
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
