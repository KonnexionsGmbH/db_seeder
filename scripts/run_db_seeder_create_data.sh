#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_create_data.sh: Creation of dummy data in an empty database.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Creation of dummy data in an empty database."
echo "--------------------------------------------------------------------------------"
echo "DBMS                              : $DB_SEEDER_DBMS"
echo "DBMS_EMBEDDED                     : $DB_SEEDER_DBMS_EMBEDDED"
echo --------------------------------------------------------------------------------
echo "FILE_CONFIGURATION_NAME           : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "JAVA_CLASSPATH                    : $DB_SEEDER_JAVA_CLASSPATH"
echo --------------------------------------------------------------------------------
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

rm -f db_seeder.log

if ! (java --enable-preview -cp $DB_SEEDER_JAVA_CLASSPATH ch.konnexions.db_seeder.DatabaseSeeder $DB_SEEDER_DBMS); then
    exit 255
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
