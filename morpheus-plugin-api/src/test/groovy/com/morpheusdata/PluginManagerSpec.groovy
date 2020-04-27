package com.morpheusdata

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.PluginManager
import spock.lang.Specification

class PluginManagerSpec extends Specification {
    void "Register and Deregister plugin from jar"() {
        given: "A MorpheusContext & jar"
        def context = Mock(MorpheusContext)
        String pathToJar = this.class.classLoader.getResource("morpheus-infoblox-plugin-7.0-all.jar").path

        when: "create a plugin manager"
        def manager = new PluginManager(context)

        then:
        manager.plugins.size() == 0
        noExceptionThrown()

        when: "register a plugin by jar"
        manager.registerPlugin(pathToJar)

        then:
        manager.plugins.size() == 1
        def thePlugin = manager.plugins.first()

        and: "the classloaders are not the same"
        manager.mainLoader != thePlugin.class.classLoader
        manager.pluginManagerClassLoader != thePlugin.class.classLoader

        and: "class loader hierarchy is correct"
        thePlugin.class.classLoader.parent == manager.pluginManagerClassLoader
        manager.pluginManagerClassLoader.parent == manager.mainLoader

        when: "deregister the plugin"
        def registeredPlugin = manager.plugins.first()
        manager.deregisterPlugin(registeredPlugin)

        then:
        manager.plugins.size() == 0
    }
}
