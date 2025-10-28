@echo off
cd /d "%~dp0"
set PATH=C:\Maven\apache-maven-3.9.9\bin;%PATH%
echo.
echo ========================================
echo Starting Task Management System
echo ========================================
echo.
java -jar target\task-menagment-system-springboot1-0.0.1-SNAPSHOT.jar
pause

