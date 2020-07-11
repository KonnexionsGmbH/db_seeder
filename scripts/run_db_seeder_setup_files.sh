#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_files.sh: Setup the database files.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a database Docker container."
echo "--------------------------------------------------------------------------------"
echo "DBMS                      : $DB_SEEDER_DBMS"
echo --------------------------------------------------------------------------------
if [ "$DB_SEEDER_DBMS" = "cubrid" ]; then
    echo "VERSION_CUBRID            : $DB_SEEDER_CUBRID_VERSION"
    echo "CUBRID_DATABASE           : $DB_SEEDER_CUBRID_DATABASE"
    if [[ -f $DB_SEEDER_CUBRID_DATABASE ]] || [[ -d $DB_SEEDER_CUBRID_DATABASE ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_CUBRID_DATABASE
        rm -f $DB_SEEDER_CUBRID_DATABASE
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    echo "VERSION_DERBY             : $DB_SEEDER_VERSION_DERRBY"
    echo "DERBY_DATABASE            : $DB_SEEDER_DERBY_DATABASE"
    if [[ -f $DB_SEEDER_DERBY_DATABASE ]] || [[ -d $DB_SEEDER_DERBY_DATABASE ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_DERBY_DATABASE
        rm -f $DB_SEEDER_DERBY_DATABASE
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "firebird" ]; then
    echo "VERSION_FIREBIRD          : $DB_SEEDER_FIREBIRD_VERSION"
    echo "FIREBIRD_DATABASE         : $DB_SEEDER_FIREBIRD_DATABASE"
    if [[ -f $DB_SEEDER_FIREBIRD_DATABASE ]] || [[ -d $DB_SEEDER_FIREBIRD_DATABASE ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_FIREBIRD_DATABASE
        rm -f $DB_SEEDER_FIREBIRD_DATABASE
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "h2_emb" ]; then
    echo "VERSION_H2                : $DB_SEEDER_H2_VERSION"
    echo "H2_DATABASE               : $DB_SEEDER_H2_DATABASE"
    if [[ -f ${DB_SEEDER_H2_DATABASE}.mv.db ]] || [[ -d ${DB_SEEDER_H2_DATABASE}.mv.db ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll ${DB_SEEDER_H2_DATABASE}.mv.db
        rm -f ${DB_SEEDER_H2_DATABASE}.mv.db
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "hsqldb_emb" ]; then
    echo "VERSION_HSQLDB            : $DB_SEEDER_HSQLDB_VERSION"
    echo "HSQLDB_DATABASE           : $DB_SEEDER_HSQLDB_DATABASE"
    if [[ -f ${DB_SEEDER_HSQLDB_DATABASE}.lobs ]] || [[ -d ${DB_SEEDER_HSQLDB_DATABASE}.tmp ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll ${DB_SEEDER_HSQLDB_DATABASE}.*
        rm -f ${DB_SEEDER_HSQLDB_DATABASE}.lck
        rm -f ${DB_SEEDER_HSQLDB_DATABASE}.lobs
        rm -f ${DB_SEEDER_HSQLDB_DATABASE}.log
        rm -f ${DB_SEEDER_HSQLDB_DATABASE}.properties
        rm -f ${DB_SEEDER_HSQLDB_DATABASE}.script
        rm -f ${DB_SEEDER_HSQLDB_DATABASE}.tmp
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    echo "VERSION_IBMDB2            : $DB_SEEDER_IBMDB2_VERSION"
    echo "IBMDB2_DATABASE           : $DB_SEEDER_IBMDB2_DATABASE"
    if [[ -f $DB_SEEDER_IBMDB2_DATABASE ]] || [[ -d $DB_SEEDER_IBMDB2_DATABASE ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_IBMDB2_DATABASE
        rm -f $DB_SEEDER_IBMDB2_DATABASE
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "informix" ]; then
    echo "VERSION_INFORMIX          : $DB_SEEDER_INFORMIX_VERSION"
    echo "INFORMIX_DATABASE         : $DB_SEEDER_INFORMIX_DATABASE"
    if [[ -f $DB_SEEDER_INFORMIX_DATABASE ]] || [[ -d $DB_SEEDER_INFORMIX_DATABASE ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_INFORMIX_DATABASE
        rm -f $DB_SEEDER_INFORMIX_DATABASE
    fi    
fi
if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    echo "VERSION_SQLITE            : $DB_SEEDER_VERSION_SQLITE"
    echo "SQLITE_DATABASE           : $DB_SEEDER_SQLITE_DATABASE"
    if [[ -f $DB_SEEDER_SQLITE_DATABASE ]] || [[ -d $DB_SEEDER_SQLITE_DATABASE ]]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_SQLITE_DATABASE
        rm -f $DB_SEEDER_SQLITE_DATABASE
    fi    
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
