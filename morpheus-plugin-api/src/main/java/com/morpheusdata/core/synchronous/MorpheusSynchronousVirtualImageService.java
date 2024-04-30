package com.morpheusdata.core.synchronous;

import com.bertramlabs.plugins.karman.CloudFile;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusVirtualImageTypeService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.User;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;

public interface MorpheusSynchronousVirtualImageService extends MorpheusSynchronousDataService<VirtualImage, VirtualImageIdentityProjection>, MorpheusSynchronousIdentityService<VirtualImage> {

	/**
	 * The context for VirtualIMageType
	 */
	MorpheusSynchronousVirtualImageTypeService getType();


	/**
	 * Get a one-off url for an image to upload to it to a cloud
	 * @since 0.15.13
	 * @param virtualImage the image
	 * @param cloudFile the specific file
	 * @param createdBy the user associated with the workload or server
	 * @param cloud the Cloud instance
	 * @return the url of the image file
	 */
	String getCloudFileStreamUrl(VirtualImage virtualImage, CloudFile cloudFile, User createdBy, Cloud cloud);
}
