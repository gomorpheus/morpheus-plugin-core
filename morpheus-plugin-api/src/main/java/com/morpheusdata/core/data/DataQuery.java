package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.AccountIdentity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is the query object to use to query data from a {@link MorpheusDataService}
 * requires the user that is requesting data
 * optionally pass a phrase as a search query and a map of filters
 * @author bdwheeler
 * @since 0.15.1
 */
public class DataQuery {

  public static final String MODE_QUERY = "query";
  public static final String MODE_COUNT = "count";
  public static final String MODE_GROUPED = "group";
  public static final String MODE_STATS = "stats";
  public static final String MODE_LOOKUP = "lookup";

  //user executing the query
  public UserIdentity user;
  //account executing the query
  public AccountIdentity account;
  //query mode - group, stats, query
  public String mode = MODE_QUERY;
  //optional search phrase - ie: "type = 'typeValue' and name = 'fred'"
  public String phrase;
  //api map of input parameters
  //todo - document the parameters the query engine checks in this map
  public ApiParameterMap<String, Object> parameters = new ApiParameterMap<>();
  //optional input filter map of equal operator criteria query ie [type:'typeValue', name:'fred']
  public Map filter;
  //list of input filters for more flexibility - list of [name, value, operator] ie [[name:'type', value:'typeValue', operator:'='], ...]
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
    this.account = user.getAccount();
  }

  public DataQuery(AccountIdentity account) {
    this.account = account;
  }

  public DataQuery(UserIdentity user, ApiParameterMap<String, Object> parameters) {
    this.user = user;
    this.account = user.getAccount();
    this.parameters = parameters;
  }

  public DataQuery(AccountIdentity account, ApiParameterMap<String, Object> parameters) {
    this.account = account;
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

  public String findParameter(String key) {
    String rtn = null;
    Set<String> keySet = parameters.keySet();
    Iterator<String> keyIterator = keySet.iterator();
    while(keyIterator.hasNext()) {
      String row = keyIterator.next();
      if(row.equals(key) || row.endsWith(key)) {
        rtn = row;
        break;
      }
    }
    return rtn;
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
    if(filter != null)
      rtn.put("filter", filter);
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
