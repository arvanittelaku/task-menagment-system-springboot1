@echo off
cd /d "%~dp0"
set PATH=C:\Maven\apache-maven-3.9.9\bin;%PATH%
echo Starting application...
java -jar target\task-menagment-system-springboot1-0.0.1-SNAPSHOT.jar > app.log 2>&1

