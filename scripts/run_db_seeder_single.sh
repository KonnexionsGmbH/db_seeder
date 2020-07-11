#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_single.sh: Run a single DBMS variation.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_EMBEDDED=no

if [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "$DB_SEEDER_DBMS" = "h2_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "$DB_SEEDER_DBMS" = "hsqldb_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi


echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Run a single DBMS variation."
echo "--------------------------------------------------------------------------------"
echo "DBMS                              : $DB_SEEDER_DBMS"
echo "DBMS_EMBEDDED                     : $DB_SEEDER_DBMS_EMBEDDED"
echo "NO_CREATE_RUNS                    : $DB_SEEDER_NO_CREATE_RUNS"
echo "SETUP_DBMS                        : $DB_SEEDER_SETUP_DBMS"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : $d.%m.%Y %H:%M:%S"
echo "================================================================================"

if [ "$DB_SEEDER_SETUP_DBMS" = "yes" ]; then
    ( ./scripts/run_db_seeder_setup_dbms.sh $DB_SEEDER_SETUP_DBMS )
fi

if [ "$DB_SEEDER_NO_CREATE_RUNS" = "1" ]; then
    ( ./scripts/run_db_seeder_create_data.sh $DB_SEEDER_NO_CREATE_RUNS )
fi

if [ "$DB_SEEDER_NO_CREATE_RUNS" = "2" ]; then
    ( ./scripts/run_db_seeder_create_data.sh $DB_SEEDER_NO_CREATE_RUNS )
    ( ./scripts/run_db_seeder_create_data.sh $DB_SEEDER_NO_CREATE_RUNS )
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
