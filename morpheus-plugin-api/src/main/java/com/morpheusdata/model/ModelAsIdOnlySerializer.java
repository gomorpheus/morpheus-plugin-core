package com.morpheusdata.model;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.LinkedHashMap;

public class ModelAsIdOnlySerializer extends StdSerializer<MorpheusModel> {

	// must have empty constructor
	public ModelAsIdOnlySerializer() {
		this(null);
	}

	public ModelAsIdOnlySerializer(Class<MorpheusModel> t) {
		super(t);
	}

	@Override
	public void serialize(MorpheusModel value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		LinkedHashMap<String,Long> idMap = new LinkedHashMap<>();
		idMap.put("id",value.id);
		gen.writeObject(idMap);
	}
}
