package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the query object to use to list data from a {@link MorpheusDataService}
 * @author bdwheeler
 * @since 0.15.1
 */
public class DataQuery {

  //user executing the query
  public User user;
  //the actual query object - ie detached criteria if used in gorm
  public Object query; 
  //the search phrase
  public String phrase;
  //api map of input parameters
  public ApiParameterMap<String, Object> parameters = new ApiParameterMap<>();
  //valid flag
  public Boolean valid;
  //success flag
  public Boolean success;
  //join configuration
  public Collection joinList;
  //query mode - group, stats, query
  public String mode;
  //input columns
  public Collection columns;
  //optional view configurations
  public Collection views;
  //input filters
  public Collection filters = new ArrayList();
  //applied filters
  public Collection appliedFilters = new ArrayList();
  //configured groupings
  public Collection groups = new ArrayList();
  //configured stats
  public Collection stats = new ArrayList();
  //alias ids used in query
  public Collection aliasIds = new ArrayList();
  //list of sort config
  public Collection sorts;
  //if sort has been applied
  public Boolean sortApplied = false;
  //list of properties to load
  public Collection propertyList = new ArrayList();
  //range query
  public Boolean rangeQuery = false;
  //range config
  public Map range;
  //options used to pass paging and sort into the list methods on execution
  public Map queryOptions;
  //view / query config
  public Map config;
  //used during execution
  public Class queryClass;
  //optional name of item list
  public String itemField;

  //paging - broken out - can get as a map with getPageConfig
  public Long max = 25l;
  public Long offset = 0l;
  public String sort;
  public String order;
  
  public DataQuery() {}

  public DataQuery(User user) {
    this.user = user;
  }

  public DataQuery(User user, ApiParameterMap<String, Object> parameters) {
    this.user = user;
    this.parameters = parameters;
  }

  public DataQuery(DatasetQuery datasetQuery) {
    this.user = datasetQuery.user;
    this.parameters = datasetQuery.parameters;
    //dataset has a higher default max
    if(datasetQuery.max != 100l)
      this.max = datasetQuery.max;
    this.offset = datasetQuery.offset;
    this.sort = datasetQuery.sort;
    this.order = datasetQuery.order;
  }

  public Object putAt(String key, Object value) {
    return put(key, value);
  }

  public Object put(String key, Object value) {
    parameters.put(key, value);
    return value;
  }

  public void putAll(Map<String, Object> parameters) {
    if(parameters != null && parameters.size() > 0)
      this.parameters.putAll(parameters);
  }

  public Object getAt(String key) {
    return get(key);
  }

  public Object get(String key) {
    return parameters.get(key);
  }

  public Collection<Object> list(String key) {
    return parameters.list(key);
  }

  public Map getPageConfig() {
    Map rtn = new LinkedHashMap();
    if(max != null && max > 0)
      rtn.put("max", max);
    if(offset != null && offset > 0)
      rtn.put("offset", offset);
    if(sort != null)
      rtn.put("sort", sort);
    if(order != null)
      rtn.put("order", order);
    return rtn;
  }

  public Map toMap() {
    Map rtn = new LinkedHashMap();
    rtn.put("valid", valid);
    rtn.put("success", success);
    if(query != null)
      rtn.put("query", query);
    if(phrase != null)
      rtn.put("phrase", phrase);
    if(parameters != null)
      rtn.put("parameters", parameters);
    if(joinList != null)
      rtn.put("joinList", joinList);
    if(mode != null)
      rtn.put("mode", mode);
    if(columns != null)
      rtn.put("columns", columns);
    if(filters != null)
      rtn.put("filters", filters);
    if(appliedFilters != null)
      rtn.put("appliedFilters", appliedFilters);
    if(groups != null)
      rtn.put("groups", groups);
    if(stats != null)
      rtn.put("stats", stats);
    if(aliasIds != null)
      rtn.put("aliasIds", aliasIds);
    if(queryOptions != null)
      rtn.put("queryOptions", queryOptions);
    if(config != null)
      rtn.put("config", config);
    //page config
    rtn.put("pageConfig", getPageConfig());
    //done
    return rtn;
  }

}
