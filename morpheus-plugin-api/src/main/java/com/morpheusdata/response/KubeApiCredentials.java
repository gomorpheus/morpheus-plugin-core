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

/**
 * Bootstrap credentials a {@link com.morpheusdata.core.providers.KubernetesClusterProvider.CredentialsFacet}
 * supplies so that Morpheus can authenticate to a managed cluster's Kubernetes API server and create the
 * long-lived {@code morpheus} service account it uses thereafter.
 *
 * <p>Managed offerings authenticate differently: EKS and GKE provide a bearer {@link #apiToken}, while AKS
 * provides a client certificate / key pair ({@link #clientCert} / {@link #clientKey}). Populate whichever
 * fields the cloud issues; {@link #caCert} and {@link #apiUrl} are common to all.</p>
 */
public class KubeApiCredentials {

	/** The Kubernetes API server URL, if the provider determines it (otherwise the cluster's serviceUrl is used). */
	private String apiUrl;
	/** Bearer token used to authenticate to the API server (EKS, GKE). */
	private String apiToken;
	/** Cluster certificate authority (PEM), used to verify the API server. */
	private String caCert;
	/** Client certificate (PEM) for client-certificate authentication (AKS). */
	private String clientCert;
	/** Client private key (PEM) for client-certificate authentication (AKS). */
	private String clientKey;

	public KubeApiCredentials() {}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public String getCaCert() {
		return caCert;
	}

	public void setCaCert(String caCert) {
		this.caCert = caCert;
	}

	public String getClientCert() {
		return clientCert;
	}

	public void setClientCert(String clientCert) {
		this.clientCert = clientCert;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}
}
