# Store Listing Draft - 30 Days of Calm Execution

Status: Draft store metadata / not a live Play listing
Baseline: `v1.0.0-rc1`
Product: 30 Days of Calm Execution
Production-store claim: Not made
Related docs:

* `docs/release/public_store_hardening_checklist.md`
* `docs/release/privacy_policy_draft.md`
* `docs/release/data_safety_assessment.md`
* `docs/release/release_signing_policy.md`

## Purpose

This document drafts consumer-facing store listing metadata for `30 Days of Calm Execution`.

It is written for the local-first release-candidate baseline prepared for public-store hardening.

This document does not claim that:

```text
The app is live on Google Play.
The app has a published Google Play listing.
The app has completed Play review.
The app is approved for production distribution.
The app has final store assets.
```

## App identity

| Field                    | Draft value                    | Status   |
| ------------------------ | ------------------------------ | -------- |
| App name                 | 30 Days of Calm Execution      | Draft    |
| Package / application ID | com.pathstoftech.calmexecution | Present  |
| App type                 | Native Android app             | Present  |
| Primary platform         | Android phone and tablet       | Draft    |
| Current release baseline | v1.0.0-rc1                     | Present  |
| Listing status           | Draft only                     | Not live |

Name length check:

```text
"30 Days of Calm Execution" is intended to fit the Google Play title limit, but this must be rechecked in Play Console before submission.
```

## Short description

Draft short description:

```text
Build calmer work habits with a structured 30-day focus journey.
```

Intent:

* explain the core user benefit quickly;
* avoid inflated productivity claims;
* avoid medical, therapeutic, or guaranteed outcome claims;
* avoid claiming public-store availability.

## Full description

Draft full description:

```text
30 Days of Calm Execution helps you build calmer work habits through a structured 30-day journey.

Each day gives you one practical tip for starting work with clarity, protecting attention, reducing reactive habits, sustaining energy, and finishing meaningful work without burnout-style productivity noise.

The app is designed for local-first use. You can browse the full journey, open detailed guidance for each tip, bookmark useful ideas, track completion from the detail screen, and adjust simple appearance and motion preferences.

Core features:

- 30 bundled work-habit tips
- Structured sections for a calm execution journey
- Daily detail screens with problem, tip, why it helps, and try-today guidance
- Bookmarks for useful tips
- Local progress and completion state
- Compact phone layout and expanded tablet layout
- Light, dark, dynamic color, and reduced motion preferences
- Local-first persistence with no account requirement

The app focuses on practical execution, not pressure. It is meant to help you reduce friction, make small decisions easier, and return to focused work with less noise.

Current privacy posture:

- no account system
- no sign-in
- no backend sync
- no analytics SDK
- no telemetry SDK
- no runtime crash-reporting SDK
- journey progress and preferences stored locally on device

Before public distribution, the final listing must be checked against the final privacy policy, Data safety assessment, release signing policy, screenshots, support contact, and Play Console requirements.
```

Content boundaries:

```text
Do not add claims about therapy, health treatment, guaranteed productivity results, workplace performance guarantees, or Google Play publication until evidence exists.
```

## Category and tags

Draft category:

```text
Productivity
```

Alternative categories to evaluate:

```text
Lifestyle
Education
```

Draft tag / keyword themes:

```text
focus
productivity
work habits
attention
planning
calm work
daily practice
routine
self-management
task completion
```

Notes:

* Final category and tags must be selected inside Play Console.
* Tags must match actual app behavior.
* Do not use misleading wellness, medical, or mental-health positioning.

## Target audience notes

Draft audience:

```text
General productivity users who want a structured, low-pressure way to improve daily work habits.
```

Likely user groups:

* people who want calmer focus routines;
* people who prefer structured daily prompts;
* people who want local progress tracking without accounts;
* tablet users who benefit from list-detail reading layouts.

Current target-audience boundary:

```text
The app is not drafted as a children-directed app.
The app is not drafted as a medical, clinical, therapeutic, or mental-health treatment app.
The app is not drafted as an enterprise team-management product.
```

Required before submission:

* complete Play Console target audience section;
* complete content rating questionnaire;
* verify whether any family/children policy implications apply;
* verify store description remains suitable for a general audience.

## Content rating notes

Draft expected content profile:

```text
No violence.
No sexual content.
No gambling.
No alcohol, tobacco, or drug content.
No user-generated content.
No social interaction feature.
No commerce or purchases.
No location-based content.
```

Required before submission:

```text
Complete the official Play Console content rating questionnaire using final app behavior.
```

## Privacy and Data safety status

Current privacy status:

```text
Privacy policy: draft exists
Data safety assessment: draft exists
Final privacy policy URL: pending
Final Data safety Play Console form: pending
Support / privacy contact: pending
```

Current app behavior summary for listing alignment:

```text
Account system: not implemented
Backend service: not implemented
Remote sync: not implemented
Analytics SDK: not integrated
Telemetry SDK: not integrated
Crash-reporting SDK: not integrated
Advertising SDK: not integrated
Primary mutable data location: local device storage
```

Listing copy must remain consistent with:

* `docs/release/privacy_policy_draft.md`;
* `docs/release/data_safety_assessment.md`;
* final app manifest;
* final dependency graph;
* final release artifact.

## Ads and monetization status

Draft status:

```text
Ads: not implemented
In-app purchases: not implemented
Paid download: not decided
Subscriptions: not implemented
```

Required before submission:

* confirm Play Console ads declaration;
* confirm pricing model;
* update privacy and Data safety docs if monetization SDKs are added.

## Screenshot inventory

Repository screenshots currently available:

| Screenshot      | Current repository path                | Listing role                            | Status                |
| --------------- | -------------------------------------- | --------------------------------------- | --------------------- |
| Home            | `docs/screenshots/home.png`            | Phone / main journey overview candidate | Present as repo asset |
| Detail          | `docs/screenshots/detail.png`          | Phone / tip detail candidate            | Present as repo asset |
| Settings        | `docs/screenshots/settings.png`        | Phone / preferences candidate           | Present as repo asset |
| Expanded tablet | `docs/screenshots/expanded_tablet.png` | Tablet / expanded layout candidate      | Present as repo asset |

Draft store screenshot plan:

| Slot     | Proposed content                        | Status               |
| -------- | --------------------------------------- | -------------------- |
| Phone 1  | Home journey overview                   | Pending final export |
| Phone 2  | Tip detail guidance                     | Pending final export |
| Phone 3  | Bookmark and completion state           | Pending final export |
| Phone 4  | Settings and appearance preferences     | Pending final export |
| Tablet 1 | Expanded list-detail layout             | Pending final export |
| Tablet 2 | Selected detail pane in expanded layout | Optional / pending   |

Screenshot requirements before public distribution:

* regenerate final screenshots from release candidate or final release build;
* verify no private local paths or test device names are visible;
* verify screenshots match final app behavior;
* verify metadata policy remains clean;
* verify phone and tablet dimensions against Play Console requirements at submission time;
* avoid adding marketing overlays that claim unavailable features.

## Feature graphic and video

Draft status:

| Asset           | Status                                 | Notes                                                      |
| --------------- | -------------------------------------- | ---------------------------------------------------------- |
| App icon        | Present in project assets              | Review final appearance before listing                     |
| Feature graphic | Not finalized                          | Required status must be checked before submission          |
| Promo video     | Not planned for current hardening pass | Optional unless later required by listing strategy         |
| App walkthrough | Optional                               | Repository/demo evidence only, not required for this draft |

Decision:

```text
Do not block PSR-016 on feature graphic or video creation. Track them as listing-asset hardening gaps.
```

## Support and contact status

Current support status:

```text
Support contact: TODO
Privacy contact: TODO
Developer name: TODO
Developer website: TODO or not applicable
Privacy policy URL: TODO
```

Required before public distribution:

* define support email or contact route;
* define privacy contact;
* define final developer display name;
* provide final privacy policy URL or approved hosting location;
* align contact details across Play Console, privacy policy, and repository docs.

## Localization status

Current listing language:

```text
English only
```

Future optional localization:

```text
Russian
Other languages only after final English listing is stable
```

Current decision:

```text
No localization claim is made for the current draft.
```

## Accessibility and inclusivity notes

Listing-safe accessibility statements:

```text
The app supports light and dark appearance.
The app supports a reduced motion preference.
The app includes image content descriptions in the bundled catalog.
The app has local accessibility review evidence in release documentation.
```

Avoid overclaiming:

```text
Do not claim full accessibility certification.
Do not claim complete device-matrix accessibility coverage.
Do not claim universal accessibility support.
```

## Store listing risk checklist

Before public submission, review the listing for:

* no live Play listing claim unless true;
* no production-readiness claim unless true;
* no therapeutic or medical claims;
* no guaranteed productivity result claims;
* no false account/cloud/sync claims;
* no analytics/crash-reporting claims inconsistent with dependencies;
* no screenshot mismatch with final app behavior;
* no unsupported tablet or form-factor claim;
* no privacy statement mismatch;
* no Data safety mismatch;
* no misleading category or tags.

## Draft metadata summary

```text
App name: 30 Days of Calm Execution
Short description: Build calmer work habits with a structured 30-day focus journey.
Category: Productivity
Primary audience: General productivity users
Ads: Not implemented
Accounts: Not implemented
Backend: Not implemented
Remote sync: Not implemented
Privacy policy: Draft only
Data safety: Draft only
Support contact: Pending
Screenshots: Repository assets present; final store inventory pending
Live Play listing: Not claimed
Production distribution: Not claimed
```

## Verification commands

Coverage check:

```powershell
Select-String -Path docs/release/store_listing_draft.md -Pattern `
  "App name",
  "Short description",
  "Full description",
  "Category",
  "keyword",
  "Screenshot inventory",
  "Support",
  "Privacy",
  "Data safety",
  "Target audience",
  "Content rating",
  "not a live Play listing"
```

Expected: matches for all required areas.

Forbidden current-claim check:

```powershell
Select-String -Path docs/release/store_listing_draft.md -Pattern `
  "live on Google Play",
  "published Google Play listing",
  "completed Play review",
  "approved for production distribution"
```

Expected: matches only inside explicit "does not claim" or boundary sections.

ASCII check:

```powershell
$file = "docs/release/store_listing_draft.md"
$lines = Get-Content -LiteralPath $file

for ($i = 0; $i -lt $lines.Count; $i++) {
  if ($lines[$i] -cmatch '[^\x00-\x7F]') {
    "${file}:$($i + 1): $($lines[$i])"
  }
}
```

Expected: no output.

## Current decision

This draft provides consumer-facing store metadata for the public-store hardening path.

It does not establish a live Google Play listing, a production distribution claim, final legal privacy text, final Play Console Data safety submission, or final store asset approval.
