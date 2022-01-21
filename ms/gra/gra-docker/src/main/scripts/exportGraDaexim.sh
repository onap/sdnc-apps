#!/bin/bash

ODL_URI=${ODL_URI:-https://localhost:8443}

export TMPDIR=/tmp/daexim-$$

if [ $# -ne 1 ]
then
    echo "Usage: $0 filename"
    exit 1
fi

if [ -z "$ODL_USER" ]
then
  echo "Error: ODL_USER must be set/exported"
  exit 1
fi

if [ -z "$ODL_PASSWORD" ]
then
  echo "Error: ODL_PASSWORD must be set/exported"
  exit 1
fi

set -e

mkdir $TMPDIR

if [[ $1 == /* ]]
then
    tarfile=$1
else
    tarfile=$TMPDIR/$1
fi

echo "Exporting services ..."
curl -k -u${ODL_USER}:${ODL_PASSWORD} ${ODL_URI}/restconf/config/GENERIC-RESOURCE-API:services/ > ${TMPDIR}/services_config.json
echo "Exporting contrail data ..."
curl -k -u${ODL_USER}:${ODL_PASSWORD} ${ODL_URI}/restconf/config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/ > ${TMPDIR}/contrail_config.json
echo "Exporting port mirror data ..."
curl -k -u${ODL_USER}:${ODL_PASSWORD} ${ODL_URI}/restconf/config/GENERIC-RESOURCE-API:port-mirror-configurations/ > ${TMPDIR}/portmirror_config.json
echo "Exporting preload data ..."
curl -k -u${ODL_USER}:${ODL_PASSWORD} ${ODL_URI}/restconf/config/GENERIC-RESOURCE-API:preload-information/ > ${TMPDIR}/preload_config.json

cd $TMPDIR
tar czf $tarfile ./*.json

echo Exported data is in $tarfile

