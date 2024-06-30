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

package com.morpheusdata.core.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MorpheusUtils {

	static Logger log = LoggerFactory.getLogger(MorpheusUtils.class);

	/**
	 * This method is now in {@link DateUtility}
	 * @param date the date to parse as a String or Object
	 * @return the parsed Date
	 */
	@Deprecated
	static public Date parseDate(Object date) {
		Date rtn = null;
		try {
			//handle multiple formats - 2019-02-08T19:15:39.259
			if (date != null) {
				if (date instanceof Date) {
					rtn = (Date) date;
				} else if (date instanceof CharSequence) {
					TimeZone timezone = TimeZone.getTimeZone("GMT");
					CharSequence dateCharSequence = (CharSequence) date;

					if (dateCharSequence.length() == 28) { //yyyy-MM-dd'T'HH:mm:ss.SSSZ
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else if (dateCharSequence.length() == 24) { //2018-03-23T16:53:27+0000 or 2018-03-23T16:53:27.432Z
						if (dateCharSequence.toString().indexOf('+') > -1 || dateCharSequence.toString().indexOf('-') > 16) {    //2018-03-23T19:00:01Z
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						} else {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						}
					} else if (dateCharSequence.length() == 22) { //2018-03-23T16:53:27Z
						try {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'UTC'");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e2) {
							try {
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss'UTC'");
								df.setTimeZone(timezone);
								rtn = df.parse(dateCharSequence.toString());
							} catch (Exception e3) {
								try {
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.s'Z'");
									df.setTimeZone(timezone);
									rtn = df.parse(dateCharSequence.toString());
								} catch (Exception e4) {
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
									df.setTimeZone(timezone);
									rtn = df.parse(dateCharSequence.toString());
								}
							}
						}
					} else if (dateCharSequence.length() == 20) { //2018-03-23T16:53:27Z
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else if (dateCharSequence.length() == 10) { ////2018-03-23
						if (dateCharSequence.toString().indexOf('-') > -1) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						} else {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						}
					} else if (dateCharSequence.length() == 27) { //2018-03-23T16:53:27.000432Z
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else if (dateCharSequence.length() == 29) { //Mon, 19 Aug 2019 19:09:41 GMT
						DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else {
						// attempt default formats
						try {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {
						}
						try {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {
						}
						try {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {
						}
						try {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {
						}
						if (rtn == null) {
							try {
								DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
								rtn = df.parse(dateCharSequence.toString());
							} catch (Exception e) {
							}
						}
						if (rtn == null) {
							try {
								DateFormat df = new SimpleDateFormat("MM/yyyy");
								rtn = df.parse(dateCharSequence.toString());
							} catch (Exception e) {
							}
						}
						if (rtn == null) {
							try {
								DateFormat df = new SimpleDateFormat("MM/yyyy HH:mm:ss");
								rtn = df.parse(dateCharSequence.toString());
							} catch (Exception e) {
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("error parsing date: ${date}", e);
		}
		return rtn;
	}

	/**
	 * Compares two software version strings in the form of 'x.x.x'
	 *
	 * @param a - Version comparing
	 * @param b - Version comparing to
	 * @return - -1 if a {@literal <} b, 0 if a == b, 1 if a {@literal >} b
	 */
	static public Integer compareVersions(String a, String b) {
		if ((a != null && a != "") && (b == null || b == "")) {
			return 1;
		}
		if ((a == null || a == "") && (b != null && b != "")) {
			return -1;
		}
		if ((a == null || a == "") && (b == null || b == "")) {
			return 0;
		}
		if (a == "all") {
			return 1;
		}

		if (a.startsWith("v")) {
			a = a.substring(1);
		}
		if (b.startsWith("v")) {
			b = b.substring(1);
		}

		StringTokenizer aVersions = new StringTokenizer(a, ".");
		StringTokenizer bVersions = new StringTokenizer(b, ".");
		Integer compareResult = null;
		Integer maxSize = Math.max(aVersions.countTokens(), bVersions.countTokens());
		for (Integer i = 0; i < maxSize; i++){
			if(compareResult == null || compareResult == 0) {
				String aString = aVersions.hasMoreTokens() ? aVersions.nextToken() : "0";
				Integer aValue = Integer.parseInt(aString);

				String bString = bVersions.hasMoreTokens() ? bVersions.nextToken() : "0";
				Integer bValue = Integer.parseInt(bString);

				compareResult = aValue.compareTo(bValue);
			}
		}


		return compareResult;
	}

	static public Long getZoneId(Map opts) {
		Object tmpValue = null;
		if(opts.get("zoneId") != null) {
			tmpValue = opts.get("zoneId");
		} else if(opts.get("cloudId") != null) {
			tmpValue = opts.get("cloudId");
		} else if (opts.get("zone") instanceof Map) {
			tmpValue = ((Map)opts.get("zone")).get("id");
		} else if (opts.get("cloud") instanceof Map) {
			tmpValue = ((Map)opts.get("cloud")).get("id");
		} else if (opts.get("storageServer") instanceof Map) {
			tmpValue = ((Map)opts.get("storageServer")).get("refId");
		}
		if(tmpValue == null) {
			Map tmpMap = null;
			if(opts.get("server") != null) {
				tmpMap = (Map)opts.get("server");
			} else if(opts.get("network") != null) {
				tmpMap = (Map)opts.get("network");
			} else if(opts.get("router") != null) {
				tmpMap = (Map)opts.get("router");
			}

			if(tmpValue == null && tmpMap != null) {
				if(tmpMap.get("zone") != null) {
					tmpValue = ((Map)tmpMap.get("zone")).get("id");
				} else {
					tmpValue = tmpMap.get("zoneId");
				}
			}
		}

		if(tmpValue == null && opts.get("parent") instanceof Map) {
			if(((Map)opts.get("parent")).get("zone") != null) {
				tmpValue = ((Map)((Map)opts.get("parent")).get("zone")).get("id");
			} else {
				tmpValue = ((Map)opts.get("parent")).get("zoneId");
			}
		}

		return parseLongConfig(tmpValue);
	}

	static public Long getResourcePoolId(Map opts) {
		Object rtn = null;
		if(opts.get("resourcePoolId") != null) {
			rtn = opts.get("resourcePoolId");
		} else if(opts.get("zonePoolId") != null) {
			rtn = opts.get("zonePoolId");
		} else if(opts.get("config") != null) {
			Map tmpMap = (Map)opts.get("config");
			if(tmpMap.get("resourcePool") != null) {
				rtn = tmpMap.get("resourcePool");
			} else if(tmpMap.get("resourcePoolId") != null) {
				rtn = tmpMap.get("resourcePoolId");
			} else if(tmpMap.get("zonePoolId") != null) {
			rtn = tmpMap.get("zonePoolId");
			} else if(tmpMap.get("zonePool") != null) {
				rtn = ((Map)tmpMap.get("zonePool")).get("id");
			}
		} else if(opts.get("zonePool") != null) {
			rtn = ((Map)opts.get("zonePool")).get("id");
		}

		return parseLongConfig(rtn);
	}

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

	/**
	 * This utility method is used to compare an object to various forms of boolean truth.  Values usually come from
	 * submitted web forms and have different values for boolean truth
	 * @param val
	 * @return
	 */
	static public Boolean parseBooleanConfig(Object val) {
		if (val instanceof Boolean) {
			return val == (Boolean)val;
		}
		else if (val instanceof CharSequence) {
			CharSequence value = (CharSequence)val;
			return value.equals("true") || value.equals("on") || value.equals("yes");
		}
		else {
			return false;
		}
	}

	/**
	 * This is a convenience utility method to get a long value from form inputs. It checks for null
	 * and verifies the value is a number before parsing into a Long
	 * @param val assumed long value from input
	 * @return the value converted to a Long
	 */
	static public Long parseLongConfig(Object val) {
		if(val != null && isNumber(val.toString())) {
			return Long.parseLong(val.toString());
		}
		return null;
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

	//parse or return a map if already a map
	static public Map getJson(Object val) {
		Map rtn = null;
		if(val != null) {
			if(val instanceof Map) {
				rtn = (Map) val;
            } else if(val instanceof Collection) {
				rtn = (Map) val;
            } else if(val instanceof CharSequence) {
				try {
					rtn = (Map) new groovy.json.JsonSlurper().parseText((String) val);
				} catch(Exception e) {

				}
			}
		}
		return rtn;
	}

	static BigDecimal parseStringBigDecimal(String str) {
		return parseStringBigDecimal(str, null);
	}

	static BigDecimal parseStringBigDecimal(String str, BigDecimal defaultValue) {
		BigDecimal rtn = defaultValue;
		try { rtn = new BigDecimal(str); } catch(Exception e) {}
		return rtn;
	}
}
