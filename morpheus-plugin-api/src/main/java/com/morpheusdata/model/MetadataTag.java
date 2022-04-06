package com.morpheusdata.model;

import com.morpheusdata.model.projection.MetadataTagIdentityProjection;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a Model representation of a MetadataTag. These tags are used throughout Morpheus
 * to associate names to various Morpheus constructs.
 */
public class MetadataTag extends MetadataTagIdentityProjection {

	protected MetadataTagType type;
	protected String value;
	protected Boolean masked = false;

	/**
	 * Returns the type of this tag
	 * @return the type of this tag
	 */
	public MetadataTagType getType() {
		return type;
	}

	/**
	 * Sets the type of this tag
	 * @param type the type of this tag
	 */
	public void setType(MetadataTagType type) {
		this.type = type;
	}

	/**
	 * Returns the value associated to this tag
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value for this tag
	 * @param value the value for this tag
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns whether this tag should be masked in the UI
	 * @return the masked
	 */
	public Boolean getMasked() {
		return masked;
	}

	/**
	 * Sets whether this tag should be masked in the UI
	 * @param masked whether this tag should be masked in the UI
	 */
	public void setMasked(Boolean masked) {
		this.masked = masked;
	}
}
