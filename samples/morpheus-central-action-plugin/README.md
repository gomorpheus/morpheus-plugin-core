# Sample Central Action Plugin

This sample demonstrates how to implement a `CentralActionProvider` using the Morpheus plugin API.
A `CentralActionProvider` lets a plugin handle a Morpheus Central Service push action without a
core release.

## What it does

Central delivers a push-action envelope whose top-level `type` is `"plugin-provider-push-action"`.
Core selects the target provider by matching the envelope's nested `payload.code` against the
provider's `getCode()`, then invokes `handle(Map payload)` with the raw payload.

This sample responds to:

- `type == "plugin-provider-push-action"` (the envelope type)
- `payload.code == "sample-central-action.echo"` (this provider's code)

When it handles that action, `handle` logs the payload and echoes the incoming `message` field back
as the acknowledgement data:

```
payload  = { "code": "sample-central-action.echo", "message": "hello" }
response = ServiceResponse.success({ "echoed": "hello" })
```

A Map-shaped `ServiceResponse.data` is coerced by core to the `ok` ack's `data` returned to Central.
The provider itself does **not** ack — core's `CentralServicesActionService` owns all acking.

## What it includes

- `SampleCentralActionPlugin` — Java plugin entry point; registers the provider in `initialize()`
  via `this.registerProvider(new SampleCentralActionProvider(this, this.morpheus))`
- `SampleCentralActionProvider` — implements `CentralActionProvider.handle(Map)`

## Build

The sample is included in the repository `settings.gradle` as
`samples:morpheus-central-action-plugin`. From the repository root:

```bash
./gradlew :samples:morpheus-central-action-plugin:shadowJar
```

The built plugin jar will be created under:

```text
samples/morpheus-central-action-plugin/build/libs/
```

## Key files

- `src/main/java/com/morpheusdata/sample/centralaction/SampleCentralActionPlugin.java`
- `src/main/java/com/morpheusdata/sample/centralaction/SampleCentralActionProvider.java`

## Notes

- This sample is intended for in-repo development and depends on `project(':morpheus-plugin-api')`
  rather than a published API artifact.
- The provider code `sample-central-action.echo` follows the recommended `<pluginCode>.<action>`
  naming convention so it does not collide with a built-in action type or another plugin. This
  convention is documented guidance only and is not enforced by core.
- `handle` runs inside the consumer's message transaction; persistence performed there is not
  independently committed.
- Loading the built plugin in a dev appliance and issuing a real
  `plugin-provider-push-action` to observe the `ok` ack is a manual/dev verification step.

See the author guide: `morpheus-plugin-docs/src/docs/asciidoc/CentralActionProviderPlugin.adoc`.
