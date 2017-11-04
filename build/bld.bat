@echo off

setlocal

if not defined BUILD_TOOLS_ROOT set BUILD_TOOLS_ROOT=..\..\tools\mswindows

set JDK_HOME=%BUILD_TOOLS_ROOT%\jdk1.6.0
set ANT_HOME=%BUILD_TOOLS_ROOT%\ant1.8.2

set CLASSPATH=%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\ant-nodeps.jar;%ANT_HOME%\lib\ant-junit.jar;%ANT_HOME%\lib\junit-4.4.jar;%ANT_HOME%\lib\ant-apache-bcel.jar;%JDK_HOME%\lib\tools.jar;%ANT_HOME%\lib\ant-launcher.jar;%ANT_HOME%\lib\ant-junit.jar

%JDK_HOME%\bin\java -Xmx128m -Dant.home=%ANT_HOME% -cp %CLASSPATH% org.apache.tools.ant.Main -buildfile build.xml %*

endlocal
