#!/bin/csh

if (! $?BUILD_TOOLS_ROOT) setenv BUILD_TOOLS_ROOT /opt/ops/tools/linux

setenv JAVA_HOME
setenv JDK_HOME $BUILD_TOOLS_ROOT/jdk
setenv ANT_HOME $BUILD_TOOLS_ROOT/ant
setenv CLASSPATH $ANT_HOME/lib/ant.jar:$ANT_HOME/lib/ant-nodeps.jar:$ANT_HOME/lib/ant-apache-bcel.jar:$ANT_HOME/lib/junit-4.4.jar:$JDK_HOME/lib/tools.jar:$ANT_HOME/lib/ant-launcher.jar:$ANT_HOME/lib/ant-junit.jar

$JDK_HOME/bin/java -Xmx512m -Dant.home=$ANT_HOME -cp $CLASSPATH org.apache.tools.ant.Main -buildfile $cwd/build.xml $*

