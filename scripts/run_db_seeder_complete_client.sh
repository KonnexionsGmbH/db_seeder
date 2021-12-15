#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete_client.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete_client.sh: Run all client DBMS variations.
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

export DB_SEEDER_DBMS_AGENS=yes
export DB_SEEDER_DBMS_COCKROACH=yes
export DB_SEEDER_DBMS_CRATEDB=yes
export DB_SEEDER_DBMS_CUBRID=yes
export DB_SEEDER_DBMS_DERBY=yes
export DB_SEEDER_DBMS_EXASOL=yes
export DB_SEEDER_DBMS_FIREBIRD=yes
export DB_SEEDER_DBMS_H2=yes
export DB_SEEDER_DBMS_HSQLDB=yes
export DB_SEEDER_DBMS_IBMDB2=yes
export DB_SEEDER_DBMS_INFORMIX=yes
export DB_SEEDER_DBMS_MARIADB=yes
export DB_SEEDER_DBMS_MIMER=yes
export DB_SEEDER_DBMS_MONETDB=yes
export DB_SEEDER_DBMS_MYSQL=yes
export DB_SEEDER_DBMS_OMNISCI=yes
export DB_SEEDER_DBMS_ORACLE=yes
export DB_SEEDER_DBMS_PERCONA=yes
export DB_SEEDER_DBMS_POSTGRESQL=yes
export DB_SEEDER_DBMS_SQLSERVER=yes
export DB_SEEDER_DBMS_VOLTDB=yes
export DB_SEEDER_DBMS_YUGABYTE=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ -z "${DB_SEEDER_FILE_STATISTICS_NAME}" ]; then
        export DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_bash_client_unknown_${DB_SEEDER_RELEASE}.tsv
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
echo "--------------------------------------------------------------------------------"
echo "DBMS_AGENS                      : ${DB_SEEDER_DBMS_AGENS}"
echo "DBMS_COCKROACH                  : ${DB_SEEDER_DBMS_COCKROACH}"
echo "DBMS_CRATEDB                    : ${DB_SEEDER_DBMS_CRATEDB}"
echo "DBMS_CUBRID                     : ${DB_SEEDER_DBMS_CUBRID}"
echo "DBMS_DERBY                      : ${DB_SEEDER_DBMS_DERBY}"
echo "DBMS_EXASOL                     : ${DB_SEEDER_DBMS_EXASOL}"
echo "DBMS_FIREBIRD                   : ${DB_SEEDER_DBMS_FIREBIRD}"
echo "DBMS_H2                         : ${DB_SEEDER_DBMS_H2}"
echo "DBMS_HSQLDB                     : ${DB_SEEDER_DBMS_HSQLDB}"
echo "DBMS_IBMDB2                     : ${DB_SEEDER_DBMS_IBMDB2}"
echo "DBMS_INFORMIX                   : ${DB_SEEDER_DBMS_INFORMIX}"
echo "DBMS_MARIADB                    : ${DB_SEEDER_DBMS_MARIADB}"
echo "DBMS_MIMER                      : ${DB_SEEDER_DBMS_MIMER}"
echo "DBMS_MONETDB                    : ${DB_SEEDER_DBMS_MONETDB}"
echo "DBMS_MYSQL                      : ${DB_SEEDER_DBMS_MYSQL}"
echo "DBMS_OMNISCI                    : ${DB_SEEDER_DBMS_OMNISCI}"
echo "DBMS_ORACLE                     : ${DB_SEEDER_DBMS_ORACLE}"
echo "DBMS_PERCONA                    : ${DB_SEEDER_DBMS_PERCONA}"
echo "DBMS_POSTGRESQL                 : ${DB_SEEDER_DBMS_POSTGRESQL}"
echo "DBMS_SQLSERVER                  : ${DB_SEEDER_DBMS_SQLSERVER}"
echo "DBMS_VOLTDB                     : ${DB_SEEDER_DBMS_VOLTDB}"
echo "DBMS_YUGABYTE                   : ${DB_SEEDER_DBMS_YUGABYTE}"
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
# AgensGraph.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_AGENS}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh agens yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# CockroachDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_COCKROACH}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh cockroach yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi
fi

# ------------------------------------------------------------------------------
# CrateDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_CRATEDB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh cratedb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# CUBRID.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_CUBRID}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh cubrid yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Apache Derby - client version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DERBY}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh derby yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Exasol - client version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_EXASOL}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh exasol yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Firebird - client version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_FIREBIRD}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh firebird yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - client version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_H2}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh h2 yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# HSQLDB - client version.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_HSQLDB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh hsqldb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# IBM Db2 Database.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_IBMDB2}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh ibmdb2 yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# IBM Informix.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_INFORMIX}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh informix yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# MariaDB Server.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_MARIADB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mariadb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Mimer SQL.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_MIMER}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mimer yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# MonetDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_MONETDB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh monetdb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# MySQL Database.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_MYSQL}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mysql yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# OmniSciDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_OMNISCI}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh omnisci yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_ORACLE}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh oracle yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Percona Server for MySQL.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_PERCONA}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh percona yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# PostgreSQL.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_POSTGRESQL}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh postgresql yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# SQL Server.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_SQLSERVER}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh sqlserver yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# TimescaleDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_TIMESCALE}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh timescale yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# VoltDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_VOLTDB}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh voltdb yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# YugabyteDB.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_YUGABYTE}" = "yes" ]; then
    if ! ( ./run_db_seeder.sh yugabyte yes "${DB_SEEDER_NO_CREATE_RUNS}" ); then
        exit 255
    fi    
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
