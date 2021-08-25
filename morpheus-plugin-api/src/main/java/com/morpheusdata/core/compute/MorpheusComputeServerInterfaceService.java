package com.morpheusdata.core.compute;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.ComputeServerInterface;
import io.reactivex.Single;
import java.util.List;

/**
 * This Context deals with interactions related to {@link ComputeServerInterface} objects. It can normally
 * be accessed via the {@link com.morpheusdata.core.MorpheusComputeServerService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getComputeServer().getComputeServerInterface()
 * }</pre>
 * @since 0.10.0
 * @author Bob Whiton
 */
public interface MorpheusComputeServerInterfaceService {

	/**
	 * Save updates to existing ComputeServerInterfaces
	 * @param computeServerInterfaces updated ComputeServerInterfaces
	 * @return success
	 */
	Single<Boolean> save(List<ComputeServerInterface> computeServerInterfaces);

	/**
	 * Create new ComputeServerInterfaces in Morpheus and add them to the {@link ComputeServer} specified
	 * @param computeServerInterfaces new ComputeServerInterfaces to persist
	 * @param computeServer the ComputeServer instance to add the interface to
	 * @return success
	 */
	Single<Boolean> create(List<ComputeServerInterface> computeServerInterfaces, ComputeServer computeServer);

	/**
	 * Remove persisted ComputeServerInterfaces from Morpheus and remove them the {@link ComputeServer} they are associated with
	 * @param computeServerInterfaces ComputeServerInterfaces to delete
	 * @param computeServer the ComputeServer instance to remove the interface from
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeServerInterface> computeServerInterfaces, ComputeServer computeServer);
}

