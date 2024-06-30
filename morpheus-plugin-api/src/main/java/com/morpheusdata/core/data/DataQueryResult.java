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

	//flag indicating success
	public Boolean success = false;
	//the execution mode of the query - query,lookup,count,group,stats
	public String mode;
	//the actual items
	public Collection<?> items;
	//the number of items returned in the item list
	public Long count;
	//the total number of items in the data that would match the query
	public Long total;
	//applied filters
	public Collection filters = new ArrayList();
	//applied groups
	public Collection groups = new ArrayList();
	//applied stats
	public Collection stats = new ArrayList();
	//applied alias ids
	public Collection aliasIds = new ArrayList();
	//organize map of grouped query results - key is the group - value is the data
	public Map layout;

	//paging information
	public Long max;
	public Long offset;
	public String sort;
	public String order;
	//execution time
	public Long queryTime;

	public DataQueryResult() {}

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
		if(success != null)
			rtn.put("success", success);
		if(mode != null)
			rtn.put("mode", mode);
		//info
		if(filters != null)
			rtn.put("filters", filters);
		if(groups != null)
			rtn.put("groups", groups);
		if(stats != null)
			rtn.put("stats", stats);
		if(aliasIds != null)
			rtn.put("aliasIds", aliasIds);
		if(layout != null)
			rtn.put("layout", layout);
		//results
		rtn.put("items", items);
		rtn.put("count", count);
		rtn.put("total", total);
		//page config
		rtn.put("pageConfig", getPageConfig());
		//done
		return rtn;
	}


}
