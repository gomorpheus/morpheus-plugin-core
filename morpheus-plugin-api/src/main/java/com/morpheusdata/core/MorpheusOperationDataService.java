package com.morpheusdata.core;

import com.morpheusdata.model.OperationData;

/**
 * Provides query/accessor methods for the {@link OperationData} object. This is data that does not conform to standard
 * Morpheus objects but may need to be used for custom UI display within an integration or for Guidance calculations.
 * It is most often used with the rawData property that can be parsed and processed as needed. An example of its use
 * would be in the aws plugin where reservation recommendations may need to be displayed on the costing tab. This
 * information is very cloud specific and so it is stored here for custom display purposes.
 *
 */
public interface MorpheusOperationDataService  extends MorpheusDataService<OperationData>, MorpheusIdentityService<OperationData>{
}
