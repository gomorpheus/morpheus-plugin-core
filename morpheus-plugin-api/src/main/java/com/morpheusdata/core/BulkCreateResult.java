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

package com.morpheusdata.core;

import com.morpheusdata.model.MorpheusModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A serializable representation of the results of bulk creating Model objects for persistence
 * @author David Estes
 * @since 0.15.2
 * @param <M> the {@link com.morpheusdata.model.MorpheusModel} class this object represents
 */
public class BulkCreateResult<M extends MorpheusModel> {

	/**
	 * A List of {@link com.morpheusdata.model.MorpheusModel} objects that were successfully persisted. Also including
	 * their result identifier.
	 */
	private List<M> persistedItems = new ArrayList<>();
	/**
	 * A List of failed {@link com.morpheusdata.model.MorpheusModel} objects that were not saved.
	 */
	private List<M> failedItems = new ArrayList<>();

	/**
	 * Optional Error Message response String. Ideally an errorCode should be used for better localization.
	 */
	private String msg=null;

	/**
	 * An i18n Property key representing a String localized error message.
	 */
	protected String errorCode=null;

	public BulkCreateResult(String msg, String errorCode, List<M> persistedItems, List<M> failedItems) {
		this.msg = msg;
		this.errorCode = errorCode;
		this.persistedItems = persistedItems;
		this.failedItems = failedItems;
	}


	/**
	 * A helper method for determining whether the operation was fully successful.
	 * @return the success state of the operation
	 */
	public Boolean getSuccess() {
		return persistedItems.size() > 0 && failedItems.size() == 0 && errorCode == null;
	}

	/**
	 * A helper method for checking if there were failed items as part of the operation.
	 * @return the failure state of the operation
	 */
	public Boolean hasFailures() {
		return failedItems.size() > 0;
	}

	/**
	 * A helper method for checking if there aer persisted items or not
	 * @return
	 */
	public Boolean hasPersistedItems() {
		return persistedItems.size() > 0;
	}

	/**
	 * Gets an i18n Property key representing a String localized error message.
	 * @return An i18n Property key representing a String localized error message.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Gets a collection of the persisted {@link com.morpheusdata.model.MorpheusModel} objects.
	 * @return A List of {@link com.morpheusdata.model.MorpheusModel} objects that were successfully persisted. Also including
	 * their result identifier.
	 */
	public List<M> getPersistedItems() {
		return persistedItems;
	}

	/**
	 * Gets a collection of the failed {@link com.morpheusdata.model.MorpheusModel} objects.
	 * @return A List of {@link com.morpheusdata.model.MorpheusModel} objects that failed ot persist.
	 */
	public List<M> getFailedItems() {
		return failedItems;
	}

	/**
	 * Returns an optional message string containing more error details in a humange readable form.
	 * Ideally an errorCode would be used for localization.
	 * @return an optional error message
	 */
	public String getMsg() {
		return msg;
	}

}
