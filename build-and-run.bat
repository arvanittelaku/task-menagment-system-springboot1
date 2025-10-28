@echo off
setlocal

REM Get the full path to this script's directory
set "PROJECT_DIR=%~dp0"

REM Set JAVA_HOME to the installed JDK
for /f "tokens=2*" %%a in ('reg query "HKLM\SOFTWARE\Microsoft\JDK\17" /v Path 2^>nul') do set "JAVA_HOME=%%b"
if not defined JAVA_HOME (
    for /f "tokens=2*" %%a in ('reg query "HKLM\SOFTWARE\JavaSoft\JDK\17" /v JavaHome 2^>nul') do set "JAVA_HOME=%%b"
)

echo.
echo ========================================
echo Building Task Management System
echo ========================================
echo.

REM Change to project directory
cd /d "%PROJECT_DIR%"

REM Run Maven clean install
call "%PROJECT_DIR%mvnw.cmd" clean install -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Starting Spring Boot Application...
echo ========================================
echo.

REM Run the Spring Boot application
call "%PROJECT_DIR%mvnw.cmd" spring-boot:run

pause

