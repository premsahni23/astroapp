@echo off
echo Stopping any running backend processes...
taskkill /f /im java.exe 2>nul

echo Cleaning test database...
del /q data\testdb.mv.db 2>nul
del /q data\testdb.lock.db 2>nul

echo Starting backend...
gradlew bootRun

pause