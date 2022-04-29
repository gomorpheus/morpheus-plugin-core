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
