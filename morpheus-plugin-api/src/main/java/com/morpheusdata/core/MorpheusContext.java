package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.LogLevel;
import com.morpheusdata.model.TaskResult;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

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

	MorpheusTaskContext getTask();



	//Common methods used across various contexts
	Single<TaskResult> executeSshCommand(String address, Integer port, String username, String password, String command, String publicKey, String privateKey, String passPhrase, Boolean ignoreExitStatus, ComputeServer computeServer, LogLevel logLevel, Boolean doPty, String runAsUser, Boolean sudo);
	Single<TaskResult> executeWindowsCommand(String address, Integer port, String username, String password, String command, Boolean noProfile, Boolean elevated);
	Single<TaskResult> executeCommandOnWorkload(Container container, String command);
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command);
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command, Boolean rpc, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile);

	//TODO: Add Locking Provider RPC Calls to acquire distributed locks when necessary

}
