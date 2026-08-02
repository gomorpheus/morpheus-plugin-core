# morpheus-plugin-core — Architecture Context

## Overview
- 15239 components, 14179 relationships
- Languages: java, javascript

## Architecture Layers
- **Plugin Framework** (4 components): Core plugin lifecycle: Plugin base class, PluginInterface contract, PluginManager lifecycle, and ChildFirstClassLoader isolation. This is the entry point every plugin author touches first.
- **Provider Interfaces** (72 components): All PluginProvider sub-interfaces that define integration contracts for Cloud, DNS, IPAM, Provisioning, Backup, Storage, Network, Tasks, UI extensions, Reporting, Credential, Configuration Workflow, Dataset, LLM, and more.
- **Context & Services (Morpheus API)** (110 components): MorpheusContext is the DI handle; MorpheusServices aggregates all domain services (network, compute, storage, identity, backup, etc.) using RxJava reactive streams. This layer bridges plugins and the Morpheus data platform.
- **Data Models** (439 components): POJO model classes in com.morpheusdata.model representing all Morpheus domain objects: Cloud, ComputeServer, Instance, Workload, Network, VirtualImage, StorageVolume, OptionType, Permission, and hundreds more.
- **Request / Response API** (53 components): ServiceResponse<T> is the universal return type for all provider operations. Request models in com.morpheusdata.request carry structured input to provider methods.
- **Views & Web** (11 components): Renderer interface and Handlebars view support (com.morpheusdata.views); PluginController base for plugin-exposed HTTP endpoints (com.morpheusdata.web).
- **Backup Domain** (30 components): Specialised sub-domain under core/backup: BackupProvider, BackupExecutionProvider, BackupRestoreProvider, and their abstract base implementations, plus dedicated Morpheus backup services.
- **Build Tooling** (0 components): morpheus-plugin-gradle Gradle plugin that handles plugin JAR assembly, manifest generation, and publishing for plugin projects consuming this SDK.
- **Sample Plugins** (2 components): Reference implementations under samples/ that demonstrate SDK usage patterns for plugin authors.

## Entry Points
- `morpheus-plugin-api/src/main/java/com/morpheusdata/core/Plugin.java`: Abstract base class for all Morpheus plugins. Plugin authors extend this class, override initialize() to register PluginProviders, and set plugin metadata (name, author, version). It holds the provider registry, a reference to PluginManager, and the MorpheusContext.

## Key Modules (most connected)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/AccountInvoice.java` (251 connections): AccountInvoice.java [java, api]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/ComputeServer.java` (234 connections): Model representing a managed VM or bare-metal server (host). Central entity in cloud and provisioning flows — contains OS, hardware spec, network config, and lifecycle state. [model, compute, servers, virtual-machines, data-model]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/AccountResource.java` (213 connections): AccountResource.java [java, api]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/OptionType.java` (174 connections): Data model representing a single form input field in Morpheus. Contains the input type code, field label, default value, validation rules, visibility conditions (visibleOnCode/requireOnCode), and dependency chain (dependsOn). Used everywhere a user-facing form is defined. [model, form-builder, option-type, input-type, ui]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/NetworkRouter.java` (161 connections): NetworkRouter.java [java, api]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/Cloud.java` (160 connections): Model representing a Morpheus cloud integration record. Holds cloud type, credentials reference, sync settings, and associated compute zones. [model, cloud, cloud-sync, data-model]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/Workload.java` (155 connections): Model for a single workload (container/VM) within an Instance. Provisioning providers interact with this when starting, stopping, or removing individual workload units. [model, workload, provisioning, data-model]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/NetworkLoadBalancerType.java` (153 connections): NetworkLoadBalancerType.java [java, api]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/BackupResult.java` (136 connections): Model capturing the outcome of a single backup execution — status, size, location, and any errors. [model, backup, data-protection, data-model]
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/Container.java` (136 connections): Container.java [java, api]

## Guided Understanding
- **Where every plugin begins — Plugin.java**: The Plugin abstract class is ground zero for any Morpheus plugin. A plugin author extends Plugin, overrides initialize(), registers one or more PluginProviders, and sets metadata (name, version, author). When Morpheus loads a plugin JAR it instantiates this class first, making it the natural entry point for understanding the whole system.
- **The provider contract — PluginProvider**: PluginProvider (in core/providers/) is the root interface every integration type extends. It declares getCode(), getName(), and isEnabled() — the minimum a provider must expose. From here, over 40 specialised sub-interfaces branch out for every integration category (Cloud, DNS, IPAM, Backup, UI, and more). Understanding this interface makes the entire provider hierarchy navigable.
- **Connecting to a cloud — CloudProvider and ProvisionProvider**: CloudProvider handles inventory sync (VMs, networks, storage) from a cloud platform; ProvisionProvider handles the provisioning lifecycle (create, start, stop, remove workloads). Together they cover the full cloud integration surface. Both are in core/providers/ and return ServiceResponse on every operation.

## Risk Hotspots
- Avg defect risk 2.2/10 across 1060 files. **Prioritize fixes by defect risk.**
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/AccountInvoice.java` — defect 6/10, maint 7/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/ComputeServer.java` — defect 6/10, maint 7/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/AccountResource.java` — defect 6/10, maint 7/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/WorkloadType.java` — defect 5/10, maint 5/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/StorageVolume.java` — defect 5/10, maint 5/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/Workload.java` — defect 5/10, maint 6/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/WorkloadState.java` — defect 5/10, maint 5/10 (fanIn 0)
- `morpheus-plugin-api/src/main/java/com/morpheusdata/model/NetworkLoadBalancerInstance.java` — defect 5/10, maint 5/10 (fanIn 0)

---
_This context was generated from .grasp/knowledge-graph.json. Use /grasp-chat for detailed queries._