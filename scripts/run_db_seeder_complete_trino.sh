#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete_trino.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete_trino.sh: Run all trino DBMS variations.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_COMPLETE_RUN=yes
export DB_SEEDER_NO_CREATE_RUNS_DEFAULT=2

if [ -z "$1" ]; then
    read -p "Number of data creation runs (0-2) [default: $DB_SEEDER_NO_CREATE_RUNS_DEFAULT] " DB_SEEDER_NO_CREATE_RUNS
    export DB_SEEDER_NO_CREATE_RUNS=${DB_SEEDER_NO_CREATE_RUNS}

    if [ -z "${DB_SEEDER_NO_CREATE_RUNS}" ]; then
        export DB_SEEDER_NO_CREATE_RUNS=$DB_SEEDER_NO_CREATE_RUNS_DEFAULT
    fi
else
    export DB_SEEDER_NO_CREATE_RUNS=$1
fi

export DB_SEEDER_DBMS_MYSQL_TRINO=yes
export DB_SEEDER_DBMS_ORACLE_TRINO=yes
export DB_SEEDER_DBMS_POSTGRESQL_TRINO=yes
export DB_SEEDER_DBMS_SQLSERVER_TRINO=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ -z "${DB_SEEDER_FILE_STATISTICS_NAME}" ]; then
        export DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_bash_trino_unknown_${DB_SEEDER_RELEASE}.tsv
fi

rm -f ${DB_SEEDER_FILE_STATISTICS_NAME}

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Run all DBMS variations."
echo "--------------------------------------------------------------------------------"
echo "COMPLETE_RUN                    : ${DB_SEEDER_COMPLETE_RUN}"
echo "FILE_STATISTICS_NAME            : ${DB_SEEDER_FILE_STATISTICS_NAME}"
echo "NO_CREATE_RUNS                  : ${DB_SEEDER_NO_CREATE_RUNS}"
echo "--------------------------------------------------------------------------------"
echo "DBMS_MYSQL_TRINO                : $DB_SEEDER_DBMS_MYSQL_TRINO"
echo "DBMS_ORACLE_TRINO               : $DB_SEEDER_DBMS_ORACLE_TRINO"
echo "DBMS_POSTGRESQL_TRINO           : $DB_SEEDER_DBMS_POSTGRESQL_TRINO"
echo "DBMS_SQLSERVER_TRINO            : $DB_SEEDER_DBMS_SQLSERVER_TRINO"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! ( ./scripts/run_db_seeder_generate_schema.sh ); then
    exit 255
fi    

if ! ( ./scripts/run_db_seeder_trino_environment.sh complete ); then
    exit 255
fi    

if ! ( ./scripts/run_db_seeder_setup_trino.sh ); then
    exit 255
fi    

unset -f "${DB_SEEDER_DBMS}"=

# ------------------------------------------------------------------------------
# MySQL Database - via trino.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MYSQL_TRINO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mysql_trino yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Oracle Database - via trino.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_ORACLE_TRINO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh oracle_trino yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# PostgreSQL.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_POSTGRESQL_TRINO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh postgresql_trino yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# SQL Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_SQLSERVER_TRINO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh sqlserver_trino yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
