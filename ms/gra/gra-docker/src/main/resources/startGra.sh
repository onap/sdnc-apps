#!/bin/bash

###
# ============LICENSE_START=======================================================
# ONAP : SDNC
# ================================================================================
# Copyright (C) 2020 AT&T Intellectual Property. All rights
#                             reserved.
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ============LICENSE_END=========================================================
###

export SDNC_HOME=${SDNC_HOME:-/opt/sdnc/gra}
export SVCLOGIC_DIR=${SVCLOGIC_DIR:-/opt/sdnc/gra/service-logic/graphs}
export LOG_PATH=${LOG_PATH:-/var/log/sdnc}
export SDNC_CONFIG_DIR=${SDNC_CONFIG_DIR:-/opt/sdnc/gra/config}
export SVCLOGIC_PROPERTIES=${SVCLOGIC_PROPERTIES:-${SDNC_CONFIG_DIR}/svclogic.properties}
export SSL_DIR=${SSL_DIR:-/opt/sdnc/data/ssl}
export SECRETS_DIR=${SECRETS_DIR:-/opt/app/secrets}
export K8_NAMESPACE=${K8_NAMESPACE:-default}

if [ -f ${SECRETS_DIR}/${K8_NAMESPACE}-keystore-cred ]
then
    export AAF_KEYSTORE_CRED=$(cat ${SECRETS_DIR}/${K8_NAMESPACE}-keystore-cred)
else
    ls -l ${SECRETS_DIR}
    echo "Not found: ${SECRETS_DIR}/${K8_NAMESPACE}-keystore-cred"
fi

if [ -f ${SECRETS_DIR}/${K8_NAMESPACE}-trustore-cred ]
then
    export AAF_TRUSTORE_CRED=$(cat ${SECRETS_DIR}/${K8_NAMESPACE}-trustore-cred | grep cadi_truststore_password | cut -d '=' -f 2 )
else
    ls -l ${SECRETS_DIR}
    echo "Not found: ${SECRETS_DIR}/${K8_NAMESPACE}-trusture-cred"
fi

if [ -f ${SECRETS_DIR}/${K8_NAMESPACE}-keystore-jks ]
then
    cat ${SECRETS_DIR}/${K8_NAMESPACE}-keystore-jks | base64 -d > ${SSL_DIR}/keystore.jks
    echo "base64 dec ${SSL_DIR}/keystore.jks"
else
    ls -l ${SECRETS_DIR}
    echo "Not found: ${SECRETS_DIR}/${K8_NAMESPACE}-keystore-jks"
fi

env | sort

#
# Wait for database
#
echo "Waiting for database"
until mysqladmin ping -h ${MYSQL_DB_HOST} --user ${MYSQL_USER} --password ${MYSQL_PASSWORD} --silent; do
  printf "."
  sleep 1
done
echo -e "\nDatabase ready"

# Create tablespace and user account

#mysql -h ${MYSQL_DB_HOST} -u root -p${MYSQL_ROOT_PASSWORD} mysql <<-END
#CREATE DATABASE ${MYSQL_DB_DATABASE};
#CREATE USER '${MYSQL_DB_USER}'@'localhost' IDENTIFIED BY '${MYSQL_DB_PASSWD}';
#CREATE USER '${MYSQL_DB_USER}'@'%' IDENTIFIED BY '${MYSQL_DB_PASSWD}';
#GRANT ALL PRIVILEGES ON ${MYSQL_DB_DATABASE}.* TO '${MYSQL_DB_USER}'@'localhost' WITH GRANT OPTION;
#GRANT ALL PRIVILEGES ON ${MYSQL_DB_DATABASE}.* TO '${MYSQL_DB_USER}'@'%' WITH GRANT OPTION;
#commit;
#END

# Initialize schema
if [ -f ${SDNC_CONFIG_DIR}/schema-mysql.sql ]
then 
  mysql -h ${MYSQL_DB_HOST} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} < ${SDNC_CONFIG_DIR}/schema-mysql.sql
fi

# Load data
if [ -f ${SDNC_CONFIG_DIR}/data-mysql.sql ]
then 
  mysql -h ${MYSQL_DB_HOST} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} < ${SDNC_CONFIG_DIR}/data-mysql.sql
fi

cd $SDNC_HOME
java ${JAVA_OPTS} -DserviceLogicDirectory=${SVCLOGIC_DIR} -DLOG_PATH=${LOG_PATH} -Dlog4j2.configurationFile=file:${SDNC_CONFIG_DIR}/log4j2.properties -jar ${SDNC_HOME}/lib/gra-app-*.jar
