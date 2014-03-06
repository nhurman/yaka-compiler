@echo off
pushd %~dp0..\Yaka
erase *.class
erase Exception\*.class
erase javacc\*.class
erase javacc\*.java
popd
