@echo off
pushd %~dp0Yaka\javacc
call javacc yaka.jj
cd ..
call javac javacc\*.java Exception\*.java *.java
popd
