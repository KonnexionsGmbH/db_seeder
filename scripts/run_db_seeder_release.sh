#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_release.bat: Release run for VMWare and WSL2.
#
# ------------------------------------------------------------------------------

mkdir -p "$PWD/tmp"
sudo rm -rf "$PWD/tmp/*"

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Release run for VMWare and WSL2."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if ! (gradle copyJarToLib); then
    exit 255
fi

rm -f db_seeder.log

echo "--------------------------------------------------------------------------------"
echo "Generator."
echo "--------------------------------------------------------------------------------"
export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company_5400.json

./scripts/run_db_seeder_generate_schema.sh

echo "--------------------------------------------------------------------------------"
echo "Constraints included."
echo "--------------------------------------------------------------------------------"
export DB_SEEDER_DROP_CONSTRAINTS=no

export DB_SEEDER_FILE_STATISTICS_NAME_DEFAULT=resources/statistics/db_seeder_bash_complete_company_9.9.9_vmware_wsl2.tsv
if [ -z "${DB_SEEDER_FILE_STATISTICS_NAME}" ]; then
    export DB_SEEDER_FILE_STATISTICS_NAME=${DB_SEEDER_FILE_STATISTICS_NAME_DEFAULT}
fi 

rm -rf ${DB_SEEDER_FILE_STATISTICS_NAME}

./run_db_seeder.sh agens            yes 1
./run_db_seeder.sh cockroach        yes 1
./run_db_seeder.sh cratedb          yes 1
./run_db_seeder.sh cubrid           yes 1
./run_db_seeder.sh derby            yes 1
./run_db_seeder.sh derby_emb        yes 1
./run_db_seeder.sh exasol           yes 1
./run_db_seeder.sh firebird         yes 1
./run_db_seeder.sh h2               yes 1
./run_db_seeder.sh h2_emb           yes 1
./run_db_seeder.sh hsqldb           yes 1
./run_db_seeder.sh hsqldb_emb       yes 1
./run_db_seeder.sh ibmdb2           yes 1
./run_db_seeder.sh informix         yes 1
./run_db_seeder.sh mariadb          yes 1
./run_db_seeder.sh mimer            yes 1
./run_db_seeder.sh monetdb          yes 1
./run_db_seeder.sh mysql            yes 1
./run_db_seeder.sh mysql_trino      yes 1
./run_db_seeder.sh omnisci          yes 1
./run_db_seeder.sh oracle           yes 1
./run_db_seeder.sh oracle_trino     yes 1
./run_db_seeder.sh percona          yes 1
./run_db_seeder.sh postgresql       yes 1
./run_db_seeder.sh postgresql_trino yes 1
./run_db_seeder.sh sqlite           yes 1
./run_db_seeder.sh sqlserver        yes 1
./run_db_seeder.sh sqlserver_trino  yes 1
./run_db_seeder.sh timescale        yes 1
# Java 15:
./run_db_seeder.sh voltdb           yes 0
./run_db_seeder.sh yugabyte         yes 1

echo "--------------------------------------------------------------------------------"
echo "Constraints excluded."
echo "--------------------------------------------------------------------------------"
export DB_SEEDER_DROP_CONSTRAINTS=yes

./run_db_seeder.sh agens            yes 1
./run_db_seeder.sh cubrid           yes 1
./run_db_seeder.sh derby            yes 1
./run_db_seeder.sh derby_emb        yes 1
./run_db_seeder.sh exasol           yes 1
./run_db_seeder.sh firebird         yes 1
./run_db_seeder.sh hsqldb           yes 1
# wwe ./run_db_seeder.sh hsqldb_emb       yes 1
./run_db_seeder.sh ibmdb2           yes 1
./run_db_seeder.sh informix         yes 1
./run_db_seeder.sh mariadb          yes 1
./run_db_seeder.sh mimer            yes 1
./run_db_seeder.sh monetdb          yes 1
./run_db_seeder.sh mysql            yes 1
./run_db_seeder.sh oracle           yes 1
./run_db_seeder.sh percona          yes 1
./run_db_seeder.sh postgresql       yes 1
./run_db_seeder.sh sqlserver        yes 1
./run_db_seeder.sh timescale        yes 1
./run_db_seeder.sh yugabyte         yes 1

#./run_db_seeder.sh agens            yes 1
#./run_db_seeder.sh cockroach        no  1
#./run_db_seeder.sh cratedb          no  1
#./run_db_seeder.sh cubrid           yes 1
#./run_db_seeder.sh derby            yes 1
#./run_db_seeder.sh derby_emb        yes 1
#./run_db_seeder.sh exasol           yes 1
#./run_db_seeder.sh firebird         yes 1
#./run_db_seeder.sh h2               no  1
#./run_db_seeder.sh h2_emb           no  1
#./run_db_seeder.sh hsqldb           yes 1
#./run_db_seeder.sh hsqldb_emb       yes 1
#./run_db_seeder.sh ibmdb2           yes 1
#./run_db_seeder.sh informix         yes 1
#./run_db_seeder.sh mariadb          yes 1
#./run_db_seeder.sh mimer            yes 1
#./run_db_seeder.sh monetdb          yes 1
#./run_db_seeder.sh mysql            yes 1
#./run_db_seeder.sh mysql_trino      no  1
#./run_db_seeder.sh omnisci          no  1
#./run_db_seeder.sh oracle           yes 1
#./run_db_seeder.sh oracle_trino     no  1
#./run_db_seeder.sh percona          yes 1
#./run_db_seeder.sh postgresql       yes 1
#./run_db_seeder.sh postgresql_trino no  1
#./run_db_seeder.sh sqlite           no  1
#./run_db_seeder.sh sqlserver        yes 1
#./run_db_seeder.sh sqlserver_trino  no  1
#./run_db_seeder.sh timescale        yes 1
#./run_db_seeder.sh voltdb           no  1
#./run_db_seeder.sh yugabyte         yes 1

./scripts/run_db_seeder_compute_improvement.sh ${DB_SEEDER_FILE_STATISTICS_NAME}

./scripts/run_db_seeder_create_summary.sh

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
