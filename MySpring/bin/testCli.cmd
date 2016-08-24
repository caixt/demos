@echo off


setlocal

SET CLASS_PATH=%~dp0..\lib\*
SET ZOOMAIN="com.github.cxt.MySpring.cli.Main"
java -cp "%CLASS_PATH%" %ZOOMAIN% %*

endlocal

