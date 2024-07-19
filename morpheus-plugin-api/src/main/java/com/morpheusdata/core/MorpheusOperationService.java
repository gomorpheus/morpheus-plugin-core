package com.morpheusdata.core;

public interface MorpheusOperationService {

	/**
	 * Returns the {@link MorpheusOperationNotificationService} which allows access operation notification services
	 * @return an instance of {@link MorpheusOperationNotificationService}
	 */
	MorpheusOperationNotificationService getNotification();
}
