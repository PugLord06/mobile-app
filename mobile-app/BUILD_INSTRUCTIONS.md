# Android Studio Setup and Build Instructions

This project is configured to work with Android Studio using Maven as the build system.

## Prerequisites

1. **Android Studio** (latest version recommended)
2. **Java Development Kit (JDK) 8** or higher
3. **Android SDK** with API level 33 installed
4. **Android Build Tools** version 33.0.2

## Opening the Project in Android Studio

1. **Open Android Studio**
2. Click on **File > Open...**
3. Navigate to the `/workspace/mobile-app` directory
4. Select the directory and click **OK**
5. Android Studio will detect the Maven project and import it automatically

## Configuring Android Studio for Maven

1. Go to **File > Settings** (or **Android Studio > Preferences** on macOS)
2. Navigate to **Build, Execution, Deployment > Build Tools > Maven**
3. Ensure Maven is properly configured:
   - Maven home directory should be set
   - Check "Import Maven projects automatically"
4. Click **Apply** and **OK**

## Setting Up Run Configuration

1. Go to **Run > Edit Configurations...**
2. Click the **+** button and select **Android Application**
3. Configure the following:
   - **Name**: Give it a meaningful name (e.g., "Run App")
   - **Module**: Select your app module
   - Under **Before launch**:
     - Remove the default "Gradle-aware Make" step
     - Click **+** and select **Run Maven Goal**
     - Enter `clean install` as the goal
4. Click **OK** to save

## Building the APK

### Option 1: Using Android Studio

1. With the project open, go to **Build > Build APK(s)**
2. Or use the Run configuration created above
3. The APK will be generated in `target/` directory

### Option 2: Using Command Line (Maven Wrapper)

#### Debug APK:
```bash
./mvnw clean install
```
The debug APK will be created at: `target/eduv4895277-app-1.0.0.apk`

#### Release APK:
First, create a keystore for signing (one-time setup):
```bash
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias your-key-alias
```

Then build the release APK:
```bash
./mvnw clean install -Prelease
```

**Note**: Before building a release APK, update the signing configuration in `pom.xml` with your keystore details:
- `storeFile`: Path to your keystore
- `storePass`: Keystore password
- `keyAlias`: Key alias name
- `keyPass`: Key password

### Option 3: Using Maven directly (if installed)

```bash
mvn clean install
```

## Running the App

1. Connect an Android device or start an emulator
2. In Android Studio, select your device from the device dropdown
3. Click the **Run** button (green play icon)
4. Android Studio will:
   - Execute Maven to build the project
   - Install the APK on the device
   - Launch the application

## Troubleshooting

### Common Issues:

1. **Android SDK not found**
   - Set `ANDROID_HOME` environment variable to your Android SDK location
   - In Android Studio: File > Project Structure > SDK Location

2. **Build Tools not found**
   - Install Android Build Tools 33.0.2 via SDK Manager
   - Tools > SDK Manager > SDK Tools > Android SDK Build-Tools

3. **Maven build fails**
   - Ensure all dependencies in `pom.xml` are available
   - Try `./mvnw clean` before building again

4. **App installation fails**
   - Enable "Developer options" and "USB debugging" on your device
   - Check that the device is properly connected (`adb devices`)

## Project Structure

```
mobile-app/
├── .idea/                  # Android Studio project files
├── .mvn/                   # Maven wrapper files
├── src/
│   └── main/
│       ├── java/          # Java source code
│       ├── res/           # Android resources
│       └── AndroidManifest.xml
├── target/                # Build output directory
├── pom.xml               # Maven configuration
├── proguard.cfg          # ProGuard rules for release builds
├── mvnw                  # Maven wrapper script (Unix/Linux/Mac)
└── eduv4895277-app.iml   # IntelliJ module file
```

## Additional Notes

- The project uses Maven Android Plugin for building
- Dependencies are managed through Maven Central repository
- The app targets Android API level 33 (Android 13)
- Minimum SDK version is 21 (Android 5.0)