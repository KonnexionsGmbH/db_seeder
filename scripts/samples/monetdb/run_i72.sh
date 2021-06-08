#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_i72.bat: Demonstration Issue 72.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Demonstration Issue 72."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"

docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm --force db_seeder_db
docker create --name db_seeder_db -p 50000:50000/tcp monetdb/monetdb:latest
docker start db_seeder_db
docker ps

java -cp "${CLASSPATH}:lib/*" ch.konnexions.db_seeder.samples.monetdb.I72

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
