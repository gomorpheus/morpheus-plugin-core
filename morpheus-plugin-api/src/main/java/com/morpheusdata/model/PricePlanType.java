package com.morpheusdata.model;

/**
 * A Price Plan Type is usually used in a reference to {@link PricePlan}. It specified the type
 * the price plan is, such as if it is a load balancer, virtual image, snapshot, etc.
 *
 * @author Dan DeVilbiss
 * @since 0.15.3
 */
public class PricePlanType extends MorpheusModel {

	protected String name;
	protected String code;

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
		markDirty("code", code);
	}
}
