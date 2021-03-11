package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.VirtualImage;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

public interface MorpheusVirtualImageContext {
	Observable<VirtualImage> list(Cloud cloud);
	Single<Void> save(List<VirtualImage> virtualImages);
	Single<Void> create(List<VirtualImage> virtualImages);
	Single<Void> update(List<VirtualImage> virtualImages);
	Single<Void> remove(List<VirtualImage> virtualImages);
}
