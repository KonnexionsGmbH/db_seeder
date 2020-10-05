#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete_presto.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete_presto.sh: Run all Presto DBMS variations.
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

export DB_SEEDER_DBMS_SQLSERVER=yes
export DB_SEEDER_DBMS_MYSQL_PRESTO=yes
export DB_SEEDER_DBMS_ORACLE_PRESTO=yes
export DB_SEEDER_DBMS_POSTGRESQL=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ "${TRAVIS}" = "true" ]; then
    export DB_SEEDER_FILE_STATISTICS_NAME=${DB_SEEDER_FILE_STATISTICS_NAME}_${DB_SEEDER_RELEASE}.tsv
else
    if [ -z "${DB_SEEDER_FILE_STATISTICS_NAME}" ]; then
            export DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_bash_presto_unknown_${DB_SEEDER_RELEASE}.tsv
    fi
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
echo "TRAVIS                          : ${TRAVIS}"
echo "--------------------------------------------------------------------------------"
echo "DBMS_MYSQL_PRESTO               : $DB_SEEDER_DBMS_MYSQL_PRESTO"
echo "DBMS_ORACLE_PRESTO              : $DB_SEEDER_DBMS_ORACLE_PRESTO"
echo "DBMS_POSTGRESQL                 : $DB_SEEDER_DBMS_POSTGRESQL"
echo "DBMS_SQLSERVER                  : $DB_SEEDER_DBMS_SQLSERVER"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! ( ./scripts/run_db_seeder_generate_schema.sh ); then
    exit 255
fi    

if ! ( ./scripts/run_db_seeder_presto_environment.sh complete ); then
    exit 255
fi    

if ! ( ./scripts/run_db_seeder_setup_presto.sh ); then
    exit 255
fi    

unset -f "${DB_SEEDER_DBMS}"=

# ------------------------------------------------------------------------------
# MySQL Database - via Presto.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MYSQL_PRESTO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mysql_presto yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Oracle Database - via Presto.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_ORACLE_PRESTO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh oracle_presto yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# PostgreSQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_POSTGRESQL" = "yes" ]; then
    if ! ( ./run_db_seeder.sh postgresql yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_SQLSERVER" = "yes" ]; then
    if ! ( ./run_db_seeder.sh sqlserver yes $DB_SEEDER_NO_CREATE_RUNS ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Upload Statistics.
# ------------------------------------------------------------------------------

if [ "${TRAVIS}" = "true" ]; then
    if ! ( ./scripts/run_travis_push_to_github.sh ); then
        exit 255
    fi    
fi  

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
