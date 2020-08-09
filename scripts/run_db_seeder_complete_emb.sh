#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete.sh: Run all DBMS variations.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DERBY_EMB=yes
export DB_SEEDER_DBMS_H2_EMB=yes
export DB_SEEDER_DBMS_HSQLDB_EMB=yes
export DB_SEEDER_DBMS_SQLITE=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ -z "$DB_SEEDER_IS_TRAVIS" ]; then
    export DB_SEEDER_IS_TRAVIS=no
fi

if [ "$DB_SEEDER_IS_TRAVIS" = "yes" ]; then
    export DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_travis_emb_${DB_SEEDER_RELEASE}.tsv
else
    export DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_local_bash_emb.tsv
fi  

rm -f $DB_SEEDER_FILE_STATISTICS_NAME

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Run all DBMS variations."
echo "--------------------------------------------------------------------------------"
echo "DBMS_DERBY_EMB                  : $DB_SEEDER_DBMS_DERBY_EMB"
echo "DBMS_H2_EMB                     : $DB_SEEDER_DBMS_H2_EMB"
echo "DBMS_HSQLDB_EMB                 : $DB_SEEDER_DBMS_HSQLDB_EMB"
echo "DBMS_SQLITE                     : $DB_SEEDER_DBMS_SQLITE"
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

unset -f "${DB_SEEDER_DBMS}"=

# ------------------------------------------------------------------------------
# Apache Derby - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY_EMB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh derby_emb yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_H2_EMB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh h2_emb yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# HyperSQL Database - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_HSQLDB_EMB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh hsqldb_emb yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# SQLite.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_SQLITE" = "yes" ]; then
    if ! ( ./run_db_seeder.sh sqlite yes 2 ); then
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
