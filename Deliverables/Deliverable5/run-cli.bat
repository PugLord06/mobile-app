@echo off
setlocal enabledelayedexpansion
set SCRIPT_DIR=%~dp0
java -jar "%SCRIPT_DIR%cli-app.jar" %*
