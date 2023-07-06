package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
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
 * @param <T> The Model class type for this service
 */
public interface MorpheusDataService<T> {

	//crud operations
	Single<T> create(T item);

	Single<Boolean> create(List<T> items);

	Single<T> save(T item);

	Single<Boolean> save(List<T> items);

	Single<Boolean> remove(T items);

	Single<Boolean> remove(List<T> items);

	//generic list and get
	Single<Long> count(DataQuery query);

	Maybe<T> get(Long id);

	Observable<T> listById(List<Long> ids);

	Observable<T> list(DataQuery query);

	Observable<Map> listOptions(DataQuery query);

	default Maybe<T> find(DataQuery query) {
		return list(query).firstElement();
	}

}
