#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_single.sh: Run a single DBMS variation.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DB=${DB_SEEDER_DBMS}

export DB_SEEDER_DBMS_EMBEDDED=no

if [ "${DB_SEEDER_DBMS}" = "derby_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "${DB_SEEDER_DBMS}" = "h2_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "${DB_SEEDER_DBMS}" = "hsqldb_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "${DB_SEEDER_DBMS}" = "sqlite" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

export DB_SEEDER_DBMS_PRESTO=no

if [ "${DB_SEEDER_DBMS}" = "mysql_presto" ]; then
    export DB_SEEDER_DBMS_DB=mysql
    export DB_SEEDER_DBMS_PRESTO=yes
fi

if [ "${DB_SEEDER_DBMS}" = "oracle_presto" ]; then
    export DB_SEEDER_DBMS_DB=oracle
    export DB_SEEDER_DBMS_PRESTO=yes
fi

if [ "${DB_SEEDER_DBMS}" = "postgresql_presto" ]; then
    export DB_SEEDER_DBMS_DB=postgresql
    export DB_SEEDER_DBMS_PRESTO=yes
fi

if [ "${DB_SEEDER_DBMS}" = "sqlserver_presto" ]; then
    export DB_SEEDER_DBMS_DB=sqlserver
    export DB_SEEDER_DBMS_PRESTO=yes
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Run a single DBMS variation."
echo "--------------------------------------------------------------------------------"
echo "COMPLETE_RUN                      : ${DB_SEEDER_COMPLETE_RUN}"
echo "DBMS                              : ${DB_SEEDER_DBMS}"
echo "DBMS_DB                           : ${DB_SEEDER_DBMS_DB}"
echo "DBMS_DEFAULT                      : ${DB_SEEDER_DBMS_DEFAULT}"
echo "DBMS_EMBEDDED                     : ${DB_SEEDER_DBMS_EMBEDDED}"
echo "DBMS_PRESTO                       : ${DB_SEEDER_DBMS_PRESTO}"
echo "DIRECTORY_CATALOG_PROPERTY        : ${DB_SEEDER_DIRECTORY_CATALOG_PROPERTY}"
echo "FILE_CONFIGURATION_NAME           : ${DB_SEEDER_FILE_CONFIGURATION_NAME}"
echo "FILE_JSON_NAME                    : ${DB_SEEDER_FILE_JSON_NAME}"
echo "FILE_STATISTICS_DELIMITER         : ${DB_SEEDER_FILE_STATISTICS_DELIMITER}"
echo "FILE_STATISTICS_HEADER            : ${DB_SEEDER_FILE_STATISTICS_HEADER}"
echo "FILE_STATISTICS_NAME              : ${DB_SEEDER_FILE_STATISTICS_NAME}"
echo "JAVA_CLASSPATH                    : ${DB_SEEDER_JAVA_CLASSPATH}"
echo "NO_CREATE_RUNS                    : ${DB_SEEDER_NO_CREATE_RUNS}"
echo "RELEASE                           : ${DB_SEEDER_RELEASE}"
echo "SETUP_DBMS                        : ${DB_SEEDER_SETUP_DBMS}"
echo "TRAVIS                            : ${TRAVIS}"
echo "VERSION_PRESTO                    : ${DB_SEEDER_VERSION_PRESTO}"
echo --------------------------------------------------------------------------------
echo "CONNECTION_HOST_PRESTO            : $DB_SEEDER_CONNECTION_HOST_PRESTO"
echo "CONNECTION_PORT_PRESTO            : $DB_SEEDER_CONNECTION_PORT_PRESTO"
echo "--------------------------------------------------------------------------------"
echo "CATALOG                           : $DB_SEEDER_CATALOG"
echo "CATALOG_SYS                       : $DB_SEEDER_CATALOG_SYS"
echo "CONNECTION_HOST                   : $DB_SEEDER_CONNECTION_HOST"
echo "CONNECTION_PORT                   : $DB_SEEDER_CONNECTION_PORT"
echo "CONNECTION_PREFIX                 : $DB_SEEDER_CONNECTION_PREFIX"
echo "CONNECTION_SERVICE                : $DB_SEEDER_CONNECTION_SERVICE"
echo "CONNECTION_SUFFIX                 : $DB_SEEDER_CONNECTION_SUFFIX"
echo "CONTAINER_PORT                    : $DB_SEEDER_CONTAINER_PORT"
echo "DATABASE                          : ${DB_SEEDER_DATABASE}"
echo "DATABASE_SYS                      : $DB_SEEDER_DATABASE_SYS"
echo "PASSWORD                          : $DB_SEEDER_PASSWORD"
echo "PASSWORD_SYS                      : $DB_SEEDER_PASSWORD_SYS"
echo "SCHEMA                            : $DB_SEEDER_SCHEMA"
echo "USER                              : $DB_SEEDER_USER"
echo "USER_SYS                          : $DB_SEEDER_USER_SYS"
echo "VERSION                           : ${DB_SEEDER_VERSION}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if [ "${DB_SEEDER_SETUP_DBMS}" = "yes" ]; then
    if ! ( ./scripts/run_db_seeder_setup_dbms.sh ); then
        exit 255
    fi    
fi

if [ "${DB_SEEDER_NO_CREATE_RUNS}" = "1" ]; then
    if ! ( ./scripts/run_db_seeder_create_data.sh ); then
        exit 255
    fi    
fi

if [ "${DB_SEEDER_NO_CREATE_RUNS}" = "2" ]; then
    if ! ( ./scripts/run_db_seeder_create_data.sh ); then
        exit 255
    fi    
    if ! ( ./scripts/run_db_seeder_create_data.sh ); then
        exit 255
    fi    
fi

if [ "${DB_SEEDER_DBMS}" = "ibmdb2" ]; then
    rm -rf $DB_SEEDER_DATABASE 2>/dev/null
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
