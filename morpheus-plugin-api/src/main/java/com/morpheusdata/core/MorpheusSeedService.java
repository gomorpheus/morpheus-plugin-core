package com.morpheusdata.core;

import io.reactivex.Single;

import java.util.List;

public interface MorpheusSeedService {

	Single<Boolean> reinstallSeedData(List<String> seedNames);

}
