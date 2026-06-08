# Public-Store Hardening Checklist - 30 Days of Calm Execution

Status: Draft hardening control document
Baseline: `v1.0.0-rc1`
Product: 30 Days of Calm Execution
Current claim: Local-first Android product candidate prepared for public-store hardening
Production-store claim: Not made

## Purpose

This checklist tracks the remaining work required before the project can make any public-store production-readiness claim.

The current repository may claim:

```text
Kotlin-first Android app prepared for public-store hardening.
Local-first release-candidate baseline.
Public-store hardening candidate.
```

The current repository must not claim:

```text
Published on Google Play.
Available on Google Play.
Store-ready.
Production-ready consumer app.
Finished public-store app.
```

until the required evidence exists.

---

## Current baseline

| Area                       | Current state                                              | Status         |
|----------------------------|------------------------------------------------------------|----------------|
| App implementation         | Local-first app implementation exists                      | Present        |
| Release-candidate baseline | `v1.0.0-rc1` release documentation exists                  | Present        |
| Package identity           | `com.pathstoftech.calmexecution`                           | Present        |
| Local persistence          | Proto DataStore for journey state and preferences          | Present        |
| Catalog source             | Bundled JSON catalog with validation                       | Present        |
| Runtime telemetry          | No analytics, telemetry, or crash-reporting SDK integrated | Not integrated |
| Public-store distribution  | No Google Play publication claim                           | Not claimed    |
| Store-readiness status     | Prepared for hardening, not a production-store release     | In progress    |

---

## Checklist status legend

| Status             | Meaning                                                              |
|--------------------|----------------------------------------------------------------------|
| Present            | Evidence exists in repo or release docs                              |
| Verified           | Evidence exists and has been freshly checked for this hardening pass |
| Draft              | Document exists but is not final policy                              |
| Pending            | Required before stronger store-readiness claim                       |
| Not assessed       | Information is unknown and must be checked                           |
| Not applicable now | Not required for the current hardening candidate claim               |

---

## Target SDK and platform policy

| Item                     | Current evidence                                                                            | Required before public-store claim                                           | Status  | Follow-up                                                                          |
|--------------------------|---------------------------------------------------------------------------------------------|------------------------------------------------------------------------------|---------|------------------------------------------------------------------------------------|
| Compile SDK              | README / release notes document `compileSdk: 36.1`                                          | Recheck before store submission                                              | Present | Confirm against current Gradle config                                              |
| Target SDK               | README / release notes document `targetSdk: 36`                                             | Recheck against current Google Play target API requirement before submission | Present | Confirm with `app/build.gradle.kts`                                                |
| Minimum SDK              | README / release notes document `minSdk: 24`                                                | Confirm supported device scope                                               | Present | Confirm with `app/build.gradle.kts`                                                |
| Permissions              | Manifest scan must show no unexpected sensitive permissions                                 | Required                                                                     | Pending | Run permission scan                                                                |
| Backup / data extraction | Manifest/XML configuration exists but final DataStore include/exclude policy is not decided | Required                                                                     | Pending | Decide cloud-backup and device-transfer behavior for journey state and preferences |

Decision:

```text
Target SDK evidence is directionally compatible with public-store hardening, but submission-time policy verification is still required.
```

---

## Release artifact path

| Item              | Current evidence                                                                                                            | Required before public-store claim                               | Status   | Follow-up                                                       |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|----------|-----------------------------------------------------------------|
| Debug build       | Unit/debug baseline is documented                                                                                           | Keep as implementation baseline                                  | Present  | Run `testDebugUnitTest assembleDebug` during final verification |
| Release APK       | `./gradlew clean testDebugUnitTest assembleDebug assembleRelease --stacktrace` passed locally; `app/build/outputs/apk/release/app-release-unsigned.apk` generated; SHA-256 `BEBE8B969B815A756102B1DF77D1124AFC01830B0C789AC3E1F145E733ADD545` recorded | Keep as release build-path evidence, not Play distribution proof | Verified | Re-run before final distribution claim                          |
| Release AAB       | `./gradlew bundleRelease --stacktrace` passed locally; `app/build/outputs/bundle/release/app-release.aab` generated; SHA-256 `BE638F9FF82E8A252C84C9FE6A4F398B5D542E0A096B306C6A57E96B618A4D13` recorded | Stronger Play-distribution build-path evidence, but still not signing, Play upload, or production-readiness proof | Verified | Re-run after signing policy and before final distribution claim |
| Artifact checksum | Rollback/hotfix plan defines SHA-256 recording                                                                              | Required for release candidates                                  | Present  | Record for verified artifact                                    |
| Release CI gate   | `assembleRelease` configured in GitHub Actions                                                                             | Required before stronger readiness claim                         | Verified | AAB and signing gates remain future hardening work              |
| Minification      | Release minification disabled                                                                                               | Must be accepted or changed before production Play claim         | Pending  | Define R8/minification policy                                   |

Current classification:

```text
Debug baseline: present
Release APK path: verified locally for PSR-021 with unsigned release APK output and SHA-256 recorded
AAB path: verified locally for PSR-022 with release AAB output and SHA-256 recorded
Release CI gate: verified for assembleRelease
```

---

## Release signing

| Item                          | Current evidence                            | Required before public-store claim | Status       | Follow-up                            |
|-------------------------------|---------------------------------------------|------------------------------------|--------------|--------------------------------------|
| Debug signing distinction     | Debug builds are not distribution artifacts | Must be documented                 | Present      | See `release_signing_policy.md`      |
| Release signing config        | Draft policy exists                         | Required                           | Draft        | Define local and Play signing policy |
| Keystore handling             | No keystore should be committed             | Required                           | Present      | Add explicit policy and secret scan  |
| Play App Signing / upload key | Not assessed                                | Required before Play release       | Not assessed | Decide Play signing process          |
| Artifact ownership            | Not finalized                               | Required                           | Pending      | Define owner and storage rules       |

Decision:

```text
Release signing is a public-store hardening gap. No public-store readiness claim is allowed until signing policy is documented.
```

---

## Privacy policy

| Item                          | Current evidence        | Required before public-store claim           | Status  | Follow-up                                         |
|-------------------------------|-------------------------|----------------------------------------------|---------|---------------------------------------------------|
| Privacy policy draft          | Draft document exists   | Required                                     | Draft   | See `privacy_policy_draft.md`                     |
| User-facing privacy statement | Draft exists            | Required before public distribution          | Draft   | See `privacy_policy_draft.md`                     |
| Data collection summary       | App appears local-first | Must be documented                           | Present | Complete privacy/data assessment                  |
| User controls                 | Reset progress exists   | Must document exact reset scope              | Pending | Verify reset clears what data                     |
| Support contact               | Not finalized           | Required for store listing / privacy contact | Pending | Define support contact placeholder or final value |

Current privacy posture:

```text
The app appears local-first and low-data-risk, but privacy documentation is not complete.
```

---

## Data safety assessment

| Item                       | Current evidence                          | Required before public-store claim | Status               | Follow-up                                  |
|----------------------------|-------------------------------------------|------------------------------------|----------------------|--------------------------------------------|
| Data safety assessment doc | Draft document exists                     | Required                           | Draft                | See `data_safety_assessment.md`            |
| Personal data collection   | Likely none, but must verify              | Required                           | Present              | See `data_safety_assessment.md`            |
| Data sharing               | Likely none, but must verify              | Required                           | Present              | See `data_safety_assessment.md`            |
| Analytics SDK              | No analytics SDK documented               | Required                           | Verified             | See `data_safety_assessment.md`            |
| Crash-reporting SDK        | Not integrated by current monitoring plan | Present                            | Keep documented      |                                            |
| Telemetry SDK              | Not integrated by current monitoring plan | Present                            | Keep documented      |                                            |
| Local state inventory      | Journey state and preferences documented  | Required                           | Pending              | List exact stored fields                   |
| Backup/data extraction     | Rules need explicit decision              | Required                           | Pending              | Decide backup policy                       |
| Third-party SDK review     | Framework dependencies known              | Required                           | Pending              | Review dependencies and Google Fonts usage |
| Sensitive permissions      | No sensitive permission expected          | Required                           | Pending              | Run manifest scan                          |

Current classification:

```text
Data safety: not complete.
Current app behavior: local-first with no telemetry SDK integrated.
Required next step: formal assessment document.
```

---

## Store listing

| Item                    | Current evidence          | Required before public-store claim | Status                      | Follow-up                       |
|-------------------------|---------------------------|------------------------------------|-----------------------------|---------------------------------|
| App name                | 30 Days of Calm Execution | Present                            | Confirm final listing title |                                 |
| Short description       | Draft exists              | Required                           | Draft                       | See `store_listing_draft.md`    |
| Full description        | Draft exists              | Required                           | Draft                       | See `store_listing_draft.md`    |
| Category                | Not finalized             | Required                           | Pending                     | Select category                 |
| Tags / keywords         | Not finalized             | Required                           | Pending                     | Draft listing metadata          |
| Support contact         | Not finalized             | Required                           | Pending                     | Define support contact          |
| Privacy policy URL/text | Not finalized             | Required                           | Pending                     | Depends on privacy draft        |
| Content rating inputs   | Not assessed              | Required                           | Not assessed                | Add to listing plan             |
| Target audience inputs  | Not assessed              | Required                           | Not assessed                | Add to listing plan             |

Decision:

```text
Store listing copy is not complete. The README is not a substitute for store listing metadata.
```

---

## Screenshots and media

| Item                       | Current evidence                               | Required before public-store claim | Status                                 | Follow-up                          |
|----------------------------|------------------------------------------------|------------------------------------|----------------------------------------|------------------------------------|
| Repository screenshots     | `docs/screenshots/` exists                     | Present                            | Keep current screenshots               |                                    |
| Screenshot metadata scan   | Known-issues doc records clean metadata chunks | Present                            | Recheck if screenshots are regenerated |                                    |
| Store screenshot inventory | Not finalized                                  | Required                           | Pending                                | Define phone/tablet screenshot set |
| Feature graphic            | Not assessed                                   | Required if Play listing needs it  | Not assessed                           | Decide later                       |
| App walkthrough / demo     | Optional                                       | Optional                           | Not applicable now                     | Keep optional                      |

Decision:

```text
Repository screenshots are present, but store listing screenshot inventory is not finalized.
```

---

## Testing track and distribution path

| Item                        | Current evidence | Required before public-store claim          | Status       | Follow-up                             |
|-----------------------------|------------------|---------------------------------------------|--------------|---------------------------------------|
| Internal testing track      | Draft plan exists | Required before Play testing path           | Draft        | See `play_distribution_plan.md`       |
| Closed testing track        | Not assessed     | May be required depending on account status | Not assessed | Document account-specific requirement |
| Production access           | Not assessed     | Required before production Play claim       | Not assessed | Document Play Console status          |
| Tester evidence             | Not present      | Required if closed testing applies          | Pending      | Document later                        |
| Distribution-track evidence | Not present      | Required before production claim            | Pending      | Track in distribution plan            |

Decision:

```text
No Play distribution status is claimed.
```

---

## Monitoring, crash reporting, and telemetry

| Item                            | Current evidence                  | Required before public-store claim        | Status           | Follow-up |
|---------------------------------|-----------------------------------|-------------------------------------------|------------------|-----------|
| Manual monitoring plan          | Present                           | Keep for rc1 baseline                     | Present          |           |
| Runtime crash-reporting SDK     | Not integrated                    | Decision required before production claim | Pending decision |           |
| Analytics SDK                   | Not integrated                    | Decision required before production claim | Pending decision |           |
| Telemetry SDK                   | Not integrated                    | Decision required before production claim | Pending decision |           |
| Privacy impact of telemetry     | Not applicable until SDK decision | Required if telemetry added               | Pending          |           |
| Data safety impact of telemetry | Not applicable until SDK decision | Required if telemetry added               | Pending          |           |

Current decision:

```text
Manual issue intake remains the monitoring model for the release-candidate baseline.
Crash reporting / telemetry integration is deferred to public-store hardening.
```

---

## CI and verification gates

| Item                          | Current evidence                  | Required before public-store claim       | Status  | Follow-up                 |
|-------------------------------|-----------------------------------|------------------------------------------|---------|---------------------------|
| Unit tests in CI              | Present                           | Keep                                     | Present |                           |
| Debug assembly in CI          | Present                           | Keep                                     | Present |                           |
| Release build in CI           | `assembleRelease` configured in GitHub Actions | Required before stronger readiness claim | Verified | Release build-path only             |
| Connected tests in CI         | Not configured                                 | Future device-matrix hardening           | Pending  | Decide CI/device strategy           |
| Lint gate                     | Not configured as CI release gate              | Future hardening                         | Pending  | Decide lint baseline                |
| Local connected test evidence | Present, emulator-scoped          | Keep but do not overclaim                | Present |                           |
| Medium Tablet evidence        | Present, local emulator           | Keep but do not overclaim                | Present |                           |

Decision:

```text
CI-backed verification currently means unit tests, debug assembly, and release APK assembly. Connected and adaptive evidence remains local emulator evidence. AAB, signing, Play upload, and production-readiness gates remain future hardening work.
```

---

## Support and user-facing operations

| Item                        | Current evidence               | Required before public-store claim | Status  | Follow-up                          |
|-----------------------------|--------------------------------|------------------------------------|---------|------------------------------------|
| Support contact             | Not finalized                  | Required                           | Pending | Define support email/contact route |
| Issue intake model          | Manual issue intake documented | Present                            | Keep    |                                    |
| Rollback/hotfix path        | Present                        | Keep                               | Present |                                    |
| Known issues list           | Present                        | Keep updated                       | Present |                                    |
| Privacy contact             | Not finalized                  | Required                           | Pending | Align with privacy policy draft    |
| In-app privacy/about access | Not present                    | Conditional                        | Pending | Decide after privacy draft         |

---

## Required hardening documents

| Document                                           | Purpose                             | Status                |
|----------------------------------------------------|-------------------------------------|-----------------------|
| `docs/release/public_store_hardening_checklist.md` | Master readiness checklist          | Present |
| `docs/release/privacy_policy_draft.md`             | Draft user-facing privacy basis     | Draft   |
| `docs/release/data_safety_assessment.md`           | Play Data safety evidence           | Draft   |
| `docs/release/release_signing_policy.md`           | Release signing and artifact policy | Draft   |
| `docs/release/store_listing_draft.md`              | Consumer-facing listing copy        | Draft   |
| `docs/release/play_distribution_plan.md`           | Internal / closed / production path | Draft   |
| `docs/release/public_store_hardening_review.md`    | Final readiness summary             | Present |

---

## Current hardening gap summary

Required before any Google Play production-readiness claim:

* final release signing decision;
* Play App Signing / upload key decision;
* release artifact signing process;
* final release artifact retention policy;
* release-build CI hardening (AAB/signing);
* final no-secrets scan before signing changes;
* final privacy policy text and contact route;
* final Data safety form;
* backup/data extraction policy decision for Proto DataStore state;
* Google Fonts / downloadable-font provider privacy review;
* final dependency and SDK privacy review;
* support contact;
* store listing final copy;
* final store screenshot inventory;
* content rating questionnaire;
* target audience questionnaire;
* Play developer account status check;
* internal / closed testing setup, if distribution proceeds;
* production access status check;
* distribution-track evidence;
* monitoring / crash-reporting / telemetry decision;
* final known-issues review before public distribution.

Backup/data extraction remains pending until the project decides whether Proto DataStore journey state and preferences are included in or excluded from Android backup and device-transfer behavior.

---

## Verification commands for this checklist

Run after creating or updating this file:

```powershell
Select-String -Path docs/release/public_store_hardening_checklist.md -Pattern `
  "target SDK",
  "release artifact",
  "release signing",
  "privacy",
  "Data safety",
  "store listing",
  "screenshots",
  "testing track",
  "support",
  "telemetry",
  "CI",
  "distribution"
```

Expected: matches for all required areas.

Check forbidden claims:

```powershell
Select-String -Path docs/release/public_store_hardening_checklist.md -Pattern `
  "published on Google Play",
  "available on Google Play",
  "store-ready",
  "production-ready consumer app",
  "finished public-store app"
```

Expected: matches only inside the explicit "must not claim" section, not as current status.

Check ASCII-only content:

```powershell
$file = "docs/release/public_store_hardening_checklist.md"
$lines = Get-Content -LiteralPath $file

for ($i = 0; $i -lt $lines.Count; $i++) {
  if ($lines[$i] -cmatch '[^\x00-\x7F]') {
    "${file}:$($i + 1): $($lines[$i])"
  }
}
```

Expected: no output.

---

## Decision

The repository is prepared for public-store hardening, not public-store release.

This checklist is the master control document for the hardening path. It records the current release-candidate evidence, the remaining gaps, and the boundary between allowed current claims and future production-store claims.
