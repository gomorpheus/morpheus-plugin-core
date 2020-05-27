package com.morpheusdata.model;

public class Request extends MorpheusModel {

//	public Account approverAccount;
	public ApprovalRequestType requestType;
	public String externalId;
	public String externalName;

	public enum ApprovalRequestType {
		EXTENSION_APPROVAL_TYPE,
		INSTANCE_APPROVAL_TYPE,
		APP_APPROVAL_TYPE,
		SHUTDOWN_EXTENSION_APPROVAL_TYPE
	}
}
