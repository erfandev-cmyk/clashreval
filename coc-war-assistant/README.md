# CoC War Assistant (Phase 1 foundation)

## Build the APK without Android Studio
1. Create a new GitHub repo and upload ALL files (keep `.github/workflows/android.yml`).
2. Open the **Actions** tab, pick "Android Build", click **Run workflow** (it also runs on every push).
3. Open the finished run, go to **Artifacts**, download `app-debug`, unzip it, and install `app-debug.apk`.

The workflow generates the Gradle Wrapper itself if it is absent.

## Status (honest)
| Module | Status |
|---|---|
| Dark UI shell + 10-section navigation | Implemented, not device-tested |
| Account Manager (name, tag, TH, Room) | Partial |
| Game data (versioned meta, TH range) | Placeholder, unverified |
| Unit test (TH range) | Written, never run |
| GitHub Actions workflow | Written, never run |
| Vision, tactics, armies, overlay, history | Not implemented |

Nothing has been compiled yet. Expect to fix first-build errors from the Actions log.
