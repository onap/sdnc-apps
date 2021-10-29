#!/bin/bash

# Copies data from NFT Azure GRA node and stores in MDSAL in NFT EOM

TMPNAME=gra-mdsal-$$

if [ $# -eq 2 ]
then
    fetchFile=false
    NFT_AZURE_EXPORT_FILE=$1
    NFT_EOM_URL=$2
elif [ $# -eq 3 ]
then
    fetchFile=true
    NFT_AZURE_EXPORT_FILE=$TMPNAME.tar.gz
    NFT_AZURE_NAMESPACE=$1
    NFT_AZURE_GRA_POD=$2
    NFT_EOM_URL=$3
else
    echo "Usage: $0 {export-file-name | nft-namespace gra-pod-name} eom-url"
    exit 1
fi


# Credentials configuration
NFT_AZURE_USER=${NFT_AZURE_USER:-m27434@dev.sdncp.att.com}
NFT_AZURE_PASSWORD=${NFT_AZURE_PASSWORD:-sdncp20190501}
NFT_EOM_USER=${NFT_EOM_USER:-admin}
NFT_EOM_PASSWORD=${NFT_EOM_PASSWORD:-admin}

set -e

TMPNAME=gra-mdsal-$$
mkdir /tmp/$TMPNAME

if [ "$fetchFile" = "true" ]
then
    # Export data on Azure
    kubectl -n $NFT_AZURE_NAMESPACE exec -ti $NFT_AZURE_GRA_POD -c gra -- env ODL_USER=$NFT_AZURE_USER AAF_MECHID_CRED=$NFT_AZURE_PASSWORD /opt/sdnc/gra/bin/exportGraDaexim.sh /tmp/$NFT_AZURE_EXPORT_FILE


    # Put exported data to NFT EOM
    kubectl -n $NFT_AZURE_NAMESPACE cp $NFT_AZURE_GRA_POD:/tmp/$NFT_AZURE_EXPORT_FILE /tmp/$TMPNAME/$NFT_AZURE_EXPORT_FILE
else
    cp $NFT_AZURE_EXPORT_FILE /tmp/$TMPNAME
fi

cd /tmp/$TMPNAME
tar xzf $NFT_AZURE_EXPORT_FILE

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
    curl -k -v -u${NFT_EOM_USER}:${NFT_EOM_PASSWORD} -H "Content-Type: application/json" -X PUT -d@services_config_upd.json ${NFT_EOM_URL}/restconf/config/GENERIC-RESOURCE-API:services/
fi

if [ -f contrail_config.json ]
then
    echo "Importing contrail data ..."
    curl -k -v -u${NFT_EOM_USER}:${NFT_EOM_PASSWORD} -H "Content-Type: application/json" -X PUT -d@contrail_config.json ${NFT_EOM_URL}/restconf/config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/
fi

if [ -f portmirror_config.json ]
then
    echo "Importing port mirror data ..."
    curl -k -v -u${NFT_EOM_USER}:${NFT_EOM_PASSWORD} -H "Content-Type: application/json" -X PUT -d@portmirror_config.json ${NFT_EOM_URL}/restconf/config/GENERIC-RESOURCE-API:port-mirror-configurations/
fi


