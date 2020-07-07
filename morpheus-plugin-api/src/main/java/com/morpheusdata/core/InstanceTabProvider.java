package com.morpheusdata.core;


import com.morpheusdata.model.Account;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.User;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.TemplateResponse;

/**
 * Provides support for custom UI tabs when viewing Instance details
 *
 * @author Mike Truso
 */
public interface InstanceTabProvider extends PluginProvider {

	/**
	 * Default is Handlebars
	 * @return renderer of specified type
	 */
	Renderer<?> getRenderer();

	/**
	 * Instance details provided to your rendering engine
	 * @param instance details of an Instance
	 * @return result of rendering an template
	 */
	TemplateResponse renderTemplate(Instance instance);

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param instance Instance details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	Boolean show(Instance instance, User user, Account account);
}
