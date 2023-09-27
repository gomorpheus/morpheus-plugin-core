package com.morpheusdata.core;

import com.morpheusdata.model.ComputePort;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing ComputePorts in Morpheus
 */
public interface MorpheusComputePortService extends MorpheusDataService<ComputePort, ComputePort> {

	/**
	 * Get a list of ComputePort objects by reference and portType
	 * @param refType the reference type
	 * @param refId the id of the referenced object
	 * @param portType the portType. (optional)
	 * @return Observable stream of ComputePort
	 */
	Observable<ComputePort> listByRef(String refType, Long refId, String portType);

	/**
	 * Create new ComputePorts in Morpheus
	 * @param computePorts new ComputePorts to persist
	 * @return success
	 */
	Single<Boolean> create(List<ComputePort> computePorts);

	/**
	 * Remove persisted ComputePorts from Morpheus
	 * @param computePorts ComputePorts to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputePort> computePorts);
}
