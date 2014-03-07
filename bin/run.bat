@echo off
if [%1]==[] goto usage
pushd %~dp0..
java YakaC.javacc.Yaka exemples/%1.yaka out/%1.yvm 
popd
goto eof

:usage
echo Usage: %0 expressions
echo (or any other file in exemples/ folder, without the extension)

:eof
