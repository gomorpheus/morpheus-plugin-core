package com.morpheusdata.response;

import com.morpheusdata.model.VirtualImage;

public class ImportWorkloadResponse {

	// the virtual image that was created by the container import
	public VirtualImage virtualImage;
	// path to the image on the storage provider
	public String imagePath;
}
