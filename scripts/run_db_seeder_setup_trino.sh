#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_trino.sh: Setup a trino Docker container.
#
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_VERSION_TRINO}" = "" ]; then
    export DB_SEEDER_VERSION_TRINO=latest
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a trino Docker container."
echo "--------------------------------------------------------------------------------"
echo "VERSION_TRINO                     : ${DB_SEEDER_VERSION_TRINO}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

echo --------------------------------------------------------------------------------
echo Stop and delete existing containers.
echo --------------------------------------------------------------------------------

echo "Docker stop/rm db_seeder_trino .............................. before:"
docker ps -a
docker ps | grep "db_seeder_trino" && docker stop db_seeder_trino
docker ps -a | grep "db_seeder_trino" && docker rm --force db_seeder_trino
echo "............................................................. after:"
docker ps -a

echo "Docker stop/rm db_seeder_db ................................. before:"
docker ps -a
docker ps | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm --force db_seeder_db
echo "............................................................. after:"
docker ps -a

echo "--------------------------------------------------------------------------------"
echo "Start trino - creating and starting the container"
echo "--------------------------------------------------------------------------------"
start=$(date +%s)
echo "Docker create trino"

docker network create db_seeder_net  2>/dev/null || true
docker create --name    db_seeder_trino \
              --network db_seeder_net \
              -p        8080:8080/tcp \
              -v        $PWD/resources/docker/trino:/etc/trino \
              trinodb/trino:${DB_SEEDER_VERSION_TRINO}

echo "Docker start trino ..."
docker start db_seeder_trino

sleep 30

docker network ls
docker network inspect db_seeder_net

end=$(date +%s)
echo "Docker trino was ready in in $((end - start)) seconds"

docker ps

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
