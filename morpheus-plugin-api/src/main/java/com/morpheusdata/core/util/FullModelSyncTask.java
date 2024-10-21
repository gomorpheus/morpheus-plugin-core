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

package com.morpheusdata.core.util;

import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This Utility Class provides an rxJava compatible means for syncing remote API objects with local/morpheus backed models
 * in a persistent database. This handles an efficeint way to match full model objects with api objects and batches
 * updates to the backend database for efficient sync. This, along with the {@link SyncTask} should be considered the standard method for caching objects
 * within a {@link CloudProvider} and many other provider types. The **Key** difference between this and the {@link SyncTask} is that this method does not require
 * you to define a projection/identity class and can just sync the model itself.
 *
 * @author David Estes
 * @param <ApiItem> The Class Object representing the individual API result object coming back in the Collection
 * @param <Model> The Model Class that the Projection Class is a subset of. This is the Class that needs to be updated
 *                with changes
 */
public class FullModelSyncTask<ApiItem,Model>  extends SyncTask<Model,ApiItem,Model> {
	public FullModelSyncTask(Observable<Model> domainRecords, Collection<ApiItem> apiItems) {
		super(domainRecords, apiItems);
		this.withLoadObjectDetails((List<UpdateItemDto<Model, ApiItem>> updateItems) -> {
			ArrayList<UpdateItem<Model,ApiItem>> currentModel = new ArrayList<>();
			for(UpdateItemDto<Model, ApiItem> updateItem : updateItems) {
				UpdateItem<Model,ApiItem> translatedUpdateItem = new UpdateItem<>();
				translatedUpdateItem.existingItem = updateItem.existingItem;
				translatedUpdateItem.masterItem = updateItem.masterItem;
				currentModel.add(translatedUpdateItem);
			}
			return Observable.fromIterable(currentModel);
			} );
	}
}
