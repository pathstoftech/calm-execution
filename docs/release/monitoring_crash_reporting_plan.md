# Monitoring / Crash-Reporting Plan - 30 Days of Calm Execution

Status: Done for rc1 manual monitoring baseline
Milestone: E5 Post-release readiness
Public-store hardening status: Runtime crash-reporting / telemetry decision deferred
Task: E5-03 Define monitoring / crash-reporting plan
Owner: Tech lead
Reviewer: QA engineer
Baseline tag: `v1.0.0-rc1`

## Scope

This document defines the monitoring and crash-reporting plan for the `v1.0.0-rc1` release-hardening baseline.

This task does not add runtime telemetry, analytics, crash-reporting SDKs, networking, account systems, or remote monitoring.

For V1, monitoring is intentionally lightweight and manual because the release candidate is a local-first release-candidate baseline prepared for public-store hardening, not a published public-store app.

## Current monitoring posture

`v1.0.0-rc1` uses manual monitoring and issue intake.

No runtime crash-reporting SDK, analytics SDK, or telemetry SDK is integrated in the current app baseline.

This is intentional for the local-first release-candidate baseline. The app does not currently claim Google Play production readiness, public-store distribution, or production runtime observability.

Future crash-reporting or telemetry integration is a public-store hardening decision. That decision must be made together with privacy policy documentation, Data safety assessment, user consent expectations, dependency review, and release-build verification.

## Release baseline

Current release candidate:

```text
v1.0.0-rc1
```

Release-blocking defect status at baseline:

```text
Open Blocker defects: 0
Open Critical defects: 0
Open High defects: 0
```

Relevant completed gates:

```text
E2-05 Regression suite: passed
E2-06 Accessibility review: passed
E2-07 Exploratory pass: passed
E2-08 Defect closure: passed
E4-01 Clean release build: passed
E4-04 Baseline tag: recorded
E4-06 Known issues list: prepared
E5-02 Rollback / hotfix path: defined
```

## Monitoring decision for V1

### Current decision

No runtime crash-reporting SDK is added for `v1.0.0-rc1`.

Reason:

```text
The app is a local-first release-candidate baseline prepared for public-store hardening with no account system, no backend, no remote sync, and no public production telemetry requirement.
Adding a crash-reporting SDK at this stage would reopen build, privacy, dependency, and regression scope.
```

Current monitoring approach:

```text
Manual smoke testing
Manual issue intake
Crash reproduction from user/device reports
Logcat capture when available
Release artifact checksum tracking
Known issues list maintenance
Hotfix path for Blocker/Critical/approved High defects
```

Decision:

```text
Accepted for v1.0.0-rc1.
Runtime crash reporting is deferred to public-store readiness or broader distribution planning.
```

## What counts as a monitored incident

A monitored incident is any post-baseline report involving:

```text
App launch failure
Crash
ANR / freeze
Blank screen
Navigation failure
Wrong tip opens
Catalog cannot load
Images missing or broken
Bookmark/completion persistence broken
Reset progress corrupts state
Settings cannot be opened or exited
TalkBack blocks core use
Large font blocks core use
Release APK cannot be installed
Release APK integrity mismatch
```

## Severity classification

### Blocker

```text
App cannot launch.
Core journey flow is unusable.
Release APK cannot be installed.
Release artifact integrity is compromised.
User state is corrupted in a way that cannot be recovered.
```

Required action:

```text
Stop promotion.
Open hotfix path.
Use E5-02 rollback / hotfix procedure.
```

### Critical

```text
Crash in main app flow.
Crash on Home, Detail, Settings, or reset.
Broken navigation that traps the user.
Broken persistence for bookmark/completion/reset.
Accessibility failure that makes a core action unusable.
```

Required action:

```text
Open hotfix path unless Product owner and Tech lead explicitly decide otherwise.
```

### High

```text
Wrong tip opens.
Reset clears wrong data or fails to clear required data.
Expanded layout opens wrong detail.
Controls are unreachable.
Major content/image mismatch that damages product meaning.
```

Required action:

```text
Tech lead and Product owner decide hotfix vs next release candidate.
```

### Medium

```text
Polish issue with workaround.
Minor layout problem.
Non-critical state stutter.
Non-blocking accessibility enhancement.
Build warning that does not break release assembly.
```

Required action:

```text
Record in known issues or post-release backlog.
No default hotfix.
```

### Low

```text
Cosmetic issue.
Minor spacing issue.
Minor copy improvement.
Non-blocking test coverage gap.
```

Required action:

```text
Record for planned cleanup.
No hotfix.
```

## Manual issue intake

For every reported issue, capture:

```text
Issue ID
Date reported
Reporter
App version / tag
Commit SHA, if known
Device model
Android version
Layout mode: compact / expanded / landscape / tablet
Steps to reproduce
Expected behavior
Actual behavior
Screenshots or screen recording, if available
Logcat excerpt, if available
Severity
Owner
Decision: reject / defer / fix / hotfix
```

Suggested issue template:

```markdown
# Issue Report - 30 Days of Calm Execution

## Summary

<one-sentence issue summary>

## Version

Tag:

```text
v1.0.0-rc1
```

Commit:

```text
<commit SHA if known>
```

## Environment

Device:

```text
<device>
```

Android version:

```text
<version>
```

Layout mode:

```text
compact / expanded / landscape / tablet
```

## Steps to reproduce

1. 
2. 
3. 

## Expected behavior

```text
<expected>
```

## Actual behavior

```text
<actual>
```

## Evidence

Screenshots / recordings:

```text
<links or filenames>
```

Logcat:

```text
<paste excerpt if available>
```

## Severity

```text
Blocker / Critical / High / Medium / Low
```

## Decision

```text
Reject / Defer / Fix in next planned release / Hotfix
```

## Owner

```text
<owner>
```
```

## Crash reproduction protocol

When a crash is reported:

1. Confirm the reported version or tag.
2. Try to reproduce on the closest available emulator/device.
3. Capture Logcat.
4. Identify whether the crash occurs on:
   - launch
   - Home
   - Detail
   - Settings
   - reset
   - bookmark/complete
   - expanded/tablet selection
   - orientation/resize
5. Classify severity.
6. Decide whether E5-02 hotfix path applies.
7. If fixing, create a hotfix branch from `v1.0.0-rc1`.
8. Validate with the required test gates.
9. Create a new candidate tag, such as `v1.0.0-rc2`.

## Minimum validation after incident fix

Every incident fix must run:

```bash
./gradlew clean test assembleRelease --stacktrace
```

If the fix touches Home, Detail, navigation, bookmark, completion, or Settings behavior:

```bash
./gradlew connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=com.pathstoftech.calmexecution.navigation.CompactNavigationTest,com.pathstoftech.calmexecution.ui.home.HomeScreenTest,com.pathstoftech.calmexecution.ui.detail.TipDetailScreenTest" --stacktrace
```

If the fix touches expanded/adaptive behavior:

```bash
./gradlew connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=com.pathstoftech.calmexecution.ui.adaptive.AdaptiveAppShellTest,com.pathstoftech.calmexecution.ui.adaptive.ExpandedListDetailLayoutTest" --stacktrace
```

If the fix touches accessibility:

```text
Re-run the relevant E2-06 manual accessibility checks.
```

If the fix touches product flow:

```text
Re-run the relevant E2-07 manual exploratory checks.
```

## Logcat capture guidance

Use this for local crash investigation:

```bash
adb logcat -c
adb logcat > crash_logcat.txt
```

Then reproduce the crash.

Stop logging after reproduction and attach:

```text
crash_logcat.txt
```

Minimum useful Logcat content:

```text
Fatal exception
Package name: com.pathstoftech.calmexecution
Stack trace
Device / API level
Timestamp
User action immediately before crash
```

Do not commit raw logs if they contain personal data, local paths, tokens, or unrelated device information. Redact before storing in the repository.

## Privacy and data handling

For V1, no automatic telemetry is collected.

Manual reports should avoid collecting unnecessary personal data.

Allowed evidence:

```text
Device model
Android version
App version / tag
Crash stack trace
Reproduction steps
Screenshots of the app UI
Screen recordings of the app UI
```

Avoid collecting:

```text
personal messages
account identifiers
emails
location
contacts
photos outside the app
unrelated Logcat data
tokens or secrets
```

If a report includes sensitive data, redact before adding it to docs or issue history.

## Release artifact monitoring

For every release candidate or hotfix build, record:

```text
Tag
Commit SHA
APK filename
APK size
SHA-256 checksum
Build command
Build result
Known warnings
```

Checksum command:

```powershell
Get-FileHash app/build/outputs/apk/release/*.apk -Algorithm SHA256
```

Artifact integrity issue classification:

```text
Missing APK: Blocker
Checksum mismatch: Blocker until explained
Wrong tag/commit used: Blocker until corrected
Unsigned/uninstallable artifact: Blocker for distribution
```

## Manual post-release smoke check

After installing a release candidate or hotfix APK, perform:

```text
1. Launch app.
2. Home appears.
3. Open first Tip Detail.
4. Toggle Bookmark.
5. Toggle Complete.
6. Return to Home.
7. Open Settings.
8. Toggle Reduced motion.
9. Open reset dialog and cancel.
10. Restart app.
11. Confirm bookmark/completion state persists.
```

If expanded/tablet distribution is relevant:

```text
1. Open app on tablet/expanded emulator.
2. Select a TipCard.
3. Confirm right pane opens.
4. Confirm selected card is visually indicated.
5. Activate "<- Journey feed".
6. Confirm feed becomes reachable again.
```

## Public-store hardening decision gate

Before any public-store production-readiness claim, the project must decide whether to keep manual issue intake or integrate runtime crash reporting.

If runtime crash reporting or telemetry is added, the change must include:

- SDK selection and dependency review;
- privacy policy update;
- Data safety assessment update;
- user-facing disclosure review;
- release-build verification;
- regression test pass;
- rollback / hotfix impact review.

Until that decision is made, the documented state is:

```text
Crash-reporting SDK: not integrated
Analytics SDK: not integrated
Telemetry SDK: not integrated
Monitoring model: manual issue intake
Production observability claim: not made
```

## Future crash-reporting option

A runtime crash-reporting SDK may be considered in a future release only after a separate decision record covers:

```text
Selected tool
Data collected
Privacy impact
User consent requirements
Dependency impact
Build impact
Release signing impact
Opt-out / deletion policy, if needed
Regression test plan
```

Candidate future triggers:

```text
Public Play Store release
External beta distribution
Multiple user devices
Need for crash-free session metrics
Need for production ANR tracking
```

Until that decision is made, V1 uses manual issue intake and local reproduction.

## Responsibilities

| Area                           | Owner                  | Backup / reviewer |
|--------------------------------|------------------------|-------------------|
| Incident triage                | Tech lead              | Product owner     |
| User-facing impact decision    | Product owner          | Tech lead         |
| Crash reproduction             | QA engineer            | Android engineer  |
| Fix implementation             | Android engineer       | Tech lead         |
| Build verification             | Build/CI engineer      | Tech lead         |
| Accessibility regression check | Accessibility reviewer | QA engineer       |
| Artifact checksum record       | Build/CI engineer      | Tech lead         |
| Known issues update            | Product owner          | QA engineer       |

## Decision

E5-03 passes.

This monitoring plan applies to the current local-first release-candidate baseline prepared for public-store hardening. It does not claim production runtime monitoring for a published public-store app.

The release-candidate baseline uses manual monitoring and issue intake. Runtime crash-reporting SDK integration is deferred to a public-store hardening decision.

