#!/bin/csh
#note, please download the tools from the dev-tool repo 

if (! $?BUILD_TOOLS_ROOT) setenv BUILD_TOOLS_ROOT /opt/tools/mac

setenv JAVA_HOME
setenv JDK_HOME $BUILD_TOOLS_ROOT/jdk/Home
setenv ANT_HOME $BUILD_TOOLS_ROOT/ant
setenv J2EE_HOME $BUILD_TOOLS_ROOT/j2ee

# FIXME: Import all the jars of J2EE.
setenv CLASSPATH $ANT_HOME/lib/ant.jar:$ANT_HOME/lib/ant-nodeps.jar:$ANT_HOME/lib/ant-apache-bcel.jar:$ANT_HOME/lib/junit-4.4.jar:$JDK_HOME/lib/tools.jar:$ANT_HOME/lib/ant-launcher.jar:$ANT_HOME/lib/ant-junit.jar:$J2EE_HOME/modules/javax.mail.jar

$JDK_HOME/bin/java -Xmx512m -Dant.home=$ANT_HOME -cp $CLASSPATH org.apache.tools.ant.Main -buildfile $cwd/build.xml $*
