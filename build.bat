@echo off
pushd %~dp0src
call javacc ../yaka.jj
call javac *.java
popd
