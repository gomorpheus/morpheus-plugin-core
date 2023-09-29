package com.morpheusdata.core.synchronous;

import java.util.List;

public interface MorpheusSynchronousSeedService {

	void reinstallSeedData(List<String> seedNames);

}
