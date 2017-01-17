@echo off


rem classpath
SET CLASS_PATH=%~dp0..\lib\*

SET EXECJAVA="java"

if exist "%~dp0..\jre\bin\java.exe" SET EXECJAVA="%~dp0..\jre\bin\java.exe"
