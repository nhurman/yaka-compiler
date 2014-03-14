@echo off
pushd %~dp0..\YakaC
erase *.class Exception\*.class Event\*.class Parser\*.class javacc\*.class javacc\*.java
cd ..\build
erase *.LST *.OBJ *.MAP *.EXE *.asm
popd
