package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.MorpheusModel;

import java.io.IOException;
import java.util.LinkedHashMap;

public class ModelIdentitySerializer extends StdSerializer<MorpheusIdentityModel> {

	// must have empty constructor
	public ModelIdentitySerializer() {
		this(null);
	}

	public ModelIdentitySerializer(Class<MorpheusModel> t) {
		super(t);
	}

	@Override
	public void serialize(MorpheusIdentityModel value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		HashMap<String, Object> outputMap = value.getIdentityProperties();
		gen.writeObject(outputMap);
	}
	
}
