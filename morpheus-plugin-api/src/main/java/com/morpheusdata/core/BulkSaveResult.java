package com.morpheusdata.core;

import com.morpheusdata.model.MorpheusModel;

import java.util.List;

/**
 * A serializable representation of the results of bulk updating Model objects for persistence
 * @author David Estes
 * @since 0.15.2
 * @param <M> the {@link com.morpheusdata.model.MorpheusModel} class this object represents
 */
public class BulkSaveResult<M extends MorpheusModel> extends BulkCreateResult<M>{
	BulkSaveResult(String msg, String errorCode, List<M> persistedItems, List<M> failedItems) {
		super(msg,errorCode,persistedItems,failedItems);
	}
}
