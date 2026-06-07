# Rollback / Hotfix Path - 30 Days of Calm Execution

Status: Done  
Milestone: E5 Post-release readiness  
Task: E5-02 Define rollback / hotfix path  
Owner: Tech lead  
Reviewer: Build/CI engineer  
Baseline tag: `v1.0.0-rc1`

## Scope

This document defines the rollback and hotfix path for the `v1.0.0-rc1` release-hardening baseline.

This task does not change runtime behavior, catalog content, image assets, build configuration, or release scope.

## Baseline

Current release candidate:

```text
v1.0.0-rc1
```

Baseline protection rule:

```text
Do not move, delete, or force-update the `v1.0.0-rc1` tag.
```

If the baseline must change, create a new candidate tag:

```text
v1.0.0-rc2
```

If a final release is approved from a release candidate, create a final release tag:

```text
v1.0.0
```

## Severity trigger policy

### Hotfix-eligible issues

A hotfix path may be opened only for:

```text
Blocker defect
Critical defect
High defect approved by Tech lead + Product owner
Security/privacy issue
Release artifact integrity issue
```

### Non-hotfix issues

The following should normally enter planned post-release work, not a hotfix:

```text
Medium polish issue
Low cosmetic issue
Build modernization warning with no build failure
Dependency modernization watch item
Accessibility enhancement with no core accessibility failure
Test coverage gap with no product failure
```

## Decision owners

| Decision                      | Primary owner     | Required reviewer |
|-------------------------------|-------------------|-------------------|
| Open hotfix branch            | Tech lead         | Product owner     |
| Approve hotfix scope          | Product owner     | Tech lead         |
| Implement hotfix              | Android engineer  | Tech lead         |
| Validate hotfix               | QA engineer       | Product owner     |
| Build / dependency validation | Build/CI engineer | Tech lead         |
| Publish new tag               | Tech lead         | Product owner     |

## Rollback path

Rollback means returning to the last known good release baseline.

For this project, the rollback baseline is:

```text
v1.0.0-rc1
```

### Rollback verification commands

```bash
git fetch --tags origin
git checkout v1.0.0-rc1
./gradlew clean test assembleRelease --stacktrace
```

Optional connected verification, if the rollback is caused by UI/navigation behavior:

```bash
./gradlew connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=com.pathstoftech.calmexecution.navigation.CompactNavigationTest,com.pathstoftech.calmexecution.ui.home.HomeScreenTest,com.pathstoftech.calmexecution.ui.detail.TipDetailScreenTest" --stacktrace

./gradlew connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=com.pathstoftech.calmexecution.ui.adaptive.AdaptiveAppShellTest,com.pathstoftech.calmexecution.ui.adaptive.ExpandedListDetailLayoutTest" --stacktrace
```

Rollback pass condition:

```text
Release baseline checks out cleanly.
Local unit/build gate passes.
Release APK is produced.
Any relevant connected regression test passes.
```

## Hotfix branch path

Create hotfix branch from the protected release-candidate tag:

```bash
git fetch --tags origin
git checkout -b hotfix/v1.0.0-rc1-<short-issue-name> v1.0.0-rc1
```

Examples:

```bash
git checkout -b hotfix/v1.0.0-rc1-launch-crash v1.0.0-rc1
git checkout -b hotfix/v1.0.0-rc1-reset-state v1.0.0-rc1
git checkout -b hotfix/v1.0.0-rc1-navigation-crash v1.0.0-rc1
```

Rules:

```text
Keep scope minimal.
Fix only the approved issue.
Do not perform opportunistic refactors.
Do not upgrade dependencies unless the dependency is the cause of the approved defect.
Do not alter frozen catalog content unless the approved defect is content-critical.
Do not move the original `v1.0.0-rc1` tag.
```

## Hotfix validation gate

Minimum validation for every hotfix:

```bash
./gradlew clean test assembleRelease --stacktrace
```

If the hotfix touches Home, Detail, navigation, bookmark, completion, or Settings behavior, also run compact connected regression:

```bash
./gradlew connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=com.pathstoftech.calmexecution.navigation.CompactNavigationTest,com.pathstoftech.calmexecution.ui.home.HomeScreenTest,com.pathstoftech.calmexecution.ui.detail.TipDetailScreenTest" --stacktrace
```

If the hotfix touches expanded layout, accessibility focus, adaptive routing, or selected detail behavior, also run expanded/adaptive connected regression:

```bash
./gradlew connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=com.pathstoftech.calmexecution.ui.adaptive.AdaptiveAppShellTest,com.pathstoftech.calmexecution.ui.adaptive.ExpandedListDetailLayoutTest" --stacktrace
```

If the hotfix touches accessibility behavior, manually re-run the relevant part of E2-06 accessibility review.

If the hotfix touches product flow, manually re-run the relevant part of E2-07 exploratory pass.

## Hotfix tagging policy

After the hotfix is approved and validated, create a new annotated tag.

For a new release candidate:

```bash
git tag -a v1.0.0-rc2 -m "30 Days of Calm Execution v1.0.0-rc2 hotfix candidate"
git push origin v1.0.0-rc2
```

For a final patch release after `v1.0.0` exists:

```bash
git tag -a v1.0.1 -m "30 Days of Calm Execution v1.0.1 hotfix release"
git push origin v1.0.1
```

Do not force-push or retag:

```text
Never move `v1.0.0-rc1`.
Never replace a pushed release candidate tag.
Create a new tag for every new release candidate or hotfix release.
```

## Hotfix evidence requirements

Every hotfix must include:

```text
Issue summary
Severity
Root cause
Files changed
Commands executed
Test results
Manual checks, if applicable
Known residual risk
Approval by Tech lead
Approval by Product owner
New tag
```

Suggested evidence file naming:

```text
docs/release/hotfix_<tag>_<short_issue_name>.md
```

Example:

```text
docs/release/hotfix_v1.0.0-rc2_launch_crash.md
```

## Rollback / hotfix decision matrix

| Situation                                 | Action                                                                                     |
|-------------------------------------------|--------------------------------------------------------------------------------------------|
| New Low issue found                       | Add to known issues / backlog. No hotfix.                                                  |
| New Medium polish issue found             | Product owner decides backlog vs next candidate. No default hotfix.                        |
| New High issue found                      | Tech lead + Product owner decide hotfix or defer.                                          |
| New Critical issue found                  | Open hotfix branch from last known good tag.                                               |
| New Blocker issue found                   | Stop release promotion. Open hotfix branch from last known good tag.                       |
| Build cannot produce release APK          | Treat as Blocker. Fix on hotfix branch or rollback to last good tag.                       |
| Release tag points to wrong commit        | Do not move tag casually. Escalate to Tech lead; create corrected candidate tag if needed. |
| Dependency upgrade breaks behavior        | Roll back dependency change or create hotfix branch from last good tag.                    |
| Catalog/content issue is release-critical | Product owner approves minimal content hotfix; rerun catalog validation and regression.    |

## Artifact integrity check

For any release candidate or hotfix APK, record:

```text
Tag
Commit SHA
APK path
APK size
SHA-256 checksum
Build command
Build result
```

Checksum command on PowerShell:

```powershell
Get-FileHash app/build/outputs/apk/release/*.apk -Algorithm SHA256
```

## Communication rule

When a hotfix is opened, document the decision before implementation:

```text
What broke?
Who approved the hotfix?
What exact scope is allowed?
Which tests are required?
Which tag will be created if validation passes?
```

This prevents the classic "small fix" that quietly becomes a dependency migration, a layout rewrite, and a small house fire.

## Decision

E5-02 passes.

Rollback and hotfix path is defined for the `v1.0.0-rc1` release-hardening baseline.

