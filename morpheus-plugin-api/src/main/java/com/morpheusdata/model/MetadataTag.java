/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
