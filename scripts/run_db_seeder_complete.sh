#!/bin/bash

set -e

exec &> >(tee -i run_db_seeder_complete.log)
sleep .1

# ------------------------------------------------------------------------------
#
# run_db_seeder_complete.sh: Run all DBMS variations.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_CRATEDB=yes
export DB_SEEDER_DBMS_CUBRID=yes
export DB_SEEDER_DBMS_DERBY=yes
export DB_SEEDER_DBMS_DERBY_EMB=yes
export DB_SEEDER_DBMS_FIREBIRD=yes
export DB_SEEDER_DBMS_H2=yes
export DB_SEEDER_DBMS_H2_EMB=yes
export DB_SEEDER_DBMS_HSQLDB=yes
export DB_SEEDER_DBMS_HSQLDB_EMB=yes
export DB_SEEDER_DBMS_IBMDB2=yes
export DB_SEEDER_DBMS_INFORMIX=yes
export DB_SEEDER_DBMS_MARIADB=yes
export DB_SEEDER_DBMS_MIMER=yes
export DB_SEEDER_DBMS_MSSQLSERVER=yes
export DB_SEEDER_DBMS_MYSQL=yes
export DB_SEEDER_DBMS_ORACLE=yes
export DB_SEEDER_DBMS_POSTGRESQL=yes
export DB_SEEDER_DBMS_SQLITE=yes

# ------------------------------------------------------------------------------
# Initialise Statistics.
# ------------------------------------------------------------------------------

if [ -z "$DB_SEEDER_IS_TRAVIS" ]; then
    export DB_SEEDER_IS_TRAVIS=no
fi

if [ "$DB_SEEDER_IS_TRAVIS" = "yes" ]; then
    export DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_travis_${DB_SEEDER_RELEASE}.tsv
else
    export DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_local_bash.tsv
fi  

rm -f $DB_SEEDER_FILE_STATISTICS_NAME

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Run all DBMS variations."
echo "--------------------------------------------------------------------------------"
echo "DBMS_CRATEDB                    : $DB_SEEDER_DBMS_CRATEDB"
echo "DBMS_CUBRID                     : $DB_SEEDER_DBMS_CUBRID"
echo "DBMS_DERBY                      : $DB_SEEDER_DBMS_DERBY"
echo "DBMS_DERBY_EMB                  : $DB_SEEDER_DBMS_DERBY_EMB"
echo "DBMS_FIREBIRD                   : $DB_SEEDER_DBMS_FIREBIRD"
echo "DBMS_H2                         : $DB_SEEDER_DBMS_H2"
echo "DBMS_H2_EMB                     : $DB_SEEDER_DBMS_H2_EMB"
echo "DBMS_HSQLDB                     : $DB_SEEDER_DBMS_HSQLDB"
echo "DBMS_HSQLDB_EMB                 : $DB_SEEDER_DBMS_HSQLDB_EMB"
echo "DBMS_IBMDB2                     : $DB_SEEDER_DBMS_IBMDB2"
echo "DBMS_INFORMIX                   : $DB_SEEDER_DBMS_INFORMIX"
echo "DBMS_MARIADB                    : $DB_SEEDER_DBMS_MARIADB"
echo "DBMS_MIMER                      : $DB_SEEDER_DBMS_MIMER"
echo "DBMS_MSSQLSERVER                : $DB_SEEDER_DBMS_MSSQLSERVER"
echo "DBMS_MYSQL                      : $DB_SEEDER_DBMS_MYSQL"
echo "DBMS_ORACLE                     : $DB_SEEDER_DBMS_ORACLE"
echo "DBMS_POSTGRESQL                 : $DB_SEEDER_DBMS_POSTGRESQL"
echo "DBMS_SQLITE                     : $DB_SEEDER_DBMS_SQLITE"
echo "--------------------------------------------------------------------------------"
echo "FILE_STATISTICS_NAME            : $DB_SEEDER_FILE_STATISTICS_NAME"
echo "--------------------------------------------------------------------------------"
echo "IS_TRAVIS                       : $DB_SEEDER_IS_TRAVIS"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

unset -f $DB_SEEDER_DBMS=

# ------------------------------------------------------------------------------
# CrateDB.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_CRATEDB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh cratedb yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# CUBRID.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_CUBRID" = "yes" ]; then
    if ! ( ./run_db_seeder.sh cubrid yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Apache Derby - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY" = "yes" ]; then
    if ! ( ./run_db_seeder.sh derby yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Apache Derby - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY_EMB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh derby_emb yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Firebird - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_FIREBIRD" = "yes" ]; then
    if ! ( ./run_db_seeder.sh firebird yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_H2" = "yes" ]; then
    if ! ( ./run_db_seeder.sh h2 yes 2 ); then
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
# HyperSQL Database - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_HSQLDB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh hsqldb yes 2 ); then
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
# IBM Db2 Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_IBMDB2" = "yes" ]; then
    if ! ( ./run_db_seeder.sh ibmdb2 yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# IBM Informix.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_INFORMIX" = "yes" ]; then
    if ! ( ./run_db_seeder.sh informix yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# MariaDB Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MARIADB" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mariadb yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MSSQLSERVER" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mssqlserver yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# Mimer SQL.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MIMER" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mimer yes 2 ); then
        exit 255
    fi    
fi

# ------------------------------------------------------------------------------
# MySQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MYSQL" = "yes" ]; then
    if ! ( ./run_db_seeder.sh mysql yes 2 ); then
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
