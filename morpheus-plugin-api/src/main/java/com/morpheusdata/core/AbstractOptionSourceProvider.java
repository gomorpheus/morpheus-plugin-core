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
