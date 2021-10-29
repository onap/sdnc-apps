#!/bin/bash

ODL_USER=${ODL_USER:-m27434@dev.sdncp.att.com}
ODL_PASSWORD=${AAF_MECHID_CRED:-admin}
ODL_URI=${ODL_URI:-https://localhost:8443}

export TMPDIR=/tmp/daexim-$$

if [ $# -ne 1 ]
then
    echo "Usage: $0 filename"
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

