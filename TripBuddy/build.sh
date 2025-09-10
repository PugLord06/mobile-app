#!/bin/bash

# TripBuddy Build Script
# This script helps configure and build the TripBuddy Android app

echo "TripBuddy Build Script"
echo "====================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven 3.6.0 or higher."
    exit 1
fi

# Check for Android SDK path
if [ -z "$ANDROID_HOME" ]; then
    echo "Warning: ANDROID_HOME environment variable is not set."
    echo "Please set it to your Android SDK path or update pom.xml manually."
    echo ""
    echo "Example:"
    echo "  export ANDROID_HOME=/Users/YourName/Library/Android/sdk"
    echo ""
else
    echo "Android SDK found at: $ANDROID_HOME"
    
    # Update pom.xml with the SDK path
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        sed -i '' "s|<path>/path/to/android-sdk</path>|<path>$ANDROID_HOME</path>|g" pom.xml
    else
        # Linux
        sed -i "s|<path>/path/to/android-sdk</path>|<path>$ANDROID_HOME</path>|g" pom.xml
    fi
    echo "Updated pom.xml with SDK path"
fi

# Create required directories
echo "Creating asset directories..."
mkdir -p src/main/res/drawable
mkdir -p src/main/res/raw

# Create placeholder files info
echo ""
echo "IMPORTANT: Add the following files before building:"
echo ""
echo "Images (in src/main/res/drawable/):"
echo "  - beach.png"
echo "  - mountain.png"
echo "  - city.png"
echo "  - forest.png"
echo "  - adventure.png"
echo ""
echo "Music files (in src/main/res/raw/):"
echo "  - relax.mp3"
echo "  - upbeat.mp3"
echo "  - ambient.mp3"
echo ""

# Ask if user wants to build
read -p "Do you want to build the project now? (y/n) " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Building TripBuddy..."
    mvn clean install
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "Build successful!"
        echo "You can now import the project into Android Studio."
    else
        echo ""
        echo "Build failed. Please check the error messages above."
    fi
else
    echo "Build skipped. You can build later using: mvn clean install"
fi

echo ""
echo "Next steps:"
echo "1. Add the required image and music files"
echo "2. Import the project into Android Studio (File > Import Project > Select pom.xml)"
echo "3. Run the app on an emulator or device"