package com.morpheusdata.core.synchronous.dashboard;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Dashboard;
import com.morpheusdata.model.DashboardItemType;
import io.reactivex.Single;

public interface MorpheusSynchronousDashboardService extends MorpheusSynchronousDataService<Dashboard, Dashboard> {

	/**
	 * Load the Dashboard requested by id
	 * @param id the id of the dashboard
	 * @return
	 */
	Dashboard getDashboard(Long id);

	/**
	 * Load the Dashboard requested by code
	 * @param code the code of the dashboard
	 * @return
	 */
	Dashboard getDashboard(String code);

	/**
	 * Load the Dashboard item type requested by id
	 * @param id the id of the dashboard item type
	 * @return
	 */
	DashboardItemType getDashboardItemType(Long id);

	/**
	 * Load the Dashboard item type requested by code
	 * @param code the code of the dashboard item type
	 * @return
	 */
	DashboardItemType getDashboardItemType(String code);
}
