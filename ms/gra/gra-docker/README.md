# generic-resource-api docker
This creates the internal AT&T docker for the generic resource api microservice.

## Prerequisites for doing local build
Before doing local builds, you'll need to do the following one-time setup steps:

1. Install docker and docker-compose
1. Make sure your id is in the 'docker' user group so that you can run docker commands.
1. Start a local docker registry by running `sudo docker run -d -p 5000:5000 --restart=always --name registry registry:2`
1. Login to dockercentral with your attuid/itservices password by running `docker login dockecentral.it.att.com:5100`

## Creating a local build
To do a local build, run this command:
    mvn clean package -Ddocker.push.registry=localhost:5000 -Ddocker.buildArg.http_proxy=$http_proxy -Ddocker.buildArg.https_proxy=$https_proxy -Ddocker.buildArg.no_proxy=$no_proxy

## Environment variables
The generic-resource-api ms expects the following environment variables to be set:
### MySQL Credentuals
* MYSQL_ROOT_PASSWORD
* MYSQL_USER
* MYSQL_PASSWORD
* MYSQL_DATABASE
### OpenDaylight controller credentials
* ODL_USER
* ODL_PASSWORD
### DMAAP message router credentials
* DMAAP_USER
* DMAAP_PASSWORD
* DMAAP_AUTHKEY
### AAI credentials
* AAI_TRUSTSTORE_PASSWORD
* AAI_CLIENT_NAME
* AAI_CLIENT_PASSWORD
### Ansible credentials
* ANSIBLE_TRUSTSTORE_PASSWORD
* ANSIBLE_USER
* ANSIBLE_PASSWORD
### Honeycomb credentials
* HONEYCOMB_USER
* HONEYCOMB_PASSWORD
### SSL/TLS truststore/keystore credentials
* TRUSTSTORE_PASSWORD
* KEYSTORE_PASSWORD
* CACERT_PASSWORD
* SDNC_KEYSTORE_PASSWORD
### Naming microservice credentials
* NENG_USER
* NENG_PASSWORD
### SO credentials
* SO_USER
* SO_PASSWORD
### CDS credentials
* CDS_USER
* CDS_PASSWORD
### Sql encryption key
export SQL_CRYPTKEY=QtfJMKggVk
### ASDC credentials
* ASDC_USER
* ASDC_PASSWORD
### AAF IP address (only needed by docker-compose.yml - does not need to be set in helm chart)
* AAF_IP_ADDRESS

## Running local copy
To start a local copy of the generic-resource-api:
1. cd to src/main/dc
1. run `docker-compose up -d`
1. To view logs, run `docker-compose logs -f`


