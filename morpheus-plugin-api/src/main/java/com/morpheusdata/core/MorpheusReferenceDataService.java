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

import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface MorpheusReferenceDataService extends MorpheusDataService<ReferenceData,ReferenceDataSyncProjection>, MorpheusIdentityService<ReferenceDataSyncProjection> {

	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<ReferenceData> referenceData);

	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<ReferenceData> referenceData);

	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<ReferenceDataSyncProjection> removeItems);

	/**
	 * List {@link ReferenceDataSyncProjection } by an Account ID and a category.
	 * @param accountId the ID of the account
	 * @param category a category
	 * @return the matched ReferenceData as a {@link ReferenceDataSyncProjection}
	 */
	@Deprecated(since="0.15.4")
	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategory(Long accountId, String category);

	/**
	 * List {@link ReferenceDataSyncProjection } by an Account ID and a partial category.
	 * @param accountId the ID of the account
	 * @param categorySubString a list of categories
	 * @return the matched ReferenceData as a {@link ReferenceDataSyncProjection}
	 */
	@Deprecated(since="0.15.4")
	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategoryMatch(Long accountId, String categorySubString);

	/**
	 * List {@link ReferenceDataSyncProjection } by an Account ID and multiple categories.
	 * @param accountId the ID of the account
	 * @param categories a partial category to match on.
	 * @return the matched ReferenceData as a {@link ReferenceDataSyncProjection}
	 */
	@Deprecated(since="0.15.4")
	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategories(Long accountId, List<String> categories);

	@Deprecated(since="0.15.4")
	Observable<ReferenceDataSyncProjection> listByCategory(String category);

	@Deprecated(since="0.15.4")
	Observable<ReferenceDataSyncProjection> listByAccountIdAndRefTypeAndRefId(Long AccountId, String refType, String refId);

	@Deprecated(since="0.15.4")
	Observable<ReferenceDataSyncProjection> listByRefTypeAndRefId(String refType, String refId);

	@Deprecated(since="0.15.4")
	Observable<ReferenceData> listByCategoryAndKeyValue(String category, String value);

	@Deprecated(since="0.15.4")
	Single<ReferenceData> findByExternalId(String externalId);

	@Deprecated(since="0.15.4")
	Observable<ReferenceData> listById(List<Long> ids);
}
