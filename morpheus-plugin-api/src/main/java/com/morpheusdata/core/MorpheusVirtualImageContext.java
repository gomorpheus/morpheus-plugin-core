package com.morpheusdata.core;

import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

public interface MorpheusVirtualImageContext {
	Observable<VirtualImageSyncProjection> listVirtualImageSyncMatch(Long cloudId);
	Observable<VirtualImage> listVirtualImagesById(Collection<Long> ids);
	Single<VirtualImage> save(List<VirtualImage> virtualImages);
	Single<VirtualImage> create(List<VirtualImage> virtualImages);
	Single<Void> remove(List<VirtualImageSyncProjection> virtualImages);
}
