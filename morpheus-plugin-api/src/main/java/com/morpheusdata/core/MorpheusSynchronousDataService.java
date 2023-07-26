package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;

import java.util.List;
import java.util.Map;

public interface MorpheusSynchronousDataService<T> {

	MorpheusDataService<T> getDataService();
	//crud operations
	default T create(T item) {
		return getDataService().create(item).blockingGet();
	}

	default Boolean create(List<T> items) {
		return getDataService().create(items).blockingGet();
	}

	default T save(T item) {
		return getDataService().save(item).blockingGet();
	}

	default Boolean save(List<T> items) {
		return getDataService().save(items).blockingGet();
	}

	default Boolean remove(T items) {
		return getDataService().remove(items).blockingGet();
	}

	default Boolean remove(List<T> items) {
		return getDataService().remove(items).blockingGet();
	}

	//generic list and get
	default Long count(DataQuery query) {
		return getDataService().count(query).blockingGet();
	}

	default T get(Long id) {
		return getDataService().get(id).blockingGet();
	}

	default List<T> listById(List<Long> ids) {
		return getDataService().listById(ids).toList().blockingGet();
	}

	default List<T> list(DataQuery query) {
		return getDataService().list(query).toList().blockingGet();
	}

	default List<Map> listOptions(DataQuery query) {
		return getDataService().listOptions(query).toList().blockingGet();
	}

	default T find(DataQuery query) {
		return getDataService().find(query).blockingGet();
	}
}
