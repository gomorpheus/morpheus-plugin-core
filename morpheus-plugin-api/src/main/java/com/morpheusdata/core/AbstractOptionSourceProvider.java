package com.morpheusdata.core;


import java.math.BigDecimal;
import java.util.Map;
import com.morpheusdata.core.util.MorpheusUtils;

public abstract class AbstractOptionSourceProvider implements OptionSourceProvider {

	@Deprecated
	static public Long getSiteId(Map opts) {
		return MorpheusUtils.getSiteId(opts);
	}

	@Deprecated
	static public Long getPlanId(Map opts) {
		return MorpheusUtils.getPlanId(opts);
	}

	@Deprecated
	static public Long getFieldId(Map data, String fieldName) {
		return MorpheusUtils.getFieldId(data, fieldName);
	}

	@Deprecated
	static public Boolean isNumber(Object obj) {
		return MorpheusUtils.isNumber(obj);
	}
}
