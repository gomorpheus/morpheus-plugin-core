package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;
import java.util.Map;

/**
 * Represents a {@link StorageServerType} implementation for creating buckets, volumes and file shares.
 * Depending on the capabilities of the referenced storage server one can add implementations for the additional
 * interfaces such as {@link StorageProviderFileShares} or {@link StorageProviderBuckets} or {@link StorageProviderVolumes}.
 * @author David Estes
 * @since 0.15.1
 * @see StorageProviderFileShares
 * @see StorageProviderVolumes
 * @see StorageProviderBuckets
 */
public interface StorageProvider {
	ServiceResponse verifyStorageServer(StorageServer storageServer, Map opts);

	ServiceResponse initializeStorageServer(StorageServer storageServer, Map opts);

	void refresh(StorageServer storageServer, Map opts);

	/**
	 * Provide custom configuration options when creating a new {@link StorageServer}
	 * @return a List of OptionType
	 */
	List<OptionType> getStorageServerOptionTypes();

	/**
	 * Returns the Storage Server Integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 * @since 0.12.3
	 */
	Icon getIcon();

}
