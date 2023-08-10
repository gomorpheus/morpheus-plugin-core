package com.morpheusdata.global

import com.morpheusdata.core.AbstractGlobalUIComponentProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.Renderer
import com.morpheusdata.views.ViewModel
import groovy.util.logging.Slf4j

/**
 * Example Global UI Component
 */
@Slf4j
class CustomGlobalProvider extends AbstractGlobalUIComponentProvider {
	Plugin plugin
	MorpheusContext morpheus

	private HandlebarsRenderer renderer;

	String code = 'custom-global-1'
	String name = 'Custom Global 1'

	CustomGlobalProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}

	@Override
	Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getWebRequest());
			renderer.registerI18nHelper(getPlugin(),getMorpheus());
		}
		return renderer;
	}

	/**
	 * Provides logic to check for when this global ui component should be displayed
	 * @param user current User details
	 * @param account Account details
	 * @return whether the component should be displayed
	 */
	@Override
	Boolean show(User user, Account account) {
		return true
	}

	/**
	 * The renderer for the global UI Component. This is typically rendered into the footer of the main layout. This is
	 * useful for rendering common components like a global support chat.
	 * @param user The current user the page is being rendered for.
	 * @param account The current account the page is being rendered for.
	 * @return result of rendering a template
	 */
	@Override
	HTMLResponse renderTemplate(User user, Account account) {
		ViewModel<User> model = new ViewModel<>()
		getRenderer().renderTemplate("hbs/global", model)
	}

	/**
	 * Add policies for resources loaded from external sources.
	 * @return policy directives for various source types
	 */
	@Override
	ContentSecurityPolicy getContentSecurityPolicy() {
		return null
	}

}
