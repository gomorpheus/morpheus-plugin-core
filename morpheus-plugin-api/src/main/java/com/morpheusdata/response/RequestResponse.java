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

package com.morpheusdata.response;

import com.morpheusdata.model.RequestReference;

import java.util.List;

/**
 * Communicate back to Morpheus the response from your ITSM solution
 */
public class RequestResponse {
	private Boolean success;
	private String msg;
	private String externalRequestId;
	private String externalRequestName;
	private List<RequestReference> references;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getExternalRequestId() {
		return externalRequestId;
	}

	public void setExternalRequestId(String externalRequestId) {
		this.externalRequestId = externalRequestId;
	}

	public String getExternalRequestName() {
		return externalRequestName;
	}

	public void setExternalRequestName(String externalRequestName) {
		this.externalRequestName = externalRequestName;
	}

	public List<RequestReference> getReferences() {
		return references;
	}

	public void setReferences(List<RequestReference> references) {
		this.references = references;
	}
}
