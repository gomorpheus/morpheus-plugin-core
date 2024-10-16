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

import java.util.List;
import java.math.BigDecimal;

public class RequestReference {

	/**
	 * The Instance or App id
	 */
	private String refId;
	private String refType;
	private String name;
	private String externalId;
	private String externalName;
	private ApprovalStatus status;
	private BigDecimal pricePerMonth;
	private String currency;
	private List<RequestChangeDetail> details;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalName() {
		return externalName;
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}

	public BigDecimal getPricePerMonth() {
		return pricePerMonth;
	}

	public void setPricePerMonth(BigDecimal pricePerMonth) {
		this.pricePerMonth = pricePerMonth;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<RequestChangeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<RequestChangeDetail> details) {
		this.details = details;
	}


	public enum ApprovalStatus {
		requesting,
		requested,
		error,
		approved,
		rejected,
		cancelled
	}
}
