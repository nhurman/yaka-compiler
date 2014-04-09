@echo off
pushd %~dp0..\doc
call doxygen
popd