package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;

import java.util.List;

public interface MorpheusSynchronousDataIdentityService<I> {
	MorpheusDataIdentityService<I> getDataIdentityService();
	default List<I> listIdentityProjections(DataQuery query) {
		return getDataIdentityService().listIdentityProjections(query).toList().blockingGet();
	}
}
