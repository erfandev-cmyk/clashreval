# Clash of Clans War Assistant

Offline-first Android tactical planning companion for Clash of Clans.

## Current implementation

- Jetpack Compose Material 3 tactical UI.
- Local multi-profile account database with Room.
- Versioned game-data catalog with TH1–TH18 schema and verified/partial-data flags.
- Screenshot import and memory-safe normalization.
- Recognition abstraction with a strict no-fake-results policy.
- Manual detection model and structured detection data.
- Base geometry analysis and directional entry scoring.
- Grid pathfinding.
- Probabilistic hidden-defense estimation, explicitly labelled as heuristic.
- Local army builder and capacity validation.
- Local strategy engine producing deployment stages, routes, reasons and risks.
- Attack history in Room.
- Floating companion service using Android overlay APIs; it does not inject, automate, or read game memory.
- GitHub Actions debug APK build and artifact upload.
- Unit and instrumentation test foundations.
- Reproducible detector dataset specification.

## Important limitations

A reliable Clash of Clans building detector requires a trained, licensed model and a sufficiently large annotated dataset. This repository intentionally does **not** ship fabricated detections or pretend that classical image filters are a production detector. `ModelAssetDetector` is the integration boundary and reports a clear warning until a compatible model adapter is installed.

Game balance and level statistics change frequently. The bundled data catalog therefore avoids inventing DPS/HP/cost numbers. It records structural unlock metadata where maintained and flags the rest as unverified. Supercell's November 1, 2026 announcement states Town Hall 19 is coming mid-November 2026; this snapshot therefore targets TH1–TH18 and keeps the schema extensible for TH19.

## Build without Android Studio

GitHub Actions is the supported build environment. Push this repository to GitHub, open **Actions → Android Build**, and run the workflow. The workflow installs JDK 17, Gradle 8.10.2, Android SDK 35, runs tests/lint, builds `app-debug.apk`, and uploads it as an artifact.

The execution environment used to assemble this source project does not contain the Android SDK or Gradle distribution and has no outbound package-network access, so an APK was not locally verified here. The workflow includes a wrapper-materialization step so the repository can still bootstrap its Gradle wrapper in CI.

## Overlay policy

The overlay requires `SYSTEM_ALERT_WINDOW` and explicit user consent. It only displays saved tactical information. It does not inject into Clash of Clans, alter the APK, inspect memory, or automate troop deployment. Third-party overlays/companion tools may have game-policy implications; users should check the current game rules before use.

## Game data provenance

Game-data updates should be made from verified official or appropriately licensed sources and recorded with a version and date. Do not add guessed balance values.
