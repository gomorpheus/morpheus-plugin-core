package com.morpheusdata.core.library;

import com.morpheusdata.model.OptionType;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link OptionType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusOptionTypeService {

	/**
	 * Get a {@link OptionType} by id.
	 * @param id option type id
	 * @return Observable stream of sync projection
	 */
	Single<OptionType> get(Long id);

	/**
	 * Get a list of OptionType objects from a list of projection ids
	 * @param ids OptionType ids
	 * @return Observable stream of OptionTypes
	 */
	Observable<OptionType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing OptionTypes
	 * @param optionTypes updated OptionType
	 * @return success
	 */
	Single<Boolean> save(List<OptionType> optionTypes);

	/**
	 * Create new OptionTypes in Morpheus
	 * @param optionTypes new OptionTypes to persist
	 * @return success
	 */
	Single<Boolean> create(List<OptionType> optionTypes);

	/**
	 * Remove persisted OptionType from Morpheus
	 * @param optionTypes to delete
	 * @return success
	 */
	Single<Boolean> remove(List<OptionType> optionTypes);

}
