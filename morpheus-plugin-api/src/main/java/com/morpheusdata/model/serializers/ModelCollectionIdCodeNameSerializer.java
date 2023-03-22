package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.MorpheusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ModelCollectionIdCodeNameSerializer extends StdSerializer<Collection<MorpheusModel>> {

	// must have empty constructor
	public ModelCollectionIdCodeNameSerializer() {this(null);}

	public ModelCollectionIdCodeNameSerializer(Class<Collection<MorpheusModel>> t) {super(t);}

	@Override
	public void serialize(Collection<MorpheusModel> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<LinkedHashMap<String, Object>> outputList = new ArrayList<>();
		Iterator<MorpheusModel> iterator = values.iterator();
		while(iterator.hasNext()) {
			MorpheusModel item = iterator.next();
			LinkedHashMap<String, Object> itemMap = new LinkedHashMap<>();
			itemMap.put("id", item.getId());
			itemMap.put("code", item.getCode());
			itemMap.put("name", item.getName());
			outputList.add(itemMap);
		}
		gen.writeObject(outputList);
	}

}
