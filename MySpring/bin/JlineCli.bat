@echo off

if "%OS%" == "Windows_NT" setlocal
call "%~dp0env.bat"

cd %~dp0..
SET MAIN_CLASS="com.github.cxt.MySpring.cli.JlineCliMain"
"%EXECJAVA%" -cp "%CLASS_PATH%" "%MAIN_CLASS%" %*

endlocal