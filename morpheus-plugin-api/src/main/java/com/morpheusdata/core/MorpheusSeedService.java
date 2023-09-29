package com.morpheusdata.core;

import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface MorpheusSeedService {

	Single<Boolean> reinstallSeedData(List<String> seedNames);

}
