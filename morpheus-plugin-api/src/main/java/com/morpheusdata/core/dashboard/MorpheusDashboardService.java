package com.morpheusdata.core.dashboard;

import com.morpheusdata.model.Dashboard;
import com.morpheusdata.model.DashboardItem;
import com.morpheusdata.model.DashboardItemType;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusDashboardService {

	/**
	 * Load the Dashboard requested by id
	 * @param id the id of the dashboard
	 * @return
	 */
	Single<Dashboard> getDashboard(Long id);

	/**
	 * Load the Dashboard requested by code
	 * @param code the code of the dashboard
	 * @return
	 */
	Single<Dashboard> getDashboard(String code);

	/**
	 * Load the Dashboard item type requested by id
	 * @param id the id of the dashboard item type
	 * @return
	 */
	Single<DashboardItemType> getDashboardItemType(Long id);

	/**
	 * Load the Dashboard item type requested by code
	 * @param code the code of the dashboard item type
	 * @return
	 */
	Single<DashboardItemType> getDashboardItemType(String code);

}
