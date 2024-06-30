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

package com.morpheusdata.model;

public class DashboardItem extends MorpheusModel {

	
	protected DashboardItemType type;
	protected Integer itemRow;
	protected Integer itemColumn;
	protected String itemGroup;
	protected Integer groupRow;

	//filters
	//ui types
	//endpoints
	//scripts
	
	public DashboardItemType getType() {
		return type;
	}

	public void setDashboardId(DashboardItemType type) {
		markDirty("type", type);
		this.type = type;
	}

	public Integer getItemRow() {
		return itemRow;
	}

	public void setItemRow(Integer itemRow) {
		markDirty("itemRow", itemRow);
		this.itemRow = itemRow;
	}

	public Integer getItemColumn() {
		return itemColumn;
	}

	public void setItemColumn(Integer itemColumn) {
		markDirty("itemColumn", itemColumn);
		this.itemColumn = itemColumn;
	}

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		markDirty("itemGroup", itemGroup);
		this.itemGroup = itemGroup;
	}

	public Integer getGroupRow() {
		return groupRow;
	}

	public void setGroupRow(Integer groupRow) {
		markDirty("groupRow", groupRow);
		this.groupRow = groupRow;
	}

	public void setType(DashboardItemType type) {
		this.type = type;
		markDirty("type", type);
	}
}
