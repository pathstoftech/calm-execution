# Release Signing Policy - 30 Days of Calm Execution

Status: Draft signing and artifact governance policy
Baseline: `v1.0.0-rc1`
Product: 30 Days of Calm Execution
Production-store claim: Not made

## Purpose

This document defines the current release signing and artifact governance policy for `30 Days of Calm Execution`.

It is written for the local-first release-candidate baseline prepared for public-store hardening.

This document does not claim that the project has a finalized Google Play signing setup, finalized upload key, finalized release keystore, or production distribution process.

## Current policy summary

Current release-signing posture:

```text
Debug builds: development/test artifacts only
Release APK: release-candidate artifact path, not automatically public distribution
Release AAB: pending verification / future Play distribution evidence
Release signing policy: draft / pending final decision
Signing keys in repository: not allowed
Keystore files in repository: not allowed
Secrets in repository: not allowed
Play App Signing / upload key process: not finalized
Production-store claim: not made
```

## Debug builds are not distribution artifacts

Debug builds are permitted for:

* local development;
* local emulator testing;
* debug verification;
* Compose UI and instrumentation test iteration;
* non-production internal smoke checks.

Debug builds must not be described as:

* public release artifacts;
* public-store release artifacts;
* production artifacts;
* Google Play upload artifacts;
* signed production builds.

Any README, release note, or checklist wording that uses debug build evidence must keep this boundary clear.

## Release artifact policy

The project distinguishes between implementation verification and distribution evidence.

| Artifact    | Current role                               | Distribution status                                                          |
| ----------- | ------------------------------------------ | ---------------------------------------------------------------------------- |
| Debug APK   | Development and verification artifact      | Not for public distribution                                                  |
| Release APK | Release-candidate verification artifact    | May be generated for local release checks, not final Play evidence by itself |
| Release AAB | Future Play distribution artifact evidence | Pending verification and signing policy                                      |
| Checksums   | Artifact integrity evidence                | Required for release candidates                                              |
| Git tag     | Source baseline evidence                   | Required for release candidates                                              |

Current artifact commands:

```powershell
./gradlew clean testDebugUnitTest assembleDebug --stacktrace
./gradlew assembleRelease --stacktrace
./gradlew bundleRelease --stacktrace
```

Current policy:

```text
Debug build success proves implementation baseline only.
Release APK success proves release build path only.
AAB success is stronger Play-distribution evidence, but still does not prove store readiness without signing, privacy, Data safety, listing, and distribution-track evidence.
```

## Signing key policy

Signing material must not be committed to the repository.

Forbidden repository content:

```text
*.jks
*.keystore
*.p12
*.pem
*.pk8
*.der
*.key
keystore.properties
signing.properties
release.properties
gradle.properties entries containing signing passwords
environment files containing signing passwords
plaintext upload-key passwords
plaintext keystore passwords
```

Allowed repository content:

```text
Documentation describing signing policy.
Non-secret placeholder variable names.
Build logic that reads signing values from environment variables or local-only ignored files, if added later.
Checksum files for public release artifacts, if they do not contain secrets.
```

If a signing configuration is added later, it must read sensitive values from a controlled local or CI secret source, not from committed plaintext files.

## Play App Signing and upload key status

Current status:

```text
Play App Signing decision: pending
Upload key decision: pending
Release keystore decision: pending
Key owner: pending
Key storage location: pending
Key rotation/recovery process: pending
```

Before any Google Play production-readiness claim, the project must document:

* whether Play App Signing is used;
* upload key generation process;
* key owner;
* storage location;
* recovery process;
* key rotation policy;
* access control;
* CI secret handling policy;
* release artifact signing path.

## Artifact naming policy

Release-candidate artifacts should use predictable names when copied out of Gradle build output.

Recommended pattern:

```text
calm-execution_<version>_<variant>_<artifact-type>.<ext>
```

Examples:

```text
calm-execution_v1.0.0-rc1_release_apk.apk
calm-execution_v1.0.0-rc1_release_aab.aab
calm-execution_v1.0.0-rc1_checksums.txt
```

Do not rename artifacts in a way that hides:

* version;
* release-candidate tag;
* artifact type;
* build variant.

## Checksum policy

Every release-candidate artifact intended for review must have a SHA-256 checksum recorded.

Recommended PowerShell command:

```powershell
Get-FileHash -Algorithm SHA256 path\to\artifact.apk
```

Recommended checksum record format:

```text
Artifact: calm-execution_v1.0.0-rc1_release_apk.apk
SHA-256: <hash>
Source tag: v1.0.0-rc1
Build command: ./gradlew assembleRelease --stacktrace
Built by: <name or role>
Build date: <YYYY-MM-DD>
```

Checksum records may be stored in release documentation if they contain no secrets.

## Secret scan policy

Before any signing-related commit, run a repository secret scan using simple local search first.

Required local scan:

```powershell
Get-ChildItem -Path . -Recurse -File -Force |
  Where-Object {
    $_.FullName -notmatch "\\.git\\" -and
    $_.FullName -notmatch "\\build\\" -and
    $_.FullName -notmatch "\\.gradle\\"
  } |
  Select-String -Pattern `
    "BEGIN PRIVATE KEY",
    "PRIVATE KEY",
    "keystorePassword",
    "keyPassword",
    "storePassword",
    "signingConfig",
    ".jks",
    ".keystore",
    ".p12",
    ".pem",
    ".pk8",
    ".der"
```

Expected current result:

```text
No committed signing key, keystore, or plaintext signing secret.
```

If the scan returns matches, manually classify them before commit. Documentation references to forbidden file extensions are acceptable. Real secrets or key material are not acceptable.

## CI signing policy

Current CI status:

```text
Debug unit/build CI: present
Release build CI gate: pending
Release signing in CI: not configured
CI signing secrets: not configured
```

Before release signing is added to CI, define:

* whether CI is allowed to sign release artifacts;
* which secret store is used;
* who can modify signing secrets;
* whether CI produces upload-ready artifacts;
* where artifacts are retained;
* how checksums are recorded;
* how failed signing attempts are audited.

Until this is defined, CI must not be presented as producing production distribution artifacts.

## Release verification gate

Before a release artifact can be treated as public-distribution evidence, complete:

* clean source state check;
* release build command;
* artifact path recording;
* signing status recording;
* SHA-256 checksum recording;
* version/tag consistency check;
* no-secrets scan;
* known-issues review;
* privacy/Data safety consistency check;
* rollback/hotfix path review.

Minimum command set:

```powershell
git status --short
./gradlew clean testDebugUnitTest assembleDebug --stacktrace
./gradlew assembleRelease --stacktrace
```

Optional stronger Play-distribution evidence after signing policy is finalized:

```powershell
./gradlew bundleRelease --stacktrace
```

## Current release artifact evidence

PSR-021 recorded local release APK build-path evidence.

```text
Command: ./gradlew clean testDebugUnitTest assembleDebug assembleRelease --stacktrace
Result: BUILD SUCCESSFUL
Release APK: app/build/outputs/apk/release/app-release-unsigned.apk
SHA-256: BEBE8B969B815A756102B1DF77D1124AFC01830B0C789AC3E1F145E733ADD545
Signing status: unsigned release APK
AAB status: pending
Distribution status: not a Play distribution artifact
```

This evidence verifies the release APK build path only. It does not prove final signing, Play App Signing, AAB readiness, store readiness, or production distribution readiness.

## Current AAB artifact evidence

PSR-022 recorded local release AAB build-path evidence.

```text
Command: ./gradlew bundleRelease --stacktrace
Result: BUILD SUCCESSFUL
Release AAB: app/build/outputs/bundle/release/app-release.aab
SHA-256: BE638F9FF82E8A252C84C9FE6A4F398B5D542E0A096B306C6A57E96B618A4D13
Signing status: pending release signing policy / not production distribution evidence by itself
Distribution status: not uploaded to Play
```

This evidence verifies the release AAB build path only. It does not prove final signing, Play App Signing, Play Console upload, store readiness, or production distribution readiness.

## Current hardening gaps

Release signing and artifact governance gaps remaining:

* final release signing decision;
* Play App Signing / upload key decision;
* key owner;
* key storage;
* key recovery process;
* CI release signing decision;
* release AAB verification;
* release artifact checksum record;
* release artifact retention policy;
* final no-secrets scan;
* release-build CI gate.

## Current decision

The current repository may claim:

```text
Release signing policy draft exists.
Debug builds are not distribution artifacts.
Signing keys and keystores must not be committed.
Release signing remains a public-store hardening gap.
```

The current repository must not claim:

```text
Final release signing is complete.
Google Play signing is configured.
Upload key policy is finalized.
CI produces production-signed artifacts.
The app is ready for public-store distribution.
```

This policy supports the repository claim that the app is prepared for public-store hardening. It does not support a production-store release claim.
