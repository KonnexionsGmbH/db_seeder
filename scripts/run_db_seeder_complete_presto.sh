#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete.sh: Run all DBMS variations.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_MSSQLSERVER=yes
export DB_SEEDER_DBMS_MYSQL_PRESTO=yes
export DB_SEEDER_DBMS_ORACLE=yes
export DB_SEEDER_DBMS_POSTGRESQL=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ -z "$DB_SEEDER_IS_TRAVIS" ]; then
    export DB_SEEDER_IS_TRAVIS=no
fi

if [ "$DB_SEEDER_IS_TRAVIS" = "yes" ]; then
    export DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_travis_presto_${DB_SEEDER_RELEASE}.tsv
else
    export DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_local_bash_presto.tsv
fi  

rm -f $DB_SEEDER_FILE_STATISTICS_NAME

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Run all DBMS variations."
echo "--------------------------------------------------------------------------------"
echo "DBMS_MSSQLSERVER                : $DB_SEEDER_DBMS_MSSQLSERVER"
echo "DBMS_MYSQL_PRESTO               : $DB_SEEDER_DBMS_MYSQL_PRESTO"
echo "DBMS_ORACLE                     : $DB_SEEDER_DBMS_ORACLE"
echo "DBMS_POSTGRESQL                 : $DB_SEEDER_DBMS_POSTGRESQL"
echo "--------------------------------------------------------------------------------"
echo "FILE_STATISTICS_NAME            : $DB_SEEDER_FILE_STATISTICS_NAME"
echo "--------------------------------------------------------------------------------"
echo "IS_TRAVIS                       : $DB_SEEDER_IS_TRAVIS"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! ( ./run_db_seeder_generate_schema.sh ); then
    exit 255
fi    

if ! ( ./run_db_seeder_presto_environment.sh ); then
    exit 255
fi    

unset -f "${DB_SEEDER_DBMS}"=

# ------------------------------------------------------------------------------
# Microsoft SQL Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MSSQLSERVER" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mssqlserver yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# MySQL Database - via Presto.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MYSQL_PRESTO" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mysql_presto yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_ORACLE" = "yes" ]; then
    if ! ( ./run_db_seeder.sh oracle yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# PostgreSQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_POSTGRESQL" = "yes" ]; then
    if ! ( ./run_db_seeder.sh postgresql yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Upload Statistics.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_IS_TRAVIS" = "yes" ]; then
    if ! ( ./scripts/run_travis_push_to_github.sh ); then
        exit 255
    fi    
fi  

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
