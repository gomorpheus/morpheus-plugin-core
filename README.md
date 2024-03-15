# Morpheus Core Plugin Library

This library provides a common framework for extending the Morpheus Ops Orchestration Tool. This includes providing plugin extensions for adding various integrations such as Cloud Integrations, Service Level Integrations or Providers for things like DNS, IPAM, Config Management, UI Extensions, Etc.


## Creating a Plugin

Ensure you are using a version of the plugin that is compatible with your version of Morpheus. See the releases tab for more info.

Also, be sure your plugin source and target compatibility is set for Java 11 (`1.11`).

### Setup a new Gradle project

The plugin API is published to Maven Central, include it in your gradle project:

```gradle
dependencies {
	compileOnly 'com.morpheusdata:morpheus-plugin-api:1.1.0'
}
```

**NOTE:** Plugin 1.1.x is designated for 7.0.x and Plugin core 1.0.x is 6.3.x. Plugin 0.15.x is for Morpheus 6.2.x


### Documentation

Please refer to the [official documentation](https://developer.morpheusdata.com/docs) for more up to date guides on how to create a plugin.


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


