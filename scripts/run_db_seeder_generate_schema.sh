#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_generate_schema.sh: Generation of database schema. 
#
# ------------------------------------------------------------------------------

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties

# export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.syntax.json

export DB_SEEDER_RELEASE=2.9.2
export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

if [ -z "${HOME_ECLIPSE}" ]; then
    export HOME_ECLIPSE=/opt/eclipse
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Generation of database schema."
echo "--------------------------------------------------------------------------------"
echo "FILE_CONFIGURATION_NAME   : ${DB_SEEDER_FILE_CONFIGURATION_NAME}"
echo "FILE_JSON_NAME            : ${DB_SEEDER_FILE_JSON_NAME}"
echo "HOME_ECLIPSE              : ${HOME_ECLIPSE}"
echo "JAVA_CLASSPATH            : ${DB_SEEDER_JAVA_CLASSPATH}"
echo "RELEASE                   : ${DB_SEEDER_RELEASE}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! (java -cp "{${DB_SEEDER_JAVA_CLASSPATH}}" ch.konnexions.db_seeder.SchemaBuilder "${DB_SEEDER_RELEASE}"); then
    exit 255
fi    

if [ -d "eclipse_workspace" ]; then 
    rm -Rf eclipse_workspace
fi

mkdir -p eclipse_workspace

if ! (${HOME_ECLIPSE}/eclipse -nosplash \
                              -data eclipse_workspace \
                              -application org.eclipse.jdt.core.JavaCodeFormatter \
                              -config src/main/resources/org.eclipse.jdt.core.prefs \
                              -quiet src/main/java/ch/konnexions/db_seeder/generated/ \
                              -vmargs -Dfile.encoding=UTF-8); then
    exit 255
fi    

if ! { gradle init; }; then
    exit 255
fi

if ! { gradle clean; }; then
    exit 255
fi

if ! (gradle copyJarToLib); then
    exit 255
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
