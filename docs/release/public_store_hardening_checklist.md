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

| Item                     | Current evidence                                            | Required before public-store claim                                           | Status  | Follow-up                                       |
|--------------------------|-------------------------------------------------------------|------------------------------------------------------------------------------|---------|-------------------------------------------------|
| Compile SDK              | README / release notes document `compileSdk: 36.1`          | Recheck before store submission                                              | Present | Confirm against current Gradle config           |
| Target SDK               | README / release notes document `targetSdk: 36`             | Recheck against current Google Play target API requirement before submission | Present | Confirm with `app/build.gradle.kts`             |
| Minimum SDK              | README / release notes document `minSdk: 24`                | Confirm supported device scope                                               | Present | Confirm with `app/build.gradle.kts`             |
| Permissions              | Manifest scan must show no unexpected sensitive permissions | Required                                                                     | Pending | Run permission scan                             |
| Backup / data extraction | XML rules exist but policy needs explicit review            | Required                                                                     | Pending | Decide include/exclude behavior for local state |

Decision:

```text
Target SDK evidence is directionally compatible with public-store hardening, but submission-time policy verification is still required.
```

---

## Release artifact path

| Item              | Current evidence                               | Required before public-store claim                       | Status  | Follow-up                                                       |
|-------------------|------------------------------------------------|----------------------------------------------------------|---------|-----------------------------------------------------------------|
| Debug build       | Unit/debug baseline is documented              | Keep as implementation baseline                          | Present | Run `testDebugUnitTest assembleDebug` during final verification |
| Release APK       | rc1 docs record release assembly evidence      | Fresh verification required after hardening docs update  | Pending | Run `./gradlew assembleRelease --stacktrace`                    |
| Release AAB       | No current evidence recorded                   | Needed for stronger Play-distribution evidence           | Pending | Run `./gradlew bundleRelease --stacktrace` after release policy |
| Artifact checksum | Rollback/hotfix plan defines SHA-256 recording | Required for release candidates                          | Present | Record for verified artifact                                    |
| Release CI gate   | Not configured as CI release gate              | Required before stronger readiness claim                 | Pending | Add CI release build gate later                                 |
| Minification      | Release minification disabled                  | Must be accepted or changed before production Play claim | Pending | Define R8/minification policy                                   |

Current classification:

```text
Debug baseline: present
Release APK path: historically evidenced, fresh verification pending
AAB path: pending
Release CI gate: pending
```

---

## Release signing

| Item                          | Current evidence                            | Required before public-store claim | Status       | Follow-up                            |
|-------------------------------|---------------------------------------------|------------------------------------|--------------|--------------------------------------|
| Debug signing distinction     | Debug builds are not distribution artifacts | Must be documented                 | Pending      | Create `release_signing_policy.md`   |
| Release signing config        | Not finalized as public distribution policy | Required                           | Pending      | Define local and Play signing policy |
| Keystore handling             | No keystore should be committed             | Required                           | Pending      | Add explicit policy and secret scan  |
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
| Privacy policy draft          | Not yet present         | Required                                     | Pending | Create `privacy_policy_draft.md`                  |
| User-facing privacy statement | Not yet present         | Required before public distribution          | Pending | Decide README, app, or hosted policy path         |
| Data collection summary       | App appears local-first | Must be documented                           | Pending | Complete privacy/data assessment                  |
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
| Data safety assessment doc | Not yet present                           | Required                           | Pending              | Create `data_safety_assessment.md`         |
| Personal data collection   | Likely none, but must verify              | Required                           | Pending              | Manifest, dependency, source review        |
| Data sharing               | Likely none, but must verify              | Required                           | Pending              | Confirm no backend/network SDK             |
| Analytics SDK              | No analytics SDK documented               | Required                           | Pending verification | Dependency scan                            |
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
| Short description       | Not finalized             | Required                           | Pending                     | Create `store_listing_draft.md` |
| Full description        | Not finalized             | Required                           | Pending                     | Create `store_listing_draft.md` |
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
| Internal testing track      | Not assessed     | Required before Play testing path           | Not assessed | Create `play_distribution_plan.md`    |
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
| Release build in CI           | Not configured                    | Required before stronger readiness claim | Pending | Add CI release gate       |
| Connected tests in CI         | Not configured                    | Future device-matrix hardening           | Pending | Decide CI/device strategy |
| Lint gate                     | Not configured as CI release gate | Future hardening                         | Pending | Decide lint baseline      |
| Local connected test evidence | Present, emulator-scoped          | Keep but do not overclaim                | Present |                           |
| Medium Tablet evidence        | Present, local emulator           | Keep but do not overclaim                | Present |                           |

Decision:

```text
CI-backed verification currently means unit tests plus debug assembly. Connected and adaptive evidence remains local emulator evidence.
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
| `docs/release/public_store_hardening_checklist.md` | Master readiness checklist          | Present after PSR-012 |
| `docs/release/privacy_policy_draft.md`             | Draft user-facing privacy basis     | Pending               |
| `docs/release/data_safety_assessment.md`           | Play Data safety evidence           | Pending               |
| `docs/release/release_signing_policy.md`           | Release signing and artifact policy | Pending               |
| `docs/release/store_listing_draft.md`              | Consumer-facing listing copy        | Pending               |
| `docs/release/play_distribution_plan.md`           | Internal / closed / production path | Pending               |
| `docs/release/public_store_hardening_review.md`    | Optional final readiness summary    | Optional later        |

---

## Current hardening gap summary

Required before any Google Play production-readiness claim:

* release signing policy;
* release artifact policy;
* fresh release APK verification;
* AAB / bundle verification if Play distribution is pursued;
* privacy policy documentation;
* Data safety assessment;
* explicit backup/data extraction decision;
* store listing metadata;
* store screenshot inventory;
* support contact;
* Play distribution plan;
* distribution-track evidence;
* release-build CI gate;
* telemetry / crash-reporting decision;
* final public-store hardening review.

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
