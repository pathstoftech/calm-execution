# Play Distribution Plan - 30 Days of Calm Execution

Status: Draft distribution-track plan / not a Play Console submission
Baseline: `v1.0.0-rc1`
Product: 30 Days of Calm Execution
Production-store claim: Not made
Play listing status: Not live
Production access status: Not assessed

Related docs:

* `docs/release/public_store_hardening_checklist.md`
* `docs/release/privacy_policy_draft.md`
* `docs/release/data_safety_assessment.md`
* `docs/release/release_signing_policy.md`
* `docs/release/store_listing_draft.md`

## Purpose

This document defines the planned Google Play distribution path for `30 Days of Calm Execution`.

It is written for the local-first release-candidate baseline prepared for public-store hardening.

This document does not claim that:

```text
The app is published on Google Play.
The app has a live Play listing.
The app has passed Play review.
The app has production access.
The app has completed closed testing.
The app is approved for production rollout.
```

## Current distribution summary

| Area                                  | Current state                              | Status       |
| ------------------------------------- | ------------------------------------------ | ------------ |
| Google Play developer account type    | Unknown                                    | Not assessed |
| Play Console app created              | Unknown                                    | Not assessed |
| Package name reserved in Play Console | Unknown                                    | Not assessed |
| Internal testing track                | Not configured in repo evidence            | Not assessed |
| Closed testing track                  | Not configured in repo evidence            | Not assessed |
| Open testing track                    | Not configured in repo evidence            | Not assessed |
| Production access                     | No evidence in repo                        | Not assessed |
| Production rollout                    | Not claimed                                | Not claimed  |
| Release AAB                           | Pending verification / signing policy      | Pending      |
| Release signing                       | Draft policy exists; final process pending | Pending      |
| Store listing                         | Draft exists                               | Draft        |
| Privacy policy                        | Draft exists                               | Draft        |
| Data safety                           | Draft assessment exists                    | Draft        |
| Support contact                       | Pending                                    | Pending      |
| Distribution-track evidence           | Not present                                | Pending      |

Current decision:

```text
No Google Play distribution status is claimed.
```

## Account status assessment

Before any Play distribution claim, record:

| Question                                                         | Current answer | Required evidence                                   |
| ---------------------------------------------------------------- | -------------- | --------------------------------------------------- |
| Is the developer account personal or organization?               | Not assessed   | Play Console account type                           |
| Was a personal account created after November 13, 2023?          | Not assessed   | Play Console account creation/account type evidence |
| Does the account already have production access?                 | Not assessed   | Play Console production access status               |
| Is device verification required or completed?                    | Not assessed   | Play Console account status                         |
| Is the app already created in Play Console?                      | Not assessed   | Play Console app dashboard                          |
| Is `com.pathstoftech.calmexecution` reserved as the app package? | Not assessed   | Play Console package/app record                     |

Decision:

```text
Account-specific requirements must be checked in Play Console. Do not assume production access.
```

## Track strategy

Recommended staged path:

```text
1. Internal testing
2. Closed testing
3. Production access request, if required
4. Optional open testing, if useful and allowed
5. Production release candidate
6. Production rollout
```

This sequence is a plan, not current evidence.

## Internal testing plan

Purpose:

```text
Use internal testing for early Play-delivered installation checks with a small trusted tester group.
```

Internal testing should verify:

* install from Play test track;
* launch behavior;
* Home screen loads catalog content;
* Detail screen opens and marks viewed state;
* bookmark state persists;
* completion state mutates only from Detail or selected expanded Detail;
* Settings changes persist;
* reset progress behavior;
* tablet/expanded layout if test devices are available;
* no unexpected privacy, permission, or network behavior;
* crash-free smoke pass based on manual observation.

Required before internal testing:

| Item                                      | Status       |
| ----------------------------------------- | ------------ |
| Play Console app created                  | Not assessed |
| Valid release artifact                    | Pending      |
| Release signing process                   | Pending      |
| Internal tester list                      | Not assessed |
| Tester feedback channel                   | Pending      |
| Store draft / temporary listing readiness | Pending      |
| Privacy/Data safety draft consistency     | Draft        |
| Known issues reviewed                     | Present      |

Internal testing evidence to record:

| Evidence                         | Required? | Status  |
| -------------------------------- | --------: | ------- |
| Track name                       |       Yes | Pending |
| Artifact versionCode/versionName |       Yes | Pending |
| Artifact type, preferably AAB    |       Yes | Pending |
| Upload date                      |       Yes | Pending |
| Tester count                     |       Yes | Pending |
| Tester list owner                |       Yes | Pending |
| Feedback channel                 |       Yes | Pending |
| Smoke-test result                |       Yes | Pending |
| Issues found                     |       Yes | Pending |
| Decision to proceed/hold         |       Yes | Pending |

## Closed testing plan

Purpose:

```text
Use closed testing for controlled pre-release validation with a wider tester group before any production-readiness claim.
```

Closed testing should verify:

* app installs from closed track;
* target users understand the app purpose;
* local-first privacy posture is clear;
* core journey flow works over repeated sessions;
* state persists across app restarts;
* reset progress behavior is understood;
* accessibility issues do not block core use;
* tablet layout is usable if tablet support is claimed;
* release notes and known issues are understandable;
* tester feedback is captured and triaged.

Closed testing requirements to assess:

| Requirement                          | Current status | Required evidence                   |
| ------------------------------------ | -------------- | ----------------------------------- |
| Closed testing track configured      | Not assessed   | Play Console track                  |
| Tester group/list created            | Not assessed   | Play Console tester list            |
| Tester opt-in link available         | Not assessed   | Track opt-in URL                    |
| Minimum tester count                 | Not assessed   | Account-specific Play requirement   |
| 14-day continuous opt-in requirement | Not assessed   | Account-specific Play requirement   |
| Tester engagement evidence           | Not assessed   | Feedback/issues/test notes          |
| Closed test summary                  | Pending        | Production access application input |
| App changes based on testing         | Pending        | Changelog / issues / commits        |

Personal-account caution:

```text
If the developer account is a newly created personal account subject to Google Play testing requirements, production access may require at least 12 opted-in closed testers for 14 continuous days. This must be verified in Play Console for the actual account before planning production access.
```

## Open testing plan

Current status:

```text
Open testing is not planned for the current hardening pass.
```

Use open testing only if:

* production access permits it;
* store listing text is ready to be visible;
* privacy policy and Data safety content are final enough for public test visibility;
* support contact is ready;
* tester feedback volume can be managed;
* screenshots and app behavior are aligned.

Open testing risk:

```text
Open testing can make the test version visible to broader Google Play users. Do not use it as a substitute for closed testing readiness.
```

## Production access plan

Current production access status:

```text
Not assessed.
```

Before applying for production access, record:

| Evidence                                       | Status  |
| ---------------------------------------------- | ------- |
| Account type checked                           | Pending |
| Production access status checked               | Pending |
| Closed testing requirement checked             | Pending |
| Closed test completed, if required             | Pending |
| Tester count evidence                          | Pending |
| 14-day continuous opt-in evidence, if required | Pending |
| Feedback summary                               | Pending |
| Changes made based on feedback                 | Pending |
| Production-readiness rationale                 | Pending |
| Known issues reviewed                          | Pending |
| Privacy/Data safety reviewed                   | Pending |
| Release signing reviewed                       | Pending |

Production access application must not be started until the closed testing evidence and final readiness rationale are available.

## Production rollout plan

Current status:

```text
No production rollout is planned or claimed.
```

Before any production rollout:

* release signing policy must be finalized;
* AAB build must be verified;
* artifact checksum must be recorded;
* privacy policy must be final or approved for submission;
* Data safety form must be final;
* store listing must be final;
* support contact must be final;
* target audience and content rating must be completed;
* country/region availability must be decided;
* staged rollout strategy must be decided;
* rollback/hotfix path must be reviewed;
* monitoring/crash-reporting decision must be reviewed;
* release notes must be final;
* known issues must be accepted or fixed.

Production rollout evidence to record:

| Evidence                         |      Required? | Status  |
| -------------------------------- | -------------: | ------- |
| Track                            |            Yes | Pending |
| Artifact versionCode/versionName |            Yes | Pending |
| Artifact checksum                |            Yes | Pending |
| Signing status                   |            Yes | Pending |
| Review status                    |            Yes | Pending |
| Release status                   |            Yes | Pending |
| Rollout percentage               | Yes, if staged | Pending |
| Country/region availability      |            Yes | Pending |
| Release notes                    |            Yes | Pending |
| Rollback owner                   |            Yes | Pending |
| Monitoring owner                 |            Yes | Pending |

## Release artifact path

Current artifact posture:

| Artifact         | Current role                                  | Status                     |
| ---------------- | --------------------------------------------- | -------------------------- |
| Debug APK        | Local verification only                       | Present                    |
| Release APK      | Release build path evidence                   | Pending fresh verification |
| Release AAB      | Preferred Play distribution artifact evidence | Pending                    |
| Signed artifact  | Required before distribution                  | Pending                    |
| SHA-256 checksum | Required for reviewed artifacts               | Pending                    |

Required commands before track upload:

```powershell
git status --short
./gradlew clean testDebugUnitTest assembleDebug --stacktrace
./gradlew assembleRelease --stacktrace
./gradlew bundleRelease --stacktrace
```

Artifact evidence to record:

```text
Artifact path:
Artifact type:
Version name:
Version code:
Source commit:
Source tag:
Build command:
Signing status:
SHA-256:
Built by:
Build date:
```

## Store readiness dependencies

Distribution cannot proceed until these documents are completed or explicitly accepted:

| Dependency                       | Current status          | Required before production claim |
| -------------------------------- | ----------------------- | -------------------------------- |
| Public-store hardening checklist | Present                 | Keep updated                     |
| Privacy policy                   | Draft                   | Finalize                         |
| Data safety assessment           | Draft                   | Finalize                         |
| Release signing policy           | Draft                   | Finalize                         |
| Store listing draft              | Draft                   | Finalize                         |
| Monitoring plan                  | Manual baseline present | Decide crash/telemetry posture   |
| Rollback/hotfix path             | Present                 | Recheck                          |
| Known issues                     | Present                 | Recheck                          |
| Support contact                  | Pending                 | Finalize                         |

## Tester feedback plan

Feedback channel status:

```text
Pending.
```

Required tester feedback fields:

```text
Tester ID or alias:
Device model:
Android version:
App version:
Track:
Install source:
Date:
Scenario tested:
Result:
Issue description:
Screenshot/screen recording attached:
Logcat attached:
Severity suggestion:
Privacy-sensitive content redacted:
```

Feedback classification:

| Severity | Meaning                                                         |
| -------- | --------------------------------------------------------------- |
| Blocker  | Prevents launch, install, core navigation, or safe use          |
| Critical | Major app behavior failure with no reasonable workaround        |
| High     | Major flow affected, but release decision possible after review |
| Medium   | Important issue, not release-blocking by default                |
| Low      | Minor polish or documentation issue                             |

Closed test summary must include:

* tester count;
* test duration;
* device spread;
* scenarios covered;
* defects found;
* defects fixed;
* defects accepted;
* feedback themes;
* app changes made;
* rationale for proceeding or holding.

## Distribution evidence checklist

Before any public-store production-readiness claim, gather:

| Evidence                             | Status                |
| ------------------------------------ | --------------------- |
| Play Console account type            | Pending               |
| Production access status             | Pending               |
| App dashboard exists                 | Pending               |
| Package name fixed in Play Console   | Pending               |
| Release artifact uploaded            | Pending               |
| Internal testing result              | Pending               |
| Closed testing result                | Pending               |
| Tester count / duration evidence     | Pending if applicable |
| Feedback summary                     | Pending               |
| Production access application result | Pending if applicable |
| Store listing final review           | Pending               |
| Privacy policy final review          | Pending               |
| Data safety final review             | Pending               |
| Content rating result                | Pending               |
| Target audience result               | Pending               |
| Support contact final value          | Pending               |
| Release signing final status         | Pending               |
| Rollout decision                     | Pending               |
| Rollback/hotfix owner                | Pending               |
| Monitoring owner                     | Pending               |

## Not assessed items

The following remain explicitly unknown:

* Play developer account type;
* Play developer account creation date;
* production access status;
* device verification status;
* Play Console app creation status;
* package reservation status;
* internal testing configuration;
* closed testing configuration;
* tester list;
* tester feedback channel;
* artifact upload status;
* review status;
* country/region availability;
* content rating status;
* target audience status;
* final support contact;
* final privacy policy URL.

These must remain `Not assessed` until verified in Play Console or final project documentation.

## Current decision

The project may claim:

```text
A Play distribution plan exists.
Distribution-track evidence is pending.
Internal, closed, and production paths are documented as future hardening work.
Unknown account-specific requirements are marked Not assessed.
```

The project must not claim:

```text
The app is published on Google Play.
The app has production access.
The app completed closed testing.
The app has a live Play listing.
The app has passed Play review.
The app is approved for production rollout.
```

This distribution plan supports the repository claim that the app is prepared for public-store hardening. It does not support a production-store release claim.

## Verification commands

Coverage check:

```powershell
Select-String -Path docs/release/play_distribution_plan.md -Pattern `
  "account",
  "internal testing",
  "closed testing",
  "production access",
  "release-track evidence",
  "Not assessed",
  "tester",
  "feedback",
  "artifact",
  "rollout",
  "distribution"
```

Expected: matches for all required areas.

Forbidden current-claim check:

```powershell
Select-String -Path docs/release/play_distribution_plan.md -Pattern `
  "published on Google Play",
  "production access",
  "completed closed testing",
  "live Play listing",
  "passed Play review",
  "approved for production rollout"
```

Expected: matches only in explicit boundary, pending, not assessed, or must-not-claim sections.

ASCII check:

```powershell
$file = "docs/release/play_distribution_plan.md"
$lines = Get-Content -LiteralPath $file

for ($i = 0; $i -lt $lines.Count; $i++) {
  if ($lines[$i] -cmatch '[^\x00-\x7F]') {
    "${file}:$($i + 1): $($lines[$i])"
  }
}
```

Expected: no output.
