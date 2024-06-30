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

import java.util.List;

/**
 * A serializable representation of the results of bulk updating Model objects for persistence
 * @author David Estes
 * @since 0.15.2
 * @param <M> the {@link com.morpheusdata.model.MorpheusModel} class this object represents
 */
public class BulkSaveResult<M extends MorpheusModel> extends BulkCreateResult<M>{
	public BulkSaveResult(String msg, String errorCode, List<M> persistedItems, List<M> failedItems) {
		super(msg,errorCode,persistedItems,failedItems);
	}
}
