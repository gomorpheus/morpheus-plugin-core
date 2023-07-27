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
 * This is the query result object with the items and details when executing a query
 * @author bdwheeler
 * @since 0.15.1
 */
public class DataQueryResult<?> {

	//flag indicating success
	Boolean success = false;
	//the execution mode of the query - query,lookup,count,group,stats
	String mode;
	//the actual items
	Collection<?> items;
	//the number of items returned in the item list
	Long count;
	//the total number of items in the data that would match the query
	Long total;
	//applied filters
	Collection filters = new ArrayList();
	//applied groups
	Collection groups = new ArrayList();
	//applied stats
	Collection stats = new ArrayList();
	//applied alias ids
	Collection aliasIds = new ArrayList();
	//organize map of grouped query results - key is the group - value is the data
	Map layout;

	//paging information
	Long max;
	Long offset;
	String sort;
	String order;
	//execution time
	Long queryTime;

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
