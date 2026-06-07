# Privacy Policy Draft - 30 Days of Calm Execution

Status: Draft / not final legal text
Baseline: `v1.0.0-rc1`
Product: 30 Days of Calm Execution
Draft effective date: 2026-06-08
Production-store claim: Not made
Support contact: TODO - define before public distribution

## Purpose

This document is a draft privacy policy basis for `30 Days of Calm Execution`.

It is written for the current local-first release-candidate baseline prepared for public-store hardening. It is not final legal text and must be reviewed before any Google Play production-readiness claim, public distribution, or final store listing submission.

## Current app privacy summary

`30 Days of Calm Execution` is a local-first Android app that presents a structured 30-day journey of practical work-habit tips.

In the current app baseline:

* no account system is implemented;
* no sign-in is implemented;
* no backend service is implemented;
* no remote sync is implemented;
* no analytics SDK is integrated;
* no telemetry SDK is integrated;
* no runtime crash-reporting SDK is integrated;
* no advertising SDK is integrated;
* no payment feature is implemented;
* no social sharing feature is implemented.

The app stores journey progress and preferences locally on the user's device.

## Data the app stores locally

The app may store the following local app state on the user's device:

* selected or active tip;
* viewed tip state;
* bookmark state;
* completion status;
* last viewed timestamps;
* completed timestamps;
* selected journey section;
* intro or onboarding seen state;
* theme mode preference;
* dynamic color preference;
* reduced motion preference.

This information is used to keep the user's app experience consistent between sessions.

## Data collection and transmission

The current app baseline does not intentionally collect, transmit, sell, or share personal data with the app developer or a third-party service.

The current app baseline does not send journey progress, bookmarks, completion status, preferences, or usage activity to a server.

If future versions add cloud sync, accounts, analytics, crash reporting, or telemetry, this privacy policy draft must be updated before those features are distributed.

## Analytics, telemetry, and crash reporting

The current app baseline does not include analytics, telemetry, or runtime crash-reporting SDK integration.

Issue reports, crash reports, screenshots, screen recordings, or Logcat excerpts may be shared manually by testers or users during support or release validation. Manual reports should avoid unnecessary personal information. If a report contains sensitive information, it should be redacted before being stored in repository documentation or issue history.

## Permissions

The current app baseline should not require sensitive runtime permissions such as location, camera, microphone, contacts, SMS, calendar, or phone access.

This statement must be rechecked against `AndroidManifest.xml` before public distribution.

## Local storage and device controls

The app uses local device storage for journey state and app preferences.

Users can control local app data by:

* using the app's reset progress flow, where available;
* clearing app storage through Android system settings;
* uninstalling the app.

The exact scope of the in-app reset progress action must be verified and documented before this draft becomes a final public privacy policy.

## Backup and data extraction

Android backup and data extraction behavior must be reviewed before public distribution.

The project must decide whether local journey state and preferences should be included in or excluded from Android backup / data extraction behavior.

Until that decision is documented, backup and data extraction policy remains a public-store hardening gap.

## Children and sensitive data

The current app baseline is a general productivity / work-habit app. It does not intentionally request sensitive personal information.

Target audience, content rating, and any children/family policy implications must be reviewed before public-store submission.

## Third-party services and SDKs

The current app baseline uses Android and Jetpack libraries for app implementation, local persistence, dependency injection, UI, and testing.

The current app baseline does not intentionally integrate analytics, advertising, remote sync, backend, or runtime crash-reporting SDKs.

A third-party dependency review must be completed before public distribution, including review of any font, platform, or SDK behavior that could affect privacy or network/data disclosure.

## User support and privacy contact

Support contact is not finalized.

Before public distribution, this document must include a real support or privacy contact route.

Placeholder:

```text
Support / privacy contact: TODO
```

## Changes to this policy

If the app's data behavior changes, this document must be updated before the changed version is distributed.

Examples of changes that require a privacy policy update:

* adding account creation or sign-in;
* adding cloud sync;
* adding analytics;
* adding telemetry;
* adding runtime crash reporting;
* adding advertising;
* adding network-backed content;
* changing local backup behavior;
* changing how reset progress works;
* adding sensitive permissions.

## Current limitations of this draft

This draft is not a final production privacy policy.

Before any public-store production-readiness claim, the project must complete:

* manifest permission scan;
* dependency and SDK privacy review;
* Data safety assessment;
* backup / data extraction decision;
* reset progress scope verification;
* support contact definition;
* store listing privacy policy placement decision;
* legal/policy review if required.

## Current draft decision

The current app baseline appears suitable for a local-first privacy posture:

```text
Account system: not implemented
Backend service: not implemented
Remote sync: not implemented
Analytics SDK: not integrated
Telemetry SDK: not integrated
Crash-reporting SDK: not integrated
Advertising SDK: not integrated
Primary storage: local device storage
Public-store privacy status: draft / pending hardening
```

This privacy policy draft supports the repository claim that the app is prepared for public-store hardening. It does not support a claim that the app is already published, store-ready, or production-ready.
