/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.dashboard;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Dashboard;
import com.morpheusdata.model.DashboardItem;
import com.morpheusdata.model.DashboardItemType;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusDashboardService extends MorpheusDataService<Dashboard, Dashboard> {

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
