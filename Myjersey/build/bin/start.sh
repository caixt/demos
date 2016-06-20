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

export LANG=zh_CN.UTF-8
#set classpath
CLASS_PATH="$BASE_DIR/module/*:$BASE_DIR/lib/*"
#set logback
LOGBACK_CONFIGFILE="$BASE_DIR/conf/logback.xml"
#set mainclass
MAIN_CLASS=uyun.boltdog.api.rest.Start
#start jvm
JAVA_OPTS="-Xms128m -Xmx1024m"


module="all"
logDir="log"

BOLTDOGPIDFILE="temp/boltdog_$module.pid"
if [ -f "$BOLTDOGPIDFILE" ]; then
  if kill -0 `cat "$BOLTDOGPIDFILE"` > /dev/null 2>&1; then
	 echo already running as process `cat "$BOLTDOGPIDFILE"`. 
	 exit 0
  fi
fi


#start jvm
java $JAVA_OPTS -Dlog.dir=$logDir -Duyun-boltdog-$module -Dlogback.configurationFile=$LOGBACK_CONFIGFILE -Dbase.dir=$BASE_DIR -cp $CLASS_PATH $MAIN_CLASS $module >/dev/null 2>&1 &

if [ $? -eq 0 ]
	then
		if /bin/echo -n $! > "$BOLTDOGPIDFILE"
	then
		sleep 1
		echo STARTED
	else
		echo FAILED TO WRITE PID
		exit 1
	fi
else
  echo SERVER DID NOT START
  exit 1
fi