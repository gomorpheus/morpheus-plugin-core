# Phase 0 Research: StoragePolicy SDK Service Pair

The spec ships with zero `[NEEDS CLARIFICATION]` markers. This document instead
resolves the implementation-shape questions the Technical Context left open, by
inspecting the actual repository conventions rather than assuming them.

## 1. Base class for the `StoragePolicy` model

- **Decision**: `StoragePolicy` extends `com.morpheusdata.model.MorpheusModel` directly
  (not `StorageServerIdentityProjection`-style "model extends its own identity
  projection").
- **Rationale**: `MorpheusDataService<M extends MorpheusModel, I extends MorpheusModel>`
  only requires `M`/`I` to be `MorpheusModel` subclasses — it does not require `M` to
  extend `I`. Inspecting simple, comparable reference/lookup models
  (`StorageControllerType`, `OsType`) confirms plenty of existing models extend
  `MorpheusModel` directly with their own flat field set, independent of any projection
  class. `StorageServer` extending `StorageServerIdentityProjection` is specific to
  `StorageServer`'s own heavier shape (it has many more fields layered on top of the
  identity subset) and is not the general rule. Since `StoragePolicy` is a simple named
  reference-style object per the spec's Assumptions ("simple named-lookup/
  reference-style domain object"), the plain-`MorpheusModel` shape is the correct,
  simpler match.
- **Alternatives considered**: Extending `StoragePolicyIdentityProjection` (mirroring
  `StorageServer`) — rejected as unnecessary indirection for a simple lookup object with
  only 5 fields; would also force the projection class to be loaded/instantiated
  whenever the full model is used, which the identity-projection pattern is meant to
  avoid (the projection exists to be *lighter than* the model, not a base class of it).

## 2. Extra methods on the service pair

- **Decision**: No methods beyond what `MorpheusDataService<StoragePolicy,
  StoragePolicyIdentityProjection>` + `MorpheusIdentityService<StoragePolicyIdentityProjection>`
  (async) and `MorpheusSynchronousDataService<StoragePolicy, StoragePolicyIdentityProjection>`
  + `MorpheusSynchronousIdentityService<StoragePolicyIdentityProjection>` (sync) already
  provide. Both new interfaces have empty bodies (`public interface
  MorpheusStoragePolicyService extends ... { }`).
- **Rationale**: FR-004/FR-005 explicitly say no storage-tier-specific methods are
  required. Inspecting `MorpheusOsTypeService` (a genuinely minimal lookup-table service)
  confirms the precedent: it only adds one deprecated convenience method beyond the base
  contract, and `MorpheusSynchronousOsTypeService` adds *nothing* — an empty interface
  body extending only the two base interfaces is a fully valid, existing SDK pattern.
  `MorpheusStorageServerService`'s extra `validateUpdate`/`refresh`/etc. methods exist
  because `StorageServer` has its own plugin-triggered lifecycle (config drift checks,
  update workflows) — the spec's Assumptions explicitly rule this out for
  `StoragePolicy` ("requires no plugin-triggered side-effect operations").
- **Alternatives considered**: Mirroring `MorpheusStorageServerService`'s shape — rejected,
  contradicts FR-004/FR-005 and the spec's Assumptions.

## 3. Where to wire the new accessors in `MorpheusServices` / `MorpheusAsyncServices`

- **Decision**: Add `getStoragePolicy()` as a new, non-deprecated method in both
  `MorpheusServices.java` and `MorpheusAsyncServices.java`, placed immediately after the
  existing `getStorageHostGroup()` entry (the last storage-related accessor in file
  order in both files today), with a plain (non-`@Deprecated`) javadoc block matching the
  `getStorageHost()`/`getStorageHostGroup()` style.
- **Rationale**: Inspecting both files shows two eras of storage accessor: an older set
  (`getStorageVolume()`, `getStorageController()`, `getStorageServer()`,
  `getStorageBucket()`) that is now `@Deprecated(since="1.2.5")` in favor of the
  `MorpheusStorageService`/`MorpheusSynchronousStorageService` aggregator
  (`getStorage()`), and a newer set added *after* that aggregator
  (`getStorageHost()`, `getStorageHostGroup()`) that was **not** folded into the
  deprecation/aggregator pattern and has no `@Deprecated` annotation. Since this is a
  brand-new addition (not a pre-existing accessor being migrated), it follows the
  current (newer, non-deprecated) precedent set by `getStorageHost()`/
  `getStorageHostGroup()`, not the legacy deprecated one. This also matches the
  constitution's literal instruction to place the new accessor "next to the existing
  alphabetically-adjacent entries" — `StorageHostGroup` and `StoragePolicy` are the two
  closest alphabetical neighbors among the current non-deprecated storage accessors.
- **Alternatives considered**:
  - Adding `getStoragePolicy()` to the `MorpheusStorageService`/
    `MorpheusSynchronousStorageService` aggregator instead (as `getPolicy()`) —
    rejected: out of scope per explicit user/spec instruction to wire only
    `MorpheusServices`/`MorpheusAsyncServices`, and `getStorageHost()`/
    `getStorageHostGroup()` (the most recent precedent) were *not* added to that
    aggregator either, confirming the aggregator is not the current pattern for new
    additions.
  - Marking the new accessor `@Deprecated` from birth — rejected: nonsensical for a
    brand-new accessor with no replacement.

## 4. Test directory placement for the projection spec

- **Decision**: `StoragePolicyIdentityProjectionSpec.groovy` is placed flat in
  `src/test/groovy/com/morpheusdata/model/`, alongside `StoragePolicySpec.groovy` —
  **not** under a `model/projection/` test subdirectory.
- **Rationale**: Inspecting the existing test tree
  (`morpheus-plugin-api/src/test/groovy/com/morpheusdata/`) shows directories only for
  `core`, `core/util`, `core/providers`, `model`, `request`, `response`, `views` — there
  is no `model/projection` test directory anywhere, even though
  `model/projection/*.java` production classes exist (e.g.
  `StorageServerIdentityProjection`, `MorpheusIdentityModel`) and could in principle have
  had specs. The one identity-model test that does exist for base behavior
  (`MorpheusModelSpec.groovy`) also sits flat in `model/`. The test tree convention here
  mirrors "one flat `model` test package for all model + projection specs", not a
  1:1 mirror of the `src/main` package layout.
- **Alternatives considered**: Mirroring `src/main/java` exactly
  (`src/test/groovy/com/morpheusdata/model/projection/...`) — rejected as inventing a
  new test-tree convention this repo has never used, which the constitution's Principle
  III explicitly says new model/projection specs "MUST follow this exact convention"
  (singular, referring to the existing flat pattern).

## 5. RxJava3 return types for the async interface

- **Decision**: `MorpheusStoragePolicyService` uses whatever `MorpheusDataService<M,I>`
  and `MorpheusIdentityService<I>` already declare (`Single<M>`, `Single<BulkCreateResult<M>>`,
  `Single<BulkSaveResult<M>>`, `Single<Boolean>`, `Single<BulkRemoveResult<I>>`,
  `Single<Long>`, `Single<M>` (get), `Observable<M>` (listById/list), `Observable<I>`
  (listIdentityProjections)) — no new return type is introduced since no new method is
  added.
- **Rationale**: Confirmed directly from `MorpheusDataService.java` /
  `MorpheusIdentityService.java` source; consistent with FR-005's "existing
  reactive-stream conventions" requirement.

## Summary

All four unknowns implied by "match the existing pattern" in the Technical Context are
now resolved by direct inspection of this repository's actual code (not assumption).
No `[NEEDS CLARIFICATION]` remains. Ready for Phase 1.
