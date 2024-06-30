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

package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.IModelUuidCodeName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ModelCollectionIdUuidCodeNameSerializer extends StdSerializer<Collection<IModelUuidCodeName>> {

	// must have empty constructor
	public ModelCollectionIdUuidCodeNameSerializer() {this(null);}

	public ModelCollectionIdUuidCodeNameSerializer(Class<Collection<IModelUuidCodeName>> t) {super(t);}

	@Override
	public void serialize(Collection<IModelUuidCodeName> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<LinkedHashMap<String, Object>> outputList = new ArrayList<>();
		Iterator<IModelUuidCodeName> iterator = values.iterator();
		while(iterator.hasNext()) {
			LinkedHashMap<String, Object> itemMap = new LinkedHashMap<>();
			IModelUuidCodeName item = iterator.next();
			itemMap.put("id", item.getId());
			itemMap.put("uuid", item.getUuid());
			itemMap.put("code", item.getCode());
			itemMap.put("name", item.getName());
			outputList.add(itemMap);
		}
		gen.writeObject(outputList);
	}

}
