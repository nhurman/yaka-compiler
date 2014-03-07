@echo off
pushd %~dp0..\YakaC\javacc
call javacc Yaka.jj
cd ..
call javac *.java javacc\*.java Event\*.java Exception\*.java
popd
