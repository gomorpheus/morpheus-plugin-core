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
