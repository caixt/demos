#!/bin/bash
BASE_DIR=$(cd `dirname $0`; pwd)
BASE_DIR=`dirname "$BASE_DIR"`
cd $BASE_DIR

if [ ! -d temp ]; then
	mkdir temp
fi

#jrePath=jre/bin/java
#if [ ! -x "$jrePath" ];
#then 
#	tar zxvf jre-8u*.gz
#	mv jre1.8* jre
#	rm -rf jre-8u*.gz
#fi

export LANG=en_US.UTF-8
#set classpath
CLASS_PATH="$BASE_DIR/module/*:$BASE_DIR/lib/*"
#set logback
LOGBACK_CONFIGFILE="$BASE_DIR/conf/logback.xml"
#set mainclass
MAIN_CLASS="com.github.cxt.Myjersey.main.JettyServer"
#start jvm
if [ -z "$JAVA_OPTS" ]; then
	JAVA_OPTS="-Xms128m -Xmx512m"
fi

if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi


module="all"
logDir="logs"

#suspend=n不等待,y等待
$JAVA $JAVA_OPTS -Dlog.dir=$logDir -Djersey-$module -Dlogback.configurationFile=$LOGBACK_CONFIGFILE \
	-Dbase.dir=$BASE_DIR -Xdebug -Xrunjdwp:transport=dt_socket,address=9000,server=y,suspend=y -cp $CLASS_PATH $MAIN_CLASS >/dev/null 2>&1
 