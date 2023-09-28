package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.KeyPair;
import com.morpheusdata.model.projection.KeyPairIdentityProjection;

public interface MorpheusSynchronousKeyPairService extends MorpheusSynchronousDataService<KeyPair, KeyPairIdentityProjection>, MorpheusSynchronousIdentityService<KeyPairIdentityProjection> {
}
