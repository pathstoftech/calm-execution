# D5-04 Accessibility Sanity Pass

Task: D5-04 Run accessibility sanity pass  
Milestone: Product completeness  
Epic: Release hardening  
Priority: P1  
Estimate: S  
Owner: QA engineer  
Reviewer: Accessibility reviewer  
Depends on: D1-11, D2-05, D4-05  
Status: In progress

## Purpose

This document records the accessibility sanity pass for **30 Days of Calm Execution**.

The goal is to verify that the current product-complete app is usable with basic Android accessibility expectations before release hardening begins.

This is a **sanity pass**, not a formal WCAG audit.

The pass focuses on:

- screen-reader usability;
- meaningful labels;
- image content descriptions;
- action discoverability;
- large-text resilience;
- compact and expanded layout behavior;
- critical accessibility blockers.

## Scope

Covered screens and flows:

- Home screen
- Tip Detail screen
- Settings screen
- Compact phone navigation
- Expanded tablet list-detail layout
- Runtime tip images
- Bookmark action
- Completion action
- Reset progress flow
- Loading state
- Error state
- Empty expanded detail placeholder

Out of scope for this pass:

- full WCAG conformance audit;
- real-user assistive technology testing;
- color-blindness simulation beyond basic scanner/manual review;
- full localization accessibility;
- production Play Store accessibility declaration;
- exhaustive device matrix.

## Source baseline

This pass is based on the current implementation state after:

| Task                              | Status |
|-----------------------------------|--------|
| D5-01 Add Settings tests          | Done   |
| D5-02 Add expanded adaptive tests | Done   |
| D5-03 Add asset validation tests  | Done   |

Relevant completed verification:

| Verification                                              | Result |
|-----------------------------------------------------------|--------|
| Settings ViewModel tests                                  | Passed |
| Compact instrumentation bucket on Pixel 9a                | Passed |
| Expanded adaptive instrumentation bucket on Medium Tablet | Passed |
| Asset validation tests                                    | Passed |
| `testDebugUnitTest`                                       | Passed |
| `assembleDebug`                                           | Passed |

## Test environment

### Compact phone environment

| Field       | Value                                      |
|-------------|--------------------------------------------|
| Device      | Pixel 9a AVD                               |
| Layout mode | Compact                                    |
| Purpose     | Home, Detail, Settings, compact navigation |
| Result      | Passed                                     |

### Expanded tablet environment

| Field       | Value                       |
|-------------|-----------------------------|
| Device      | Medium Tablet AVD           |
| Layout mode | Expanded                    |
| Purpose     | Adaptive list-detail layout |
| Result      | Passed                      |

## Verification commands

### JVM / build verification

```bash
./gradlew testDebugUnitTest assembleDebug --stacktrace
```

Result:

```text
Passed on 17.05.2026
```

### Compact instrumentation bucket

Run on Pixel 9a AVD:

```powershell
./gradlew connectedDebugAndroidTest ^
  --project-prop "android.testInstrumentationRunnerArguments.class=com.example.a30daysofcalmexecution.navigation.CompactNavigationTest,com.example.a30daysofcalmexecution.ui.home.HomeScreenTest,com.example.a30daysofcalmexecution.ui.detail.TipDetailScreenTest" ^
  --stacktrace
```

Result:

```text
Passed on Pixel 9a AVD. 20 tests executed, 0 failed.
```

### Expanded instrumentation bucket

Run on Medium Tablet AVD:

```powershell
./gradlew connectedDebugAndroidTest ^
  --project-prop "android.testInstrumentationRunnerArguments.class=com.example.a30daysofcalmexecution.ui.adaptive.AdaptiveAppShellTest,com.example.a30daysofcalmexecution.ui.adaptive.ExpandedListDetailLayoutTest" ^
  --stacktrace
```

Result:

```text
Passed on Medium Tablet AVD. 5 tests executed, 0 failed.
```

## Accessibility test methods

This pass uses:

1. Manual TalkBack traversal
2. Accessibility Scanner review
3. Large text / display-size sanity check
4. Existing Compose UI instrumentation tests
5. Existing JVM validation tests for image metadata

## Manual TalkBack pass

### Setup

Before starting:

- Enable TalkBack.
- Use default system font size first.
- Use the Pixel 9a AVD for compact checks.
- Use the Medium Tablet AVD for expanded checks.
- Start each screen from a clean app launch where practical.

### General TalkBack expectations

Across all screens:

- Focus order should be understandable.
- Critical actions should be reachable.
- Interactive elements should have meaningful labels.
- State changes should not trap the user.
- Decorative or redundant images should not create noisy duplicate announcements.
- Non-decorative images should have useful content descriptions.
- Error and loading states should be understandable.
- Dialogs should expose title, message, confirm action, and cancel action.

## Home screen checklist

Device: Pixel 9a AVD  
Layout: Compact

### Expected behavior

- App title is reachable and understandable.
- Intro / hero copy is reachable.
- Journey progress is reachable and meaningful.
- Section chips are reachable.
- Current selected section is understandable.
- Tip cards expose useful information:
  - day number;
  - title;
  - preview text;
  - category;
  - bookmark state where applicable;
  - completion state where applicable.
- Tip card tap/open behavior is reachable.
- Bookmark action is reachable.
- Completion action is reachable.
- Settings entry point is reachable and clearly labeled.
- Empty filtered state is understandable.
- Error state exposes a retry path.
- Loading state does not expose stale or confusing content.

### Result

- [x] Pass
- [ ] Issue found
- [ ] Not tested

### Notes

```text
TalkBack traversal completed on Pixel 9a. Main content, section chips, tip cards, bookmark action, completion action, and Settings entry point were reachable. No P0/P1 issue found.
```

## Detail screen checklist

Device: Pixel 9a AVD  
Layout: Compact

### Expected behavior

- Back navigation is reachable.
- Hero image is not noisy.
- If the hero image is non-decorative, it has a useful content description.
- Day number is reachable.
- Title is reachable.
- Category is reachable.
- Content sections are reachable in logical order:
  1. Problem
  2. Tip
  3. Why it helps
  4. Try today
- Bookmark action is reachable.
- Bookmark action label reflects current state.
- Completion action is reachable.
- Completion action label reflects current state.
- Error state is understandable.
- Retry action is reachable in error state.
- Loading state is understandable.

### Result

- [x] Pass
- [ ] Issue found
- [ ] Not tested

### Notes

```text
Detail screen TalkBack traversal completed. Back action, title, category, content sections, bookmark action, and completion action were reachable and understandable. No P0/P1 issue found.
```

## Settings screen checklist

Device: Pixel 9a AVD  
Layout: Compact

### Expected behavior

- Back navigation is reachable.
- Screen title is reachable.
- Theme mode setting is reachable.
- Current theme mode is understandable.
- Theme mode options are reachable.
- Dynamic color switch is reachable.
- Dynamic color checked/unchecked state is understandable.
- Reduced motion switch is reachable.
- Reduced motion checked/unchecked state is understandable.
- Reset progress action is reachable.
- Reset confirmation dialog exposes:
  - title;
  - message;
  - confirm action;
  - cancel action.
- Cancel dismisses the reset dialog.
- Reset confirms the destructive action.
- The dialog does not trap focus after dismissal.

### Result

- [x] Pass
- [ ] Issue found
- [ ] Not tested

### Notes

```text
Settings TalkBack traversal completed. Theme mode, dynamic color, reduced motion, reset progress, and reset confirmation dialog were reachable. No P0/P1 issue found.
```

## Expanded tablet layout checklist

Device: Medium Tablet AVD  
Layout: Expanded

### Expected behavior

- Feed pane is reachable.
- Detail pane is reachable.
- Empty detail placeholder is understandable.
- Selecting a tip updates the detail pane.
- The selected detail content is reachable.
- The list remains usable while detail is visible.
- Duplicate visible title in list and detail panes is not confusing.
- Bookmark action in expanded detail is reachable.
- Completion action in expanded detail is reachable.
- Back/clear behavior does not trap focus.
- Touch exploration can move between panes.

### Result

- [ ] Pass
- [ ] Issue found
- [ ] Not tested

### Notes

```text
Add notes here.
```

## Runtime image accessibility checklist

Source: `app/src/main/res/raw/tips_catalog.json`  
Runtime assets: `app/src/main/res/drawable-nodpi/`

### Automated checks already covered by D5-03

| Check                                                      | Result |
|------------------------------------------------------------|--------|
| Catalog image keys resolve to runtime WebP assets          | Passed |
| Runtime WebP assets are all referenced by catalog          | Passed |
| Exactly 30 tip assets are present                          | Passed |
| Asset names follow `tip_XX_lower_snake_case.webp`          | Passed |
| Image descriptions satisfy decorative/non-decorative rules | Passed |
| `CatalogValidator` validates with real runtime image keys  | Passed |

### Manual sanity expectations

- Image descriptions describe what is actually visible.
- Image descriptions are concise.
- Image descriptions do not repeat nearby text unnecessarily.
- Decorative images are not announced as meaningful content.
- No tip has a missing image during normal runtime use.

### Result

- [ ] Pass
- [ ] Issue found
- [ ] Not tested

### Notes

```text
Add notes here.
```

## Accessibility Scanner pass

Run Android Accessibility Scanner on the following screens.

| Screen / state             | Device        | Result  | Notes |
|----------------------------|---------------|---------|-------|
| Home compact               | Pixel 9a      | Pending |       |
| Home with filtered section | Pixel 9a      | Pending |       |
| Detail compact             | Pixel 9a      | Pending |       |
| Detail error state         | Pixel 9a      | Pending |       |
| Settings compact           | Pixel 9a      | Pending |       |
| Reset confirmation dialog  | Pixel 9a      | Pending |       |
| Expanded empty detail      | Medium Tablet | Pending |       |
| Expanded selected detail   | Medium Tablet | Pending |       |

### Scanner triage rules

| Severity       | Meaning                                                                                                             | Required action                                                          |
|----------------|---------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| P0             | Critical blocker: inaccessible navigation, unlabeled destructive action, inaccessible dialog, unusable primary flow | Must fix before release hardening                                        |
| P1             | Important issue affecting common flow or critical understanding                                                     | Must fix before release hardening or explicitly defer with justification |
| P2             | Minor issue, redundancy, non-blocking warning, low-risk improvement                                                 | Document; may defer                                                      |
| False positive | Scanner warning not applicable to current UI semantics                                                              | Document reason                                                          |

## Large text sanity pass

Increase system font size and display size.

### Expected behavior

- No critical clipping.
- Primary actions remain reachable.
- Tip cards remain readable.
- Detail content remains scrollable.
- Settings controls remain usable.
- Reset dialog remains usable.
- Expanded layout remains navigable.

| Device        | Setting                | Result  | Notes |
|---------------|------------------------|---------|-------|
| Pixel 9a      | Increased font size    | Pending |       |
| Pixel 9a      | Increased display size | Pending |       |
| Medium Tablet | Increased font size    | Pending |       |
| Medium Tablet | Increased display size | Pending |       |

## Keyboard / non-touch sanity pass

Optional for this release stage, but useful if time permits.

| Check                                    | Device        | Result  | Notes |
|------------------------------------------|---------------|---------|-------|
| Basic focus traversal on Home            | Pixel 9a      | Pending |       |
| Basic focus traversal on Detail          | Pixel 9a      | Pending |       |
| Basic focus traversal on Settings        | Pixel 9a      | Pending |       |
| Basic focus traversal on expanded layout | Medium Tablet | Pending |       |

## Findings log

| ID       | Severity | Screen / flow          | Finding                                                                                                                                                                        | Evidence                                                  | Decision                                | Status |
|----------|----------|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|-----------------------------------------|--------|
| A11Y-001 | P1       | Expanded tablet layout | After selecting a tip, TalkBack linear navigation cannot move directly into the detail pane; user must traverse the full feed first.                                           | Manual TalkBack pass on Medium Tablet                     | Fix before D5-04 Done                   | Open   |
| A11Y-002 | P1       | Home / Detail images   | Runtime images are not visually displayed. Home announces `imageKey`; Detail announces image description text, but no actual image is visible.                                 | Manual TalkBack pass + visual inspection                  | Fix before D5-04 Done                   | Open   |
| A11Y-003 | P1       | Journey progress       | At maximum font size and maximum display size, the current day value is clipped/not visible and percentage layout breaks vertically.                                           | Large text pass on Pixel 9a and Medium Tablet screenshots | Fix before D5-04 Done                   | Open   |
| A11Y-004 | P2       | Home TipCard           | Some TipCard titles and preview text truncate at maximum font/display size. Section chips and primary actions remain usable; Detail screen remains available for full reading. | Large text pass screenshots                               | Document; fix if cheap during same pass | Open   |

## Issue severity guide

### P0

Use P0 for issues that block basic accessibility use.

Examples:

- User cannot navigate back.
- User cannot open Detail.
- User cannot access Settings.
- User cannot cancel/reset progress dialog.
- Destructive action is unlabeled.
- Critical button has no meaningful label.
- TalkBack gets trapped.

### P1

Use P1 for important issues in common flows.

Examples:

- Bookmark/complete actions are ambiguous.
- Current selected state is not understandable.
- Image announcements are misleading.
- Scanner reports a meaningful touch-target or label issue in a primary flow.
- Large text causes a primary action to become unreachable.

### P2

Use P2 for non-blocking improvements.

Examples:

- Slightly redundant announcement.
- Minor copy improvement.
- Non-critical scanner warning.
- Low-risk traversal awkwardness.

## Final decision

Select one:

- [ ] Pass — no unresolved P0/P1 accessibility sanity issues remain.
- [ ] Conditional pass — only documented P2 issues remain.
- [x] Fail — unresolved P0/P1 issues remain.

## Final summary

```text
Add final reviewer summary here.
```

## Done criteria

D5-04 is done when:

- [ ] Accessibility sanity report exists.
- [ ] Compact TalkBack pass completed on Pixel 9a.
- [ ] Expanded TalkBack pass completed on Medium Tablet.
- [ ] Accessibility Scanner pass completed.
- [ ] Large text sanity pass completed.
- [ ] Runtime image accessibility expectations reviewed.
- [ ] No unresolved P0/P1 accessibility issues remain.
- [ ] Any P2 issues are documented.
- [ ] `testDebugUnitTest` passes.
- [ ] `assembleDebug` passes.
- [ ] Compact instrumentation bucket passes on Pixel 9a.
- [ ] Expanded instrumentation bucket passes on Medium Tablet.

## Tracker update

When this pass is complete:

```text
D5-04  Run accessibility sanity pass  Product completeness  Release hardening  P1  S  QA engineer  Accessibility reviewer  D1-11,D2-05,D4-05  Done
```
