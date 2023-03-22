package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.MorpheusModel;

import java.io.IOException;
import java.util.LinkedHashMap;

public class ModelIdUuidCodeNameSerializer extends StdSerializer<MorpheusModel> {

	//must have empty constructor
	public ModelIdUuidCodeNameSerializer() {
		this(null);
	}

	public ModelIdUuidCodeNameSerializer(Class<MorpheusModel> t) {
		super(t);
	}

	@Override
	public void serialize(MorpheusModel value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		LinkedHashMap<String, Object> outputMap = new LinkedHashMap<>();
		outputMap.put("id", value.getId());
		outputMap.put("uuid", value.getUuid());
		outputMap.put("code", value.getCode());
		outputMap.put("name", value.getName());
		gen.writeObject(outputMap);
	}

}
