package com.morpheusdata

import com.morpheusdata.core.MorpheusComputeContext
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext

/**
 * Testing Implementation of the Morpehus Context
 */
class MorpheusContextImpl implements MorpheusContext {

    protected MorpheusComputeContext computeContext
    protected MorpheusNetworkContext networkContext

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
}
