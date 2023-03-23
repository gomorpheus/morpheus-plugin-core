package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

import java.io.IOException;
import java.util.HashMap;

public class ModelIdentitySerializer extends StdSerializer<MorpheusIdentityModel> {

	// must have empty constructor
	public ModelIdentitySerializer() {
		this(null);
	}

	public ModelIdentitySerializer(Class<MorpheusIdentityModel> t) {
		super(t);
	}

	@Override
	public void serialize(MorpheusIdentityModel value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		HashMap<String, Object> outputMap = value.getIdentityProperties();
		gen.writeObject(outputMap);
	}
	
}
