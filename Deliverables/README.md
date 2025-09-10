# Eduvos Initial Project Deliverables

- Deliverable 1: Text document summarizing the approach and structure
- Deliverables 2-5: Executables for CLI and Swing apps

Import into Android Studio: File > New > Project from Existing Sources... and select `/workspace/app-parent` (Maven).

## Android App (Maven module)
- Module: /workspace/app-parent/android-app
- Import into Android Studio: File > New > Project from Existing Sources... and select /workspace/app-parent (Maven). Android Studio will index modules; you can open only the android module to run on device/emulator.
- Requirements: Installed Android SDK (ANDROID_HOME/ANDROID_SDK_ROOT), build-tools, and platform ${android.sdk.platform}.
- Build with Maven (from host): mvn -pl android-app -am clean package
- If Android Studio prefers Gradle, use the IDE option to create Gradle build files from sources for this module.
