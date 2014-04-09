@echo off
pushd %~dp0..\YakaC
del /q *.class Event\*.class Exception\*.class javacc\*.class javacc\*.java Parser\*.class Target\*.class
cd ..
rmdir /s /q build
popd
