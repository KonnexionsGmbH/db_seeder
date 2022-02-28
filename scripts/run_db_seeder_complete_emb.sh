#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete_embedded.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete_embedded.sh: Run all embedded DBMS variations.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_COMPLETE_RUN=yes
export DB_SEEDER_NO_CREATE_RUNS_DEFAULT=2

if [ -z "$1" ]; then
    read -p "Number of data creation runs (0-2) [default: ${DB_SEEDER_NO_CREATE_RUNS}_DEFAULT] " DB_SEEDER_NO_CREATE_RUNS
    export DB_SEEDER_NO_CREATE_RUNS=${DB_SEEDER_NO_CREATE_RUNS}

    if [ -z "${DB_SEEDER_NO_CREATE_RUNS}" ]; then
        export DB_SEEDER_NO_CREATE_RUNS=${DB_SEEDER_NO_CREATE_RUNS}_DEFAULT
    fi
else
    export DB_SEEDER_NO_CREATE_RUNS=$1
fi

export DB_SEEDER_DBMS_DERBY_EMB=yes
export DB_SEEDER_DBMS_H2_EMB=yes
export DB_SEEDER_DBMS_HSQLDB_EMB=yes
export DB_SEEDER_DBMS_SQLITE_EMB=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ -z "${DB_SEEDER_FILE_STATISTICS_NAME}" ]; then
        export DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_bash_embedded_unknown_${DB_SEEDER_RELEASE}.tsv
fi

rm -f "${DB_SEEDER_FILE_STATISTICS_NAME}"

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Run all DBMS variations."
echo "--------------------------------------------------------------------------------"
echo "COMPLETE_RUN                    : ${DB_SEEDER_COMPLETE_RUN}"
echo "FILE_STATISTICS_NAME            : ${DB_SEEDER_FILE_STATISTICS_NAME}"
echo "NO_CREATE_RUNS                  : ${DB_SEEDER_NO_CREATE_RUNS}"
echo "TRAVIS                          : ${TRAVIS}"
echo "--------------------------------------------------------------------------------"
echo "DBMS_DERBY_EMB                  : ${DB_SEEDER_DBMS_DERBY_EMB}"
echo "DBMS_H2_EMB                     : ${DB_SEEDER_DBMS_H2_EMB}"
echo "DBMS_HSQLDB_EMB                 : ${DB_SEEDER_DBMS_HSQLDB_EMB}"
echo "DBMS_SQLITE_EMB                 : ${DB_SEEDER_DBMS_SQLITE_EMB}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

unset -f "${DB_SEEDER_DBMS}"=

# ------------------------------------------------------------------------------
# Apache Derby - embedded version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DERBY_EMB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh derby_emb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - embedded version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_H2_EMB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh h2_emb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# HSQLDB - embedded version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_HSQLDB_EMB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh hsqldb_emb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# SQLite.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_SQLITE_EMB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh sqlite yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
