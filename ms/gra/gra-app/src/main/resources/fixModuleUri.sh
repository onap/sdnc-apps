#!/bin/sh

PYTHON=`which python`
if [ "$PYTHON" = "" ]
then
    PYTHON=`which python3`
fi
if [ "$PYTHON" = "" ]
then
    PYTHONPP=cat
else
    PYTHONPP="$PYTHON -mjson.tool"
fi

mv $2 $2.backup
cat $2.backup | ${PYTHONPP} | sed -e 's/'$1':/_&/1' -e 's/\([^_]\)'$1':/\//g' -e 's/_\('$1':\)/\1/' > $2
rm $2.backup

