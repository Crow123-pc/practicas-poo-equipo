@echo off
setlocal
cd /d "%~dp0"
if not exist build\classes mkdir build\classes
javac -encoding UTF-8 --release 17 -d build\classes src\bienestar\*.java
if errorlevel 1 exit /b 1
java -cp build\classes bienestar.AppSwing
endlocal
