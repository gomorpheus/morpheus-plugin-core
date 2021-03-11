package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.VirtualImage;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

public interface MorpheusVirtualImageContext {
	Observable<VirtualImage> listVirtualImages(Cloud cloud);
	Single<Void> saveVirtualImage(List<VirtualImage> virtualImages);
	Single<Void> updateVirtualImage(List<VirtualImage> virtualImages);
	Single<Void> removeVirtualImage(List<VirtualImage> virtualImages);
}
