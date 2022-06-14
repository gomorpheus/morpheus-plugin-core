package com.morpheusdata.core;

import com.morpheusdata.model.AccountDiscovery;
import com.morpheusdata.model.Icon;
import com.morpheusdata.views.HTMLResponse;
import  com.morpheusdata.model.AccountDiscoveryType;

/**
 * This provider allows the creation of custom recommendations that can be shown to the user via the "Guidance" section of Morpheus.
 * These recommendations can represent cost savings as well as be executed upon.
 * @since 0.13.2
 * @author David Estes
 */
public interface GuidanceRecommendationProvider  extends PluginProvider {


	/**
	 * This is the main entry point for creating discoveries / recommendations for the end user. This method will perform any logic necessary and generate new discovery records
	 */
	void calculateRecommendations();

	/**
	 * Performs an action based on the data in the discovery object.
	 * @param discovery details of the recommendation used for performing an action
	 */
	void execute(AccountDiscovery discovery);

	/**
	 * Discovery details provided to your rendering engine
	 * @param discovery details of the recommendation used for rendering a detailed recommendation description
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(AccountDiscovery discovery);


	/**
	 * Returns the Discovery Icon related to this particularly associated {@link AccountDiscoveryType}
	 * @since 0.13.2
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provide a more user friendly title of the guidance recommendation
	 * @return the desired title of the discovery type
	 */
	String getTitle();

	/**
	 * Provide a more user friendly description of the guidance recommendation
	 * @return the desired description of the discovery type
	 */
	String getDescription();
}
