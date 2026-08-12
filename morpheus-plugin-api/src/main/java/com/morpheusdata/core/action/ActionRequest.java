package com.morpheusdata.core.action;


import com.morpheusdata.core.providers.ActionProvider;
import com.morpheusdata.model.projection.UserIdentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes a request to evaluate or run an {@link ActionProvider}. Carries the
 * provider coordinates ({@code namespace}/{@code key}), the target reference
 * ({@code refType}/{@code refId} the provider loads its model from), the invoking
 * {@code user}, and a free-form {@code data} map. For bulk actions the request can
 * carry multiple target ids in {@code refIds}; {@code refId} remains the single-target
 * convenience and stays populated with the first id when a bulk list is supplied.
 */
public class ActionRequest {

	public String namespace;
	public String key;
	public String refType;
	public Long refId;
	public List<Long> refIds = new ArrayList<>();
	public UserIdentity user;
	public Map data;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public List<Long> getRefIds() {
		return refIds;
	}

	public void setRefIds(List<Long> refIds) {
		this.refIds = refIds;
	}

	public UserIdentity getUser() {
		return user;
	}

	public void setUser(UserIdentity user) {
		this.user = user;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}
}
