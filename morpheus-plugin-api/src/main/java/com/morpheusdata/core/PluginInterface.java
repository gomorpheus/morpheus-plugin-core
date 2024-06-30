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

package com.morpheusdata.core;

public interface PluginInterface {
	/**
	 * This is a hook called by the {@link PluginManager} when a plugin is loaded into memory. In many cases no code
	 * execution should be needed in this phase, but some scenarios may require this.
	 */
	void initialize();

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	void onDestroy();

	String getName();
	void setName(String name);

	/**
	 * Gets the code of the current plugin. This is an internal name that must be unique across plugins.
	 * @return the code for the plugin
	 */
	String getCode();

	String getFileName();
	void setFileName(String fileName);
	String getVersion();
	void setVersion(String version);
	String getDescription();
	void setDescription(String description);
	String getAuthor();
	void setAuthor(String author);
	String getWebsiteUrl();
	void setWebsiteUrl(String websiteUrl);
	String getSourceCodeLocationUrl();
	void setSourceCodeLocationUrl(String sourceCodeLocationUrl);
	String getIssueTrackerUrl();
	void setIssueTrackerUrl(String issueTrackerUrl);
}
