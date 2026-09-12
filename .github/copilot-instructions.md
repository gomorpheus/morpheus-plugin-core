# morpheus-plugin-core

Morpheus plugin **SDK**: extension interfaces + models only, no product logic. Published as
`com.morpheusdata:morpheus-plugin-api`; consumed by plugin repos, implemented by `morpheus-ui`.
Supports many use cases (clouds, DNS, IPAM, UI extensions, tasks, configuration workflows).

## Contribution rules
- Edit the SDK under `morpheus-plugin-api/`; keep interfaces binary-compatible (semver).
- Provider methods return `ServiceResponse` — never throw across the plugin boundary.
- Data access is reactive (RxJava `Observable`/`Maybe`); avoid `blockingGet()`.
- Run `./gradlew morpheus-plugin-api:test` before pushing.
