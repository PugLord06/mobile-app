# Eduvos Campus App

A comprehensive Android application designed for Eduvos students to manage their academic life efficiently.

## Features

- **User Authentication**: Secure login and registration system
- **Dashboard**: Quick access to important information and features
- **Course Management**: View enrolled courses with details
- **Timetable**: Daily class schedule with venue and lecturer information
- **Grades**: Track academic performance with visual indicators
- **Profile Management**: Edit personal and academic information
- **Announcements**: Stay updated with latest campus news
- **Material Design**: Modern and intuitive user interface

## Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library
- **UI**: Material Design Components
- **Navigation**: Android Navigation Component
- **Async Operations**: Kotlin Coroutines
- **Image Loading**: Glide

## Project Structure

```
EduvosCampusApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/eduvos/campusapp/
│   │   │   │   ├── activities/      # All activity classes
│   │   │   │   ├── adapters/        # RecyclerView adapters
│   │   │   │   ├── database/        # Room database and DAOs
│   │   │   │   ├── fragments/       # Fragment classes
│   │   │   │   ├── models/          # Data models
│   │   │   │   └── utils/           # Utility classes
│   │   │   └── res/
│   │   │       ├── layout/          # XML layouts
│   │   │       ├── values/          # Colors, strings, themes
│   │   │       ├── drawable/        # Vector drawables
│   │   │       └── menu/            # Navigation menus
│   └── build.gradle                 # App-level build configuration
├── gradle/                          # Gradle wrapper files
├── build.gradle                     # Project-level build configuration
└── settings.gradle                  # Project settings
```

## Setup Instructions

### Prerequisites

1. **Android Studio**: Arctic Fox or later
2. **Android SDK**: API 24 or higher
3. **JDK**: Version 8 or higher

### Installation Steps

1. **Clone or Download the Project**
   ```bash
   cd /workspace/EduvosCampusApp
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the EduvosCampusApp directory
   - Click "OK"

3. **Update SDK Path**
   - Open `local.properties`
   - Update the SDK path: `sdk.dir=/path/to/your/Android/Sdk`

4. **Sync Project**
   - Click "Sync Project with Gradle Files" in the toolbar
   - Wait for dependencies to download

5. **Run the App**
   - Connect an Android device or start an emulator (API 24+)
   - Click the "Run" button or press Shift+F10
   - Select your device
   - Wait for the app to build and install

## Demo Credentials

For testing purposes, use these credentials:
- **Email**: demo@eduvos.com
- **Password**: demo123

## App Features in Detail

### 1. Authentication
- Login with email and password
- Registration for new students
- Remember me functionality
- Secure password storage

### 2. Home Dashboard
- Personalized greeting
- Next class reminder
- Quick access cards
- Recent announcements
- Course overview

### 3. Course Management
- List of enrolled courses
- Detailed course information
- Color-coded course cards
- Lecturer and venue details

### 4. Timetable
- Weekly schedule view
- Day-wise class listing
- Time, venue, and lecturer info
- Color-coded by course

### 5. Grades
- Semester-wise grade tracking
- Visual grade indicators
- Overall average calculation
- Pass/Fail status

### 6. Profile
- View and edit personal info
- Academic information
- Profile picture placeholder
- Logout functionality

## Database Schema

The app uses Room database with the following entities:

- **User**: Student information and credentials
- **Course**: Course details and metadata
- **Grade**: Academic performance records
- **Announcement**: Campus news and updates
- **TimetableEntry**: Class schedule entries

## Future Enhancements

- Push notifications for announcements
- Assignment submission tracking
- Library book management
- Campus map integration
- Dark mode support
- Offline data synchronization
- Export grades to PDF
- Chat with lecturers

## Troubleshooting

### Common Issues

1. **Gradle Sync Failed**
   - Check internet connection
   - Verify Android SDK path
   - Update Gradle version if needed

2. **App Crashes on Launch**
   - Clear app data
   - Check minimum SDK version
   - Review logcat for errors

3. **Login Issues**
   - Use demo credentials
   - Check internet connection
   - Verify email format

## Contributing

This is an educational project. Feel free to fork and modify for your own learning purposes.

## License

This project is for educational purposes only.

## Contact

For questions or support, please contact the development team.