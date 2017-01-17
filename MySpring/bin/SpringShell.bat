@echo off

if "%OS%" == "Windows_NT" setlocal
call "%~dp0env.bat"

cd %~dp0..
SET MAIN_CLASS="org.springframework.shell.Bootstrap"
"%EXECJAVA%" -cp "%CLASS_PATH%" "%MAIN_CLASS%" %*

endlocal