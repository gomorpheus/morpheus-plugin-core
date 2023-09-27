package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;

public interface MorpheusSynchronousVirtualImageService extends MorpheusSynchronousDataService<VirtualImage, VirtualImageIdentityProjection>, MorpheusSynchronousIdentityService<VirtualImage> {
}
