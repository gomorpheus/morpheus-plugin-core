package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the query object to use to query data from a {@link MorpheusDataService}
 * requires the user that is requesting data
 * optionally pass a phrase as a search query and a map of filters
 * @author bdwheeler
 * @since 0.15.1
 */
public class DataQueryResult {

  //flag indicating if the query succeeded
  public Boolean success = false;
  //flag indicating if the query created a warning
  public Boolean warning = false;
  //message if any returned from the query
  public String message = null;
  //error code casued by the query
  public String errorCode;
  //map of errors caused by the query
  public Map<String,String> errors = new LinkedHashMap<>();
  //the items found in the query
  public Collection items;
  //total number of items in the datastore that match the query
  public Long total;
  //number of items returned in this query - should match max if paged
  public Long size; 
  //the offset used in the paged query
  public Long offset = 0l;
  //the max per page value used in the query
  public Long max;
  //the sort used in the query
  public String sort;
  //the order used in the query
  public String order;
  
  public DataQueryResult() {}

  //todo - add constructors for success and error case.

}
