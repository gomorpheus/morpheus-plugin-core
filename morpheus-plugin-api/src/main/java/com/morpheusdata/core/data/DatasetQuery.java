package com.morpheusdata.core.data;

import com.morpheusdata.core.providers.DatasetProvider;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the an extension of the {@link DataQuery} to use to list data from a {@link DatasetProvider}
 * @author bdwheeler
 * @since 0.15.1
 */
public class DatasetQuery extends DataQuery {

  public String namespace;
  public String key;
  
  public DatasetQuery() {}

  public DatasetQuery(UserIdentity user) {
    super(user);
  }

  public DatasetQuery(UserIdentity user, ApiParameterMap<String, Object> parameters) {
    super(user, parameters);
  }

}
