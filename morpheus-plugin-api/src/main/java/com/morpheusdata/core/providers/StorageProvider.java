package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
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
public interface StorageProvider extends PluginProvider {
	ServiceResponse verifyStorageServer(StorageServer storageServer, Map opts);

	ServiceResponse initializeStorageServer(StorageServer storageServer, Map opts);

	ServiceResponse refreshStorageServer(StorageServer storageServer, Map opts);

	/**
	 * Provide a {@link StorageServerType} to be added to the morpheus environment
	 * as the type for this {@link StorageServer}. The StorageServerType also defines the
	 * OptionTypes for configuration of a new server and its volume types.
	 * @return StorageServerType
	 */
	StorageServerType getStorageServerType();

	// /**
	//  * Provide custom configuration options when creating a new {@link StorageServer}
	//  * @return a Collection of OptionType
	//  */
	// Collection<OptionType> getStorageServerOptionTypes();

	/**
	 * Grabs the description for the StorageProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the Storage Server Integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();


}
