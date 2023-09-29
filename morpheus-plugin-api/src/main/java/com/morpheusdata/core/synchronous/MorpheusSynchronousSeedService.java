package com.morpheusdata.core.synchronous;

import io.reactivex.Single;

import java.util.List;

public interface MorpheusSynchronousSeedService {

	void reinstallSeedData(List<String> seedNames);

}
