package com.morpheusdata

import com.morpheusdata.core.MorpheusComputeContext
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.core.MorpheusTaskContext

/**
 * Testing Implementation of the Morpehus Context
 */
class MorpheusContextImpl implements MorpheusContext {

    protected MorpheusComputeContext computeContext
    protected MorpheusNetworkContext networkContext
    protected MorpheusTaskContext taskContext

    MorpheusContextImpl() {
        computeContext = new MorpheusComputeContextImpl()
        networkContext = new MorpheusNetworkContextImpl()
    }

    MorpheusContextImpl(MorpheusComputeContext computeContext, MorpheusNetworkContext networkContext) {
        this.computeContext = computeContext
        this.networkContext = networkContext
    }

    @Override
    MorpheusComputeContext getCompute() {
        return computeContext
    }

    @Override
    MorpheusNetworkContext getNetwork() {
        return networkContext
    }

	@Override
	MorpheusTaskContext getTask() {
		return taskContext
	}
}
