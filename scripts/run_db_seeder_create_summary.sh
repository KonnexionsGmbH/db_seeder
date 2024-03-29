#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_create_summary.sh: Creation of the benchmark data summary file.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT=src/main/resources/db_seeder.properties
if [ -z "${DB_SEEDER_FILE_CONFIGURATION_NAME}" ]; then
    export DB_SEEDER_FILE_CONFIGURATION_NAME=${DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT}
fi

export DB_SEEDER_FILE_SUMMARY_NAME=resources/statistics/db_seeder_summary_3.0.3-9.9.9.tsv

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Creation of the benchmark data summary file."
echo "--------------------------------------------------------------------------------"
echo "FILE_CONFIGURATION_NAME        : ${DB_SEEDER_FILE_CONFIGURATION_NAME}"
echo "FILE_SUMMARY_NAME              : ${DB_SEEDER_FILE_SUMMARY_NAME}"
echo "FILE_SUMMARY_SOURCE            : ${DB_SEEDER_FILE_SUMMARY_SOURCE}"
echo "JAVA_CLASSPATH                 : ${DB_SEEDER_JAVA_CLASSPATH}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! (gradle copyJarToLib); then
    exit 255
fi

rm -f db_seeder.log

if ! (java -cp "{${DB_SEEDER_JAVA_CLASSPATH}}" ch.konnexions.db_seeder.CreateSummaryFile); then
    exit 255
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
