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
#java -Xms128m -Xmx512m -Duyun-biltdog -Dlogback.configurationFile=./conf/logback.xml -jar ./jarlib/jerseymain-0.0.1-SNAPSHOT.jar

module="all"
logDir="logs"

if [ "$foreground" == "true" ];
then 
    #前台转后台1.CTRL-z  2.bg %1  3.jobs 4.disown -h %1
	$JAVA $JAVA_OPTS -Dlog.dir=$logDir -Djersey-$module -Dlogback.configurationFile=$LOGBACK_CONFIGFILE \
	-Dbase.dir=$BASE_DIR -cp $CLASS_PATH $MAIN_CLASS >/dev/null 2>&1
else


BOLTDOGPIDFILE="temp/server_$module.pid"
if [ -f "$BOLTDOGPIDFILE" ]; then
  if kill -0 `cat "$BOLTDOGPIDFILE"` > /dev/null 2>&1; then
	 echo already running as process `cat "$BOLTDOGPIDFILE"`. 
	 exit 0
  fi
fi


#start jvm

nohup $JAVA $JAVA_OPTS -Dlog.dir=$logDir -Djersey-$module -Dlogback.configurationFile=$LOGBACK_CONFIGFILE \
-Dbase.dir=$BASE_DIR -cp $CLASS_PATH $MAIN_CLASS >/dev/null 2>&1 &

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

fi