@echo off
REM APK Build Script for Eduvos Android App (Windows)
REM This script helps build the APK with proper Android SDK configuration

echo ===================================
echo Eduvos Android App APK Builder
echo ===================================

REM Check if ANDROID_HOME is set
if "%ANDROID_HOME%"=="" (
    echo ERROR: ANDROID_HOME environment variable is not set!
    echo.
    echo Please set it to your Android SDK location:
    echo   set ANDROID_HOME=C:\path\to\android-sdk
    echo.
    exit /b 1
)

echo Android SDK found at: %ANDROID_HOME%

REM Navigate to the project directory
set PROJECT_DIR=%~dp0..\mobile-app
cd /d "%PROJECT_DIR%"

echo Building from: %CD%

REM Check if Maven wrapper exists
if exist "mvnw.cmd" (
    echo Using Maven wrapper...
    set BUILD_CMD=mvnw.cmd
) else (
    echo Using system Maven...
    set BUILD_CMD=mvn
)

REM Clean previous builds
echo.
echo Cleaning previous builds...
call %BUILD_CMD% clean

REM Build the APK
echo.
echo Building APK...
call %BUILD_CMD% install -DskipTests

REM Check if build was successful
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===================================
    echo BUILD SUCCESSFUL!
    echo ===================================
    echo.
    echo APK location:
    for /r target %%f in (*.apk) do (
        echo   - %%f
    )
    
    REM Copy APK to apk-build folder
    echo.
    echo Copying APK to apk-build folder...
    copy target\*.apk ..\apk-build\ >nul 2>&1
    
    echo.
    echo You can now install the APK on your Android device!
) else (
    echo.
    echo ===================================
    echo BUILD FAILED!
    echo ===================================
    echo.
    echo Please check the error messages above.
    echo Common issues:
    echo   - Android SDK not properly installed
    echo   - Missing SDK Platform 33
    echo   - Missing Build Tools 33.0.2
    exit /b 1
)