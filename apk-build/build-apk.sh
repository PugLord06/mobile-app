#!/bin/bash

# APK Build Script for Eduvos Android App
# This script helps build the APK with proper Android SDK configuration

echo "==================================="
echo "Eduvos Android App APK Builder"
echo "==================================="

# Check if ANDROID_HOME is set
if [ -z "$ANDROID_HOME" ]; then
    echo "ERROR: ANDROID_HOME environment variable is not set!"
    echo ""
    echo "Please set it to your Android SDK location:"
    echo "  export ANDROID_HOME=/path/to/android-sdk"
    echo ""
    exit 1
fi

echo "Android SDK found at: $ANDROID_HOME"

# Navigate to the project directory
PROJECT_DIR="$(dirname "$0")/../mobile-app"
cd "$PROJECT_DIR" || exit 1

echo "Building from: $(pwd)"

# Check if Maven wrapper exists
if [ -f "./mvnw" ]; then
    echo "Using Maven wrapper..."
    BUILD_CMD="./mvnw"
else
    echo "Using system Maven..."
    BUILD_CMD="mvn"
fi

# Clean previous builds
echo ""
echo "Cleaning previous builds..."
$BUILD_CMD clean

# Build the APK
echo ""
echo "Building APK..."
$BUILD_CMD install -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "==================================="
    echo "BUILD SUCCESSFUL!"
    echo "==================================="
    echo ""
    echo "APK location:"
    find target -name "*.apk" -type f | while read apk; do
        echo "  - $apk"
        ls -lh "$apk"
    done
    
    # Copy APK to apk-build folder
    echo ""
    echo "Copying APK to apk-build folder..."
    cp target/*.apk ../apk-build/ 2>/dev/null
    
    echo ""
    echo "You can now install the APK on your Android device!"
else
    echo ""
    echo "==================================="
    echo "BUILD FAILED!"
    echo "==================================="
    echo ""
    echo "Please check the error messages above."
    echo "Common issues:"
    echo "  - Android SDK not properly installed"
    echo "  - Missing SDK Platform 33"
    echo "  - Missing Build Tools 33.0.2"
    exit 1
fi