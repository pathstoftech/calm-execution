# Post-Release Ownership — 30 Days of Calm Execution

Status: Done  
Milestone: E5 Post-release readiness  
Task: E5-01 Assign post-release ownership  
Owner: Product owner  
Reviewer: Tech lead  
Baseline tag: `v1.0.0-rc1`

## Scope

This document assigns ownership for post-release follow-up after the `v1.0.0-rc1` release-hardening baseline.

This task does not change runtime behavior, catalog content, image assets, build configuration, or release scope.

## Release baseline

Current release candidate:

```text
v1.0.0-rc1
```

Release baseline status:

```text
E1 product freeze: complete
E2 quality closure: complete
E3 architecture/code-quality closure: complete
E4 release/configuration closure: complete through E4-06
```

Release-blocking defect status:

```text
Open Blocker defects: 0
Open Critical defects: 0
Open High defects: 0
```

## Ownership model

| Area                             | Primary owner            | Reviewer / backup | Responsibility                                                                           |
|----------------------------------|--------------------------|-------------------|------------------------------------------------------------------------------------------|
| Product scope                    | Product owner            | Tech lead         | Decide whether deferred items enter a future release.                                    |
| Release baseline                 | Tech lead                | Product owner     | Protect the tagged baseline and approve release-candidate changes.                       |
| Android implementation           | Android engineer         | Tech lead         | Implement approved fixes and post-release improvements.                                  |
| Build / dependency modernization | Build/CI engineer        | Tech lead         | Handle Gradle, AGP, Kotlin, KSP, AndroidX, and release pipeline modernization.           |
| QA regression                    | QA engineer              | Product owner     | Re-run regression gates after fixes or dependency upgrades.                              |
| Accessibility                    | Accessibility reviewer   | Product owner     | Own TalkBack, font scale, touch target, heading semantics, and reduced-motion follow-up. |
| Content / catalog                | Product/content reviewer | Product owner     | Own future changes to tips, descriptions, sectioning, and editorial quality.             |
| Design / UI polish               | Design reviewer          | Product owner     | Own visual polish items and adaptive-layout refinements.                                 |
| Hotfix decision                  | Tech lead                | Product owner     | Decide whether a defect requires a hotfix branch and patch release.                      |

## Named post-release workstreams

### PR-001 — Build modernization

Primary owner: Build/CI engineer  
Reviewer: Tech lead  
Priority: P1 after V1 baseline

Scope:

- remove or resolve `android.newDsl=false` warning
- investigate Gradle deprecated feature warnings
- prepare for Gradle 10 compatibility
- prepare for AGP 10 compatibility
- review Java/JDK baseline pinning
- decide whether Java 21 daemon remains accepted or is pinned differently

Source known issues:

- KI-002
- KI-003
- E4-02 JVM policy note

Exit criteria:

```text
Build runs without known Gradle/AGP deprecation warnings, or warnings are reclassified with current evidence.
```

---

### PR-002 — Dependency modernization

Primary owner: Build/CI engineer  
Reviewer: Android engineer / Tech lead  
Priority: P1 after V1 baseline

Scope:

- revisit Kotlin / KSP / Hilt alignment
- review AndroidX Core version
- review AndroidX Lifecycle version
- preserve Compose BOM alignment
- rerun full E2 regression suite after dependency changes

Source known issues:

- KI-006
- KI-007

Exit criteria:

```text
Dependency updates are applied or explicitly deferred with current compatibility evidence.
Full regression suite passes after any dependency changes.
```

---

### PR-003 — Accessibility refinement

Primary owner: Accessibility reviewer  
Reviewer: Android engineer / Product owner  
Priority: P1 / P2 depending on release target

Scope:

- add heading semantics to detail sections:
  - Problem
  - Tip
  - Why it helps
  - Try today
- review expanded return action touch target
- keep TalkBack list-detail focus behavior stable
- keep large-font behavior stable

Source known issues:

- KI-014
- KI-015

Exit criteria:

```text
Accessibility improvements are implemented without regressing TalkBack flow, large font behavior, or expanded detail focus behavior.
```

---

### PR-004 — Adaptive layout / navigation stress testing

Primary owner: Android engineer  
Reviewer: QA engineer / Tech lead  
Priority: P2 unless reproduced as user-visible defect

Scope:

- stress-test compact ↔ expanded transitions
- test foldable-style resize behavior
- test split-screen resizing
- verify NavHost continuity
- verify no wrong destination opens
- verify no lost bookmark/complete state after resize

Source known issue:

- KI-010

Exit criteria:

```text
Adaptive transition behavior is either confirmed stable or redesigned with passing compact/expanded regression coverage.
```

---

### PR-005 — Settings instrumentation coverage

Primary owner: QA engineer  
Reviewer: Android engineer  
Priority: P2

Scope:

- add dedicated `SettingsScreenTest`
- cover theme mode control
- cover dynamic color toggle
- cover reduced motion toggle
- cover reset confirmation dialog
- cover back navigation from Settings

Source known issue:

- KI-008

Exit criteria:

```text
SettingsScreenTest exists under androidTest and passes in the compact connected test group.
```

---

### PR-006 — Public store readiness

Primary owner: Product owner  
Reviewer: Tech lead / Build/CI engineer  
Priority: P2 / future release

Scope:

- signing policy
- release artifact policy
- minification / shrinking / obfuscation decision
- ProGuard/R8 review
- Play Store workflow
- privacy / data safety review
- crash reporting decision
- monitoring plan

Source known issues:

- KI-009
- KI-016

Exit criteria:

```text
A public-store release checklist exists and is approved before any public distribution.
```

---

### PR-007 — Runtime monitoring / crash-reporting plan

Primary owner: Tech lead  
Reviewer: Product owner  
Priority: Covered by E5-03

Scope:

- decide whether crash reporting is needed for V1 distribution model
- define manual issue intake if no telemetry is used
- define incident response for launch crash, data corruption, and navigation crash

Exit criteria:

```text
Monitoring and crash-reporting plan is documented under E5-03.
```

## Hotfix ownership

Hotfix decision owner:

```text
Tech lead
```

Product approval owner:

```text
Product owner
```

Implementation owner:

```text
Android engineer
```

QA owner:

```text
QA engineer
```

A hotfix may be opened only for:

```text
Blocker defect
Critical defect
High defect approved by Tech lead + Product owner
Security/privacy issue
Release artifact integrity issue
```

Medium/Low issues should normally enter a planned post-release task instead of a hotfix.

## Baseline protection rule

The tag `v1.0.0-rc1` must not be moved or force-updated.

If another release candidate is required, create a new tag:

```text
v1.0.0-rc2
```

If a final release is approved from a release candidate, create a new final tag:

```text
v1.0.0
```

## Decision

E5-01 passes.

Post-release ownership is assigned for:

- product decisions
- release baseline control
- Android implementation
- QA regression
- accessibility refinement
- dependency/build modernization
- content/catalog ownership
- adaptive navigation follow-up
- public store readiness
- hotfix decision-making

