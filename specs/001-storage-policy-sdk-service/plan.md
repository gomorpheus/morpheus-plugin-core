# Implementation Plan: StoragePolicy SDK Service Pair

**Branch**: `001-storage-policy-sdk-service` | **Date**: 2026-09-03 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/001-storage-policy-sdk-service/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command; its definition describes the execution workflow.

## Summary

Add a plugin-facing `StoragePolicy` domain model and its identity/match projection to
`morpheus-plugin-api`, plus the paired async/sync SDK service interfaces
(`MorpheusStoragePolicyService`, `MorpheusSynchronousStoragePolicyService`), wired into
the two existing plugin service locators (`MorpheusAsyncServices`, `MorpheusServices`).
This replaces the need for storage plugins to overload generic `ReferenceData` rows to
represent named QoS/performance storage tiers (Silver/Gold/Platinum). No new methods
beyond the SDK's existing base CRUD/list contract (`MorpheusDataService` /
`MorpheusSynchronousDataService` + `MorpheusIdentityService` /
`MorpheusSynchronousIdentityService`) are required — this is a minimal-lookup domain
object, following the `MorpheusOsTypeService` pattern rather than the
`MorpheusStorageServerService` pattern (which adds update/refresh side-effect methods
this feature does not need). This is purely additive to the SDK contract; it does not
implement morpheus-core's backing persistence, does not touch `morpheus-ui`, and does
not touch `morpheus-storage-alletra-mp-plugin` — both are separate downstream specs.

## Technical Context

**Language/Version**: Java 11 (`sourceCompatibility`/`targetCompatibility = '1.11'` in
`morpheus-plugin-api/build.gradle`). The module also applies the Groovy Gradle plugin,
but every existing `com.morpheusdata.core.*Service` interface and `com.morpheusdata.model.*`
class is plain Java — the four new production files (two interfaces, one model, one
projection) are plain Java to match.

**Primary Dependencies**: `io.reactivex.rxjava3:rxjava:3.1.7` (already a project
dependency; the async service interface uses RxJava3 types consistent with every other
`core.*Service` interface — see Data Model / Contracts below for which types). No new
dependency is introduced.

**Storage**: N/A — this repo defines the SDK contract only; morpheus-core (downstream,
out of scope here) owns the actual persistence.

**Testing**:
- Unit tests: Spock specs under `morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/`
  — `StoragePolicySpec.groovy` and `StoragePolicyIdentityProjectionSpec.groovy`. Both new
  model/projection classes go in the same flat `model/` test directory used by every
  existing model test (`OptionTypeSpec.groovy`, `MorpheusModelSpec.groovy`,
  `CloudSpec.groovy`) — there is no existing `model/projection/` test subdirectory in this
  repo, so the projection spec is **not** nested under a `projection/` package path in
  `src/test/groovy`, it sits flat alongside the model specs (mirrors existing convention,
  not the `src/main` package layout).
- The two new pure interface files (`MorpheusStoragePolicyService`,
  `MorpheusSynchronousStoragePolicyService`) get **no** dedicated test, per this repo's
  constitution Principle III: none of the ~90 existing `MorpheusXService` /
  `MorpheusSynchronousXService` interfaces have one, since an interface with no method
  bodies produces no test signal.
- Functional/integration coverage of actual CRUD behavior (does `create`/`save`/`list`/
  `remove` really persist and query `StoragePolicy` rows correctly) is explicitly
  **out of scope for this repo** and deferred to the downstream `morpheus-ui` spec
  (which implements these interfaces) and the downstream
  `morpheus-storage-alletra-mp-plugin` spec (which consumes them) — this repo has no
  functional/integration test layer of its own, per constitution Principle III.

**Target Platform**: JVM library, published as a Maven artifact
(`com.morpheusdata:morpheus-plugin-api`), consumed as a `compileOnly` dependency by
plugins (and, transitively, by morpheus-core's own implementation).

**Project Type**: Single library module (`morpheus-plugin-api`), not web/mobile.

**Performance Goals**: N/A — no performance-sensitive logic is added; this is a plain
data-holder model, a lightweight projection, and two zero-body interface pairs that
inherit all behavior from existing base interfaces.

**Constraints**: Every new file MUST carry the exact `Copyright 2024 Morpheus Data, LLC.`
/ `PLUGIN CORE SOURCE LICENSE` header block already on every existing file under
`com.morpheusdata.**` (copied verbatim from a neighbor such as
`MorpheusStorageServerService.java`), and MUST use the existing package layout
(`com.morpheusdata.core`, `com.morpheusdata.core.synchronous`, `com.morpheusdata.model`,
`com.morpheusdata.model.projection`). The change MUST be purely additive — no existing
SDK interface, model, projection, or aggregator method may be modified, renamed, or
removed (constitution Principle IV, FR-009).

**Scale/Scope**: 4 new production files + 2 new test files + edits to 2 existing
aggregator files (`MorpheusServices.java`, `MorpheusAsyncServices.java`) — no other files
change.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Gate | Status |
|---|---|---|
| I. Stable, Vendor-Mirrored SDK Surface | New files carry the exact copyright/license header and use only the existing package layout (`core`, `core.synchronous`, `model`, `model.projection`) — no new package. | **PASS** — confirmed by inspecting `MorpheusStorageServerService.java` (header) and existing package structure; plan uses only these four packages. |
| II. Every New Domain Service Follows the Existing Data-Service Pattern | Service pair MUST extend `MorpheusDataService<Model, IdentityProjection>` (+ `MorpheusIdentityService<IdentityProjection>`) and `MorpheusSynchronousDataService<Model, IdentityProjection>` (+ `MorpheusSynchronousIdentityService<IdentityProjection>`), with no re-declared CRUD, and MUST be wired into `MorpheusServices`/`MorpheusAsyncServices` next to the alphabetically-adjacent storage accessors. | **PASS** — confirmed base interfaces provide `create`/`bulkCreate`/`save`/`bulkSave`/`remove`/`bulkRemove`/`list`/`count`/`listIdentityProjections` for free (inspected `MorpheusDataService.java`, `MorpheusSynchronousDataService.java`, `MorpheusIdentityService.java`, `MorpheusSynchronousIdentityService.java`); spec requires no extra methods (FR-004, FR-005); `MorpheusOsTypeService` confirmed as the precedent minimal case. Accessor placement resolved in Research (see below). |
| III. Testing Scope Is Interface-Contract, Not Business Logic | No test for the two service interfaces; Spock specs required for model + projection only; no functional/integration layer added here. | **PASS** — matches the plan's Testing section above; confirmed against `OptionTypeSpec.groovy`/`MorpheusModelSpec.groovy` conventions (no existing service-interface test exists to mirror, confirming the "no test" rule is not an oversight). |
| IV. Read/Write Discipline for a Vendor Fork | Purely additive; no existing interface/model/aggregator method is renamed, removed, or has its signature changed. | **PASS** — plan only adds new files/methods; existing deprecated accessors (`getStorageServer()`, etc., in `MorpheusServices`/`MorpheusAsyncServices`) are left untouched. |

No violations. No entries required in Complexity Tracking.

## Project Structure

### Documentation (this feature)

```text
specs/001-storage-policy-sdk-service/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
│   └── storage-policy-sdk-contract.md
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root: `morpheus-plugin-core`)

```text
morpheus-plugin-api/
├── src/main/java/com/morpheusdata/
│   ├── core/
│   │   ├── MorpheusStoragePolicyService.java              # NEW — async service interface
│   │   ├── MorpheusServices.java                          # EDIT — add getStoragePolicy() accessor
│   │   ├── MorpheusAsyncServices.java                     # EDIT — add getStoragePolicy() accessor
│   │   └── synchronous/
│   │       └── MorpheusSynchronousStoragePolicyService.java  # NEW — sync service interface
│   └── model/
│       ├── StoragePolicy.java                              # NEW — domain model
│       └── projection/
│           └── StoragePolicyIdentityProjection.java        # NEW — identity/match projection
└── src/test/groovy/com/morpheusdata/model/
    ├── StoragePolicySpec.groovy                            # NEW — Spock spec for model
    └── StoragePolicyIdentityProjectionSpec.groovy           # NEW — Spock spec for projection
```

**Structure Decision**: Single existing library module (`morpheus-plugin-api`), no new
module or directory root. All four new production files land in the same package
locations as every comparable existing SDK service/model (Principle I) — no new
package is introduced. The two edited files are aggregators only; no other existing file
changes. This feature does not touch `morpheus-ui` or
`morpheus-storage-alletra-mp-plugin` (separate repos/specs, explicitly out of scope per
the spec's Assumptions).

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

No violations — table intentionally omitted.
