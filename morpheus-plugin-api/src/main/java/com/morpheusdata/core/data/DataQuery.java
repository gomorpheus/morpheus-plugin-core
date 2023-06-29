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
public class DataQuery {
  
  //user executing the query
  public UserIdentity user;
  //query mode - group, stats, query
  public String mode = "query";
  //optional search phrase - ie: "type = 'typeValue' and name = 'fred'"
  public String phrase;
  //optional map of equal operator filter criteria query ie [type:'typeValue', name:'fred']
  public Map query;
  //api map of input parameters
  //todo - document the parameters the query engine checks in this map
  public ApiParameterMap<String, Object> parameters = new ApiParameterMap<>();
  //input filters - list of [name, value, operator] ie [[name:'type', value:'typeValue', operator:'='], ...]
  public Collection filters = new ArrayList();
  //list of property names to load instead of the full object - (called propertyList since groovy doesn't like properties as a name)
  public Collection propertyList = new ArrayList();

  //paging - broken out - can get as a map with getPageConfig
  public Long max = 25l;
  public Long offset = 0l;
  public String sort;
  public String order;
  
  public DataQuery() {}

  public DataQuery(UserIdentity user) {
    this.user = user;
  }

  public DataQuery(UserIdentity user, ApiParameterMap<String, Object> parameters) {
    this.user = user;
    this.parameters = parameters;
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
    if(query != null)
      rtn.put("query", query);
    if(phrase != null)
      rtn.put("phrase", phrase);
    if(filters != null)
      rtn.put("filters", filters);
    if(parameters != null)
      rtn.put("parameters", parameters);
    if(mode != null)
      rtn.put("mode", mode);
    if(filters != null)
      rtn.put("filters", filters);
    //page config
    rtn.put("pageConfig", getPageConfig());
    //done
    return rtn;
  }

}
