# Morpheus Core Plugin Library

This library provides a common framework for extending the Morpheus Ops Orchestration Tool. This includes providing plugin extensions for adding various integrations such as Cloud Integrations, Service Level Integrations or Providers for things like DNS, IPAM, Config Management, Etc.

## Creating a Plugin

### Implementation

A plugin must depend the `morpheus-core` library and implement the `Plugin` class.

### Dependencies

The `PluginManager` creates a new classpath for each plugin loaded. This is done to ensure classes in a plugin do not interfere with Morpheus or other loaded plugins.

### Packaging

Plugins are packaged by using the `shadowJar` gradle task. This task bundles all dependencies required for the plugin.

A jar manifest must also be created to tell the PluginManager which class to load for the plugin. For example:

build.gradle
```groovy
jar {
    manifest {
        attributes(
                'Plugin-Class': 'com.morpheusdata.infoblox.InfobloxPlugin',
                'Plugin-Version': archiveVersion
        )
    }
}
```

## Things To Be Done

* Morpheus Core interfaces and javadoc
* Gradle Plugin packager implementation (Similar to Jenkins JPI Plugin for Gradle)
* Test Provider implementation for allowing users to easily run automation testing on their provider
* Option Type Specification for custom inputs
* Rx Java
