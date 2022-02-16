package com.morpheusdata.model;

/**
 * A Policy Type is usually used in a reference to {@link Policy}. It specified the type
 * the policy is, such as if it is an instance naming policy, max policy, approval policy, etc.
 *
 * @author David Estes
 * @since 0.12.2
 */
public class PolicyType extends MorpheusModel {

	protected String name;
	protected String code;

	protected String description;
	protected String category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code",code);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description",description);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category",category);
	}
}
