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
import com.morpheusdata.model.MorpheusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ModelCollectionAsIdsOnlySerializer extends StdSerializer<Collection<MorpheusModel>> {

	// must have empty constructor
	public ModelCollectionAsIdsOnlySerializer() {this(null);}

	public ModelCollectionAsIdsOnlySerializer(Class<Collection<MorpheusModel>> t) {super(t);}

	@Override
	public void serialize(Collection<MorpheusModel> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<LinkedHashMap<String,Long>> idsMap = new ArrayList<>();

		Iterator<MorpheusModel> iterator = values.iterator();

		while(iterator.hasNext()) {
			LinkedHashMap<String,Long> idMap = new LinkedHashMap<>();
			idMap.put("id", iterator.next().getId());

			idsMap.add(idMap);
		}

		gen.writeObject(idsMap);
	}
}
