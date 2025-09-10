# TripBuddy - Travel Companion App

Version 1.0 - Educational Demo Assignment for Android Development

## Overview

TripBuddy is a comprehensive travel companion Android application designed for trip planning, budgeting, memory creation, and loyalty rewards. This is a fully functional demo app built with Java and Maven for educational purposes.

## Features

1. **User Management**
   - Login/Registration system
   - Session management with auto-login
   - User preferences storage

2. **Trip Planning & Budgeting**
   - Create trips with destination, dates, and notes
   - Select from predefined activities (Sightseeing, Hiking, Dining, Museum Tour)
   - Add custom expenses
   - Real-time budget calculation
   - Loyalty discount (10% after 3 trips)

3. **Memory Creation**
   - Create travel memories with location and mood
   - Background music player with controls
   - Image preview (simulated)
   - SQLite persistence

4. **Gallery**
   - Grid view of all memories
   - Full-screen image viewer
   - Tap to view details
   - Music playback option

5. **Settings**
   - Dark/Light theme toggle
   - Music enable/disable
   - Language selection (UI only)
   - Account information

## Technical Stack

- **Language**: Java
- **Build System**: Maven (Android Maven Plugin 4.6.0)
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 30 (Android 11)
- **Database**: SQLite
- **UI**: Material Design Components
- **Architecture**: MVC pattern

## Project Structure

```
TripBuddy/
├── pom.xml                              # Maven configuration
├── src/main/
│   ├── AndroidManifest.xml             # App manifest
│   ├── java/com/tripbuddy/app/
│   │   ├── activities/                 # All Activity classes
│   │   ├── adapters/                   # RecyclerView adapters
│   │   ├── database/                   # SQLite helper
│   │   ├── models/                     # Data models
│   │   └── utils/                      # Utility classes
│   └── res/
│       ├── layout/                     # XML layouts
│       ├── values/                     # Colors, strings, themes
│       ├── drawable/                   # Vector drawables
│       └── raw/                        # Music files
└── README.md                           # This file
```

## Build Instructions

### Prerequisites

1. **Android Studio** (latest version recommended)
2. **Android SDK** (API level 30 or higher)
3. **Maven** (3.6.0 or higher)
4. **Java JDK** (8 or higher)

### Setup Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd TripBuddy
   ```

2. **Configure SDK Path**
   - Open `pom.xml` in a text editor
   - Find the line: `<path>/path/to/android-sdk</path>`
   - Replace with your actual Android SDK path:
     - **Windows**: `C:\Users\YourName\AppData\Local\Android\Sdk`
     - **macOS**: `/Users/YourName/Library/Android/sdk`
     - **Linux**: `/home/YourName/Android/Sdk`

3. **Add Required Assets**
   
   Create the following placeholder files:
   
   **Images** (in `src/main/res/drawable/`):
   - `beach.png` - Beach scene
   - `mountain.png` - Mountain landscape
   - `city.png` - City skyline
   - `forest.png` - Forest scene
   - `adventure.png` - Adventure theme
   
   **Music** (in `src/main/res/raw/`):
   - `relax.mp3` - Relaxing music
   - `upbeat.mp3` - Upbeat music
   - `ambient.mp3` - Ambient music

4. **Import in Android Studio**
   - Open Android Studio
   - Select "Import Project"
   - Navigate to the TripBuddy folder
   - Select the `pom.xml` file
   - Click "OK" and wait for import

5. **Build the Project**
   - In Android Studio: Build → Make Project
   - Or via command line:
     ```bash
     mvn clean install
     ```

6. **Run the App**
   - Create an AVD (Android Virtual Device) with API 30
   - Click the "Run" button in Android Studio
   - Or via command line:
     ```bash
     mvn android:deploy android:run
     ```

## Default Credentials

For demo purposes, any username/password combination will create a new user automatically.

## Key Implementation Details

### Database Schema

**Users Table**
- id (INTEGER PRIMARY KEY)
- username (TEXT UNIQUE)
- email (TEXT)
- password (TEXT)

**Trips Table**
- id (INTEGER PRIMARY KEY)
- user_id (INTEGER)
- destination (TEXT)
- start_date (TEXT)
- end_date (TEXT)
- notes (TEXT)
- total_cost (REAL)
- activities_json (TEXT)
- custom_expenses_json (TEXT)

**Memories Table**
- id (INTEGER PRIMARY KEY)
- user_id (INTEGER)
- image_path (TEXT)
- location (TEXT)
- date (TEXT)
- mood (TEXT)
- music_path (TEXT)

**Activities Table** (Predefined)
- id (INTEGER PRIMARY KEY)
- name (TEXT)
- cost (REAL)
- category (TEXT)

### App Flow

1. **Splash Screen** (2 seconds) → Login/Dashboard
2. **Login** → Dashboard (auto-login if previously logged in)
3. **Dashboard** → Trip Planning, Memory Creation, Gallery, Settings
4. **Trip Planning** → Budget Summary → Dashboard
5. **Memory Creation** → Save → Gallery
6. **Gallery** → Full Screen View → Gallery

### Features Implementation

- **Animations**: Fade-in, scale, pulse effects using AnimationUtil
- **Music**: MediaPlayer with play/pause/stop controls
- **Themes**: Light/Dark mode toggle with immediate apply
- **Loyalty**: 10% discount after 3 trips (stored in SharedPreferences)
- **TES Score**: Dummy calculation (memories×3 + gallery×1 + loyalty×4)

## Troubleshooting

1. **Build Fails**
   - Ensure Android SDK path is correct in pom.xml
   - Check Maven and Java versions
   - Clean and rebuild: `mvn clean install`

2. **App Crashes**
   - Check logcat for errors
   - Ensure all required assets are added
   - Verify minimum SDK version compatibility

3. **Import Issues**
   - Use "Import Project" not "Open Project"
   - Select pom.xml, not the folder
   - Let Android Studio download dependencies

## Demo Features

- Predefined activities with costs
- Simulated image upload in gallery
- Dummy music playback (requires MP3 files)
- Auto-generated demo memories if gallery is empty
- Simple login system (creates user if not exists)

## Assignment Coverage

✓ Project Structure and Navigation (10 marks)
✓ Trip Planning and Budgeting (20 marks)
✓ Memory Creation and Gallery (20 marks)
✓ User Management and Persistence (30 marks)
✓ Overall App Flow and Features (20 marks)

## Notes

- This is an educational demo app
- No real backend integration
- Basic security (plain text passwords)
- Dummy data used where appropriate
- Focus on functionality over production readiness

## License

Educational use only. Created for Android Development course assignment.