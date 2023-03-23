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
