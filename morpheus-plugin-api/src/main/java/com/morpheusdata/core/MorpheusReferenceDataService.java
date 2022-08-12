package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

public interface MorpheusReferenceDataService {

	Single<Boolean> create(List<ReferenceData> referenceData);

	Single<Boolean> save(List<ReferenceData> referenceData);

	Single<Boolean> remove(List<ReferenceDataSyncProjection> removeItems);

	Observable<ReferenceDataSyncProjection> listByAccountIdAndCategory(Long AccountId, String category);

	Observable<ReferenceDataSyncProjection> listByCategory(String category);

	Observable<ReferenceDataSyncProjection> listByAccountIdAndRefTypeAndRefId(Long AccountId, String refType, String refId);

	Observable<ReferenceDataSyncProjection> listByRefTypeAndRefId(String refType, String refId);

	Single<ReferenceData> findByExternalId(String externalId);

	Observable<ReferenceData> listById(List<Long> ids);

	Single<ReferenceData> get(Long id);

}
