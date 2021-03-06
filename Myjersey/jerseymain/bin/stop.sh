#!/bin/bash
BASE_DIR=$(cd `dirname $0`; pwd)
BASE_DIR=`dirname "$BASE_DIR"`
cd $BASE_DIR

module="all"

BOLTDOGPIDFILE="temp/server_$module.pid"

echo -n "Stopping jersey server ... "
if [ ! -f "$BOLTDOGPIDFILE" ]
then
  echo "no jersey server to stop (could not find file $BOLTDOGPIDFILE)"
else
  kill -9 $(cat "$BOLTDOGPIDFILE")
  rm "$BOLTDOGPIDFILE"
  echo STOPPED
fi
exit 0