package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusVirtualImageTypeService;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;

public interface MorpheusSynchronousVirtualImageService extends MorpheusSynchronousDataService<VirtualImage, VirtualImageIdentityProjection>, MorpheusSynchronousIdentityService<VirtualImage> {

	/**
	 * The context for VirtualIMageType
	 */
	MorpheusSynchronousVirtualImageTypeService getType();

}
