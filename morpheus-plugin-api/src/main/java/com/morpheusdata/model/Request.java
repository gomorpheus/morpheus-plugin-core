package com.morpheusdata.model;

import java.util.List;

public class Request extends MorpheusModel {

//	public Account approverAccount;
	public ApprovalRequestType requestType;
	public String externalId;
	public String externalName;
	public List<RequestReference> refs;

	public enum ApprovalRequestType {
		EXTENSION_APPROVAL_TYPE,
		INSTANCE_APPROVAL_TYPE,
		APP_APPROVAL_TYPE,
		SHUTDOWN_EXTENSION_APPROVAL_TYPE
	}
}
