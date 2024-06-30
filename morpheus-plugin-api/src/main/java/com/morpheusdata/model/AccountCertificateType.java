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

/**
 * Represents a type of {@link AccountCertificate}.  These can be certificates, csrs, CAs that are managed by morpheus or synced
 * in from various types of integrations (nsx-t, nsx-v, venafi, etc).  These types are generally seeded in by the morpheus
 * appliance.
 * @see AccountCertificate
 * @author Jordon Saardchit
 */
public class AccountCertificateType extends MorpheusModel {
	
	protected String code;
	protected String name;
	protected String description;
	protected Boolean enabled = true;
	protected Boolean creatable = true;
	protected Boolean editable = false;
	protected Boolean selectable = true;
	protected String externalRefType;
	protected String providerCode;
	protected String category;
	protected String categoryName;
	protected String formView="create";
	protected String viewSet="nsxv";
	protected Boolean supportsCertificateSigning;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}

	public String getExternalRefType() {
		return externalRefType;
	}

	public void setExternalRefType(String externalRefType) {
		this.externalRefType = externalRefType;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getFormView() {
		return formView;
	}

	public void setFormView(String formView) {
		this.formView = formView;
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
	}

	public Boolean getSupportsCertificateSigning() {
		return supportsCertificateSigning;
	}

	public void setSupportsCertificateSigning(Boolean supportsCertificateSigning) {
		this.supportsCertificateSigning = supportsCertificateSigning;
	}
}
