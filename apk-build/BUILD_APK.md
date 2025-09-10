# APK Build Instructions

Since the Android SDK is not available in this environment, you'll need to build the APK on a machine with Android development tools installed.

## Prerequisites

1. **Java Development Kit (JDK) 8** or higher
2. **Android SDK** with:
   - Android SDK Platform 33
   - Android SDK Build-Tools 33.0.2
3. **Set ANDROID_HOME** environment variable pointing to your Android SDK location

## Building the APK

### Option 1: Using the Maven Wrapper (Recommended)

1. Navigate to the project directory:
   ```bash
   cd /workspace/mobile-app
   ```

2. Build the debug APK:
   ```bash
   ./mvnw clean install
   ```

3. The APK will be generated at:
   ```
   /workspace/mobile-app/target/eduv4895277-app-1.0.0-debug.apk
   ```

### Option 2: Using Maven directly

If you have Maven installed:

```bash
cd /workspace/mobile-app
mvn clean install
```

### Option 3: Using Android Studio

1. Open Android Studio
2. Open the `/workspace/mobile-app` directory as a project
3. Let Android Studio sync the project
4. Click Build → Build APK(s)

## Setting Android SDK Path

If the build fails with "No Android SDK path could be found", you can:

1. **Set environment variable:**
   ```bash
   export ANDROID_HOME=/path/to/your/android-sdk
   ```

2. **Or pass it as a parameter:**
   ```bash
   ./mvnw clean install -Dandroid.sdk.path=/path/to/your/android-sdk
   ```

3. **Or add to pom.xml:**
   ```xml
   <properties>
     <android.sdk.path>/path/to/your/android-sdk</android.sdk.path>
   </properties>
   ```

## Building Release APK

To build a signed release APK:

1. Create a keystore (if you don't have one):
   ```bash
   keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias release-key
   ```

2. Update the signing configuration in `pom.xml` with your keystore details

3. Build the release APK:
   ```bash
   ./mvnw clean install -Prelease
   ```

## Troubleshooting

### Android SDK not found
- Make sure ANDROID_HOME is set correctly
- Verify SDK Platform 33 is installed via SDK Manager

### Build Tools not found
- Install Android SDK Build-Tools 33.0.2 via SDK Manager

### Java version issues
- Make sure you're using JDK 8 or higher
- Set JAVA_HOME to point to your JDK installation

## Expected Output

After a successful build, you'll find the APK at:
- Debug: `target/eduv4895277-app-1.0.0-debug.apk`
- Release: `target/eduv4895277-app-1.0.0-release.apk`

The APK can be installed on any Android device with API level 21 (Android 5.0) or higher.