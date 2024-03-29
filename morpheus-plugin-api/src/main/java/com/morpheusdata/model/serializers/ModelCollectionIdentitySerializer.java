package com.morpheusdata.model.serializers;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ModelCollectionIdentitySerializer extends StdSerializer<Collection<MorpheusIdentityModel>> {

	// must have empty constructor
	public ModelCollectionIdentitySerializer() {this(null);}

	public ModelCollectionIdentitySerializer(Class<Collection<MorpheusIdentityModel>> t) {super(t);}

	@Override
	public void serialize(Collection<MorpheusIdentityModel> values, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
		Collection<HashMap<String, Object>> outputList = new ArrayList<>();
		Iterator<MorpheusIdentityModel> iterator = values.iterator();
		while(iterator.hasNext()) {
			HashMap<String, Object> itemMap = iterator.next().getIdentityProperties();
			outputList.add(itemMap);
		}
		gen.writeObject(outputList);
	}
	
}
