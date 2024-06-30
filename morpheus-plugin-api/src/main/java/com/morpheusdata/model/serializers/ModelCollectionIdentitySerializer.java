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
import com.morpheusdata.model.projection.MorpheusIdentityModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ModelCollectionIdentitySerializer extends StdSerializer<Collection<MorpheusIdentityModel>> {

	// must have empty constructor
	public ModelCollectionIdentitySerializer() {this(null);}

	public ModelCollectionIdentitySerializer(Class<Collection<MorpheusIdentityModel>> t) {super(t);}

	@Override
	public void serialize(Collection<MorpheusIdentityModel> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<HashMap<String, Object>> outputList = new ArrayList<>();
		Iterator<MorpheusIdentityModel> iterator = values.iterator();
		while(iterator.hasNext()) {
			HashMap<String, Object> itemMap = iterator.next().getIdentityProperties();
			outputList.add(itemMap);
		}
		gen.writeObject(outputList);
	}
	
}
