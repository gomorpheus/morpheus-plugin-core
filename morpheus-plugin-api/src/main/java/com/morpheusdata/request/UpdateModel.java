package com.morpheusdata.request;

import java.util.Map;

public class UpdateModel<T> {

	public T existingModel;
	public java.util.Map updateProps;

	public UpdateModel(T existingModel, Map updateProps) {
		this.existingModel = existingModel;
		this.updateProps = updateProps;
	}
}
