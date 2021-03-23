# Morpheus Core Plugin Library

This library provides a common framework for extending the Morpheus Ops Orchestration Tool. This includes providing plugin extensions for adding various integrations such as Cloud Integrations, Service Level Integrations or Providers for things like DNS, IPAM, Config Management, UI Extensions, Etc.

The plugin architecture requires Morpheus 5 or later.

## Creating a Plugin

Ensure you are using a version of the plugin that is compatible with your version of Morpheus. See the releases tab for more info.

Also, be sure your plugin source and target compatibility is set for Java 8 (`1.8`).

### Setup a new Gradle project

The plugin API is [published to Bintray](https://bintray.com/bertramlabs/gomorpheus/morpheus-plugin-api), include it in your gradle project:

```gradle
dependencies {
	implementation 'com.morpheusdata:morpheus-plugin-api:0.6.0'
}
```

**NOTE:** The plugin API has significant changes for 0.8.0 as we move to add cloud plugin support and enhance ease of development. This will be the required target for Morpheus 5.3.1 when it is released

### Implement your plugin

Create and implement the `Plugin` class.

### Packaging

Plugins are packaged by using the `shadowJar` gradle task. This task bundles all dependencies required for the plugin.

A jar manifest must also be created to tell the PluginManager which class to load for the plugin. For example:

build.gradle
```groovy
jar {
    manifest {
        attributes(
                'Plugin-Class': 'com.morpheusdata.infoblox.InfobloxPlugin',
                'Plugin-Version': version
        )
    }
}
```

### Plugin Dependencies

Dependancies your plugin requires such as other clouds sdks or similar will be scoped to your plugins classpath. The `PluginManager` creates a new classpath for each plugin loaded. This is done to ensure classes in a plugin do not interfere with Morpheus or other loaded plugins.

## Things To Be Done

* Morpheus Backup Provider in development
* Morpheus Cloud Provider in development
* Morpheus Network Service Provider in development

