package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.synchronous.MorpheusSynchronousMetadataTagTypeService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.MetadataTag;
import com.morpheusdata.model.MetadataTagType;
import com.morpheusdata.model.projection.MetadataTagIdentityProjection;

public interface MorpheusSynchronousMetadataTagService extends MorpheusSynchronousDataService<MetadataTag, MetadataTagIdentityProjection>, MorpheusSynchronousIdentityService<MetadataTagIdentityProjection> {

	/**
	 * Returns the MetadataTypeType context used for performing updates or queries on {@link MetadataTagType} related assets within Morpheus.
	 * @return An instance of the MetadataTypeType Context
	 */
	MorpheusSynchronousMetadataTagTypeService getMetadataTagType();
	
}
