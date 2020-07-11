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
unset -f DB_SEEDER_DBMS_DERBY_EMB=
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
date +"DATE TIME : $d.%m.%Y %H:%M:%S"
echo "================================================================================"

rm -f $DB_SEEDER_FILE_STATISTICS_NAME

# ------------------------------------------------------------------------------
# CrateDB.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_CRATEDB" = "yes" ]; then
    export DB_SEEDER_DBMS=cratedb
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# CUBRID.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_CUBRID" = "yes" ]; then
    export DB_SEEDER_DBMS=cubrid
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# Apache Derby - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY" = "yes" ]; then
    export DB_SEEDER_DBMS=derby
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# Apache Derby - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY_EMB" = "yes" ]; then
    export DB_SEEDER_DBMS=derby_emb
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# Firebird - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_FIREBIRD" = "yes" ]; then
    export DB_SEEDER_DBMS=firebird
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_H2" = "yes" ]; then
    export DB_SEEDER_DBMS=h2
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_H2_EMB" = "yes" ]; then
    export DB_SEEDER_DBMS=h2_emb
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# HyperSQL Database - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_HSQLDB" = "yes" ]; then
    export DB_SEEDER_DBMS=hsqldb
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# HyperSQL Database - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_HSQLDB_EMB" = "yes" ]; then
    export DB_SEEDER_DBMS=hsqldb_emb
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# IBM Db2 Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_IBMDB2" = "yes" ]; then
    export DB_SEEDER_DBMS=ibmdb2
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# IBM Informix.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_INFORMIX" = "yes" ]; then
    export DB_SEEDER_DBMS=informix
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# MariaDB Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MARIADB" = "yes" ]; then
    export DB_SEEDER_DBMS=mariadb
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MSSQLSERVER" = "yes" ]; then
    export DB_SEEDER_DBMS=mssqlserver
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# Mimer SQL.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MIMER" = "yes" ]; then
    export DB_SEEDER_DBMS=mimer
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# MySQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MYSQL" = "yes" ]; then
    export DB_SEEDER_DBMS=mysql
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_ORACLE" = "yes" ]; then
    export DB_SEEDER_DBMS=oracle
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# PostgreSQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_POSTGRESQL" = "yes" ]; then
    export DB_SEEDER_DBMS=postgresql
    ( ./scripts/run_db_seeder_single.sh )
fi

# ------------------------------------------------------------------------------
# SQLite.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_SQLITE" = "yes" ]; then
    export DB_SEEDER_DBMS=sqlite
    ( ./scripts/run_db_seeder_single.sh )
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================""
