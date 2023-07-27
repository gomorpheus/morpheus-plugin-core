package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.MorpheusModel;

import java.util.List;

public interface MorpheusSynchronousIdentityService<I extends MorpheusModel> {
	MorpheusIdentityService<I> getDataIdentityService();
	default List<I> listIdentityProjections(DataQuery query) {
		return getDataIdentityService().listIdentityProjections(query).toList().blockingGet();
	}
}
