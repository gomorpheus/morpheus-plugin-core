package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.OptionType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link OptionType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusOptionTypeService extends MorpheusDataService<OptionType, OptionType> {

	/**
	 * Get a list of OptionType objects from a list of projection ids
	 * @param ids OptionType ids
	 * @return Observable stream of OptionTypes
	 */
	@Deprecated(since="0.15.4")
	Observable<OptionType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing OptionTypes
	 * @param optionTypes updated OptionType
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<OptionType> optionTypes);

	/**
	 * Create new OptionTypes in Morpheus
	 * @param optionTypes new OptionTypes to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<OptionType> optionTypes);

	/**
	 * Remove persisted OptionType from Morpheus
	 * @param optionTypes to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<OptionType> optionTypes);

}
