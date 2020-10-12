package com.morpheusdata.model;

public class CloudType extends MorpheusModel {
	private String name;
	private String description;
	private String code;
	private String category;
	private String computeService;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getComputeService() {
		return computeService;
	}

	public void setComputeService(String computeService) {
		this.computeService = computeService;
	}
}
