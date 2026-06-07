# Follow-Up Review Schedule - 30 Days of Calm Execution

Status: Done  
Milestone: E5 Post-release readiness  
Task: E5-04 Schedule follow-up review date  
Owner: Product owner  
Reviewer: Tech lead  
Baseline tag: `v1.0.0-rc1`

## Scope

This document records the follow-up review schedule for the `v1.0.0-rc1` release-hardening baseline.

This task does not change runtime behavior, catalog content, image assets, build configuration, release artifacts, or release scope.

## Release baseline

Current release candidate:

```text
v1.0.0-rc1
```

Release readiness status:

```text
E1 product freeze: complete
E2 quality closure: complete
E3 architecture/code-quality closure: complete
E4 release/configuration closure: complete
E5 post-release ownership: complete through E5-03
```

Release-blocking defect status at scheduling time:

```text
Open Blocker defects: 0
Open Critical defects: 0
Open High defects: 0
```

## Follow-up review date

Primary follow-up review:

```text
2026-06-08 10:00 local project time
```

Backup review date:

```text
2026-06-15 10:00 local project time
```

Rationale:

```text
The primary review is scheduled for the first business-day review window after the V1 release-hardening closure period. This gives enough time to inspect any manual smoke-test feedback, issue reports, build warnings, and post-release ownership items without delaying release closure.
```

## Review attendees

| Role                     | Required                                          |
|--------------------------|---------------------------------------------------|
| Product owner            | Yes                                               |
| Tech lead                | Yes                                               |
| Android engineer         | Yes                                               |
| QA engineer              | Yes                                               |
| Build/CI engineer        | Yes                                               |
| Accessibility reviewer   | Optional unless accessibility issue is reported   |
| Product/content reviewer | Optional unless content/catalog issue is reported |
| Design reviewer          | Optional unless UI/adaptive issue is reported     |

## Review objectives

The follow-up review will answer:

```text
Did v1.0.0-rc1 remain stable after release-hardening closure?
Were any Blocker/Critical/High defects reported?
Did any accepted known issue become more severe?
Is a new release candidate needed?
Is the baseline ready to become v1.0.0?
Which post-release modernization items should start first?
```

## Required inputs before the review

Prepare the following before the review:

```text
Latest issue reports
Manual smoke-test notes
Any Logcat evidence for crashes
APK checksum / artifact record
Known issues list
Post-release ownership document
Rollback / hotfix path document
Monitoring / crash-reporting plan
Dependency modernization notes
Build warning carryovers
Accessibility follow-up notes, if any
```

Relevant documents:

```text
docs/release/v1.0.0-rc1_release_notes.md
docs/release/v1.0.0-rc1_known_issues.md
docs/release/post_release_ownership.md
docs/release/rollback_hotfix_path.md
docs/release/monitoring_crash_reporting_plan.md
```

## Review agenda

### 1. Baseline status

Confirm:

```text
v1.0.0-rc1 tag still points to the intended baseline.
No tag was moved or force-updated.
Release artifact checksum remains recorded.
No unapproved release-candidate changes were made.
```

### 2. Defect review

Review all reported issues since baseline closure.

Classify each issue as:

```text
Blocker
Critical
High
Medium
Low
Rejected / not reproducible
```

Gate decision:

```text
Any confirmed Blocker or Critical issue requires hotfix review.
Any confirmed High issue requires Tech lead + Product owner decision.
Medium and Low issues enter planned post-release work unless escalated.
```

### 3. Known issues review

Review accepted known issues:

```text
KI-001 Compact landscape top app bar appears slightly low
KI-002 android.newDsl=false deprecation warning
KI-003 Gradle 10 compatibility warnings
KI-004 Kotlin annotation default-target warning
KI-005 Native debug symbol strip warning
KI-006 Kotlin / KSP / Hilt modernization watch item
KI-007 AndroidX Core / Lifecycle modernization watch item
KI-008 No dedicated Settings instrumentation test
KI-009 Release minification disabled
KI-010 Adaptive NavHost lifecycle / resizing watch item
KI-011 ViewModel initial-state stutter watch item
KI-012 Journey timestamp capture skew watch item
KI-013 Tip detail route argument read once from SavedStateHandle
KI-014 Detail heading semantics enhancement
KI-015 Expanded return action touch target enhancement
KI-016 Public store readiness deferred
```

For each item, decide:

```text
Keep deferred
Start post-release task
Escalate severity
Close as accepted
Move to future release planning
```

### 4. Smoke-test review

Confirm smoke-test result:

```text
App launches.
Home appears.
Tip Detail opens.
Bookmark toggles.
Complete toggles.
Settings opens.
Reset dialog opens and cancel works.
Restart preserves bookmark/completion state.
Expanded layout opens selected detail.
Expanded return action clears selection.
```

### 5. Build / dependency review

Review:

```text
Gradle / AGP warning carryovers
Kotlin annotation warning
KSP/Hilt watch item
AndroidX Core/Lifecycle modernization item
Release minification decision
JDK 17+ compatibility / Java 21 daemon acceptance
```

Decision:

```text
Do not start dependency modernization without scheduling full regression rerun.
```

### 6. Accessibility review

Review whether any new accessibility concern was reported for:

```text
TalkBack flow
large font
image descriptions
bookmark/complete state announcements
expanded detail focus
reduced motion
touch targets
heading semantics
```

Decision:

```text
Core accessibility regression triggers hotfix triage.
Enhancements remain post-release polish unless they block core use.
```

### 7. Release decision

Choose one:

```text
Promote v1.0.0-rc1 toward final v1.0.0.
Create v1.0.0-rc2 after approved fixes.
Keep v1.0.0-rc1 as release-hardening baseline and defer final promotion.
Stop release promotion due to confirmed Blocker/Critical issue.
```

## Decision rules

### Promote to final candidate

Allowed when:

```text
No Blocker defects open.
No Critical defects open.
No High defects open.
Medium/Low items are documented and accepted.
No artifact integrity issue exists.
No release scope violation exists.
```

### Create new release candidate

Required when:

```text
A fix changes runtime behavior after v1.0.0-rc1.
A release-critical content/catalog change is approved.
A dependency/build change is required before final release.
A confirmed High issue is fixed before final release.
```

Use next tag:

```text
v1.0.0-rc2
```

### Open hotfix path

Required when:

```text
Confirmed Blocker defect.
Confirmed Critical defect.
Confirmed High defect approved for immediate fix by Tech lead + Product owner.
Security/privacy issue.
Release artifact integrity issue.
```

Follow:

```text
docs/release/rollback_hotfix_path.md
```

## Follow-up outputs

The review must produce:

```text
Review decision
Updated issue list, if any
Updated known issues list, if any
Post-release workstream priorities
Decision on v1.0.0 promotion or v1.0.0-rc2
Required validation gates for any follow-up change
Owner for each accepted action item
```

Suggested output file:

```text
docs/release/follow_up_review_2026-06-08.md
```

## Post-review action item format

Use this format for each action item:

```text
ID:
Title:
Severity / Priority:
Owner:
Reviewer:
Decision:
Required validation:
Target milestone:
```

Example:

```text
ID: PR-005
Title: Add SettingsScreenTest instrumentation coverage
Severity / Priority: P2
Owner: QA engineer
Reviewer: Android engineer
Decision: Start after V1 final tag
Required validation: compact connected test group
Target milestone: Post-V1 maintenance
```

## Calendar / reminder note

No external calendar event is created by this document.

The scheduled review date is recorded as a project release-readiness artifact. Calendar scheduling, if needed, should be created by the Product owner using the team calendar.

## Decision

E5-04 passes.

Follow-up review is scheduled for:

```text
2026-06-08 10:00 local project time
```

Backup review date:

```text
2026-06-15 10:00 local project time
```

## Notes

This completes the E5 post-release readiness set for the `v1.0.0-rc1` release-hardening baseline.
