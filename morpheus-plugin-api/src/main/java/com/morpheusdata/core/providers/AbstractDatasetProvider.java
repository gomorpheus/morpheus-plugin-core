package com.morpheusdata.core.providers;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.data.DatasetInfo;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.Map;

/**
 * Base class 
 * @author bdwheeler
 * @since 0.15.1
 * @param <T> The Model class type for this dataset - or just basic types for fixed lists
 * @param <V> The "value" or identifier type on the map of option items - usually a long or a string
 */
public abstract class AbstractDatasetProvider<T, V> implements DatasetProvider<T, V> {

	public static DatasetInfo datasetInfo;
	public Plugin plugin;
	public MorpheusContext morpheusContext;

	public MorpheusContext getMorpheus() {
		return morpheusContext;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public DatasetInfo getInfo() {
		return datasetInfo;
	}

	public String getCode() {
		String rtn = null;
		if(datasetInfo != null)
			rtn = datasetInfo.getDatasetCode();
		return rtn;
	}

	public String getName() {
		String rtn = null;
		if(datasetInfo != null)
			rtn = datasetInfo.name;
		return rtn;
	}
	
}
