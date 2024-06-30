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

import java.util.Map;

/**
 * Represents a single data entry in a generated report result. This contains a JSON DataMap
 * that should be consistent by section for exporting and display.
 * A section could be something like 'main' or 'header' to represent the category of data. This can be used in the output
 * render template to designate where it is displayed.
 * Display Order is also very important as this sets the order in which the record is output to the final report file.
 *
 * <p><strong>NOTE:</strong> the SECTION_MAIN section is the primary section for data export. it should be your primary section
 * and is therefore the default section.</p>
 *
 * @see ReportResult
 * @since 0.8.0
 * @author David Estes
 */
public class ReportResultRow extends MorpheusModel {

	public static final String SECTION_MAIN = "main";
	public static final String SECTION_HEADER = "header";
	public static final String SECTION_FOOTER = "footer";

	protected ReportResult reportResult;
	protected String section = SECTION_MAIN;
	protected Long displayOrder;

	protected Map<String,Object> dataMap;

	public ReportResult getReportResult() {
		return reportResult;
	}

	public void setReportResult(ReportResult reportResult) {
		this.reportResult = reportResult;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
}
