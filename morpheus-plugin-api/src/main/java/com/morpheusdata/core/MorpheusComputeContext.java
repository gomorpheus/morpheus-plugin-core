package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Date;
import java.util.List;

public interface MorpheusComputeContext {

	void updateZoneStatus(Cloud cloud, String status, String message, Date syncDate);

	/**
	 *	Get the ssh credentials associated with an account
	 * @param account to lookup
	 * @return Morpheus KeyPair
	 */
	Single<KeyPair> findOrGenerateKeyPair(Account account);

	/**
	 *	Update Morpheus with an external reference to the KeyPair in your Cloud API.
	 * @param keyPair that was updated
	 * @param cloud associated with the credentials
	 * @return void
	 */
	Single<Void> updateKeyPair(KeyPair keyPair, Cloud cloud);

	Observable<VirtualImage> listVirtualImages(Cloud cloud);
	Single<Void> saveVirtualImage(List<VirtualImage> virtualImages);
	Single<Void> updateVirtualImage(List<VirtualImage> virtualImages);
	Single<Void> removeVirtualImage(List<VirtualImage> virtualImages);

	Single<Void> cachePlans(List<ServicePlan> servicePlans);
}
