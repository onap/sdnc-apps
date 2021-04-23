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

export SDNC_HOME=${SDNC_HOME:-/opt/onap/sdnc}
export GRA_JAR=${GRA_JAR:-@sdnc.gra.jar@}
export SVCLOGIC_DIR=${SVCLOGIC_DIR:-opt/onap/sdnc/svclogic/graphs}
export LOG_PATH=${LOG_PATH:-/var/log/onap/sdnc}
export SDNC_CONFIG_DIR=${SDNC_CONFIG_DIR:-/opt/onap/sdnc/config}
export MYSQL_DB_HOST=${MYSQL_DB_HOST:-dbhost}
export SVCLOGIC_PROPERTIES=${SVCLOGIC_PROPERTIES:-${SDNC_CONFIG_DIR}/svclogic.properties}

cd $SDNC_HOME

  echo "Waiting for mysql"
  until mysql -h ${MYSQL_DB_HOST} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} &> /dev/null
  do
    printf "."
    sleep 1
  done
  echo -e "\nmysql ready"
  
java -DserviceLogicDirectory=${SVCLOGIC_DIR} -DLOG_PATH=${LOG_PATH} -jar ${SDNC_HOME}/lib/${GRA_JAR}
