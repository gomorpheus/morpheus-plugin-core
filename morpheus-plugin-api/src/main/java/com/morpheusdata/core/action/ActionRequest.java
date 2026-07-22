package com.morpheusdata.core.action;


import com.morpheusdata.core.providers.ActionProvider;
import com.morpheusdata.model.User;

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
	public User user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}
}
