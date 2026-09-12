---
name: optiontype-reference
description: >-
  Field-level reference for OptionType — the model that defines a form input in Morpheus
  (used by wizards, provisioning, policies, and settings). Use when creating or editing
  OptionType fields, choosing an InputType, or wiring conditional visibility/requirement
  via visibleOnCode / requireOnCode / dependsOn.
---

# OptionType reference

Source (open for all 60+ props): `morpheus-plugin-api/src/main/java/com/morpheusdata/model/OptionType.java`.

## Core props
`code`, `name`, `fieldName`, `fieldLabel`, `fieldContext` (default `'config'`), `inputType`,
`required`, `defaultValue`, `displayOrder`, `fieldGroup`, `helpText`, `placeHolderText`.

## Validation
`verifyPattern` (regex), `minVal`/`maxVal`, `minLength`/`maxLength`.

## Options (SELECT / TYPEAHEAD)
`optionSource` = DatasetProvider key; `optionSourceType` = namespace. **No inline `optionList`.**

## Conditional logic
- `visibleOnCode` / `requireOnCode` — format `'fieldName:regex'`;
  multi: `'matchAll::f1:v1,f2:v2'` or `'matchAny::...'`.
- `dependsOn` — comma-separated field codes that trigger a refresh.

## Custom UI
`fieldComponent` = registered JS component name; `config` = JSON string of component props.

## InputType enum
TEXT, PASSWORD, NUMBER, CHECKBOX, SELECT, RADIO, TEXTAREA, HIDDEN, TYPEAHEAD,
MULTISELECT, BYTESIZE, CODE_EDITOR, FILE, TILE_SELECT, ... (see enum in source).
