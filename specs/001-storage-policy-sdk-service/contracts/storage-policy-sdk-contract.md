# SDK Contract: StoragePolicy Service Pair

This document is the interface contract this feature adds to the `morpheus-plugin-api`
public SDK surface — i.e. what a plugin author (or morpheus-core, as the implementer)
can rely on after this feature ships. It is descriptive of the contract shape, not a
copy of the implementation.

## 1. `com.morpheusdata.core.MorpheusStoragePolicyService` (async, new)

```java
public interface MorpheusStoragePolicyService
        extends MorpheusDataService<StoragePolicy, StoragePolicyIdentityProjection>,
                MorpheusIdentityService<StoragePolicyIdentityProjection> {
}
```

Inherited capability (no new methods declared):

| Method (from base interfaces) | Signature | Purpose |
|---|---|---|
| `create` | `Single<StoragePolicy> create(StoragePolicy item)` | Create one record. |
| `bulkCreate` | `Single<BulkCreateResult<StoragePolicy>> bulkCreate(List<StoragePolicy> items)` | Create many records. |
| `save` | `Single<StoragePolicy> save(StoragePolicy item)` | Update one record. |
| `bulkSave` | `Single<BulkSaveResult<StoragePolicy>> bulkSave(List<StoragePolicy> items)` | Update many records. |
| `remove` | `Single<Boolean> remove(StoragePolicyIdentityProjection item)` | Delete one record (by identity). |
| `bulkRemove` | `Single<BulkRemoveResult<StoragePolicyIdentityProjection>> bulkRemove(List<StoragePolicyIdentityProjection> items)` | Delete many records. |
| `count` | `Single<Long> count(DataQuery query)` | Count matching records. |
| `get` | `Single<StoragePolicy> get(Long id)` | Fetch one record by id. |
| `listById` | `Observable<StoragePolicy> listById(List<Long> ids)` | Fetch many records by id. |
| `list` | `Observable<StoragePolicy> list(DataQuery query)` | Query records. |
| `listIdentityProjections` | `Observable<StoragePolicyIdentityProjection> listIdentityProjections(DataQuery query)` | Query lightweight identity records for sync/reconcile. |
| `getIdentityProperties` | `Collection<String> getIdentityProperties()` | Field names used for identity comparison. |

## 2. `com.morpheusdata.core.synchronous.MorpheusSynchronousStoragePolicyService` (sync, new)

```java
public interface MorpheusSynchronousStoragePolicyService
        extends MorpheusSynchronousDataService<StoragePolicy, StoragePolicyIdentityProjection>,
                MorpheusSynchronousIdentityService<StoragePolicyIdentityProjection> {
}
```

Same capability list as §1, blocking (`M`/`Boolean`/`List<M>` instead of `Single`/`Observable`),
via the base interfaces' default methods that delegate to the async service and
`.blockingGet()`/`.toList().blockingGet()`.

## 3. Discovery (service-locator wiring)

### `com.morpheusdata.core.MorpheusServices` (sync locator) — edited

```java
/**
 * Returns the StoragePolicy Service
 *
 * @return An instance of the StoragePolicy Service
 */
MorpheusSynchronousStoragePolicyService getStoragePolicy();
```

Added immediately after the existing `getStorageHostGroup()` method (FR-006).

### `com.morpheusdata.core.MorpheusAsyncServices` (async locator) — edited

```java
/**
 * Returns the StoragePolicy Service
 *
 * @return An instance of the StoragePolicy Service
 */
MorpheusStoragePolicyService getStoragePolicy();
```

Added immediately after the existing `getStorageHostGroup()` method (FR-007).

## 4. Model contract

See `data-model.md` for the full field list of `StoragePolicy` and
`StoragePolicyIdentityProjection`. Both classes are plain Java, extend the SDK's
existing model base classes (`MorpheusModel`, `MorpheusIdentityModel` respectively),
and follow the universal `markDirty(field, value)` setter convention (FR-003).

## 5. Compatibility contract (FR-009)

- No existing method signature in `MorpheusServices`, `MorpheusAsyncServices`,
  `MorpheusDataService`, `MorpheusSynchronousDataService`, `MorpheusIdentityService`,
  `MorpheusSynchronousIdentityService`, or any existing model/projection class changes.
- No existing package is repurposed; all new files land in
  `com.morpheusdata.core`, `com.morpheusdata.core.synchronous`, `com.morpheusdata.model`,
  and `com.morpheusdata.model.projection` — packages that already exist and already hold
  directly comparable files.
- Plugins and morpheus-core versions built against a pre-feature SDK jar continue to
  compile and run unchanged; only code that explicitly calls the two new
  `getStoragePolicy()` accessors (or references `StoragePolicy`/
  `StoragePolicyIdentityProjection`/the two new service interfaces directly) depends on
  this feature at all.
