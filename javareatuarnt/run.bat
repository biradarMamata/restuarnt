@echo off
echo ==========================================
echo Restaurant Management System - Startup
echo ==========================================

echo [1/3] Creating bin directory for compiled files...
if not exist bin mkdir bin

echo [2/3] Compiling Java source files...
dir /s /b src\*.java > sources.txt
javac -cp ".;lib\sqlite-jdbc.jar" -d bin @sources.txt

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed! 
    echo Please make sure you have the Java Development Kit (JDK) installed 
    echo and that 'javac' is added to your system's PATH.
    echo.
    pause
    exit /b %errorlevel%
)

echo [3/3] Compilation successful! Starting Application...
echo.
java -cp "bin;lib\sqlite-jdbc.jar" com.restaurant.main.MainApplication

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Application crashed or 'java' command is not recognized.
    echo.
    pause
    exit /b %errorlevel%
)

pause
