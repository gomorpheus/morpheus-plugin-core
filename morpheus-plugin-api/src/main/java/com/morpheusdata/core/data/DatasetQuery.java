/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  public String valueField;
  
  public DatasetQuery() {}

  public DatasetQuery(UserIdentity user) {
    super(user);
  }

  public DatasetQuery(UserIdentity user, ApiParameterMap<String, Object> parameters) {
    super(user, parameters);
  }

}
