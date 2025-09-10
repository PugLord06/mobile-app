# Eduvos Android App (EDUV4895277)

Author: Student number EDUV4895277

Java + Maven Android application implementing Deliverables 2–5 and features 3.x–5.x. Importable into Android Studio. Uses SharedPreferences for sessions/preferences and SQLite for structured data.

## Prerequisites
- JDK 8 (recommended) or JDK 11
- Android SDK (Platform 33)
- Maven 3.8+
- Windows PowerShell setup:
```powershell
$env:ANDROID_HOME = "$HOME\AppData\Local\Android\Sdk"
[Environment]::SetEnvironmentVariable('ANDROID_HOME', $env:ANDROID_HOME, 'User')
```
Install Platform 33 via Android Studio SDK Manager.

## Build & Run (Maven)
```powershell
mvn -Dandroid.sdk.path=%ANDROID_HOME% clean package
mvn -Dandroid.sdk.path=%ANDROID_HOME% android:deploy android:run
```
APK output: `target/`

## Import into Android Studio
- File → Open → select `mobile-app/pom.xml`
- Let it sync; run on device/emulator.

## Project Structure
- `src/main/AndroidManifest.xml`
- Java: `src/main/java/za/ac/eduvos/eduv4895277/*`
- Resources: `src/main/res/*`
- DB: `DatabaseHelper` (SQLite tables: `memories`, `trips`)
- Preferences: `Preferences` (session, theme, music, language, trip counter)

## Features Overview
- Splash → Login → Main dashboard navigation.
- Registration kept for reference; Login controls session.
- Memories: create memory with photo, optional background music, and notes. Saved to SQLite. Tap to view full-screen and play/pause/stop audio.
- Gallery: dynamic grid of saved photos (from SQLite). Tap any to open full-screen viewer.
- Budget: activity spinner with fixed costs, custom and meals inputs, realtime subtotal. Save Trip increments counter and stores trip in SQLite. Confirm shows summary with loyalty discount (10% after 3 trips).
- Settings: toggle dark/light theme, background music enable, logout.
- Branding: app icon, colors/theme, splash screen.

## How to Use (Walkthrough)
1) Launch app → Splash routes to `LoginActivity` if not logged in.
2) Login: enter email/username → `LOGIN`.
3) Main menu:
   - Memories: Pick Photo, Pick Music (optional), write notes, Save Memory. List fades in. Tap a memory to view fullscreen; use Play/Pause/Stop.
   - Gallery: Add Photo to populate grid. Tap to view fullscreen.
   - Budget: Choose activity (e.g., Sightseeing 1700, Beach Day 2600), enter custom/meals. Subtotal updates as you type. Save Trip (persists + increments counter). Confirm Trip shows subtotal, discount (if trips ≥ 3), final total.
   - About: app/author info. Settings: theme/music toggle, logout.

## Data & Persistence
- SharedPreferences
  - `Preferences`: username, logged_in, theme, music, language
  - `TripStore`: persistent trip counter (reward/loyalty)
- SQLite (`DatabaseHelper`)
  - `memories(id, photo_uri, audio_uri, notes, date)`
  - `trips(id, destination, notes, activity, custom, meals, created_at)`

## Theming & Branding
- Colors: `res/values/colors.xml`
- Theme: `res/values/styles.xml` (`AppTheme`, `SplashTheme`)
- Splash: `res/drawable/splash_background.xml`
- Icon: `res/mipmap-anydpi-v26/ic_launcher.xml`

## Permissions
- `READ_MEDIA_IMAGES`, `READ_MEDIA_AUDIO` declared in `AndroidManifest.xml`

## Notes
- If using JDK 17, consider Maven Toolchains for Java 8 compatibility with the android-maven-plugin.
- For older Android versions, adapt permissions (READ_EXTERNAL_STORAGE). This app targets API 33.

## Troubleshooting
- If Maven cannot find the SDK: ensure `ANDROID_HOME` points to the SDK root and Platform 33 is installed.
- If device not detected: enable USB debugging or start an emulator from Android Studio. 