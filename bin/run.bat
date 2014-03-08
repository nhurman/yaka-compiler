@echo off
if [%1]==[] goto usage
pushd %~dp0..
java YakaC.Main examples/%1.yaka
popd
goto eof

:usage
echo Usage: %0 expressions
echo (or any other yaka file in the examples/ folder, without the extension)

:eof
