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
echo "DBSeeder - creating database files or directories."
echo "--------------------------------------------------------------------------------"
echo "DATABASE                  : ${DB_SEEDER_DATABASE}"
echo "DBMS                      : ${DB_SEEDER_DBMS}"
echo "VERSION                   : ${DB_SEEDER_VERSION}"
echo --------------------------------------------------------------------------------

unset -f DB_SEEDER_DATABASE_INTERN

if [ "${DB_SEEDER_DBMS}" = "derby" ] || [ "${DB_SEEDER_DBMS}" = "emb" ]; then
    export DB_SEEDER_DATABASE_INTERN=${DB_SEEDER_DATABASE}
fi

if [ "${DB_SEEDER_DBMS}" = "h2" ] || [ "${DB_SEEDER_DBMS}" = "h2_emb" ]; then
    export DB_SEEDER_DATABASE_INTERN=${DB_SEEDER_DATABASE}
fi

if [ "${DB_SEEDER_DBMS}" = "hsqldb_emb" ]; then
    export DB_SEEDER_DATABASE_INTERN=${DB_SEEDER_DATABASE}
fi

if [ "${DB_SEEDER_DBMS}" = "ibmdb2" ]; then
    export DB_SEEDER_DATABASE_INTERN=${DB_SEEDER_DATABASE}
fi

if [ "${DB_SEEDER_DBMS}" = "sqlite" ]; then
    export DB_SEEDER_DATABASE_INTERN=${DB_SEEDER_DATABASE}
fi

if [ -n "${DB_SEEDER_DATABASE_INTERN}" ]; then
    if [ -d "${DB_SEEDER_DATABASE}" ]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll "${DB_SEEDER_DATABASE}"
        rm -rf "${DB_SEEDER_DATABASE}"
    fi    

    if [ -f "${DB_SEEDER_DATABASE}" ] || [ -f "${DB_SEEDER_DATABASE}".lobs ]; then 
        echo ""
        echo "............................................................ before:"
        ls -ll "${DB_SEEDER_DATABASE}"*
        rm -f "${DB_SEEDER_DATABASE}"*
    fi    
    
    if [ "${DB_SEEDER_DBMS}" = "ibmdb2" ]; then
        mkdir -p "${DB_SEEDER_DATABASE}"
    fi
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
