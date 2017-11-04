@echo off
setlocal
if not defined BUILD_TOOLS_ROOT set BUILD_TOOLS_ROOT=..\..\..\tools\mswindows
echo %BUILD_TOOLS_ROOT%
set JDK_HOME=%BUILD_TOOLS_ROOT%\jdk1.5.0_06

%JDK_HOME%\bin\java.exe -jar ..\..\thirdlib\compile-time-jars\selenium\selenium-server.jar -interactive

endlocal