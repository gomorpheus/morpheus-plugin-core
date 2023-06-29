package com.morpheusdata.core.data;

import com.morpheusdata.core.providers.DatasetProvider;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.User;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the query object to use to list data from a {@link DatasetProvider}
 * @author bdwheeler
 * @since 0.15.1
 */
public class DatasetQuery {

  public User user;
  public String namespace;
  public String key;
  public ApiParameterMap<String, Object> parameters = new ApiParameterMap<>();
  //paging
  public Long max = 100l;
  public Long offset = 0l;
  public String sort;
  public String order;
  
  public DatasetQuery() {}

  public DatasetQuery(User user) {
    this.user = user;
  }

  public DatasetQuery(User user, ApiParameterMap<String, Object> parameters) {
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

}
