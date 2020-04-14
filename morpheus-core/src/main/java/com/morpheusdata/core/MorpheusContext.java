package com.morpheusdata.core;

/**
 * Provides a means to interact or query data from the main Morpheus application back into the various provider extensions
 * It is important to note that most methods in the context are asynchronous and rely on RxJava based interfaces so as
 * to present the ability for the implementation of the MorpheusContext to be disconnected from the core application.
 *
 * The MorpheusContext typically provides getters for multiple subcontexts. These Sub Context interfaces are useful for
 * organizing calls so as to reduce the size of the individual Context Class
 *
 * (i.e. a Connector app could implement the MorpheusContext and relay communication back to the Morpheus Application itself)
 *
 * @see MorpheusComputeContext
 * @see MorpheusNetworkContext
 *
 * @author David Estes
 */
public interface MorpheusContext {

	/**
	 * Returns the Compute Context used for performing updates or queries on compute related assets within Morpheus
	 * @return An Instance of the Compute Context to be used typically by {@link CloudProvider} implementations.
	 */
	MorpheusComputeContext getCompute();


	/**
	 * Returns the NetworkContext used for performing updates or queries on network related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return And Instance of the Network Context to be used for calls by various network providers
	 */
	MorpheusNetworkContext getNetwork();


	/**
	 * Returns the MorpheusBackupContext used for performing updates or queries on backup related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return And Instance of the Backup Context to be used for calls by various backup providers
	 */
	MorpheusBackupContext getBackup();

	/**
	 * Returns the MorpheusBackupContext used for performing updates or queries on backup related assets within Morpheus.
	 * Typically this would be called by a ProvisionProvider or
	 * @return And Instance of the Backup Context to be used for calls by various backup providers
	 */
	MorpheusProvisionContext getProvision();

	//Common methods used across various contexts

	//TODO: Add Locking Provider RPC Calls to acquire distributed locks when necessary

}
