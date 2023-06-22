package com.morpheusdata.model;

import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.core.providers.TaskProvider;

/**
 * Stores path information related to logos/icons in use when using different Providers as Integrations of certain types.
 * @author David Estes
 * @since 0.12.3
 * @see IPAMProvider
 * @see TaskProvider
 * @see DNSProvider
 * @see com.morpheusdata.core.CloudProvider
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
