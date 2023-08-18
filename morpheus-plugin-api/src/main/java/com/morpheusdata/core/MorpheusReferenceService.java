package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.MorpheusModel;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.util.Map;

/**
 * This interface is a standard base reference service for all supporting reference data services to provide consistent lookup methods
 * @author bdwheeler
 * @since 0.16.1
 * @param <M> The {@link com.morpheusdata.model.MorpheusModel} class type for this service to query against
 */
public interface MorpheusReferenceService<M extends MorpheusModel> {

	Maybe<M> get(Long id);

	Observable<M> listAll();

	Observable<M> listAllOptions();

}
