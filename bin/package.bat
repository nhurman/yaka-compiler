@echo off
pushd %~dp0..
call bin\build.bat
if not exist build (mkdir build)
if exist build\YakaC (rmdir /s /q build\YakaC)
xcopy /f /s /q YakaC build\YakaC\
cd build\
del /f /s /q *.java
jar cf YakaC.jar YakaC
rmdir /s /q YakaC
popd