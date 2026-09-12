---
name: repo-architecture
description: >-
  Module layout, source-path map, tech stack, and build/publish commands for morpheus-plugin-core.
  Use to navigate the repo, locate a provider/model source file, or build and publish the
  morpheus-plugin-api SDK.
---

# morpheus-plugin-core — architecture & build

Multi-module Gradle project. The published SDK is `morpheus-plugin-api`; everything else supports it.

## Modules (`settings.gradle`)
- `morpheus-plugin-api` — the SDK (interfaces, models, responses). **Edit here.**
- `morpheus-plugin-docs` / `morpheus-plugin-site` — docs & developer site.
- `morpheus-plugin-gradle` — Gradle plugin used to build downstream plugins.
- `morpheus-vmware-plugin`, `samples/` — reference plugin implementations.

## Source map (`morpheus-plugin-api/src/main/java/com/morpheusdata/`)
- `core/providers/` — extension-point interfaces + `Abstract*` bases.
- `model/` — domain models (`OptionType`, `Wizard`, `ConfigurationWorkflow`, ...).
- `response/ServiceResponse.java` — standard return wrapper.
- `data/` — `DatasetInfo`, `DatasetQuery`.

## Stack
Java 11, Groovy 3.0.9, Gradle, RxJava 3.1.7. Version in `gradle.properties`.

## Build / publish
- `./gradlew morpheus-plugin-api:test`
- `./gradlew morpheus-plugin-api:publishToMavenLocal` (`pTML`) — for local plugin builds
- `./build.sh` — test + publish API, assemble all `*-plugin` modules
