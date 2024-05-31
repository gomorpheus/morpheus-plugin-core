package com.morpheusdata.request;

import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.Workload;

public class ImportWorkloadRequest {

	/**
	 * The workload to import
	 */
	public Workload workload;

	/**
	 * The source image to import
	 */
	public VirtualImage sourceImage;

	/**
	 * The image to save the import results.
	 */
	public VirtualImage targetImage;

	/**
	 * The base path for saving the image on the storage provider
 	 */
	public String imageBasePath;

	/**
	 * The storage bucket to save the image. The storage provider can be created from the
	 * bucket using {@link com.morpheusdata.core.MorpheusStorageBucketService#getBucketStorageProvider(Long)}.
	 */
	public StorageBucket storageBucket;

	public Workload getWorkload() {
		return workload;
	}

	public void setWorkload(Workload workload) {
		this.workload = workload;
	}

	public VirtualImage getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(VirtualImage sourceImage) {
		this.sourceImage = sourceImage;
	}

	public VirtualImage getTargetImage() {
		return targetImage;
	}

	public void setTargetImage(VirtualImage targetImage) {
		this.targetImage = targetImage;
	}

	public String getImageBasePath() {
		return imageBasePath;
	}

	public void setImageBasePath(String imageBasePath) {
		this.imageBasePath = imageBasePath;
	}

	public StorageBucket getStorageBucket() {
		return storageBucket;
	}

	public void setStorageBucket(StorageBucket storageBucket) {
		this.storageBucket = storageBucket;
	}
}
