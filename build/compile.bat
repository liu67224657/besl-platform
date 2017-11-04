@echo off

if "%1"=="" goto usage

set FIVEWH_COM_BUILD_INCLUDE=%1 %2 %3 %4 %5 %6 %7 %8 %9

bld compile

exit /B

:usage

echo.
echo USAGE: compile includes
echo.
echo.
echo        includes should be an ant compatible fileset specification
echo        relative to the view root.
echo.
echo        IMPORTANT NOTE:
echo        when this command executes, all class files that correspond
echo        to the specified java files will be deleted to ensure freshness.
echo        this is done by copying the includes variable while replacing all
echo        instances of ".java" with ".class".  then the files in the
echo        working directory that correspond to this new string are deleted.
echo.
echo        examples:
echo.
echo            compile com/fivewh/game/client/backgammon/BackgammonApplet.java
echo              compiles the BackgammonApplet.java
echo.
echo            compile com/fivewh/besl/*.java
echo              compiles all java files in the com/fivewh/besl directory
echo.
echo            compile com/fivewh/besl/**/*.java
echo              compiles all java files in the directory tree rooted at com/fivewh/besl
echo.
echo            compile **/test/**/*.java
echo              compiles all java files in all directory trees that are rooted at 
echo              any directory named test
echo.
