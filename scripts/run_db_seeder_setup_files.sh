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
echo "DB Seeder - creating database files or directories."
echo "--------------------------------------------------------------------------------"
echo "DBMS                      : $DB_SEEDER_DBMS"
echo --------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "derby" ] || [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    echo "DERBY_VERSION             : $DB_SEEDER_DERBY_VERSION"
    echo "DERBY_DATABASE            : $DB_SEEDER_DERBY_DATABASE"
    export DB_SEEDER_DATABASE=$DB_SEEDER_DERBY_DATABASE
fi

if [ "$DB_SEEDER_DBMS" = "h2" ] || [ "$DB_SEEDER_DBMS" = "h2_emb" ]; then
    echo "H2_VERSION                : $DB_SEEDER_H2_VERSION"
    echo "H2_DATABASE               : $DB_SEEDER_H2_DATABASE"
    export DB_SEEDER_DATABASE=$DB_SEEDER_H2_DATABASE
fi

if [ "$DB_SEEDER_DBMS" = "hsqldb_emb" ]; then
    echo "HSQLDB_VERSION            : $DB_SEEDER_HSQLDB_VERSION"
    echo "HSQLDB_DATABASE           : $DB_SEEDER_HSQLDB_DATABASE"
    export DB_SEEDER_DATABASE=$DB_SEEDER_HSQLDB_DATABASE
fi

if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    echo "IBMDB2_VERSION            : $DB_SEEDER_IBMDB2_VERSION"
    echo "IBMDB2_DATABASE           : $DB_SEEDER_IBMDB2_DATABASE"
    export DB_SEEDER_DATABASE=$DB_SEEDER_IBMDB2_DATABASE
fi

if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    echo "SQLITE_VERSION            : $DB_SEEDER_SQLITE_VERSION"
    echo "SQLITE_DATABASE           : $DB_SEEDER_SQLITE_DATABASE"
    export DB_SEEDER_DATABASE=$DB_SEEDER_SQLITE_DATABASE
fi

if [ ! -z "$DB_SEEDER_DATABASE" ]; then
    if [ -f $DB_SEEDER_DATABASE ] || [ -f ${DB_SEEDER_DATABASE}.lobs ]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll ${DB_SEEDER_DATABASE}*
        rm -f ${DB_SEEDER_DATABASE}*
    fi    
    
    if [ -d $DB_SEEDER_DATABASE ]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll $DB_SEEDER_DATABASE
        rm -rf $DB_SEEDER_DATABASE
    fi    

    if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
        mkdir -p $DB_SEEDER_DATABASE
    else
        fileDirectory=$DB_SEEDER_DATABASE
        mkdir -p ${fileDirectory%/*}
    fi
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
