package com.morpheusdata.core;

import io.reactivex.Single;

public interface MorpheusTaskContext {
	Single<Void> disableTask(String code);
}
