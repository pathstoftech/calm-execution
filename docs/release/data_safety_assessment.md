# Data Safety Assessment - 30 Days of Calm Execution

Status: Draft assessment / not final Play Console submission
Baseline: `v1.0.0-rc1`
Product: 30 Days of Calm Execution
Assessment date: 2026-06-08
Production-store claim: Not made
Related draft: `docs/release/privacy_policy_draft.md`

## Purpose

This document records the current Data safety evidence for `30 Days of Calm Execution`.

It is written for the current local-first release-candidate baseline prepared for public-store hardening. It is not a final Google Play Data safety form submission.

Before any public-store production-readiness claim, this assessment must be rechecked against:

* the final source tree;
* the final dependency graph;
* the final Android manifest;
* the final release artifact;
* the final privacy policy;
* any Play Console account-specific requirements.

## Current assessment summary

Current baseline classification:

```text
Account system: not implemented
Backend service: not implemented
Remote sync: not implemented
Network-backed user content: not implemented
Analytics SDK: not integrated
Telemetry SDK: not integrated
Crash-reporting SDK: not integrated
Advertising SDK: not integrated
Payment SDK: not integrated
Runtime sensitive permissions: not observed in current scan
Primary mutable data location: local device storage
Data safety status: draft / pending final review
```

The app appears to be a local-first Android app that stores journey state and preferences on the user's device.

No current evidence indicates that the app intentionally sends journey progress, bookmarks, completion status, preferences, usage activity, or personal information to the developer or a third-party service.

## Data collection and sharing classification

| Question                                          | Current answer                                     | Evidence status            | Follow-up                              |
|---------------------------------------------------|----------------------------------------------------|----------------------------|----------------------------------------|
| Does the app collect personal data from the user? | No intentional personal-data collection identified | Pending final verification | Recheck source, manifest, dependencies |
| Does the app transmit user data off device?       | No intentional transmission identified             | Pending final verification | Confirm no network SDK / backend path  |
| Does the app share user data with third parties?  | No sharing identified                              | Pending final verification | Confirm third-party SDK behavior       |
| Does the app sell user data?                      | No                                                 | Pending final verification | Recheck before public submission       |
| Does the app use analytics?                       | No analytics SDK identified                        | Pending final verification | Recheck dependency graph               |
| Does the app use telemetry?                       | No telemetry SDK identified                        | Pending final verification | Recheck dependency graph               |
| Does the app use runtime crash reporting?         | No crash-reporting SDK identified                  | Pending final verification | Recheck dependency graph               |
| Does the app use ads?                             | No ads SDK identified                              | Pending final verification | Recheck dependency graph               |
| Does the app use accounts or sign-in?             | No account/sign-in implementation identified       | Pending final verification | Recheck source and UI                  |
| Does the app use remote sync?                     | No remote sync implementation identified           | Pending final verification | Recheck dependencies and source        |

## Locally stored app data

The current app baseline stores mutable app state locally.

Known local state includes:

| Local data                  | Purpose                                  | User-facing? | Transmitted off device? | Current classification |
|-----------------------------|------------------------------------------|-------------:|------------------------:|------------------------|
| Viewed tip state            | Track reading/progress state             |          Yes |             No evidence | Local app state        |
| Bookmark state              | Preserve bookmarked tips                 |          Yes |             No evidence | Local app state        |
| Completion status           | Preserve completed tips                  |          Yes |             No evidence | Local app state        |
| Last viewed timestamp       | Preserve journey state metadata          |      Limited |             No evidence | Local app state        |
| Completed timestamp         | Preserve completion metadata             |      Limited |             No evidence | Local app state        |
| Selected journey section    | Preserve user navigation/preference      |          Yes |             No evidence | Local app state        |
| Intro/onboarding seen state | Avoid repeating intro UI                 |          Yes |             No evidence | Local app state        |
| Theme mode preference       | Preserve appearance preference           |          Yes |             No evidence | Local app preference   |
| Dynamic color preference    | Preserve appearance preference           |          Yes |             No evidence | Local app preference   |
| Reduced motion preference   | Preserve accessibility/motion preference |          Yes |             No evidence | Local app preference   |

Current evidence points to local persistence through Proto DataStore-backed journey state and user preferences.

## DataStore state inventory

Journey state evidence:

```text
JourneyStateProto
TipCompletionStatusProto
JourneyStateSerializer
DataStoreJourneyDataSource
JourneyRepository
JourneyStateMapper
TipUserState
isBookmarked
completionStatus
lastViewedAtEpochMillis
completedAtEpochMillis
```

Preference state evidence:

```text
UserPreferencesProto
UserPreferencesSerializer
DataStorePreferencesDataSource
PreferencesRepository
PreferencesMapper
UserPreferences
dynamicColorEnabled
reducedMotionEnabled
```

Current assessment:

```text
Journey state and preferences are stored locally.
No current evidence shows off-device transmission of this state.
```

## Permissions assessment

Current expected permission posture:

```text
No sensitive runtime permissions expected.
```

Sensitive permission classes to recheck before public distribution:

```text
Location
Camera
Microphone
Contacts
SMS
Calendar
Phone
Call logs
Nearby devices
Storage/media access
Health data
Bluetooth
Notification permission behavior
```

Current verification command:

```powershell
Select-String -Path app/src/main/AndroidManifest.xml -Pattern `
  "uses-permission",
  "INTERNET",
  "ACCESS_",
  "CAMERA",
  "LOCATION",
  "READ_",
  "WRITE_",
  "RECORD_AUDIO",
  "CONTACTS",
  "SMS"
```

Current expected result:

```text
No sensitive permission output.
```

If any permission output appears later, this assessment must be updated before public distribution.

## Network, analytics, telemetry, crash, and ads SDK assessment

Current expected dependency posture:

```text
No Firebase Analytics.
No Firebase Crashlytics.
No Sentry.
No Datadog.
No Amplitude.
No Mixpanel.
No Retrofit.
No OkHttp.
No Ktor.
No ads SDK.
```

Current verification command:

```powershell
Select-String -Path gradle/libs.versions.toml,app/build.gradle.kts -Pattern `
  "firebase",
  "crashlytics",
  "analytics",
  "sentry",
  "datadog",
  "amplitude",
  "mixpanel",
  "retrofit",
  "okhttp",
  "ktor",
  "ads"
```

Current expected result:

```text
No telemetry/network/ads SDK output.
```

If any such dependency is added later, this assessment and the privacy policy draft must be updated before distribution.

## Third-party SDK and dependency review

Current implementation dependencies are used for Android app construction, local persistence, dependency injection, UI, serialization, and tests.

Known dependency categories:

| Category                               | Current role                  | Data safety concern                                  |
|----------------------------------------|-------------------------------|------------------------------------------------------|
| AndroidX / Jetpack                     | App platform and UI support   | Review normal platform behavior                      |
| Jetpack Compose                        | UI rendering                  | No telemetry claim without dependency review         |
| Material 3                             | UI components                 | No telemetry claim without dependency review         |
| Hilt / Dagger                          | Dependency injection          | No user-data collection expected                     |
| Proto DataStore                        | Local persistence             | Stores local journey/preferences state               |
| Kotlin coroutines / Flow               | Async/state handling          | No user-data collection expected                     |
| Kotlin serialization                   | Local bundled catalog parsing | No user-data collection expected                     |
| Protobuf                               | Local state serialization     | No user-data collection expected                     |
| Google Fonts / downloadable-font provider | UI typography support | Font-provider behavior must be reviewed before public distribution |
| Test libraries                         | Test-only                     | Not part of distributed runtime unless misconfigured |

Current follow-up:

```text
Complete final dependency review before public distribution.
Specifically verify Google Fonts / downloadable-font provider behavior and any transitive SDK data behavior.
```

## Google Fonts / downloadable-font provider review

Current repository evidence:

- the theme layer uses a `GoogleFont.Provider`;
- the provider authority is `com.google.android.gms.fonts`;
- the provider package is `com.google.android.gms`;
- app typography references the provider through `fontProvider`;
- font certificate resources exist in app resources;
- no analytics, telemetry, crash-reporting, ads, Retrofit, OkHttp, or Ktor dependency was identified by the current dependency scan;
- no app-declared `INTERNET` permission was identified by the current manifest scan, if the scan remains empty.

Current classification:

- Google Fonts / downloadable-font provider usage: Present
- Purpose: typography / visual presentation
- Analytics role: none identified
- Crash-reporting role: none identified
- Advertising role: none identified
- App-owned network client: none identified
- Runtime font-provider behavior: Pending review
- Privacy/Data safety impact: Pending review

Decision:

The project must not claim that there is no possible network-related dependency behavior until Google Fonts / downloadable-font provider behavior is reviewed against the final release artifact and current Android/Google documentation.

Before public distribution, verify:

- whether the font provider can request or resolve fonts outside the APK;
- whether fonts are bundled, cached, or provider-resolved at runtime;
- whether any Google Play services behavior affects privacy or Data safety disclosures;
- whether fallback fonts are available if provider resolution fails;
- whether privacy policy or Data safety wording needs to mention font-provider behavior.

Current status:

```text
Google Fonts dependency review: Pending
No telemetry SDK identified: Still true
No crash-reporting SDK identified: Still true
No advertising SDK identified: Still true
No final statement about font-provider network behavior is made.
```

## Backup and data extraction gap

Backup and data extraction behavior is not finalized.

Current repository evidence:

- Android backup / data extraction configuration exists in app resources and manifest configuration must be reviewed before public distribution.
- Current documentation does not treat backup behavior as a decided privacy or Data safety policy.
- No final decision is recorded for whether Proto DataStore journey state and preferences should be included in or excluded from cloud backup, device transfer, or other Android data extraction behavior.

Required decision before public distribution:

```text
Should local journey state be included in Android backup / data extraction?
Should local journey state be excluded from Android backup / data extraction?
Should user preferences be included while journey progress is excluded?
Should backup behavior differ between cloud backup and device-to-device transfer?
How should reset progress, uninstall, storage clear, backup, and restore be described to users?
```

This gap affects:

- privacy policy accuracy;
- Data safety assessment accuracy;
- user expectations after device transfer or restore;
- reset/delete behavior documentation;
- support guidance for state restoration issues.

Current status:

```text
Backup / data extraction policy: Pending
DataStore journey state backup decision: Pending
DataStore preferences backup decision: Pending
Cloud backup behavior: Pending review
Device-transfer behavior: Pending review
No final Play Data safety assumption is made.
```

Until this policy is decided, the project must not claim that local journey state is definitely backed up, definitely excluded from backup, or definitely deleted across all restore paths.

## User controls and deletion

Current known user controls:

| Control                          | Current expected behavior                                              | Status                  |
|----------------------------------|------------------------------------------------------------------------|-------------------------|
| Reset progress                   | Clears journey progress state in app                                   | Must verify exact scope |
| Android system app storage clear | Clears app-local storage                                               | Platform control        |
| App uninstall                    | Removes app-local data from device, subject to backup/restore behavior | Backup policy pending   |

Required follow-up:

```text
Verify whether reset progress clears viewed state, bookmark state, completion status, timestamps, selected section, intro state, and preferences.
```

If reset progress does not clear all local app data, the privacy policy and Data safety assessment must describe the exact scope.

## Manual issue reports

Manual issue reports may include user-provided evidence such as:

* device model;
* Android version;
* app version/tag;
* reproduction steps;
* screenshots of the app UI;
* screen recordings of the app UI;
* Logcat excerpts.

Manual reports are not collected automatically by the app.

Manual reports must avoid unnecessary personal data. If a report includes sensitive data, it should be redacted before storage in repository documentation or issue history.

## Preliminary Play Data safety posture

This is not a final Play Console answer. It is a draft evidence summary.

Preliminary current posture:

| Data safety topic             | Draft answer                                                                 | Confidence                              |
|-------------------------------|------------------------------------------------------------------------------|-----------------------------------------|
| Data collected                | No intentional off-device user-data collection identified                    | Medium, pending final verification      |
| Data shared                   | No user-data sharing identified                                              | Medium, pending final verification      |
| Data processed locally        | Journey state and preferences stored locally                                 | High                                    |
| Data encrypted in transit     | Not applicable if no off-device transmission                                 | Pending final verification              |
| User can request deletion     | No account/backend data exists; local deletion through reset/system controls | Pending exact reset-scope verification  |
| Data retention                | Local until reset, storage clear, uninstall, or backup/restore behavior      | Pending backup decision                 |
| Third-party SDK data handling | No analytics/crash/ads SDK identified                                        | Medium, pending final dependency review |

## Required before final Play Console Data safety submission

Before using this assessment for a final Play Console declaration, complete:

* final manifest permission scan;
* final runtime dependency review;
* final source scan for network or telemetry behavior;
* final local state inventory;
* reset progress scope verification;
* backup / data extraction policy decision;
* third-party SDK behavior review;
* privacy policy draft update;
* support/privacy contact definition;
* release artifact verification;
* review against final Google Play Data safety form fields.

## Current decision

The current repository evidence supports this limited claim:

```text
The app is local-first and prepared for public-store hardening.
```

The current repository evidence does not yet support these claims:

```text
The app has a final Play Console Data safety declaration.
The app is published on Google Play.
The app is production-store ready.
The app has completed public-store privacy review.
```

This document is accepted as the draft Data safety evidence baseline for PSR-014.
