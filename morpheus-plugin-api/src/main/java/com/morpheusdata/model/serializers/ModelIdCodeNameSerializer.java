package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.IModelCodeName;
import java.io.IOException;
import java.util.LinkedHashMap;

public class ModelIdCodeNameSerializer extends StdSerializer<IModelCodeName> {

	//must have empty constructor
	public ModelIdCodeNameSerializer() {
		this(null);
	}

	public ModelIdCodeNameSerializer(Class<IModelCodeName> t) {
		super(t);
	}

	@Override
	public void serialize(IModelCodeName value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		LinkedHashMap<String, Object> outputMap = new LinkedHashMap<>();
		outputMap.put("id", value.getId());
		outputMap.put("code", value.getCode());
		outputMap.put("name", value.getName());
		gen.writeObject(outputMap);
	}

}
