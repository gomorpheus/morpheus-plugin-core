---
name: configuration-workflow-api
description: >-
  API contracts for building multi-step, wizard-driven Configuration Workflows in Morpheus
  plugins. Use when implementing or reviewing ConfigurationWorkflowProvider, WizardProvider,
  DatasetProvider, or InputTypeLibraryProvider; defining form fields with OptionType; wiring
  dynamic dropdowns/typeahead; provider registration order; or workflow state/validation.
  Applies to the morpheus-plugin-core (morpheus-plugin-api) SDK.
---

# Configuration Workflow API

Extension points: `morpheus-plugin-api/src/main/java/com/morpheusdata/core/providers/`; models: `.../model/`.
**Open the interface source for exact signatures** (methods marked `default` are optional). All methods
return `ServiceResponse` — never throw across the boundary.

## Extension points
- **ConfigurationWorkflowProvider** — orchestrates the multi-step workflow: defines steps, `saveStepConfiguration`
  (merge per step), `updateParentState` (persist JSON), `validate*` / `submit*`.
- **WizardProvider** — one wizard: `getWizardSteps()` (each `WizardStep.optionTypes` = `OptionType` fields),
  `validateWizard` / `submitWizard`.
- **DatasetProvider<T,V>** (extend `AbstractDatasetProvider`) — dynamic SELECT/TYPEAHEAD options; `list`/`listOptions`
  return RxJava `Observable`; set `DatasetInfo(namespace, key, name, desc)` in the ctor.
- **InputTypeLibraryProvider** (extend `AbstractInputTypeLibraryProvider`) — register a JS component (served from
  `src/main/assets/js/`) via `window.Morpheus.components.registry`; reference it through `OptionType.fieldComponent`.

For form-field details (props, InputType, conditional logic) use the **optiontype-reference** skill.

## Registration order (`Plugin.initialize()`) — deps flow up
InputType/Dataset → Wizard → ConfigurationWorkflow → domain/System provider.
`ConfigurationWorkflowStep.wizard` must come from `plugin.getProviderByCode(code).getWizard()` — never `new Wizard()`.

## Patterns
- **State**: JSON on the parent, append per step, simple types/maps only. Persist via
  `system.setConfigurationWorkflowState(JsonOutput.toJson(state))` then `morpheusContext.async.system.save(...)`.
- **Validation levels**: field (`verifyPattern`/`required`/min-max) → `validateWizardStep` → `validateWizard` → `validateConfigurationWorkflow`.
- **RxJava**: `filter`/`map`/`flatMap`; `onErrorResumeNext { Observable.empty() }`; avoid `blockingGet()`/`toList()`.

## Pitfalls
1. Registration order (workflow before its wizards/datasets).
2. `OptionType.optionList` instead of a DatasetProvider.
3. Throwing instead of `ServiceResponse.error(...)`.
4. Non-serializable objects in workflow state.
5. Bare `new Wizard()` instead of resolving from the provider.

Reference impl: `pcbe-system-plugin`. Platform side: `morpheus-ui`.
