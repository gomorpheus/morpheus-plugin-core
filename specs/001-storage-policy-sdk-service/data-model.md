# Phase 1 Data Model: StoragePolicy SDK Service Pair

## Entity: `StoragePolicy` (`com.morpheusdata.model.StoragePolicy`)

Represents one named QoS/performance storage tier (e.g. Silver, Gold, Platinum) exposed
by a storage plugin to morpheus-core. Extends `com.morpheusdata.model.MorpheusModel`
directly (see `research.md` §1 for rationale), gaining `id`, dirty-tracking
(`markDirty`), and `config`/`configMap` handling for free from the base class.

| Field | Type | Source requirement | Notes |
|---|---|---|---|
| `code` | `String` | FR-001, Key Entities | Stable per-tier code; the intended upsert/match key (spec Edge Cases + Assumptions: upsert-conflict resolution is a downstream `morpheus-ui` concern, not enforced here). |
| `name` | `String` | FR-001, Key Entities | Display name. |
| `displayOrder` | `Integer` | FR-001, Key Entities | For consistent presentation ordering. No default enforced by the SDK (spec Edge Cases: "the SDK model itself enforces no validation"). |
| `enabled` | `Boolean` | FR-001, Key Entities | Enabled flag. No default enforced by the SDK, same rationale as `displayOrder`. |
| `provisionTypeCode` | `String` | FR-001, Key Entities, User Story 3 | The stable code of the target provisioning technology this tier applies to — a **plain value**, not a reference to morpheus-core's internal provision-type record (spec Assumptions: the SDK exposes no plugin-facing lookup service for provision types, so only the stable code is carried). |

All five getters/setters follow the SDK's universal convention: every setter calls
`markDirty(fieldName, value)` after assignment (FR-003), matching
`StorageControllerType`/`StorageServerIdentityProjection` and every other existing
model.

No validation, no state machine, no relationships to other model objects are declared
on this class — per spec Assumptions, validation/resolution of `provisionTypeCode` and
upsert-conflict handling for `code` are morpheus-core implementation concerns, out of
scope for this SDK contract.

## Entity: `StoragePolicyIdentityProjection` (`com.morpheusdata.model.projection.StoragePolicyIdentityProjection`)

A lightweight match/comparison representation of `StoragePolicy`, for sync/reconcile use
without transferring the full record (FR-002). Extends
`com.morpheusdata.model.projection.MorpheusIdentityModel` (which itself extends
`MorpheusModel`), mirroring `StorageServerIdentityProjection` exactly.

| Field | Type | Source requirement | Notes |
|---|---|---|---|
| `id` | `Long` | FR-002 | Inherited from `MorpheusModel` — not redeclared. |
| `code` | `String` | FR-002, Key Entities ("Storage Policy Identity") | Stable per-tier code, same semantic as the full model's `code`. |
| `name` | `String` | FR-002, Key Entities ("Storage Policy Identity") | Display name, same semantic as the full model's `name`. |

Mirrors `StorageServerIdentityProjection`'s structure:
- A no-arg default constructor.
- A convenience constructor `StoragePolicyIdentityProjection(Long id, String code, String name)`
  for fallback-match construction (analogous to `StorageServerIdentityProjection(Long id, String name)`,
  extended here with `code` since `code` — not just `name` — is the documented
  upsert/match key per FR-001/Edge Cases).
- Both `code` and `name` getters/setters call `markDirty(fieldName, value)` (FR-003).

No relationship to `StoragePolicy` beyond field-name/semantic parity is declared in code
— per SDK convention, the full model does not extend the projection (see `research.md`
§1) and the projection does not reference the full model.

## Field-level Change Tracking (FR-003)

Both classes rely entirely on the inherited `MorpheusModel.markDirty(String, Object)` /
`getDirtyPropertyValues()` / `isDirty(String)` / `markClean()` machinery — no new
change-tracking logic is written. This is validated by the Spock specs (see
`quickstart.md`), which assert `markDirty` is invoked with the correct field name/value
per setter, following the exact assertions already used in `MorpheusModelSpec.groovy`.

## Non-Entities (explicitly not modeled here)

- **Provisioning technology / provision type**: no new or changed model — only a plain
  `String provisionTypeCode` field on `StoragePolicy` references it by stable code
  (spec Assumptions).
- **Upsert/reconciliation logic**: no code in this repo resolves `code` duplicates or
  validates `provisionTypeCode` against real provision-type records — downstream
  `morpheus-ui` concern (spec Assumptions, Edge Cases).
