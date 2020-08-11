#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_generate_schema.sh: Generation of database schema. 
#
# ------------------------------------------------------------------------------

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties

# export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.syntax.json

export DB_SEEDER_RELEASE=2.1.0

export DB_SEEDER_IS_ECLIPSE_INSTALLED=true
export DB_SEEDER_ECLIPSE_VERSION_1=2020-06
export DB_SEEDER_ECLIPSE_VERSION_2=R

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Generation of database schema."
echo "--------------------------------------------------------------------------------"
echo "FILE_CONFIGURATION_NAME   : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "FILE_JSON_NAME            : $DB_SEEDER_FILE_JSON_NAME"
echo "RELEASE                   : $DB_SEEDER_RELEASE"
echo "--------------------------------------------------------------------------------"
echo "IS_ECLIPSE_INSTALLED      : $DB_SEEDER_IS_ECLIPSE_INSTALLED"
echo "DB_SEEDER_ECLIPSE_VERSION : ${DB_SEEDER_ECLIPSE_VERSION_1} ${DB_SEEDER_ECLIPSE_VERSION_2}"
echo "HOME_ECLIPSE              : $HOME_ECLIPSE"
echo "--------------------------------------------------------------------------------"
echo "JAVA_CLASSPATH            : $DB_SEEDER_JAVA_CLASSPATH"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! [ -d "/eclipse" ]; then 
    sudo wget --quiet https://www.mirrorservice.org/sites/download.eclipse.org/eclipseMirror/technology/epp/downloads/release/${DB_SEEDER_ECLIPSE_VERSION_1}/${DB_SEEDER_ECLIPSE_VERSION_2}/eclipse-java-${DB_SEEDER_ECLIPSE_VERSION_1}-${DB_SEEDER_ECLIPSE_VERSION_2}-linux-gtk-x86_64.tar.gz
    sudo tar -xf eclipse-java-*-linux-gtk-x86_64.tar.gz
    sudo mv eclipse /
    sudo rm eclipse-java-*-linux-gtk-x86_64.tar.gz
    export HOME_ECLIPSE=/eclipse
fi

if ! (java --enable-preview -cp "{$DB_SEEDER_JAVA_CLASSPATH}" ch.konnexions.db_seeder.SchemaBuilder "${DB_SEEDER_RELEASE}"); then
    exit 255
fi    

if [ -d "eclipse_workspace" ]; then 
    rm -Rf eclipse_workspace
fi

mkdir eclipse_workspace

if ! ($HOME_ECLIPSE/eclipse -nosplash -data eclipse_workspace -application org.eclipse.jdt.core.JavaCodeFormatter -config src/main/resources/org.eclipse.jdt.core.prefs -quiet src/main/java/ch/konnexions/db_seeder/generated/ -vmargs -Dfile.encoding=UTF-8); then
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
