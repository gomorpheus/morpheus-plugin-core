package com.morpheusdata.model;

/**
 * Stores path information related to logos/icons in use when using different Providers as Integrations of certain types.
 * @author David Estes
 * @since 0.12.3
 * @see com.morpheusdata.core.IPAMProvider
 * @see com.morpheusdata.core.TaskProvider
 * @see com.morpheusdata.core.DNSProvider
 */
public class Icon {
	protected String path;
	protected String hidpiPath;
	protected String darkPath;
	protected String darkHidpiPath;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHidpiPath() {
		return hidpiPath;
	}

	public void setHidpiPath(String hidpiPath) {
		this.hidpiPath = hidpiPath;
	}

	public String getDarkPath() {
		return darkPath;
	}

	public void setDarkPath(String darkPath) {
		this.darkPath = darkPath;
	}

	public String getDarkHidpiPath() {
		return darkHidpiPath;
	}

	public void setDarkHidpiPath(String darkHidpiPath) {
		this.darkHidpiPath = darkHidpiPath;
	}
}
