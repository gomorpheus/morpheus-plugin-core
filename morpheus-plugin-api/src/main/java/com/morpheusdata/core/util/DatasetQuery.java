package com.morpheusdata.core.util;

import com.morpheusdata.core.DatasetProvider;
import com.morpheusdata.model.User;
import java.util.Collection;
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

}
