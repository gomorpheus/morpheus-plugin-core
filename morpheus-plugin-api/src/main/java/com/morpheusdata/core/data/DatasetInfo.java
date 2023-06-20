package com.morpheusdata.core.data;

/**
 * information about a dataset
 * @author bdwheeler
 * @since 0.15.1
 */
public class DatasetInfo {

	public String namespace;
	public String key;
	public String name;
	public String description;

	public DatasetInfo() {}

	public DatasetInfo(String namespace, String key) {
		this.namespace = namespace;
		this.key = key;
	}

	public DatasetInfo(String namespace, String key, String name) {
		this.namespace = namespace;
		this.key = key;
		this.name = name;
	}

	public DatasetInfo(String namespace, String key, String name, String description) {
		this.namespace = namespace;
		this.key = key;
		this.name = name;
		this.description = description;
	}

	public String getDatasetCode() {
		String rtn;
		if(namespace != null)
			rtn = namespace;
		else
			rtn = "global";
		rtn = rtn + "." + key;
		return rtn;
	}

}
