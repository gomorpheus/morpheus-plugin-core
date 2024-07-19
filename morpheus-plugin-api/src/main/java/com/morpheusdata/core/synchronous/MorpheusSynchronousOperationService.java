package com.morpheusdata.core.synchronous;

public interface MorpheusSynchronousOperationService {

	/**
	 * Returns the {@link MorpheusSynchronousOperationNotificationService} which allows access operation notification services
	 * @return an instance of {@link MorpheusSynchronousOperationNotificationService}
	 */
	MorpheusSynchronousOperationNotificationService getNotification();


}
