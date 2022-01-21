#!/bin/bash

# Copies data from GRA microservice and stores in MDSAL in ODL

TMPNAME=gra-mdsal-$$

if [ $# -eq 2 ]
then
    fetchFile=false
    GRA_EXPORT_FILE=$1
    MDSAL_URL=$2
elif [ $# -eq 3 ]
then
    fetchFile=true
    GRA_EXPORT_FILE=$TMPNAME.tar.gz
    GRA_NAMESPACE=$1
    GRA_GRA_POD=$2
    MDSAL_URL=$3
else
    echo "Usage: $0 {export-file-name | nft-namespace gra-pod-name} eom-url"
    exit 1
fi


# Check credentials configuration
if [ -z "$GRA_USER" ]
then
    echo "Error: GRA_USER must be set/exported"
    exit 1
fi

if [ -z "$GRA_PASSWORD" ]
then
    echo "Error: GRA_PASSWORD must be set/exported"
    exit 1
fi

if [ -z "$MDSAL_USER" ]
then
    echo "Error: MDSAL_USER must be set/exported"
    exit 1
fi

if [ -z "$MDSAL_PASSWORD" ]
then
    echo "Error: MDSAL_PASSWORD must be set/exported"
    exit 1
fi

set -e

TMPNAME=gra-mdsal-$$
mkdir /tmp/$TMPNAME

if [ "$fetchFile" = "true" ]
then
    # Export data on Azure
    kubectl -n $GRA_NAMESPACE exec -ti $GRA_GRA_POD -c gra -- env ODL_USER=$GRA_USER AAF_MECHID_CRED=$GRA_PASSWORD /opt/sdnc/gra/bin/exportGraDaexim.sh /tmp/$GRA_EXPORT_FILE


    # Put exported data to NFT EOM
    kubectl -n $GRA_NAMESPACE cp $GRA_GRA_POD:/tmp/$GRA_EXPORT_FILE /tmp/$TMPNAME/$GRA_EXPORT_FILE
else
    cp $GRA_EXPORT_FILE /tmp/$TMPNAME
fi

cd /tmp/$TMPNAME
tar xzf $GRA_EXPORT_FILE

# Massage files and create daexim input
echo "{" > lsc_backup_config_$TMPNAME.json
addComma=false
if [ -f services_config.json ]
then
    echo "Converting service data ..."
    cat services_config.json | python3 -mjson.tool | sed -e "s/services/GENERIC-RESOURCE-API:services/" -e "/\"gateway-address\": \"\"/d"  > services_config_upd.json
    cat services_config_upd.json | sed -e "1s/{//" -e "$ s/}//" >> lsc_backup_config_$TMPNAME.json
    addComma=true
fi

if [ -f contrail_config.json ]
then
    if [ "$addComma" = "true" ]
    then
        echo "," >> lsc_backup_config_$TMPNAME.json
    fi
    cat contrail_config.json | sed -e "s/{//" -e "s/contrail-route-allotted-resources/GENERIC-RESOURCE-API:contrail-route-allotted-resources/" -e "s/}$//" >> lsc_backup_config_$TMPNAME.json
    addComma=true
fi

if [ -f portmirror_config.json ]
then
    if [ "$addComma" = "true" ]
    then
        echo "," >> lsc_backup_config_$TMPNAME.json
    fi
    cat portmirror_config.json | sed -e "s/{//" -e "s/port-mirror-configurations/GENERIC-RESOURCE-API:port-mirror-configurations/" -e "s/}$//" >> lsc_backup_config_$TMPNAME.json
    addComma=true
fi

echo "}" >> lsc_backup_config_$TMPNAME.json

echo "Exported data files are in /tmp/$TMPNAME"

if [ -f services_config_upd.json ]
then
    echo "Importing service data ..."
    curl -k -v -u${MDSAL_USER}:${MDSAL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@services_config_upd.json ${MDSAL_URL}/restconf/config/GENERIC-RESOURCE-API:services/
fi

if [ -f contrail_config.json ]
then
    echo "Importing contrail data ..."
    curl -k -v -u${MDSAL_USER}:${MDSAL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@contrail_config.json ${MDSAL_URL}/restconf/config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/
fi

if [ -f portmirror_config.json ]
then
    echo "Importing port mirror data ..."
    curl -k -v -u${MDSAL_USER}:${MDSAL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@portmirror_config.json ${MDSAL_URL}/restconf/config/GENERIC-RESOURCE-API:port-mirror-configurations/
fi


