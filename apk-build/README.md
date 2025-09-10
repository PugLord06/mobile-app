# APK Build Folder

This folder contains scripts and instructions for building the Android APK.

## Quick Start

### On Windows:
```batch
build-apk.bat
```

### On Linux/Mac:
```bash
./build-apk.sh
```

## Files in this folder:

- `BUILD_APK.md` - Detailed build instructions
- `build-apk.sh` - Build script for Linux/Mac
- `build-apk.bat` - Build script for Windows
- APK files will be copied here after successful build

## Requirements:

1. Android SDK installed with:
   - Platform API 33
   - Build Tools 33.0.2
2. ANDROID_HOME environment variable set
3. Java 8 or higher

## Project Location:

The Android project is located at: `/workspace/mobile-app/`

## Note:

The APK cannot be built in this environment because Android SDK is not available. You need to:

1. Clone or copy the `/workspace/mobile-app/` folder to a machine with Android SDK
2. Set up ANDROID_HOME environment variable
3. Run one of the build scripts or follow the manual instructions in BUILD_APK.md

The project is fully configured to work with Android Studio - just open the `/workspace/mobile-app/` folder in Android Studio and click the Run button!