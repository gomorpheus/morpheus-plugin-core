# Morpheus Core Plugin Library

This library provides a common framework for extending the Morpheus Ops Orchestration Tool. This includes providing plugin extensions for adding various integrations such as Cloud Integrations, Service Level Integrations or Providers for things like DNS, IPAM, Config Management, UI Extensions, Etc.

The plugin architecture requires Morpheus 5 or later.

**NOTE:** As of 0.9.x This repository should be compiled using Java 11.. But please ensure targetCompatibility in your build.gradle files is set to 1.8 as Morpheus runs in Java 8. This allows for html5 based javadoc to be generated.

## Creating a Plugin

Ensure you are using a version of the plugin that is compatible with your version of Morpheus. See the releases tab for more info.

Also, be sure your plugin source and target compatibility is set for Java 8 (`1.8`).

### Setup a new Gradle project

The plugin API is published to Maven Central, include it in your gradle project:

```gradle
dependencies {
	compileOnly 'com.morpheusdata:morpheus-plugin-api:0.8.0'
}
```

**NOTE:** The plugin API has significant changes for 0.8.0 as we move to add cloud plugin support and enhance ease of development. This will be the required target for Morpheus 5.3.1


### Documentation

Please refer to the [official documentation](https://developer.morpheusdata.com/docs) for more up to date guides on how to create a plugin.

### Samples

Samples exist in the project underneath the `samples` directory. Please be sure to test these from the appropriate tagged version for release until `1.0`. For example, use branch `v0.8.x` to generate and test samples for Morpheus `5.3.1`

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

