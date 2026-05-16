# 30 Days of Calm Execution

**Kotlin-first Android portfolio project built with Jetpack Compose, Material 3, Hilt, Proto DataStore, Flow, repository-based architecture, and tested local persistence.**

`30 Days of Calm Execution` is an Android app that presents a structured 30-day journey of practical work-habit tips. The product focus is calm execution: starting work with clarity, protecting attention, reducing reactive habits, sustaining energy, and finishing meaningful work without burnout-style productivity noise.

This repository is being developed as a **hiring-facing Android portfolio project**. It is not presented as a finished consumer app yet. It is intended to demonstrate disciplined Android implementation: clean data boundaries, stable domain models, local source of truth, Proto DataStore persistence, repository contracts, Compose UI state, Kotlin tests, and incremental milestone-based delivery.

---

## 60-second overview

| Area | Current state |
|---|---|
| App type | Native Android app |
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Layered architecture, ViewModel-driven UI state, repository-based data layer |
| Persistence | Proto DataStore for mutable local journey state and preferences |
| Content source | Bundled JSON catalog with validation |
| Dependency injection | Hilt application, repository, DataStore, serialization, and image resolver modules |
| Implemented milestones | A — Foundation, B — Local truth, C — First usable slice, D1 — Settings, D2 — Adaptive layout, D3 — Preview/fake data, D4 — Assets/content |
| Current milestone | D5 — Product-complete test pass |
| Next implementation focus | Settings tests, expanded adaptive tests, asset validation tests, accessibility sanity pass, and manual exploratory pass |

The current implementation supports the full local product flow: **browse the 30-day journey → open a tip detail screen → bookmark or complete a tip → adjust app preferences → use compact or expanded layouts → keep local state consistent through repositories and Proto DataStore**.

---

## Product concept

The app helps users build better work habits through 30 daily tips. Each day is structured around one practical idea, not vague motivation.

Each tip contains:

- a real work problem;
- one practical recommendation;
- why the recommendation helps;
- one action to try today;
- a category;
- image metadata and accessibility description.

Product philosophy:

> Good work is not frantic, reactive, or performative.  
> Good work is calm, focused, intentional, and sustainable.

---

## User experience

### Home screen

The Home screen implements the **Editorial Journey Layout**:

1. top app bar / app shell;
2. intro block;
3. 30-day journey progress strip;
4. phase chip row;
5. sectioned feed of editorial tip cards.

The Home screen renders real catalog content, supports loading/error/empty states, section filtering, bookmark toggles, completion toggles, and navigation into a detail screen.

### Tip card

Each tip card presents:

- image area / image reference;
- day label;
- title;
- preview text;
- category chip;
- bookmark state;
- completion state.

The card opens the correct tip detail destination using the stable `TipId`, not the display day number.

### Detail screen

The Detail screen preserves the approved reading order:

1. image;
2. day number;
3. title;
4. category;
5. problem;
6. tip;
7. why it helps;
8. try today;
9. bookmark / completion actions.

Opening a detail screen marks the tip as viewed, but journey progress is derived from completion/progression state rather than last viewed state.

### Settings screen

The Settings screen is implemented as part of product completeness.

It supports:

- theme mode selection;
- dynamic color toggle;
- reduced motion toggle;
- reset progress flow with confirmation;
- app shell reaction to persisted preference changes.

---

## Screenshots

Home and Detail screenshot files are not committed in this README update. Add them when available, for example:

```text
docs/screenshots/home.png
docs/screenshots/detail.png
```

Then reference them with:

```markdown
![Home screen](docs/screenshots/home.png)
![Detail screen](docs/screenshots/detail.png)
```

---

## Current implementation status

### Completed — Milestone A: Foundation

- Android project baseline;
- Gradle / AGP / Kotlin / Compose setup;
- GitHub Actions Android CI;
- Hilt application bootstrap;
- Compose app shell;
- core domain models;
- bundled `tips_catalog.json`;
- catalog DTOs;
- catalog data source;
- catalog mapper;
- catalog validator;
- catalog repository;
- image resolver contract and implementation;
- catalog validation tests;
- actual bundled catalog validation.

### Completed — Milestone B: Local truth

- `journey_state.proto`;
- `user_preferences.proto`;
- Journey serializer, data source, mapper, repository, and mutation flows;
- Preferences serializer, data source, mapper, and repository;
- bookmark persistence;
- completion persistence;
- viewed-state persistence;
- selected-section persistence;
- theme / dynamic color / reduced motion preference persistence;
- reset progress behavior;
- Hilt modules for app, data, repositories, DataStore, serialization, and image resolving;
- repository and failure-path tests.

### Completed — Milestone C: First usable slice

- Material 3 design-system integration;
- app token layer for colors, typography, shapes, spacing, elevation, and motion;
- shared top bar, chips, labels, error panel, and loading placeholders;
- typed route specs;
- NavHost with Home, TipDetail, and Settings routes;
- compact back behavior;
- invalid `tipId` handling path;
- Home route, state, actions, ViewModel, and screen;
- intro block;
- journey progress strip;
- phase chip row;
- grouped feed;
- editorial tip card;
- real catalog rendering;
- bookmark toggle;
- completion toggle;
- section filtering;
- loading, error, and empty states;
- Tip Detail route, state, actions, ViewModel, and screen;
- detail meta block, content block, section cards, and action row;
- `markViewed()` on detail open;
- Home UI tests;
- Detail UI tests;
- compact navigation tests;
- Home / Detail ViewModel tests.

### Completed — Milestone D1: Settings

- Settings state, actions, ViewModel, route, and screen;
- theme mode preference row;
- dynamic color preference row;
- reduced motion preference row;
- reset progress flow;
- reset confirmation dialog;
- app shell reaction to settings changes.

### Completed — Milestone D2: Adaptive layout

- expanded app shell;
- list-detail layout for larger screens;
- feed pane and selected detail pane;
- empty detail placeholder;
- selection/back behavior for expanded layout;
- compact layout preserved.

### Completed — Milestone D3: Preview and fake-data hardening

- preview data;
- fake repositories;
- sample UI states;
- preview-only content helpers;
- screen previews that do not depend on runtime DI.

### Completed — Milestone D4: Assets and content finalization

- 30 runtime WebP assets;
- runtime image directory under `drawable-nodpi`;
- image-key to drawable mapping;
- finalized image content descriptions;
- fallback asset support.

### Next — Milestone D5: Product-complete test pass

- Settings tests;
- expanded adaptive tests;
- asset validation tests;
- accessibility sanity pass;
- manual exploratory pass.

### Later — Milestone E: Release hardening

- full regression pass;
- release build verification;
- release-critical TODO/FIXME cleanup;
- accessibility review;
- dependency-state review;
- release notes;
- known issues list;
- rollback / hotfix path;
- crash-reporting or monitoring plan if the project moves toward release.

---

## Tech stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design | Material 3 |
| Architecture | UI layer + data layer, repository pattern, ViewModel state holders |
| Async/state | Kotlin coroutines, Flow / StateFlow |
| Persistence | Proto DataStore |
| Serialization | kotlinx.serialization for bundled JSON |
| Dependency injection | Hilt |
| Testing | JUnit unit tests, ViewModel tests, Compose UI tests, navigation tests |
| Build | Gradle, Android Gradle Plugin, Version Catalog |
| CI | GitHub Actions |

Current baseline:

```text
AGP: 9.1.1
Gradle wrapper: 9.3.1
Kotlin: 2.2.20
JDK: 17
Compose BOM: 2026.02.01
minSdk: 24
targetSdk: 36
compileSdk: 36.1
```

---

## Architecture

The project separates two different truths:

1. **Immutable editorial catalog** — the 30 tips and their section metadata.
2. **Mutable user state** — viewed tips, completion status, bookmarks, timestamps, selected section, and preferences.

Current high-level shape:

```text
Compose UI layer
        ↓
ViewModel layer
        ↓
Repository layer
        ↓
Data sources
        ↓
Bundled JSON / Proto DataStore / image resources
```

Catalog flow:

```text
res/raw/tips_catalog.json
        ↓
RawResourceCatalogDataSource
        ↓
CatalogValidator
        ↓
CatalogMapper
        ↓
CatalogRepository
        ↓
Domain models
```

Journey state flow:

```text
Proto DataStore<JourneyStateProto>
        ↓
JourneyDataSource
        ↓
JourneyStateMapper
        ↓
JourneyRepository
        ↓
JourneyUserState / TipUserState
```

Preferences flow:

```text
Proto DataStore<UserPreferencesProto>
        ↓
PreferencesDataSource
        ↓
PreferencesMapper
        ↓
PreferencesRepository
        ↓
UserPreferences
```

UI code consumes state through ViewModels and repositories. It should not read JSON, DataStore, or Proto classes directly.

---

## Package structure

Current important packages:

```text
com.example.a30daysofcalmexecution
  CalmExecutionApp.kt
  MainActivity.kt

com.example.a30daysofcalmexecution.core.model
  JourneyCatalog.kt
  JourneyUserState.kt
  SectionKey.kt
  ThemeMode.kt
  Tip.kt
  TipBody.kt
  TipCategoryKey.kt
  TipCompletionStatus.kt
  TipId.kt
  TipImageRef.kt
  TipSection.kt
  TipUserState.kt
  UserPreferences.kt

com.example.a30daysofcalmexecution.core.data.catalog
  CatalogDataSource.kt
  CatalogDto.kt
  CatalogMapper.kt
  CatalogRepository.kt
  CatalogRepositoryImpl.kt
  CatalogValidationResult.kt
  CatalogValidator.kt
  RawResourceCatalogDataSource.kt
  SectionDto.kt
  TipDto.kt

com.example.a30daysofcalmexecution.core.data.images
  DrawableTipImageResolver.kt
  TipImageResolver.kt

com.example.a30daysofcalmexecution.core.data.journey
  DataStoreJourneyDataSource.kt
  JourneyDataSource.kt
  JourneyRepository.kt
  JourneyRepositoryImpl.kt
  JourneyStateMapper.kt
  JourneyStateSerializer.kt

com.example.a30daysofcalmexecution.core.data.preferences
  DataStorePreferencesDataSource.kt
  PreferencesDataSource.kt
  PreferencesMapper.kt
  PreferencesRepository.kt
  PreferencesRepositoryImpl.kt
  UserPreferencesSerializer.kt

com.example.a30daysofcalmexecution.di
  AppModule.kt
  DataModule.kt
  DataStoreModule.kt
  RepositoryModule.kt
  SerializationModule.kt

com.example.a30daysofcalmexecution.navigation
  AppRoute.kt
  CalmExecutionNavHost.kt

com.example.a30daysofcalmexecution.ui.home
  HomeAction.kt
  HomeRoute.kt
  HomeScreen.kt
  HomeUiState.kt
  HomeViewModel.kt
  HomeIntroBlock.kt
  JourneyProgressStrip.kt
  SectionChipRow.kt
  TipCard.kt
  TipSectionFeed.kt

com.example.a30daysofcalmexecution.ui.detail
  TipDetailAction.kt
  TipDetailRoute.kt
  TipDetailScreen.kt
  TipDetailUiState.kt
  TipDetailViewModel.kt
  TipDetailMetaBlock.kt
  TipDetailContentBlock.kt
  DetailActionsRow.kt
  
  com.example.a30daysofcalmexecution.ui.settings
  SettingsAction.kt
  SettingsRoute.kt
  SettingsScreen.kt
  SettingsUiState.kt
  SettingsViewModel.kt

com.example.a30daysofcalmexecution.ui.adaptive
  AdaptiveAppShell.kt
  ExpandedJourneyRoute.kt
  ExpandedListDetailLayout.kt

com.example.a30daysofcalmexecution.ui.preview
  PreviewContent.kt
  PreviewData.kt
  PreviewUiStates.kt
  ScreenPreviews.kt
  fake/
```

Generated Proto classes are build output and should remain under `build/generated/...`. They should not be manually committed as source files.

---

## Domain model summary

### Catalog domain

The immutable catalog contains:

- `JourneyCatalog`;
- `TipSection`;
- `Tip`;
- `TipBody`;
- `TipImageRef`;
- `TipId`;
- `SectionKey`;
- `TipCategoryKey`.

Example stable tip ID:

```text
day_01_define_real_priority
```

Day number is presentation order. The stable string ID is the identity used for persistence, bookmarks, navigation, and future analytics.

### Journey user state

Mutable journey state contains:

- active / viewed tip state;
- per-tip completion status;
- bookmark state;
- last viewed timestamp;
- completed timestamp.

This state is persisted through Proto DataStore.

### Preferences state

Preferences include:

- theme mode;
- dynamic color enabled;
- reduced motion enabled;
- last selected section;
- intro seen flag.

Preferences are persisted through Proto DataStore and exposed through `PreferencesRepository`.

---

## Repository contracts

### CatalogRepository

Responsible for immutable tip content.

```kotlin
interface CatalogRepository {
    suspend fun getCatalog(): JourneyCatalog
    suspend fun getTip(tipId: TipId): Tip?
    suspend fun getSection(sectionKey: SectionKey): TipSection?
    suspend fun getTipsForSection(sectionKey: SectionKey): List<Tip>
    suspend fun getAdjacentTipIds(tipId: TipId): AdjacentTipIds
}
```

### JourneyRepository

Responsible for mutable journey progress.

```kotlin
interface JourneyRepository {
    fun observeJourneyState(): Flow<JourneyUserState>
    fun observeTipState(tipId: TipId): Flow<TipUserState>
    suspend fun markViewed(tipId: TipId)
    suspend fun setBookmarked(tipId: TipId, bookmarked: Boolean)
    suspend fun setCompletionStatus(tipId: TipId, status: TipCompletionStatus)
    suspend fun resetProgress()
}
```

### PreferencesRepository

Responsible for app-level preferences.

```kotlin
interface PreferencesRepository {
    fun observePreferences(): Flow<UserPreferences>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setDynamicColorEnabled(enabled: Boolean)
    suspend fun setReducedMotionEnabled(enabled: Boolean)
    suspend fun setLastSelectedSection(sectionKey: SectionKey?)
    suspend fun setHasSeenIntro(seen: Boolean)
}
```

Repository consumers work with domain models, not DTO or Proto models.

---

## Validation and tests

The project has a test suite covering the local data model, repositories, ViewModels, navigation, and first usable Home/Detail flow.

### Catalog validation coverage

Catalog tests verify:

- exactly 30 tips;
- unique tip IDs;
- unique day numbers;
- valid day numbers;
- valid section keys;
- valid category keys;
- valid section definitions;
- section day ranges match contained tips;
- non-blank editorial fields;
- valid image accessibility metadata;
- image-key validation callback behavior;
- actual bundled `tips_catalog.json` validity.

### Journey persistence coverage

Journey tests verify:

- Proto serializer default value;
- Proto serializer round trip;
- corrupted Proto handling;
- DataStore-backed journey data source behavior;
- Proto-to-domain mapping;
- domain-to-Proto mapping;
- default / blank / zero Proto value handling;
- full journey state observation;
- single tip state observation;
- `markViewed()` mutation;
- bookmark mutation;
- completion status mutation;
- reset progress mutation.

### Preferences coverage

Preferences tests verify:

- Proto serializer behavior;
- DataStore-backed preferences behavior;
- mapping between Proto and domain models;
- theme mode mutation;
- dynamic color mutation;
- reduced motion mutation;
- selected section mutation;
- intro seen flag mutation.

### First usable slice coverage

Feature tests cover:

- Home UI rendering;
- Detail UI rendering;
- compact navigation behavior;
- Home ViewModel behavior;
- Detail ViewModel behavior;
- consistency of bookmark/completion state between Home and Detail.

---

## Commands

Run unit tests:

```bash
./gradlew testDebugUnitTest --stacktrace
```

Assemble debug build:

```bash
./gradlew assembleDebug --stacktrace
```

Run full local verification:

```bash
./gradlew clean testDebugUnitTest assembleDebug --stacktrace
```

Generate Proto classes manually when needed:

```bash
./gradlew clean generateDebugProto compileDebugKotlin --stacktrace
```

---

## CI

The repository includes a GitHub Actions workflow for Android verification.

Workflow responsibilities:

- checkout;
- set up JDK 17;
- set up Gradle;
- make Gradle wrapper executable;
- run unit tests;
- assemble debug build.

The workflow runs on:

- push;
- pull request.

---

## How to run the project

Clone the repository:

```bash
git clone https://github.com/pathstoftech/calm-execution.git
cd calm-execution
```

Open the project in Android Studio, or run from the command line:

```bash
./gradlew assembleDebug
```

For verification:

```bash
./gradlew clean testDebugUnitTest assembleDebug --stacktrace
```

---

## Design direction

The visual identity is calm, modern, editorial, focused, and structured.

Material 3 direction:

| Role | Direction |
|---|---|
| Primary | Slate Indigo |
| Secondary | Muted Teal |
| Tertiary | Soft Amber |
| Typography | clear editorial hierarchy |
| Shapes | soft-rectangular, moderately rounded |
| Dynamic color | default off |

The design-system token layer is integrated. Further polish belongs to product-completeness and release-hardening work.

---

## What is intentionally not implemented yet

This repository is under active development.

The main local product experience is implemented, including Home, Detail, Settings, adaptive layout, preview/fake-data support, persisted local state, and the 30-image runtime asset set.

Remaining work is verification and release hardening:

- Settings test coverage;
- expanded adaptive layout test coverage;
- asset validation tests;
- accessibility sanity pass;
- manual exploratory pass;
- final polished screenshots / demo video;
- full release-readiness pass;
- dependency-state review;
- release notes and known issues list.

The project is past the first usable slice. Current work is product-complete verification, not core feature construction.

---

## Roadmap

### Milestone A — Foundation

Status: **Done**

- app bootstrap;
- core domain models;
- catalog JSON;
- catalog loader;
- catalog mapper;
- catalog validator;
- catalog repository;
- validation tests.

### Milestone B — Local truth

Status: **Done**

- journey Proto schema and serializer;
- journey DataStore source;
- journey mapper;
- journey repository;
- journey repository flow and mutation tests;
- preferences Proto schema and serializer;
- preferences DataStore source;
- preferences mapper;
- preferences repository;
- DI modules;
- repository verification tests.

### Milestone C — First usable slice

Status: **Done**

- Material 3 design-system integration;
- Navigation;
- Home screen;
- Detail screen;
- real catalog rendering;
- bookmark/completion interaction;
- Home/Detail tests;
- compact navigation tests.

### Milestone D — Product completeness

Status: **In progress — D1/D2/D3/D4 done, D5 next**

Completed:

- Settings;
- adaptive layout;
- preview/fake-data hardening;
- 30-image asset pipeline;
- finalized image content descriptions;
- fallback asset support.

Next:

- Settings tests;
- expanded adaptive tests;
- asset validation tests;
- accessibility sanity pass;
- manual exploratory pass.

### Milestone E — Release hardening

Status: **Planned**

- regression testing;
- release build verification;
- release notes;
- known issues list;
- operational checklist.

---

## Hiring signal

This project is intended to demonstrate practical Android engineering habits:

- Kotlin-first Android development;
- Jetpack Compose UI implementation;
- Material 3 design-system integration;
- clean domain modeling;
- repository-based data access;
- separation of DTO, Proto, domain, and UI models;
- local source of truth with Proto DataStore;
- defensive persistence mapping;
- Flow-based repository APIs;
- ViewModel-owned screen state;
- catalog validation for bundled product content;
- stable identifiers instead of fragile display-order identity;
- Home → Detail navigation with stable route arguments;
- bookmark/completion state consistency;
- unit-tested state transitions;
- Compose UI and navigation tests;
- small commits aligned to implementation milestones;
- CI-backed verification.

The next major hiring-facing improvement is **product-complete test coverage + screenshots/demo polish**, not the first visible Compose slice. The first visible slice, Settings, adaptive layout, preview support, and runtime asset pipeline already exist.

---

## Repository link

https://github.com/pathstoftech/calm-execution
