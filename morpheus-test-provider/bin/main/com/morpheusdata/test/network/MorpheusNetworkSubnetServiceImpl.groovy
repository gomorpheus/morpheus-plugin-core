package com.morpheusdata.test.network

import com.morpheusdata.core.network.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.NetworkSubnetIdentityProjection
import groovy.transform.AutoImplement
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@AutoImplement
class MorpheusNetworkSubnetServiceImpl implements MorpheusNetworkSubnetService {

	/**
	 * Lists all subnet projection objects for a specified network.
	 * The projection is a subset of the properties on a full {@link NetworkSubnet} object for sync matching.
	 * @param network the {@link Network} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkSubnetIdentityProjection> listIdentityProjections(Network network){
		return
	}

	/**
	 * Lists all {@link NetworkSubnet} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkSubnet} objects from.
	 * @return an RxJava Observable stream of {@link NetworkSubnet} to be subscribed to.
	 */
	Observable<NetworkSubnet> listById(Collection<Long> ids){
		return null
	}

	/**
	 * Removes Missing NetworkSubnets on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetworkSubnet().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param network Network to remove the NetworkSubnet from
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<NetworkSubnetIdentityProjection> removeList){
		return null
	}

	/**
	 * Creates new NetworkSubnet Domains from cache / sync implementations
	 * @param addList List of new {@link NetworkSubnet} objects to be inserted into the database
	 * @param network Network to add the NetworkSubnet to
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(List<NetworkSubnet> addList, Network network){
		return null
	}

	/**
	 * Saves a list of {@link NetworkSubnet} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param networkSubnetsToSave a List of NetworkSubnet objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<NetworkSubnet> networkSubnetsToSave){
		return null
	}

	/**
	 * Saves a {@link NetworkSubnet} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param networkSubnetToSave a NetworkSubnet Object that need to be updated in the database.
	 * @return the Single Observable stating the resultant NetworkSubnet Object
	 */
	Single<NetworkSubnet> save(NetworkSubnet networkSubnetToSave){
		return null
	}
}
