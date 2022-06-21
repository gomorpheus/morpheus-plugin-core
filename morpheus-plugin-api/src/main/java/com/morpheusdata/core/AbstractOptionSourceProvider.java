package com.morpheusdata.core;


import java.math.BigDecimal;
import java.util.Map;

public abstract class AbstractOptionSourceProvider implements OptionSourceProvider {

	static public Long getSiteId(Map opts) {
		//get the value
		Object rtn = opts.get("siteId");
		if (rtn == null && opts.get("site") instanceof Map) {
			rtn = ((Map) opts.get("site")).get("id");
		}
		if (rtn == null && opts.get("instance") instanceof Map) {
			Map instanceMap = (Map) opts.get("instance");
			if (instanceMap.containsKey("site")) {
				rtn = ((Map) instanceMap.get("site")).get("id");
			}
		}
		if (rtn == null) {
			rtn = opts.get("groupId");
		}
		if (rtn == null && opts.get("group") instanceof Map) {
			rtn = ((Map) opts.get("group")).get("id");
		}

		if (rtn == null) {
			if (opts.get("server") instanceof Map) {
				Map serverMap = (Map) opts.get("server");
				if (serverMap.get("site") instanceof Map) {
					rtn = ((Map) serverMap.get("site")).get("id");
				}
				if (rtn == null) {
					rtn = serverMap.get("siteId");
				}
			}
			if (rtn == null && opts.get("instance") instanceof Map) {
				Map instanceMap = (Map) opts.get("instance");
				if (instanceMap.get("site") instanceof Map) {
					rtn = ((Map) instanceMap.get("site")).get("id");
				}
				if (rtn == null) {
					rtn = instanceMap.get("siteId");
				}
			}
			if (rtn == null) {
				//by domain or parent
				if (opts.containsKey("domain")) {
					if (opts.get("domain") instanceof Map) {
						Map domainMap = (Map) opts.get("domain");
						if (domainMap.get("site") instanceof Map) {
							rtn = ((Map) domainMap.get("site")).get("id");
						}
						if (rtn == null) {
							rtn = domainMap.get("siteId");
						}
					}
				}
				if (rtn == null) {
					if (opts.containsKey("parent")) {
						if (opts.get("parent") instanceof Map) {
							Map parentMap = (Map) opts.get("parent");
							if (parentMap.get("site") instanceof Map) {
								rtn = ((Map) parentMap.get("site")).get("id");
							}
							if (rtn == null) {
								rtn = parentMap.get("siteId");
							}
						}
					}
				}
			}
		}
		return rtn != null ? Long.parseLong(rtn.toString()) : null;
	}

	static public Long getPlanId(Map opts) {
		Long rtn = getFieldId(opts, "plan");
		if(rtn == null)
			rtn = getFieldId(opts, "servicePlan");
		return rtn;
	}

	static public Long getFieldId(Map data, String fieldName) {
		Object rtn = null;
		if(data.get(fieldName + "Id") != null && isNumber(data.get(fieldName + "Id"))) {
			rtn = data.get(fieldName + "Id");
		} else if(data.get(fieldName + ".id") != null && isNumber(data.get(fieldName + ".id"))) {
			rtn = data.get(fieldName + ".id");
		}
		if(rtn == null && data.get(fieldName) != null) {
			if(isNumber(data.get(fieldName))) {
				rtn = data.get(fieldName);
			} else if(data.get(fieldName) instanceof Map) {
				if(isNumber(((Map)data.get(fieldName)).get("id")))
					rtn = ((Map)data.get(fieldName)).get("id");
			}
		}
		return rtn != null ? Long.parseLong(rtn.toString()) : null;
	}

	static public Boolean isNumber(Object obj) {
		Boolean rtn;
		rtn = obj != null && (obj instanceof Number);
		if(rtn == false && obj instanceof CharSequence) {
			try {
				new BigDecimal(obj.toString().trim());
				rtn = true;
			} catch (NumberFormatException nfe) {
				// ignore
			}
		}
		return rtn;
	}
}
