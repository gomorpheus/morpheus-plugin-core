package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.IModelCodeName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ModelCollectionIdCodeNameSerializer extends StdSerializer<Collection<IModelCodeName>> {

	// must have empty constructor
	public ModelCollectionIdCodeNameSerializer() {this(null);}

	public ModelCollectionIdCodeNameSerializer(Class<Collection<IModelCodeName>> t) {super(t);}

	@Override
	public void serialize(Collection<IModelCodeName> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<LinkedHashMap<String, Object>> outputList = new ArrayList<>();
		Iterator<IModelCodeName> iterator = values.iterator();
		while(iterator.hasNext()) {
			IModelCodeName item = iterator.next();
			LinkedHashMap<String, Object> itemMap = new LinkedHashMap<>();
			itemMap.put("id", item.getId());
			itemMap.put("code", item.getCode());
			itemMap.put("name", item.getName());
			outputList.add(itemMap);
		}
		gen.writeObject(outputList);
	}

}
