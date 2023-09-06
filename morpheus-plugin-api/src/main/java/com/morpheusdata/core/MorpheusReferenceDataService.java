package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

public interface MorpheusReferenceDataService extends MorpheusDataService<ReferenceData,ReferenceDataSyncProjection> {

	Single<Boolean> create(List<ReferenceData> referenceData);

	Single<Boolean> save(List<ReferenceData> referenceData);

	Single<Boolean> remove(List<ReferenceDataSyncProjection> removeItems);

	/**
	 * List {@link ReferenceDataSyncProjection } by an Account ID and a category.
	 * @param accountId the ID of the account
	 * @param category a category
	 * @return the matched ReferenceData as a {@link ReferenceDataSyncProjection}
	 */
	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategory(Long accountId, String category);

	/**
	 * List {@link ReferenceDataSyncProjection } by an Account ID and a partial category.
	 * @param accountId the ID of the account
	 * @param categorySubString a list of categories
	 * @return the matched ReferenceData as a {@link ReferenceDataSyncProjection}
	 */
	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategoryMatch(Long accountId, String categorySubString);

	/**
	 * List {@link ReferenceDataSyncProjection } by an Account ID and multiple categories.
	 * @param accountId the ID of the account
	 * @param categories a partial category to match on.
	 * @return the matched ReferenceData as a {@link ReferenceDataSyncProjection}
	 */
	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategories(Long accountId, List<String> categories);

	Observable<ReferenceDataSyncProjection> listByCategory(String category);

	Observable<ReferenceDataSyncProjection> listByAccountIdAndRefTypeAndRefId(Long AccountId, String refType, String refId);

	Observable<ReferenceDataSyncProjection> listByRefTypeAndRefId(String refType, String refId);

	Observable<ReferenceData> listByCategoryAndKeyValue(String category, String value);

	Single<ReferenceData> findByExternalId(String externalId);

	Observable<ReferenceData> listById(List<Long> ids);
}
