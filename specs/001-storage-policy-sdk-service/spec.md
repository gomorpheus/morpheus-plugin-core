# Feature Specification: StoragePolicy SDK Service Pair

**Feature Branch**: `001-storage-policy-sdk-service`

**Created**: 2026-09-03

**Status**: Draft

**Input**: User description: "Add a new plugin-facing SDK service pair for a `StoragePolicy` domain model, so storage plugins (starting with `morpheus-storage-alletra-mp-plugin`) can directly create, update, and delete rows representing named QoS/performance storage tiers (e.g. Silver/Gold/Platinum), instead of the current indirect approach of writing generic `ReferenceData` rows that a separate `MINUTE`-frequency background job in morpheus-core has to poll and reconcile. This SDK addition is the first of three planned specs (the other two, against `morpheus-ui` and `morpheus-storage-alletra-mp-plugin`, will consume it in follow-up passes — out of scope for this spec, which is `morpheus-plugin-core` only)."

## User Scenarios & Testing *(mandatory)*

<!--
  IMPORTANT: User stories should be PRIORITIZED as user journeys ordered by importance.
  Each user story/journey must be INDEPENDENTLY TESTABLE - meaning if you implement just ONE of them,
  you should still have a viable MVP (Minimum Viable Product) that delivers value.

  Assign priorities (P1, P2, P3, etc.) to each story, where P1 is the most critical.
  Think of each story as a standalone slice of functionality that can be:
  - Developed independently
  - Tested independently
  - Deployed independently
  - Demonstrated to users independently
-->

### User Story 1 - Plugin author manages storage tiers through a dedicated data model (Priority: P1)

A storage plugin author (starting with the Alletra MP plugin) needs a first-class,
typed way to represent a named QoS/performance storage tier (e.g. Silver, Gold,
Platinum) that morpheus-core understands natively, so that the plugin's own code and
morpheus-core's UI/API layers share one unambiguous domain concept instead of the
plugin overloading a generic reference-data record and morpheus-core having to guess
which reference-data rows are "really" storage policies via a periodic reconciliation
job.

**Why this priority**: This is the foundational contract change — without a
`StoragePolicy` model and its identity/match key, no plugin can create, list, or
correlate storage tier records in a targeted way, and the two downstream specs
(morpheus-ui implementation, plugin consumption) cannot start.

**Independent Test**: Can be fully tested by confirming the new model and projection
classes exist in the SDK, expose the documented fields, and carry the same
getter/setter change-tracking behavior already relied upon by every other SDK model
(verified by a unit spec against the model/projection classes alone, with no
morpheus-core or plugin runtime involved).

**Acceptance Scenarios**:

1. **Given** the SDK is compiled as a dependency, **When** a plugin author references
   the new storage-tier domain model, **Then** it exposes a stable per-tier code, a
   display name, a display order, an enabled flag, and a reference to the target
   provisioning technology's stable code, all independent of any morpheus-core
   database identifiers.
2. **Given** two storage-tier records loaded from morpheus-core, **When** a plugin
   compares them using the identity/match representation, **Then** the comparison
   relies only on the lightweight identity fields (id, code, name) rather than the
   full record, consistent with every other identity-projection pattern in the SDK.

---

### User Story 2 - Plugin author performs CRUD on storage tiers via a dedicated service (Priority: P1)

A plugin author needs to create, update, and delete named storage-tier records
directly through the SDK's plugin-facing service layer — both in blocking
(synchronous) and reactive (asynchronous) plugin code paths — the same way they
already do for every other core domain object (e.g. storage servers, OS types),
without needing any tier-specific methods beyond the create/update/delete/list
operations every other simple reference-style domain object already gets for free.

**Why this priority**: This is the mechanism that actually replaces the indirect
`ReferenceData` + polling-job approach described in the feature input — it is the
direct causal fix for the problem being solved, and is equally foundational to the P1
model story above (both are required before any plugin can stop using the indirect
approach).

**Independent Test**: Can be fully tested by confirming the new service pair is
reachable from the SDK's existing plugin service-locator entry points (the
synchronous and asynchronous service aggregators) and exposes exactly the
inherited create/update/delete/list operations, with no dedicated test needed beyond
confirming the wiring compiles and resolves (this repo defines the contract; actual
CRUD behavior is implemented and tested downstream in morpheus-core).

**Acceptance Scenarios**:

1. **Given** a plugin running in blocking mode, **When** it asks the plugin's
   synchronous service locator for the storage-tier service, **Then** it receives a
   service capable of creating, saving, listing (by query), and removing
   storage-tier records without needing any additional tier-specific method.
2. **Given** a plugin running in reactive mode, **When** it asks the plugin's
   asynchronous service locator for the storage-tier service, **Then** it receives
   the reactive-stream equivalent of the same create/save/list/remove capability.
3. **Given** the new service pair, **When** a plugin author looks at the placement of
   the new accessors in both service locators, **Then** they are grouped with the
   other existing storage-related accessors, consistent with how every other domain
   service is discoverable in the SDK.

---

### User Story 3 - Plugin author links a storage tier to a specific provisioning technology (Priority: P2)

A plugin author needs to indicate which provisioning technology (e.g. a specific
cloud/hypervisor integration) a given storage tier applies to, using a value the
plugin already has locally (the provisioning technology's stable code), because the
plugin has no way today to look up or hold onto morpheus-core's internal numeric
identifier for that provisioning technology.

**Why this priority**: This enables the tier-to-provisioning-technology association
that the downstream morpheus-core implementation needs to resolve internally, but a
plugin can still create/list/remove basic tier records (P1) even before this
association is populated, so it is a closely-related but separable increment.

**Independent Test**: Can be fully tested by confirming the storage-tier model
carries a plain stable-code field for the target provisioning technology (not a
direct reference to morpheus-core's internal provisioning-technology record), and
that this field participates in the model's standard field-level change tracking
like every other field.

**Acceptance Scenarios**:

1. **Given** a plugin author creates a storage-tier record, **When** they set the
   target provisioning technology using its stable code, **Then** the record carries
   that code as a plain value without requiring the plugin to resolve or hold any
   morpheus-core-internal numeric identifier.

---

### Edge Cases

- What happens when a plugin submits a storage-tier record whose stable code
  duplicates an existing record's code? (Resolution of upsert-by-code semantics is a
  morpheus-core implementation concern for the downstream spec; this spec only
  guarantees the code field exists and is documented as the intended upsert key.)
- What happens when a plugin references a provisioning-technology code that does not
  exist in morpheus-core? (Resolution/validation of that reference is a
  morpheus-core implementation concern for the downstream spec; this spec only
  guarantees the field is carried as a plain stable-code value.)
- How does the SDK behave if a plugin omits optional-looking fields (e.g. display
  order, enabled)? (The SDK model itself enforces no validation — validation, if any,
  is a morpheus-core implementation concern for the downstream spec.)

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The SDK MUST expose a plugin-facing domain model representing a named
  storage QoS/performance tier, carrying: a stable per-tier code (the intended
  upsert/match key), a display name, a display order, an enabled flag, and the stable
  code of the target provisioning technology it applies to.
- **FR-002**: The SDK MUST expose a lightweight identity/match representation of the
  storage-tier model (mirroring the SDK's existing identity-projection pattern used
  by comparable domain objects), carrying at minimum the record's identifier, stable
  code, and display name, so plugins can efficiently compare and reconcile
  already-known records without needing the full record.
- **FR-003**: Both the full storage-tier model and its identity/match representation
  MUST support the SDK's existing field-level change-tracking behavior (the same
  getter/setter dirty-tracking convention used by every other SDK model/projection),
  so partial updates behave consistently with the rest of the SDK.
- **FR-004**: The SDK MUST expose a blocking (synchronous) plugin-facing service for
  the storage-tier domain model that provides create, save/update, list-by-query, and
  remove operations, consistent with the SDK's existing base data-service contract
  used by every other simple reference-style domain object — no storage-tier-specific
  methods beyond that base contract are required.
- **FR-005**: The SDK MUST expose a reactive (asynchronous) plugin-facing service for
  the storage-tier domain model providing the same create/save/list/remove
  capability as FR-004, using the SDK's existing reactive-stream conventions.
- **FR-006**: The SDK MUST make the new synchronous storage-tier service discoverable
  through the SDK's existing synchronous plugin service locator, grouped alongside
  the SDK's other storage-related service accessors.
- **FR-007**: The SDK MUST make the new asynchronous storage-tier service
  discoverable through the SDK's existing asynchronous plugin service locator,
  grouped alongside the SDK's other storage-related service accessors.
- **FR-008**: The new model, projection, and service files MUST follow the SDK's
  existing package layout and file-header conventions applied uniformly across the
  rest of the SDK's plugin-facing surface, so the addition is indistinguishable in
  form from long-standing SDK contracts.
- **FR-009**: This feature MUST NOT alter or remove any existing SDK service,
  model, or projection — it is purely additive, so that plugins and morpheus-core
  versions that do not yet use the storage-tier capability continue to function
  unchanged.

### Key Entities *(include if feature involves data)*

- **Storage Policy**: Represents one named QoS/performance storage tier (e.g.
  Silver, Gold, Platinum) that a storage plugin exposes to morpheus-core. Key
  attributes: a stable per-tier code (upsert/match key), a display name, a display
  order (for consistent presentation ordering), an enabled flag, and the stable code
  of the provisioning technology the tier applies to. Does not itself hold a direct
  reference to morpheus-core's internal provisioning-technology record — only that
  record's stable code, since the SDK provides no other way for a plugin to resolve
  or hold that internal reference.
- **Storage Policy Identity**: A lightweight match/comparison representation of a
  Storage Policy, carrying just enough information (identifier, stable code, display
  name) for a plugin to recognize and reconcile already-known records without
  transferring the full record.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A plugin author can represent, create, update, and remove a named
  storage tier using exactly one dedicated SDK domain concept, with zero need to
  overload a generic/unrelated record type to represent tier data.
- **SC-002**: A plugin author can access full create/update/list/remove capability
  for storage tiers through the same two discovery points (blocking and reactive
  service locators) used for every other domain object in the SDK, with no
  tier-specific learning curve beyond locating the accessor.
- **SC-003**: The downstream morpheus-core and plugin consumption specs can begin
  their work immediately after this spec ships, with zero ambiguity about the shape,
  field names, or location of the storage-tier contract they must implement/consume.
- **SC-004**: 100% of existing SDK consumers (plugins and morpheus-core versions not
  yet updated to use storage tiers) continue to compile and run unchanged, since the
  addition introduces no breaking change to any existing contract.

## Assumptions

- This spec covers only the `morpheus-plugin-core` SDK contract (the domain model,
  its identity/match projection, and the paired blocking/reactive services). It does
  not implement morpheus-core's backing persistence/reconciliation logic, does not
  change morpheus-core's UI, and does not change the consuming
  `morpheus-storage-alletra-mp-plugin` plugin's own code — those are explicitly the
  two follow-up specs referenced in the feature input.
- "Provisioning technology" in this spec corresponds to what morpheus-core internally
  models as a provision type; the SDK already exposes a stable code for that concept
  but no plugin-facing lookup service for it, which is why the storage-tier model
  carries that stable code as a plain value rather than a structured reference.
- The storage-tier model requires no plugin-triggered side-effect operations
  (e.g. no refresh/validate/definition-update behavior), because it is a simple
  named-lookup/reference-style domain object, not an object with its own lifecycle
  operations invoked by morpheus-core against the plugin.
- Resolution of the provisioning-technology stable code into morpheus-core's internal
  record, and any validation/upsert-conflict handling for the tier's stable code, are
  morpheus-core implementation concerns to be addressed in the downstream
  `morpheus-ui` spec, not in this SDK contract.
- Functional/integration-level verification that the new service pair actually
  performs correct create/update/delete/list behavior against real data is out of
  scope for this SDK-contract repo; that coverage belongs to the downstream
  morpheus-core implementation and plugin consumption specs, per this repo's own
  testing-scope conventions.
