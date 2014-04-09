@echo off
if [%1]==[] goto usage
pushd %~dp0..
if not exist build (mkdir build)
java YakaC.Main %1 build/%~n1.asm
popd
goto eof

:usage
echo Usage: %0 yaka_file

:eof
