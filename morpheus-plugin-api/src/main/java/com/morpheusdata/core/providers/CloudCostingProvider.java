package com.morpheusdata.core.providers;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

import java.util.Date;

/**
 * Morpheus provides a way to generate monthly invoices based on resources in a {@link Cloud}. For most On-Prem clouds
 * this is handled automatically as the Morpheus Usage/Metering engine automatically tracks usage and applies Standard
 * Costing invoices based on the price tables imported. However, when dealing with external public clouds, the source
 * of usage truth is the cloud and not Morpheus. Therefore, to provide accurate costing data for the end user, that data
 * needs imported from the third party cloud. Amazon, for example creates a CUR report file in S3. Azure provides thirteen
 * million different APIs depending on your account type.
 *
 * @since 0.15.3
 * @author David Estes
 */
public interface CloudCostingProvider extends UIExtensionProvider {

	/**
	 * The primary method that needs implemented for syncing in pricing data. This is called nightly by Morpheus to
	 * update costing data.
	 * @param cloud the current cloud object with costing data we will be refreshing
	 * @param costDate the current costing date of the run. This is important if regenerating data from a previous billing period
	 * @param opts Any custom refresh options that may be passed by the manual refresh trigger
	 */
	void refreshDailyZoneCosting(Cloud cloud, Date costDate, CloudRefreshOptions opts);
	/**
	 * The render method for rendering Costing Summary information on the Costing section. By default there is already
	 * some generalized costing data shown and this is an optional snippet that can be added.
	 * @param cloud details of the current cloud
	 * @return result of rendering a template
	 */
	default HTMLResponse renderTemplate(Cloud cloud) {
		return null;
	}

	/**
	 * Provide logic when costing cloud summary should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param cloud Cloud details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	default Boolean show(Cloud cloud, User user, Account account) {
		return true;
	}

	public class CloudRefreshOptions {
		public Boolean noLag = false;
	}
}
