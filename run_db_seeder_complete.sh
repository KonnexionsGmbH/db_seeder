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

unset -f DB_SEEDER_FILE_STATISTICS_DELIMITER=
unset -f DB_SEEDER_FILE_STATISTICS_HEADER=
export DB_SEEDER_FILE_STATISTICS_NAME=resources/db_seeder_bash.tsv

unset -f DB_SEEDER_MAX_ROW_CITY=
unset -f DB_SEEDER_MAX_ROW_COMPANY=
unset -f DB_SEEDER_MAX_ROW_COUNTRY=
unset -f DB_SEEDER_MAX_ROW_COUNTRY_STATE=
unset -f DB_SEEDER_MAX_ROW_TIMEZONE=

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
echo "MAX_ROW_CITY                    : $DB_SEEDER_MAX_ROW_CITY"
echo "MAX_ROW_COMPANY                 : $DB_SEEDER_MAX_ROW_COMPANY"
echo "MAX_ROW_COUNTRY                 : $DB_SEEDER_MAX_ROW_COUNTRY"
echo "MAX_ROW_COUNTRY_STATE           : $DB_SEEDER_MAX_ROW_COUNTRY_STATE"
echo "MAX_ROW_TIMEZONE                : $DB_SEEDER_MAX_ROW_TIMEZONE"
echo "--------------------------------------------------------------------------------"
echo "FILE_STATISTICS_DELIMITER       : $DB_SEEDER_FILE_STATISTICS_DELIMITER"
echo "FILE_STATISTICS_HEADER          : $DB_SEEDER_FILE_STATISTICS_HEADER"
echo "FILE_STATISTICS_NAME            : $DB_SEEDER_FILE_STATISTICS_NAME"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : $d.%m.%Y %H:%M:%S"
echo "================================================================================"

rm -f $DB_SEEDER_FILE_STATISTICS_NAME

# ------------------------------------------------------------------------------
# CrateDB.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_CRATEDB" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh cratedb yes )
    ( ./run_db_seeder.sh            cratedb )
    ( ./run_db_seeder.sh            cratedb )
fi

# ------------------------------------------------------------------------------
# CUBRID.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_CUBRID" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh cubrid yes )
    ( ./run_db_seeder.sh            cubrid )
    ( ./run_db_seeder.sh            cubrid )
fi

# ------------------------------------------------------------------------------
# Apache Derby - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh derby yes )
    ( ./run_db_seeder.sh            derby )
    ( ./run_db_seeder.sh            derby )
fi

# ------------------------------------------------------------------------------
# Apache Derby - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_DERBY_EMB" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh derby_emb no )
    ( ./run_db_seeder.sh            derby_emb )
    ( ./run_db_seeder.sh            derby_emb )
fi

# ------------------------------------------------------------------------------
# Firebird - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_FIREBIRD" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh firebird yes )
    ( ./run_db_seeder.sh            firebird )
    ( ./run_db_seeder.sh            firebird )
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_H2" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh h2 yes )
    ( ./run_db_seeder.sh            h2 )
    ( ./run_db_seeder.sh            h2 )
fi

# ------------------------------------------------------------------------------
# H2 Database Engine - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_H2_EMB" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh h2_emb no )
    ( ./run_db_seeder.sh            h2_emb )
    ( ./run_db_seeder.sh            h2_emb )
fi

# ------------------------------------------------------------------------------
# HyperSQL Database - client version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_HSQLDB" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh hsqldb yes )
    ( ./run_db_seeder.sh            hsqldb )
    ( ./run_db_seeder.sh            hsqldb )
fi

# ------------------------------------------------------------------------------
# HyperSQL Database - embedded version.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_HSQLDB_EMB" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh hsqldb_emb no )
    ( ./run_db_seeder.sh            hsqldb_emb )
    ( ./run_db_seeder.sh            hsqldb_emb )
fi

# ------------------------------------------------------------------------------
# IBM Db2 Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_IBMDB2" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh ibmdb2 yes )
    ( ./run_db_seeder.sh            ibmdb2 )
    ( ./run_db_seeder.sh            ibmdb2 )
fi

# ------------------------------------------------------------------------------
# IBM Informix.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_INFORMIX" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh informix yes )
    ( ./run_db_seeder.sh            informix )
    ( ./run_db_seeder.sh            informix )
fi

# ------------------------------------------------------------------------------
# MariaDB Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MARIADB" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh mariadb yes )
    ( ./run_db_seeder.sh            mariadb )
    ( ./run_db_seeder.sh            mariadb )
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MSSQLSERVER" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh mssqlserver yes )
    ( ./run_db_seeder.sh            mssqlserver )
    ( ./run_db_seeder.sh            mssqlserver )
fi

# ------------------------------------------------------------------------------
# Mimer SQL.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MIMER" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh mimer yes )
    ( ./run_db_seeder.sh            mimer )
    ( ./run_db_seeder.sh            mimer )
fi

# ------------------------------------------------------------------------------
# MySQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_MYSQL" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh mysql yes )
    ( ./run_db_seeder.sh            mysql )
    ( ./run_db_seeder.sh            mysql )
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_ORACLE" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh oracle yes )
    ( ./run_db_seeder.sh            oracle )
    ( ./run_db_seeder.sh            oracle )
fi

# ------------------------------------------------------------------------------
# PostgreSQL Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_POSTGRESQL" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh postgresql yes )
    ( ./run_db_seeder.sh            postgresql )
    ( ./run_db_seeder.sh            postgresql )
fi

# ------------------------------------------------------------------------------
# SQLite.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS_SQLITE" = "yes" ]; then
    ( ./run_db_seeder_setup_dbms.sh sqlite no )
    ( ./run_db_seeder.sh            sqlite )
    ( ./run_db_seeder.sh            sqlite )
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================""
