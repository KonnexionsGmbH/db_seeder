#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_compute_improvement.sh: Compute runtime improvement with and 
#                                       without constraints. 
#
# ------------------------------------------------------------------------------

export DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT=src/main/resources/db_seeder.properties
if [ -z "${DB_SEEDER_FILE_CONFIGURATION_NAME}" ]; then
    export DB_SEEDER_FILE_CONFIGURATION_NAME=${DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT}
fi

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

if [ -z "${HOME_ECLIPSE}" ]; then
    export HOME_ECLIPSE=/opt/eclipse
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Compute runtime improvement with and without constraints."
echo "--------------------------------------------------------------------------------"
echo "Filename via parameter    : $1"
echo "JAVA_CLASSPATH            : ${DB_SEEDER_JAVA_CLASSPATH}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! (java -cp "{${DB_SEEDER_JAVA_CLASSPATH}}" ch.konnexions.db_seeder.ComputeImprovement "$1"); then
    exit 255
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
