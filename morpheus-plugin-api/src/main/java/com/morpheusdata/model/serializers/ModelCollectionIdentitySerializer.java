package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.MorpheusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ModelCollectionIdentitySerializer extends StdSerializer<Collection<MorpheusModel>> {

	// must have empty constructor
	public ModelCollectionIdentitySerializer() {this(null);}

	public ModelCollectionIdentitySerializer(Class<Collection<MorpheusModel>> t) {super(t);}

	@Override
	public void serialize(Collection<MorpheusIdentityModel> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<LinkedHashMap<String, Object>> outputList = new ArrayList<>();
		Iterator<MorpheusIdentityModel> iterator = values.iterator();
		while(iterator.hasNext()) {
			HashMap<String, Object> itemMap = iterator.next().getIdentityProperties();
			outputList.add(itemMap);
		}
		gen.writeObject(outputList);
	}
	
}
