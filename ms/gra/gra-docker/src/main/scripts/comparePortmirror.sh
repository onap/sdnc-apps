#!/bin/bash

if [ $# -eq 2 ]
then
	verbose=false
	file1=$1
	file2=$2
elif [ $# -eq 3 -a $1 = "-v" ]
then
	verbose=true
	file1=$2
	file2=$3
else
	echo "Usage: $0 [-v] file1 file2"
	exit 1
fi

tmppfx=portmirror$$

formattedfile1=/tmp/$tmppfx-formatted1
formattedfile2=/tmp/$tmppfx-formatted2
idlist1=/tmp/$tmppfx-configuration-ids-1
idlist2=/tmp/$tmppfx-configuration-ids-2


echo "Comparing $file1 to $file2 ...."

cat $file1 | python3 -mjson.tool > $formattedfile1
cat $file2 | python3 -mjson.tool > $formattedfile2

cat $formattedfile1 | grep '"configuration-id":' | cut -d: -f2 | sed -e "s/,$//" -e 's/\"//g' -e '/^[[:space:]]*$/d' | sort -u > $idlist1
cat $formattedfile2 | grep '"configuration-id":' | cut -d: -f2 | sed -e "s/,$//" -e 's/\"//g' -e '/^[[:space:]]*$/d' | sort -u > $idlist2

echo
echo "Diff of configuration ids:"
echo "--------------------------"
diff $idlist1 $idlist2

if [ "$verbose" = "true" ]
then
	echo
	echo "Full diff:"
	echo "----------"
	diff $formattedfile1 $formattedfile2
fi

echo
echo "Configuration counts:"
echo "---------------------"
echo "   $file1 : $(cat $idlist1 | wc -l)"
echo "   $file2 : $(cat $idlist2 | wc -l)"




