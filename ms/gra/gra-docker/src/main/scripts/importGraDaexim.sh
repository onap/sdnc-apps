#!/bin/bash

export CHUNK_SIZE=${CHUNK_SIZE:-75}
set -e
CURLFLAGS="--silent --show-error"
if [ $# -eq 3 ]
then
    if [ "$1" != "-v" ]
    then
        echo "Usage: $0 [-v] filename controller-url"
        exit 1
    fi
    CURLFLAGS="-v"
    filename=$2
    url=$3
elif [ $# -eq 2 ]
then
    filename=$1
    url=$2
else
    echo "Usage: $0 [-v] filename controller-url"
    exit 1
fi

ODL_USER=${ODL_USER:-admin}
ODL_PASSWORD=${ODL_PASSWORD:-admin}


export TMPDIR=/tmp/daexim-$$
# Unzip file
if [[ "$filename" == *.zip ]]
then
    unzip -qd $TMPDIR $filename
    DAEXIM_CONFIG=$(find $TMPDIR -name *config.json)
elif [[ "$filename" == *config.json ]]
then
    mkdir $TMPDIR
    DAEXIM_CONFIG=$filename
else
    echo "$filename is not a recognized format"
    exit 1
fi



cat ${DAEXIM_CONFIG} | python3 -c '
import sys,json,os

data = json.load(sys.stdin)

chunk_size = int(os.environ["CHUNK_SIZE"])
if chunk_size == "":
    chunk_size = 75

if "GENERIC-RESOURCE-API:services" in data:
    chunk = 0
    lineno = chunk_size
    f = None
    for service in data["GENERIC-RESOURCE-API:services"]["service"]:
        if lineno >= chunk_size:
            chunk = chunk+1
            lineno = 1
            if f != None:
                print ("]}}", file=f)
                f.flush()
                f.close()
            outfile = os.environ["TMPDIR"] + "/config_gra_services_"+str(chunk).zfill(3)+".json"
            f = open(outfile, "w")
            print("{\"services\":", file=f, end="")
            print("{\"service\":[", file=f, end="")
            json.dump(service, f)
        else:
            lineno = lineno+1
            print(",",file=f, end="")
            json.dump(service, f)
    if f != None:
        print("]}}", file=f)
        f.flush()
        f.close()
    print("Split "+str(len(data["GENERIC-RESOURCE-API:services"]["service"]))+ " services into "+str(chunk)+" chunks")

if "GENERIC-RESOURCE-API:contrail-route-allotted-resources" in data:
    chunk = 0
    lineno = chunk_size
    f = None
    for contrail_route in data["GENERIC-RESOURCE-API:contrail-route-allotted-resources"]["contrail-route-allotted-resource"]:
        if lineno >= chunk_size:
            chunk = chunk+1
            lineno = 1
            if f != None:
                print ("]}}", file=f)
                f.flush()
                f.close()
            outfile = os.environ["TMPDIR"] + "/config_gra_contrail_"+str(chunk).zfill(3)+".json"
            f = open(outfile, "w")
            print("{\"contrail-route-allotted-resources\":", file=f, end="")
            print("{\"contrail-route-allotted-resource\":[", file=f, end="")
            json.dump(contrail_route, f)
        else:
            lineno = lineno+1
            print(",",file=f, end="")
            json.dump(contrail_route, f)
    if f != None:
        print("]}}", file=f)
        f.flush()
        f.close()
    print("Split "+ str(len(data["GENERIC-RESOURCE-API:contrail-route-allotted-resources"]["contrail-route-allotted-resource"]))+ " contrail-route-allotted-services into "+str(chunk)+" chunks")

if "GENERIC-RESOURCE-API:port-mirror-configurations" in data:
    chunk = 0
    lineno = chunk_size
    f = None
    for port_mirror in data["GENERIC-RESOURCE-API:port-mirror-configurations"]["port-mirror-configuration"]:
        if lineno >= chunk_size:
            chunk = chunk+1
            lineno = 1
            if f != None:
                print ("]}}", file=f)
                f.flush()
                f.close()
            outfile = os.environ["TMPDIR"] + "/config_gra_port_mirror_"+str(chunk).zfill(3)+".json"
            f = open(outfile, "w")
            print("{\"port-mirror-configurations\":", file=f, end="")
            print("{\"port-mirror-configuration\":[", file=f, end="")
            json.dump(port_mirror, f)
        else:
            lineno = lineno+1
            print(",",file=f, end="")
            json.dump(port_mirror, f)
    if f != None:
        print("]}}", file=f)
        f.flush()
        f.close()
    print("Split "+str(len(data["GENERIC-RESOURCE-API:port-mirror-configurations"]["port-mirror-configuration"]))+" port-mirror-configuration into "+str(chunk)+" chunks")

if "GENERIC-RESOURCE-API:preload-information" in data:
    chunk = 0
    lineno = chunk_size
    f = None
    for preload in data["GENERIC-RESOURCE-API:preload-information"]["preload-list"]:
        if lineno >= chunk_size:
            chunk = chunk+1
            lineno = 0
            if f != None:
                print ("]}}", file=f)
                f.flush()
                f.close()
            outfile = os.environ["TMPDIR"] + "/config_gra_preloads_"+str(chunk).zfill(3)+".json"
            f = open(outfile, "w")
            print("{\"preload-information\":", file=f, end="")
            print("{\"preload-list\":[", file=f, end="")
            json.dump(preload, f)
        else:
            lineno = lineno+1
            print(",",file=f, end="")
            json.dump(preload, f)
    if f != None:
        print("]}}", file=f)
        f.flush()
        f.close()
    print ("Split "+str(len(data["GENERIC-RESOURCE-API:preload-information"]["preload-list"]))+" preloads into "+str(chunk)+" chunks")'

first=true

if [ $(ls -1 $TMPDIR/config_gra_services_*.json 2>/dev/null | wc -l) -gt 0 ]
then
    echo "Transferring services data ..."
    for file in $(ls $TMPDIR/config_gra_services_*.json)
    do
        echo "Transferring file $file ..."
        curl ${CURLFLAGS} --fail -k -u${ODL_USER}:${ODL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@$file ${url}/restconf/config/GENERIC-RESOURCE-API:services/
    done
fi

if [ $(ls -1 $TMPDIR/config_gra_contrail_*.json 2>/dev/null | wc -l) -gt 0 ]
then
    echo "Transferring contrail data ..."
    for file in $(ls $TMPDIR/config_gra_contrail_*.json)
    do
        echo "Transferring file $file ..."
        curl ${CURLFLAGS} --fail -k -u${ODL_USER}:${ODL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@$file ${url}/restconf/config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/
    done
fi

if [ $(ls -1 $TMPDIR/config_gra_port_mirror_*.json 2>/dev/null | wc -l) -gt 0 ]
then
    echo "Transferring port mirror data ..."
    for file in $(ls $TMPDIR/config_gra_port_mirror_*.json)
    do
        echo "Transferring file $file ..."
        curl ${CURLFLAGS} --fail -k -u${ODL_USER}:${ODL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@$file ${url}/restconf/config/GENERIC-RESOURCE-API:port-mirror-configurations/
    done
fi

if [ $(ls -1 $TMPDIR/config_gra_preloads_*.json 2>/dev/null | wc -l) -gt 0 ]
then
    echo "Transferring preload data ..."
    first=true
    for file in $(ls $TMPDIR/config_gra_preloads_*.json)
    do
        echo "Transferring file $file ..."
        curl ${CURLFLAGS} --fail -k -u${ODL_USER}:${ODL_PASSWORD} -H "Content-Type: application/json" -X PUT -d@$file ${url}/restconf/config/GENERIC-RESOURCE-API:preload-information/
    done
fi

rm -rf $TMPDIR
echo "Done!"