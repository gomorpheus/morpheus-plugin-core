package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.core.data.DataQueryResult;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

/**
 * Provides a standard base service interface to extends so all services provide consistent crud methods and finders
 * @author bdwheeler
 * @since 0.15.1
 * @param <M> The Model class type for this service
 */
public interface MorpheusDataService<M> {

	//crud operations
	Single<M> create(M item);

	Single<Boolean> create(List<M> items);

	Single<M> save(M item);

	Single<Boolean> save(List<M> items);

	Single<Boolean> remove(M items);

	Single<Boolean> remove(List<M> items);

	//generic list and get
	Single<Long> count(DataQuery query);

	Maybe<M> get(Long id);

	Observable<M> listById(List<Long> ids);

	Single<DataQueryResult> search(DataQuery query);

	Observable<M> list(DataQuery query);

	Observable<Map> listOptions(DataQuery query);

	default Maybe<M> find(DataQuery query) {
		return list(query).firstElement();
	}

}
