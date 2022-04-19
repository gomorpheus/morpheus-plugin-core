package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusWikiPageService;
import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.WikiPage} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusWikiPageService
 * @author Alex Clement
 */
public class WikiPageIdentityProjection extends MorpheusModel {

	protected String refType;
	protected Long refId;
	protected String name;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param name the Name of the object as a secondary comparison
	 * @param refId the id of the object of type refType. Typically the id of the Cloud for Cloud related tags
	 * @param refType the type of the object referenced. Typically 'ComputeZone' for Cloud related tags
	 */
	public WikiPageIdentityProjection(Long id, String name, Long refId, String refType) {
		this.id = id;
		this.name = name;
		this.refId = refId;
		this.refType = refType;
	}

	public WikiPageIdentityProjection() {

	}

	/**
	 * Returns the name of the WikiPage.
	 * @return the name of the WikiPage.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the WikiPage. In this class this should not be called directly
	 * @param name the name to set on the object
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name",name);
	}

	/**
	 * Returns the id of the WikiPage.
	 * @return the id of the WikiPage.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the WikiPage. In this class this should not be called directly
	 * @param id the id to set on the object
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the refType of the WikiPage.
	 * @return the refType of the WikiPage.
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * Sets the refType of the WikiPage. In this class this should not be called directly
	 * @param refType the refType to set on the object
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * Returns the refId of the WikiPage.
	 * @return the refId of the WikiPage.
	 */
	public Long getRefId() {
		return refId;
	}

	/**
	 * Sets the refId of the WikiPage. In this class this should not be called directly
	 * @param refId the refId to set on the object
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}

}
