@echo off
if [%1]==[] goto usage
pushd %~dp0..
java YakaC.Main exemples/%1.yaka
popd
goto eof

:usage
echo Usage: %0 expressions
echo (or any other file in exemples/ folder, without the extension)

:eof
