@echo off
tasm /l /zi ..\lib\biblio.asm
tasm /l /zi %1.asm
tlink /v %1 %1 biblio.obj