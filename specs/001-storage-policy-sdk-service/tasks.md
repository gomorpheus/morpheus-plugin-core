---

description: "Task list for StoragePolicy SDK Service Pair"
---

# Tasks: StoragePolicy SDK Service Pair

**Input**: Design documents from `/specs/001-storage-policy-sdk-service/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/storage-policy-sdk-contract.md, quickstart.md

**Tests**: Per this repo's `.specify/memory/constitution.md` (Principle III), this is a
pure SDK-contract repo with no functional/integration test layer of its own — that
coverage is deferred to the downstream `morpheus-ui` and plugin-consumption specs. Within
that scope:
- The two new model/projection classes (`StoragePolicy.java`,
  `StoragePolicyIdentityProjection.java`) DO get dedicated Spock unit-test tasks
  (`src/test/groovy/com/morpheusdata/model/*Spec.groovy`), written before their
  implementation tasks, per the workspace's mandatory-test template rule.
- The two new pure interface files (`MorpheusStoragePolicyService.java`,
  `MorpheusSynchronousStoragePolicyService.java`) get **no** dedicated test — none of the
  ~90 existing `MorpheusXService`/`MorpheusSynchronousXService` interfaces have one, since
  an interface with no method bodies produces no test signal.
- The two aggregator wiring edits (`MorpheusServices.java`, `MorpheusAsyncServices.java`)
  get a **compilation/wiring verification task** (per `quickstart.md` steps 1 & 4) in
  place of a dedicated unit test, since there is no behavior to unit test in an
  aggregator accessor line.

**Organization**: Tasks are grouped by user story (from spec.md) to enable independent
implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

Single existing library module, no new module/directory root:

- Production: `morpheus-plugin-api/src/main/java/com/morpheusdata/{core,core/synchronous,model,model/projection}/`
- Tests: `morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/`

---

## Phase 1: Setup

**Purpose**: Confirm a clean baseline before any change (this is an established module —
no new project/framework initialization is needed).

- [X] T001 Confirm clean baseline: from repo root run `./gradlew :morpheus-plugin-api:compileJava` and `./gradlew :morpheus-plugin-api:test`; confirm both succeed before making any changes in this feature.

**Checkpoint**: Baseline confirmed green — safe to start Foundational work.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Gather the exact precedent artifacts every subsequent task copies from —
MUST be complete before any user story work begins.

- [X] T002 [P] Extract the exact `Copyright 2024 Morpheus Data, LLC.` / `PLUGIN CORE SOURCE LICENSE` header block verbatim from `morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusStorageServerService.java` for reuse, unmodified, at the top of every new file this feature adds (constitution Principle I, FR-008).
- [X] T003 [P] Inspect and record the exact generic signatures of `morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusDataService.java`, `morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusIdentityService.java`, `morpheus-plugin-api/src/main/java/com/morpheusdata/core/synchronous/MorpheusSynchronousDataService.java`, `morpheus-plugin-api/src/main/java/com/morpheusdata/core/synchronous/MorpheusSynchronousIdentityService.java`, `morpheus-plugin-api/src/main/java/com/morpheusdata/model/MorpheusModel.java`, and `morpheus-plugin-api/src/main/java/com/morpheusdata/model/projection/MorpheusIdentityModel.java`, so the new `StoragePolicy`/`StoragePolicyIdentityProjection`/service files satisfy their generic bounds exactly as documented in `research.md` §1, §2, §5.

**Checkpoint**: Foundation ready — User Story 1 can now begin.

---

## Phase 3: User Story 1 - Plugin author manages storage tiers through a dedicated data model (Priority: P1) 🎯 MVP

**Goal**: Introduce the `StoragePolicy` domain model (core fields: `code`, `name`,
`displayOrder`, `enabled` — `provisionTypeCode` is added later in User Story 3) and its
`StoragePolicyIdentityProjection` identity/match representation, both with the SDK's
standard `markDirty` change-tracking convention.

**Independent Test**: Confirm the new model and projection classes exist in the SDK,
expose the documented fields, and carry the same getter/setter change-tracking behavior
as every other SDK model — verified by a unit spec against the model/projection classes
alone, no morpheus-core or plugin runtime involved (spec.md US1 Independent Test).

### Tests for User Story 1 (MANDATORY) ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T004 [P] [US1] Write failing Spock spec `morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/StoragePolicySpec.groovy` asserting that each of the `code` (String), `name` (String), `displayOrder` (Integer), `enabled` (Boolean) setters on `StoragePolicy` calls `markDirty(fieldName, value)`, following the exact assertion style already used in `MorpheusModelSpec.groovy`/`OptionTypeSpec.groovy`. Spec MUST fail to compile/run until T006 creates `StoragePolicy.java`.
- [X] T005 [P] [US1] Write failing Spock spec `morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/StoragePolicyIdentityProjectionSpec.groovy` asserting that the `code` and `name` setters on `StoragePolicyIdentityProjection` call `markDirty(fieldName, value)`, and that the convenience constructor `StoragePolicyIdentityProjection(Long id, String code, String name)` populates all three fields (`id`, `code`, `name`). Spec MUST fail to compile/run until T007 creates `StoragePolicyIdentityProjection.java`.

### Implementation for User Story 1

- [X] T006 [P] [US1] Create `morpheus-plugin-api/src/main/java/com/morpheusdata/model/StoragePolicy.java`: plain Java class extending `com.morpheusdata.model.MorpheusModel` directly, carrying the header from T002, with fields `code` (String), `name` (String), `displayOrder` (Integer), `enabled` (Boolean) — each with a getter and a setter that calls `markDirty(fieldName, value)` per data-model.md (do **not** add `provisionTypeCode` yet — that is User Story 3's task T014). Run T004's spec afterward to confirm it now passes.
- [X] T007 [P] [US1] Create `morpheus-plugin-api/src/main/java/com/morpheusdata/model/projection/StoragePolicyIdentityProjection.java`: plain Java class extending `com.morpheusdata.model.projection.MorpheusIdentityModel`, carrying the header from T002, mirroring `StorageServerIdentityProjection` exactly — a no-arg default constructor, a convenience constructor `StoragePolicyIdentityProjection(Long id, String code, String name)`, and `code`/`name` fields (String) each with a getter and a setter that calls `markDirty(fieldName, value)` per data-model.md (`id` is inherited from `MorpheusModel`, not redeclared). Run T005's spec afterward to confirm it now passes.

**Checkpoint**: At this point, `StoragePolicy` and `StoragePolicyIdentityProjection` exist, compile, and are independently unit-tested — User Story 1 is fully functional and testable on its own.

---

## Phase 4: User Story 2 - Plugin author performs CRUD on storage tiers via a dedicated service (Priority: P1)

**Goal**: Add the paired `MorpheusStoragePolicyService` (async) / `MorpheusSynchronousStoragePolicyService` (sync) interfaces — each inheriting create/save/list/remove from the SDK's base data-service contract with no tier-specific methods — and wire both into the existing plugin service locators (`MorpheusServices`, `MorpheusAsyncServices`), grouped with the other storage accessors.

**Independent Test**: Confirm the new service pair is reachable from the SDK's existing
plugin service-locator entry points and exposes exactly the inherited
create/update/delete/list operations, with no dedicated test needed beyond confirming
the wiring compiles and resolves (spec.md US2 Independent Test).

### Tests for User Story 2 (MANDATORY — see note above) ⚠️

> Per constitution Principle III, the two new interfaces below carry no dedicated unit
> test (an interface with no method bodies produces no test signal — no existing
> `MorpheusXService`/`MorpheusSynchronousXService` has one either). This story's
> test-equivalent is task **T012**, a compilation/wiring verification task run
> immediately after the implementation tasks, per `quickstart.md` steps 1 and 4.

### Implementation for User Story 2

- [X] T008 [P] [US2] Create `morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusStoragePolicyService.java`: `public interface MorpheusStoragePolicyService extends MorpheusDataService<StoragePolicy, StoragePolicyIdentityProjection>, MorpheusIdentityService<StoragePolicyIdentityProjection> { }` (empty body, no new methods), carrying the header from T002. Depends on T006, T007 (needs `StoragePolicy`/`StoragePolicyIdentityProjection` to exist).
- [X] T009 [P] [US2] Create `morpheus-plugin-api/src/main/java/com/morpheusdata/core/synchronous/MorpheusSynchronousStoragePolicyService.java`: `public interface MorpheusSynchronousStoragePolicyService extends MorpheusSynchronousDataService<StoragePolicy, StoragePolicyIdentityProjection>, MorpheusSynchronousIdentityService<StoragePolicyIdentityProjection> { }` (empty body, no new methods), carrying the header from T002. Depends on T006, T007.
- [X] T010 [P] [US2] Edit `morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusServices.java` to add a `MorpheusSynchronousStoragePolicyService getStoragePolicy();` accessor with a plain (non-`@Deprecated`) javadoc block (`Returns the StoragePolicy Service` / `@return An instance of the StoragePolicy Service`), inserted immediately after the existing `getStorageHostGroup()` method, matching the `getStorageHost()`/`getStorageHostGroup()` style (research.md §3, contracts/storage-policy-sdk-contract.md §3). Depends on T009.
- [X] T011 [P] [US2] Edit `morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusAsyncServices.java` to add a `MorpheusStoragePolicyService getStoragePolicy();` accessor with the same javadoc style, inserted immediately after the existing `getStorageHostGroup()` method (research.md §3, contracts/storage-policy-sdk-contract.md §3). Depends on T008.
- [X] T012 [US2] Verify wiring compiles and resolves (replaces a dedicated unit test per constitution Principle III): run `./gradlew :morpheus-plugin-api:compileJava` from repo root and confirm a clean compile; then run `grep -n "getStoragePolicy" morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusServices.java morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusAsyncServices.java` and confirm exactly one match in each file, immediately following `getStorageHostGroup()` (quickstart.md steps 1 & 4). Depends on T008, T009, T010, T011.

**Checkpoint**: At this point, User Stories 1 AND 2 both work independently — a plugin can now discover and use a fully-wired `StoragePolicy` CRUD service pair (minus the provisioning-technology link, added next).

---

## Phase 5: User Story 3 - Plugin author links a storage tier to a specific provisioning technology (Priority: P2)

**Goal**: Add the `provisionTypeCode` plain-String field to `StoragePolicy`, so a plugin
can record which provisioning technology (by its stable code) a tier applies to, without
needing to resolve morpheus-core's internal provision-type identifier.

**Independent Test**: Confirm the storage-tier model carries a plain stable-code field
for the target provisioning technology (not a direct reference to morpheus-core's
internal provisioning-technology record), and that this field participates in the
model's standard field-level change tracking like every other field (spec.md US3
Independent Test).

### Tests for User Story 3 (MANDATORY) ⚠️

> **NOTE: Write this test FIRST, ensure it FAILS before implementation**

- [X] T013 [US3] Add a failing Spock test method to `morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/StoragePolicySpec.groovy` (created in T004) asserting that `StoragePolicy`'s `provisionTypeCode` (String) setter calls `markDirty("provisionTypeCode", value)`, following the same assertion style as the other field tests in that spec. Test MUST fail until T014 adds the field. Depends on T004 (file must already exist).

### Implementation for User Story 3

- [X] T014 [US3] Edit `morpheus-plugin-api/src/main/java/com/morpheusdata/model/StoragePolicy.java` (created in T006) to add a `provisionTypeCode` (String) field — a plain value, not a reference to any provision-type model — with a getter and a setter that calls `markDirty("provisionTypeCode", value)`, per data-model.md/FR-001/User Story 3. Run T013's test afterward to confirm it now passes. Depends on T006, T013.

**Checkpoint**: All three user stories are now independently functional — `StoragePolicy` records can carry a provisioning-technology link on top of the P1 CRUD capability.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final repo-wide regression and acceptance validation (FR-009, SC-001–SC-004).

- [X] T015 Run the full existing test suite for regression: `./gradlew :morpheus-plugin-api:test` from repo root; confirm 100% of pre-existing tests still pass unchanged, confirming the purely-additive nature of the change (quickstart.md step 3, FR-009). Depends on T006, T007, T008, T009, T010, T011, T014.
- [X] T016 Execute the full `quickstart.md` validation guide end-to-end (steps 1–4) and confirm every expected outcome is met. Depends on T012, T015.
- [X] T017 Review all 4 new production files (`StoragePolicy.java`, `StoragePolicyIdentityProjection.java`, `MorpheusStoragePolicyService.java`, `MorpheusSynchronousStoragePolicyService.java`) and the 2 edited aggregator files (`MorpheusServices.java`, `MorpheusAsyncServices.java`) for exact copyright/license header parity (against T002's extracted block) and correct package placement (constitution Principle I, FR-008). Depends on T006, T007, T008, T009, T010, T011.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately.
- **Foundational (Phase 2)**: Depends on Setup (T001) completion — BLOCKS all user stories.
- **User Story 1 (Phase 3)**: Depends on Foundational (T002, T003) completion. No dependency on other stories.
- **User Story 2 (Phase 4)**: Depends on Foundational completion **and** on User Story 1's model/projection classes existing (T006, T007) — the service interfaces are generically typed over `StoragePolicy`/`StoragePolicyIdentityProjection` and cannot compile without them.
- **User Story 3 (Phase 5)**: Depends on Foundational completion **and** on User Story 1's `StoragePolicy.java`/`StoragePolicySpec.groovy` existing (T004, T006) — it edits the same files to add one field. Independent of User Story 2.
- **Polish (Phase 6)**: Depends on all desired user stories being complete.

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational — no dependency on other stories. This is the true MVP root.
- **User Story 2 (P1)**: Can start once US1's model/projection files exist (T006, T007) — cannot be implemented as pure "no dependency" since the service interfaces reference `StoragePolicy`/`StoragePolicyIdentityProjection` by generic type, but is otherwise independently testable/deployable once US1 lands.
- **User Story 3 (P2)**: Can start once US1's `StoragePolicy.java` exists (T006) — edits the same file/spec to add one field. Independent of US2; US2 and US3 can proceed in parallel once US1 is done.

### Within Each User Story

- Tests MUST be written and FAIL before implementation (US1: T004/T005 before T006/T007; US3: T013 before T014).
- US2 has no dedicated pre-implementation test (constitution Principle III) — its verification task (T012) runs after implementation instead.
- Models/interfaces before aggregator wiring (US2: T008/T009 before T010/T011).
- Story complete before moving to Polish.

### Parallel Opportunities

- T002 and T003 (Foundational) can run in parallel.
- T004 and T005 (US1 tests) can run in parallel.
- T006 and T007 (US1 implementation) can run in parallel with each other once their respective tests exist.
- T008 and T009 (US2 service interfaces) can run in parallel once US1 is done.
- T010 and T011 (US2 aggregator edits) can run in parallel with each other (different files) once T008/T009 exist respectively.
- Once User Story 1 is complete, **User Story 2 and User Story 3 can proceed in parallel** (different files: US2 touches `core/`, `core/synchronous/`, and the two aggregators; US3 touches `StoragePolicy.java`/`StoragePolicySpec.groovy` only).

---

## Parallel Example: User Story 1

```bash
# Launch both failing Spock specs for User Story 1 together:
Task: "Write failing Spock spec in morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/StoragePolicySpec.groovy"
Task: "Write failing Spock spec in morpheus-plugin-api/src/test/groovy/com/morpheusdata/model/StoragePolicyIdentityProjectionSpec.groovy"

# Once both specs exist and fail, implement both classes together:
Task: "Create StoragePolicy.java in morpheus-plugin-api/src/main/java/com/morpheusdata/model/StoragePolicy.java"
Task: "Create StoragePolicyIdentityProjection.java in morpheus-plugin-api/src/main/java/com/morpheusdata/model/projection/StoragePolicyIdentityProjection.java"
```

## Parallel Example: User Story 2

```bash
# Once User Story 1 is complete, create both service interfaces together:
Task: "Create MorpheusStoragePolicyService.java in morpheus-plugin-api/src/main/java/com/morpheusdata/core/MorpheusStoragePolicyService.java"
Task: "Create MorpheusSynchronousStoragePolicyService.java in morpheus-plugin-api/src/main/java/com/morpheusdata/core/synchronous/MorpheusSynchronousStoragePolicyService.java"

# Then wire both aggregators together:
Task: "Edit MorpheusServices.java to add getStoragePolicy() accessor"
Task: "Edit MorpheusAsyncServices.java to add getStoragePolicy() accessor"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (T001).
2. Complete Phase 2: Foundational (T002, T003) — CRITICAL, blocks all stories.
3. Complete Phase 3: User Story 1 (T004–T007).
4. **STOP and VALIDATE**: `StoragePolicy`/`StoragePolicyIdentityProjection` compile and their Spock specs pass — the model contract is real, even though no plugin can call CRUD on it yet.

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready.
2. Add User Story 1 (model + projection) → unit-tested independently → this is the MVP contract slice.
3. Add User Story 2 (service pair + wiring) → verify wiring compiles/resolves independently → plugins can now perform full CRUD.
4. Add User Story 3 (`provisionTypeCode` field) → unit-tested independently → tiers can now be linked to a provisioning technology.
5. Complete Polish (T015–T017) → full regression + quickstart.md acceptance.

### Parallel Team Strategy

With multiple developers, after Foundational (T002, T003):

1. Developer A: User Story 1 (T004–T007) — must land first (US2/US3 both depend on it).
2. Once US1 lands: Developer B takes User Story 2 (T008–T012) while Developer C takes User Story 3 (T013–T014) — fully in parallel, different files.
3. Either developer (or a third) completes Polish (T015–T017) once both are done.

---

## Notes

- [P] tasks = different files, no dependencies.
- [Story] label maps task to specific user story for traceability.
- `provisionTypeCode` is deliberately deferred out of User Story 1's implementation
  (T006) into User Story 3 (T014), even though data-model.md documents it as one of
  `StoragePolicy`'s five fields — this gives User Story 3 a genuinely separable,
  independently-testable increment (adding one field to an existing file) rather than
  splitting one field's worth of code across two "parallel" tasks on the same file.
- Verify tests fail before implementing (T004/T005 before T006/T007; T013 before T014).
- Commit after each task or logical group.
- Stop at any checkpoint to validate a story independently.
- Avoid: vague tasks, same-file conflicts within a phase, cross-story dependencies that break independence (only the documented US1→US2 and US1→US3 dependencies exist; US2 and US3 are mutually independent).
