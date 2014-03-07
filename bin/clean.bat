@echo off
pushd %~dp0..\YakaC
erase *.class
erase Exception\*.class
erase Event\*.class
erase javacc\*.class
erase javacc\*.java
popd
