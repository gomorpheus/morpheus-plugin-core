package com.morpheusdata.model;

public class RequestChangeDetail {

	private Category category;
	private Type type;
	private String name;
	private String fromValue;
	private String toValue;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}


	public String getRefId() {
		return name;
	}

	public void setRefId(String name) {
		this.name = name;
	}


	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}


	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public enum Category {
		network,
		volume,
		plan,
		planMemory,
		planCores,
		planCoresPerSocket
	}

	public enum Type {
		increase,
		decrease,
		add,
		remove,
		change
	}
}
