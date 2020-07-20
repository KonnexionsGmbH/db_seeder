#!/bin/bash

export -e

exec &> >(tee -i run_db_seeder_generate_schema.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_generate_schema.bat: Generation of database schema. 
#
# ------------------------------------------------------------------------------

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties
unset -f DB_SEEDER_FILE_JSON_NAME=
export DB_SEEDER_RELEASE=1.15.11

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Generation of database schema."
echo "--------------------------------------------------------------------------------"
echo "FILE_CONFIGURATION_NAME : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "FILE_JSON_NAME          : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "RELEASE                 : $DB_SEEDER_RELEASE"
echo --------------------------------------------------------------------------------
echo "JAVA_CLASSPATH                    : $DB_SEEDER_JAVA_CLASSPATH"
echo --------------------------------------------------------------------------------
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! (java --enable-preview -cp $DB_SEEDER_JAVA_CLASSPATH ch.konnexions.db_seeder.SchemaGenerator $DB_SEEDER_RELEASE); then
    exit 255
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
