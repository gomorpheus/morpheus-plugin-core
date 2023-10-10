package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Workload;
import com.morpheusdata.model.WorkloadType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeService extends MorpheusDataService<WorkloadType, WorkloadType> {

}
