package com.morpheusdata

import com.morpheusdata.core.MorpheusComputeContext
import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.KeyPair
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.VirtualImage
import groovy.transform.AutoImplement
import io.reactivex.Single

@AutoImplement
class MorpheusComputeContextImpl implements MorpheusComputeContext {

    @Override
    void updateZoneStatus(Cloud zone, String status, String message, Date syncDate) {

    }

	@Override
	Single<KeyPair> findOrGenerateKeyPair(Account account) {
		return null
	}

	@Override
	Single<Void> updateKeyPair(KeyPair keyPair, Cloud cloud) {
		return null
	}

	@Override
	Single<Void> cachePlans(List<ServicePlan> servicePlans) {
		return null
	}
}
