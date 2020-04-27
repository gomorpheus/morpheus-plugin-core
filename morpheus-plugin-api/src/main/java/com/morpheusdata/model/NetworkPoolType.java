package com.morpheusdata.model;

/**
 * Each implementation of IPAM Typically define a pool type for human readable reference. This enables the user to correctly
 * select the pool of the appropriate type. It is also possible for some {@link com.morpheusdata.core.IPAMProvider} implementations to provide
 * multiple types (for example IPV6 vs. IPV4).
 *
 * @see com.morpheusdata.core.IPAMProvider
 *
 * @author David Estes
 */
public class NetworkPoolType extends MorpheusModel{

	/**
	 * A Unique Code string for easier programmatic reference and API Use. Should be unique globally
	 */
	protected String code;

	/**
	 * Human Readable name of the Pool Type. (can be overridden with i18n schemas).
	 */
	protected String name;

	/**
	 * An optional description can be provided if absolutely necessary.
	 */
	protected String description;

	/**
	 * Defines whether or not a Pool can be created from the UI (Currently not supported by the {@link com.morpheusdata.core.IPAMProvider} implementation).
	 */
	protected Boolean creatable = true;


	/**
	 * Fetches the unique code pertaining to this particular network pool type. Morpheus uses codes for easier reference instead
	 * of simply using database ids as these can change from appliance to appliance where as the code is static and unique.
	 * @return The unique string code representation of the pool type
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Used for assigning a unique code for the NetworkPoolType. These should be globally unique and are typically provided via
	 * the relevant {@link com.morpheusdata.core.IPAMProvider}.
	 * @param code a Unique code representation of a pool type. (i.e. 'infoblox').
	 */
	public void setCode(String code) {
		this.code = code;
		markDirty("code",code);
	}

	/**
	 * Gets the human readable name of the Pool Type that will be displayed in the Morpheus UI for reference. This should ideally be unique but
	 * is not mandatory.
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Allows one to set the human readable name of the Pool Type that will be displayed in the Morpheus UI for reference.
	 * @param name A human readable name representing the provided network type.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name",name);
	}

	/**
	 * Gets the description of the network pool type if it is necessary for further clarification. In most cases this is not necessary
	 * and can be left blank since the pool type name should be descriptive enough.
	 * @return A description of the current pool type is returned.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description text of the current pool type. This description information can be used
	 * @param description description
	 */
	public void setDescription(String description) {
		this.description = description;
		markDirty("description",description);
	}

	/**
	 * Gets creatable flag from this particular pool type. It informs the UI whether or not this pool type can be created from the UI.
	 * This is currently not supported by third party {@link com.morpheusdata.core.IPAMProvider} implementations.
	 * @return whether or not this pool type can be created directly from the Morpheus UI
	 */
	public Boolean getCreatable() {
		return creatable;
	}

	/**
	 * Sets creatable flag from this particular pool type. It informs the UI whether or not this pool type can be created from the UI.
	 * This is currently not supported by third party {@link com.morpheusdata.core.IPAMProvider} implementations.
	 * @param creatable whether or not this pool type can be created directly from the Morpheus UI
	 */
	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable",creatable);
	}
}
