# Quickstart: Validating the StoragePolicy SDK Service Pair

This is a validation guide for confirming the feature is correctly implemented in
`morpheus-plugin-core`. It does not contain full implementation code — see
`data-model.md` and `contracts/storage-policy-sdk-contract.md` for the exact shapes, and
`tasks.md` (generated separately by `/speckit.tasks`) for the implementation steps.

## Prerequisites

- JDK 11 available (matches `sourceCompatibility`/`targetCompatibility = '1.11'`).
- Repository checked out at branch `001-storage-policy-sdk-service`.
- Run all commands from the `morpheus-plugin-api/` module directory (or pass
  `-p morpheus-plugin-api` / equivalent Gradle module targeting from the repo root,
  matching however this repo's existing CI already invokes Gradle).

## 1. Compile the SDK module

```bash
./gradlew :morpheus-plugin-api:compileJava
```

**Expected outcome**: Compiles cleanly. Confirms:
- `StoragePolicy.java`, `StoragePolicyIdentityProjection.java`,
  `MorpheusStoragePolicyService.java`, `MorpheusSynchronousStoragePolicyService.java` all
  compile with no errors.
- `MorpheusServices.java` and `MorpheusAsyncServices.java` still compile after adding
  `getStoragePolicy()` — i.e. the new interfaces satisfy the generic bounds required by
  `MorpheusDataService<M extends MorpheusModel, I extends MorpheusModel>` /
  `MorpheusSynchronousDataService<...>` / `MorpheusIdentityService<...>` /
  `MorpheusSynchronousIdentityService<...>`.

## 2. Run the new unit specs

```bash
./gradlew :morpheus-plugin-api:test --tests "com.morpheusdata.model.StoragePolicySpec" \
                                     --tests "com.morpheusdata.model.StoragePolicyIdentityProjectionSpec"
```

**Expected outcome**: Both Spock specs pass, exercising (per `data-model.md`):
- Every `StoragePolicy` setter (`code`, `name`, `displayOrder`, `enabled`,
  `provisionTypeCode`) marks the corresponding field dirty via `markDirty`, following the
  same assertion style as `MorpheusModelSpec.groovy`'s `markDirty` tests.
- Every `StoragePolicyIdentityProjection` setter (`code`, `name`) marks the
  corresponding field dirty, and the convenience constructor
  `(Long id, String code, String name)` populates all three fields.

## 3. Run the full existing test suite (regression check, FR-009)

```bash
./gradlew :morpheus-plugin-api:test
```

**Expected outcome**: 100% of pre-existing tests still pass unchanged — confirms the
purely-additive nature of the change (no existing model, projection, service, or
aggregator test starts failing).

## 4. Manual contract-discovery sanity check

Confirm the two new accessors are reachable exactly where a plugin author would look,
per FR-006/FR-007:

```bash
grep -n "getStoragePolicy" morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusServices.java
grep -n "getStoragePolicy" morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusAsyncServices.java
```

**Expected outcome**: Both greps return exactly one match each, immediately following
the existing `getStorageHostGroup()` entry in each file (see `research.md` §3 for why
this placement was chosen over the deprecated `MorpheusStorageService` aggregator
pattern).

## Out of scope for this quickstart

- Actually persisting/querying a `StoragePolicy` row against a real morpheus-core
  database — that requires the downstream `morpheus-ui` implementation spec.
- Exercising the service pair from inside `morpheus-storage-alletra-mp-plugin` — that
  requires the downstream plugin-consumption spec.
- Any UI-visible behavior — this repo has no UI.
