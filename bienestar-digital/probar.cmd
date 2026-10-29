@echo off
cd /d "%~dp0"
chcp 65001 >nul
if not exist build mkdir build
javac -encoding UTF-8 --release 17 -d build src\bienestar\*.java pruebas\PruebasSistema.java
if errorlevel 1 (pause & exit /b 1)
java -Dfile.encoding=UTF-8 -cp build bienestar.PruebasSistema
pause
