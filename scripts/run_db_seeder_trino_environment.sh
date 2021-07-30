#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_trino_environment.sh: Generating trino catalog properties files.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DEFAULT=complete

if [ -z "$1" ]; then
    echo "========================================================="
    echo "complete           - All implemented trino enabled DBMSs"
    echo "mysql              - MySQL Database}"
    echo "oracle             - Oracle Database}"
    echo "postgresql         - PostgreSQL"
    echo "sqlserver          - SQL Server"
    echo "---------------------------------------------------------"
    read -p "Enter the desired database management system [default: ${DB_SEEDER_DBMS_DEFAULT}] " DB_SEEDER_DBMS
    export DB_SEEDER_DBMS=${DB_SEEDER_DBMS}

    if [ -z "${DB_SEEDER_DBMS}" ]; then
        export DB_SEEDER_DBMS=${DB_SEEDER_DBMS_DEFAULT}
    fi
else
    export DB_SEEDER_DBMS=$1
fi

if [ "${DB_SEEDER_DBMS}" = "complete" ]; then
    export DB_SEEDER_DBMS="mysql oracle postgresql sqlserver"
fi

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

export DB_SEEDER_DIRECTORY_CATALOG_PROPERTY_BASE=resources/docker/trino
export DB_SEEDER_DIRECTORY_CATALOG_PROPERTY=${DB_SEEDER_DIRECTORY_CATALOG_PROPERTY_BASE}/catalog

export DB_SEEDER_MYSQL_CONNECTION_HOST=db_seeder_db
export DB_SEEDER_MYSQL_CONNECTION_PORT=3306
export DB_SEEDER_MYSQL_CONNECTION_PREFIX="jdbc:mysql://"
export DB_SEEDER_MYSQL_CONNECTION_SUFFIX="?serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true"
export DB_SEEDER_MYSQL_PASSWORD=mysql
export DB_SEEDER_MYSQL_USER=kxn_user

export DB_SEEDER_ORACLE_CONNECTION_HOST=db_seeder_db
export DB_SEEDER_ORACLE_CONNECTION_PORT=1521
export DB_SEEDER_ORACLE_CONNECTION_PREFIX="jdbc:oracle:thin:@//"
export DB_SEEDER_ORACLE_CONNECTION_SERVICE=orclpdb1
export DB_SEEDER_ORACLE_PASSWORD=oracle
export DB_SEEDER_ORACLE_USER=kxn_user

export DB_SEEDER_POSTGRESQL_CONNECTION_HOST=db_seeder_db
export DB_SEEDER_POSTGRESQL_CONNECTION_PORT=5432
export DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX="jdbc:postgresql://"
export DB_SEEDER_POSTGRESQL_DATABASE=kxn_db
export DB_SEEDER_POSTGRESQL_PASSWORD=postgresql
export DB_SEEDER_POSTGRESQL_USER=kxn_user

export DB_SEEDER_SQLSERVER_CONNECTION_HOST=db_seeder_db
export DB_SEEDER_SQLSERVER_CONNECTION_PORT=1433
export DB_SEEDER_SQLSERVER_CONNECTION_PREFIX="jdbc:sqlserver://"
export DB_SEEDER_SQLSERVER_DATABASE=kxn_db
export DB_SEEDER_SQLSERVER_PASSWORD=sqlserver_2019
export DB_SEEDER_SQLSERVER_USER=kxn_user

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Generating trino catalog properties files."
echo "--------------------------------------------------------------------------------"
echo "DBMS_DEFAULT                  : ${DB_SEEDER_DBMS_DEFAULT}"
echo "DB_SEEDER_DBMS                : ${DB_SEEDER_DBMS}"
echo "DIRECTORY_CATALOG_PROPERTY    : ${DB_SEEDER_DIRECTORY_CATALOG_PROPERTY}"
echo "JAVA_CLASSPATH                : ${DB_SEEDER_JAVA_CLASSPATH}"
echo "--------------------------------------------------------------------------------"
echo "CONNECTION_HOST_TRINO         : ${DB_SEEDER_CONNECTION_HOST_TRINO}"
echo "CONNECTION_PORT_TRINO         : ${DB_SEEDER_CONNECTION_PORT_TRINO}"
echo "--------------------------------------------------------------------------------"
echo "MYSQL_CONNECTION_HOST         : ${DB_SEEDER_MYSQL_CONNECTION_HOST}"
echo "MYSQL_CONNECTION_PORT         : ${DB_SEEDER_MYSQL_CONNECTION_PORT}"
echo "MYSQL_CONNECTION_PREFIX       : ${DB_SEEDER_MYSQL_CONNECTION_PREFIX}"
echo "MYSQL_CONNECTION_SUFFIX       : ${DB_SEEDER_MYSQL_CONNECTION_SUFFIX}"
echo "MYSQL_PASSWORD                : ${DB_SEEDER_MYSQL_PASSWORD}"
echo "MYSQL_USER                    : ${DB_SEEDER_MYSQL_USER}"
echo "--------------------------------------------------------------------------------"
echo "ORACLE_CONNECTION_HOST        : ${DB_SEEDER_ORACLE_CONNECTION_HOST}"
echo "ORACLE_CONNECTION_PORT        : ${DB_SEEDER_ORACLE_CONNECTION_PORT}"
echo "ORACLE_CONNECTION_PREFIX      : ${DB_SEEDER_ORACLE_CONNECTION_PREFIX}"
echo "ORACLE_CONNECTION_SERVICE     : ${DB_SEEDER_ORACLE_CONNECTION_SERVICE}"
echo "ORACLE_PASSWORD               : ${DB_SEEDER_ORACLE_PASSWORD}"
echo "ORACLE_USER                   : ${DB_SEEDER_ORACLE_USER}"
echo "--------------------------------------------------------------------------------"
echo "POSTGRESQL_CONNECTION_HOST    : ${DB_SEEDER_POSTGRESQL_CONNECTION_HOST}"
echo "POSTGRESQL_CONNECTION_PORT    : ${DB_SEEDER_POSTGRESQL_CONNECTION_PORT}"
echo "POSTGRESQL_CONNECTION_PREFIX  : ${DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX}"
echo "POSTGRESQL_DATABASE           : ${DB_SEEDER_POSTGRESQL_DATABASE}"
echo "POSTGRESQL_PASSWORD           : ${DB_SEEDER_POSTGRESQL_PASSWORD}"
echo "POSTGRESQL_USER               : ${DB_SEEDER_POSTGRESQL_USER}"
echo "--------------------------------------------------------------------------------"
echo "SQLSERVER_CONNECTION_HOST     : ${DB_SEEDER_SQLSERVER_CONNECTION_HOST}"
echo "SQLSERVER_CONNECTION_PORT     : ${DB_SEEDER_SQLSERVER_CONNECTION_PORT}"
echo "SQLSERVER_CONNECTION_PREFIX   : ${DB_SEEDER_SQLSERVER_CONNECTION_PREFIX}"
echo "SQLSERVER_PASSWORD            : ${DB_SEEDER_SQLSERVER_PASSWORD}"
echo "SQLSERVER_DATABASE            : ${DB_SEEDER_SQLSERVER_DATABASE}"
echo "SQLSERVER_USER                : ${DB_SEEDER_SQLSERVER_USER}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"
echo "Generate and compile catalog properties files."
echo "--------------------------------------------------------------------------------"

rm -f ${DB_SEEDER_DIRECTORY_CATALOG_PROPERTY}/db_seeder_*.properties

if ! (java -cp "{${DB_SEEDER_JAVA_CLASSPATH}}" ch.konnexions.db_seeder.TrinoEnvironment "${DB_SEEDER_DBMS}"); then
    exit 255
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
