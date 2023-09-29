package com.morpheusdata.core.synchronous;

import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface MorpheusSynchronousSeedService {

	void reinstallSeedData(List<String> seedNames);

}
