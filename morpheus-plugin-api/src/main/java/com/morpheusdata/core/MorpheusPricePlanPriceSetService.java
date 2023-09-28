package com.morpheusdata.core;

import com.morpheusdata.model.PricePlanPriceSet;
import com.morpheusdata.model.projection.PricePlanPriceSetIdentityProjection;

/**
 * Context methods for syncing PricePlanPriceSet in Morpheus
 * @author Dustin DeYoung
 * @since 0.15.3
 */
public interface MorpheusPricePlanPriceSetService extends MorpheusDataService<PricePlanPriceSet, PricePlanPriceSetIdentityProjection>, MorpheusIdentityService<PricePlanPriceSetIdentityProjection> {
}
