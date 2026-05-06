# 30 Days of Calm Execution

**Kotlin-first Android portfolio project built with Jetpack Compose, Hilt, Proto DataStore, Flow, repository-based architecture, and unit-tested local persistence.**

`30 Days of Calm Execution` is an Android app that presents a structured 30-day journey of practical work-habit tips. The product focus is calm execution: starting work with clarity, protecting attention, reducing reactive habits, sustaining energy, and finishing meaningful work without burnout-style productivity noise.

This repository is currently being developed as a **hiring-facing Android portfolio project**. It is not presented as a finished consumer app yet. It is intended to show disciplined Android implementation: clean data boundaries, stable domain models, local source of truth, Proto DataStore persistence, repository contracts, Kotlin tests, and incremental milestone-based delivery.

---

## 60-second overview

| Area | Current state |
|---|---|
| App type | Native Android app |
| Language | Kotlin |
| UI direction | Jetpack Compose + Material 3 |
| Architecture | Layered architecture, repository-based data layer |
| Persistence | Proto DataStore for mutable local state |
| Content source | Bundled JSON catalog with validation |
| Dependency injection | Hilt bootstrap present; repository/DataStore modules planned |
| Current milestone | Milestone B — Local Truth |
| Completed core | Catalog foundation + Journey persistence |
| Next core work | Preferences persistence, DI modules, then Home/Detail Compose UI |

The current implementation is strongest in the **data, persistence, repository, and testing layers**. The polished Compose UI is the next visible milestone.

---

## Product concept

The app helps users build better work habits through 30 daily tips. Each day is structured around one practical idea, not vague motivation.

Each tip contains:

- a real work problem
- one practical recommendation
- why the recommendation helps
- one action to try today
- a category
- image metadata and accessibility description

Product philosophy:

> Good work is not frantic, reactive, or performative.  
> Good work is calm, focused, intentional, and sustainable.

---

## Planned user experience

### Home screen

The planned Home screen is an **Editorial Journey Layout**:

1. top app bar
2. intro / hero block
3. 30-day journey progress strip
4. phase chip row
5. sectioned feed of editorial tip cards

### Tip card

Each tip card will show:

- image
- day label
- title
- preview text
- category chip
- bookmark / completion state

### Detail screen

The detail screen will show:

1. image
2. day number
3. title
4. category
5. problem
6. tip
7. why it helps
8. try today
9. bookmark / completion actions

### Settings screen

Planned preferences:

- theme mode
- dynamic color
- reduced motion
- reset progress

---

## Current implementation status

### Completed — Milestone A: Foundation

- Android project baseline
- Gradle / AGP / Kotlin / Compose setup
- GitHub Actions Android CI
- Hilt application bootstrap
- Compose app shell placeholder
- core domain models
- bundled `tips_catalog.json`
- catalog DTOs
- catalog data source
- catalog mapper
- catalog validator
- catalog repository
- image resolver contract
- catalog validation tests
- actual bundled catalog validation

### Completed — Milestone B1: Journey persistence

- `journey_state.proto`
- `JourneyStateSerializer`
- `JourneyDataSource`
- `DataStoreJourneyDataSource`
- `JourneyStateMapper`
- `JourneyRepository`
- `JourneyRepositoryImpl`
- tests for full journey-state observation
- tests for single-tip state observation
- tests for `markViewed()`
- tests for bookmark mutation
- tests for completion status mutation
- tests for reset progress mutation

### In progress / next — Milestone B2 and B3

- preferences Proto schema and serializer
- preferences DataSource
- preferences mapper
- preferences repository
- Hilt repository modules
- Hilt DataStore modules
- temporary injection verification

### Planned — Milestone C

- Home screen
- Tip detail screen
- Navigation
- real catalog rendering
- bookmark/completion interaction in UI
- first usable app slice: browse → read → mark progress

---

## Tech stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design | Material 3 |
| Architecture | UI layer + data layer, repository pattern |
| Async/state | Kotlin coroutines, Flow |
| Persistence | Proto DataStore |
| Serialization | kotlinx.serialization for bundled JSON |
| Dependency injection | Hilt |
| Testing | JUnit unit tests |
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
2. **Mutable user state** — viewed tip, completion status, bookmarks, timestamps, and preferences.

Current high-level shape:

```text
UI layer
  ↓
ViewModel layer              planned next
  ↓
Repository layer             implemented for catalog + journey state
  ↓
Data sources                 implemented for catalog + journey DataStore
  ↓
Bundled JSON / Proto DataStore
```

Current data-layer flow:

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

Journey persistence flow:

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

The UI and future ViewModels should consume domain models through repositories. They should not read JSON, DataStore, or Proto classes directly.

---

## Package structure

Current important packages:

```text
com.example.a30daysofcalmexecution
  AppShell.kt
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
  TipImageResolver.kt
  DrawableTipImageResolver.kt

com.example.a30daysofcalmexecution.core.data.journey
  JourneyStateSerializer.kt
  JourneyDataSource.kt
  DataStoreJourneyDataSource.kt
  JourneyStateMapper.kt
  JourneyRepository.kt
  JourneyRepositoryImpl.kt

com.example.a30daysofcalmexecution.core.data.journey.proto
  Generated from journey_state.proto

com.example.a30daysofcalmexecution.di
  CatalogModule.kt
  SerializationModule.kt
```

Generated Proto classes are build output and should remain under `build/generated/...`. They should not be manually committed as source files.

---

## Domain model summary

### Catalog domain

The immutable catalog contains:

- `JourneyCatalog`
- `TipSection`
- `Tip`
- `TipBody`
- `TipImageRef`
- `TipId`
- `SectionKey`
- `TipCategoryKey`

Example stable tip ID:

```text
day_01_define_real_priority
```

Day number is presentation order. The stable string ID is the identity used for persistence, bookmarks, navigation, and future analytics.

### Journey user state

Mutable journey state contains:

- active tip ID
- per-tip completion status
- bookmark state
- last viewed timestamp
- completed timestamp

This state is persisted through Proto DataStore.

### Preferences state

Planned preferences include:

- theme mode
- dynamic color enabled
- reduced motion enabled
- last selected section
- intro seen flag

Preferences persistence is the next milestone.

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

    suspend fun setBookmarked(
        tipId: TipId,
        bookmarked: Boolean
    )

    suspend fun setCompletionStatus(
        tipId: TipId,
        status: TipCompletionStatus
    )

    suspend fun resetProgress()
}
```

Repository consumers work with domain models, not Proto models.

---

## Validation and tests

The project has a growing unit test suite focused on correctness before UI implementation.

### Catalog validation coverage

Catalog tests verify:

- exactly 30 tips
- unique tip IDs
- unique day numbers
- valid day numbers
- valid section keys
- valid category keys
- valid section definitions
- section day ranges match contained tips
- non-blank editorial fields
- valid image accessibility metadata
- image-key validation callback behavior
- actual bundled `tips_catalog.json` validity

### Journey persistence coverage

Journey tests verify:

- Proto serializer default value
- Proto serializer round trip
- corrupted Proto handling
- DataStore-backed journey data source behavior
- Proto-to-domain mapping
- domain-to-Proto mapping
- default / blank / zero Proto value handling
- full journey state observation
- single tip state observation
- `markViewed()` mutation
- bookmark mutation
- completion status mutation
- reset progress mutation

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

- checkout
- set up JDK 17
- set up Gradle
- make Gradle wrapper executable
- run unit tests
- assemble debug build

The workflow runs on:

- push
- pull request

---

## How to run the project

Clone the repository:

```bash
git clone https://github.com/pathstoftech/calm-execution.git
```

Open the project in Android Studio.

Then run:

```bash
./gradlew assembleDebug
```

For verification:

```bash
./gradlew clean testDebugUnitTest assembleDebug --stacktrace
```

Current visible UI is still a placeholder shell while the data and persistence foundation is being completed.

---

## Design direction

The planned visual identity is calm, modern, editorial, focused, and structured.

Planned Material 3 direction:

| Role | Direction |
|---|---|
| Primary | Slate Indigo |
| Secondary | Muted Teal |
| Tertiary | Soft Amber |
| Typography | clear editorial hierarchy |
| Shapes | soft-rectangular, moderately rounded |
| Dynamic color | default off |

The current theme still contains placeholder Material template colors in parts of the project. Full design-system integration is planned for Milestone C.

---

## What is intentionally not implemented yet

This repository is under active development. These parts are planned but not yet complete:

- final Home screen UI
- final Detail screen UI
- Navigation host
- Settings screen
- Preferences DataStore repository
- Hilt DataStore / Repository modules for all local state
- final image assets for all 30 tips
- adaptive large-screen layout
- Compose UI tests
- polished screenshots / demo video

This is deliberate milestone sequencing, not accidental omission. The project is being built from verified data contracts upward.

---

## Roadmap

### Milestone A — Foundation

Status: **Done**

- app bootstrap
- core domain models
- catalog JSON
- catalog loader
- catalog mapper
- catalog validator
- catalog repository
- validation tests

### Milestone B — Local truth

Status: **In progress**

Completed:

- journey Proto schema and serializer
- journey DataSource
- journey mapper
- journey repository
- journey repository flow and mutation tests

Next:

- preferences Proto schema and serializer
- preferences DataSource
- preferences mapper
- preferences repository
- DI modules
- repository injection verification

### Milestone C — First usable slice

Status: **Planned**

- Material 3 design system integration
- Navigation
- Home screen
- Detail screen
- real catalog rendering
- bookmark/completion interaction

### Milestone D — Product completeness

Status: **Planned**

- Settings
- adaptive layout
- preview/fake-data hardening
- 30-image asset pipeline
- accessibility sanity pass

### Milestone E — Release hardening

Status: **Planned**

- regression testing
- release build verification
- release notes
- known issues list
- operational checklist

---

## Hiring signal

This project is intended to demonstrate practical Android engineering habits:

- Kotlin-first Android development
- Compose-ready architecture
- clean domain modeling
- repository-based data access
- separation of DTO, Proto, domain, and future UI models
- local source of truth with Proto DataStore
- defensive persistence mapping
- Flow-based repository APIs
- unit-tested state transitions
- catalog validation for bundled product content
- stable identifiers instead of fragile display-order identity
- small commits aligned to implementation milestones
- CI-backed verification

The next major hiring-facing improvement will be the first visible Compose slice: **Home → Detail → mark progress**.

---

## Repository link

https://github.com/pathstoftech/calm-execution
