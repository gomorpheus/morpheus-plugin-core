package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.MetadataTagTypeIdentityProjection;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a Model representation of a MetadataTagType. These types are associated to MetadataTags.
 */
public class MetadataTagType extends MetadataTagTypeIdentityProjection {

	protected String valueType;

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
}
