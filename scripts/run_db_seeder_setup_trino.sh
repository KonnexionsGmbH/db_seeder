#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_trino.sh: Setup a Trino Distributed Query Engine Docker container.
#
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_VERSION_TRINO}" = "" ]; then
    export DB_SEEDER_VERSION_TRINO=latest
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a Trino Distributed Query Engine Docker container."
echo "--------------------------------------------------------------------------------"
echo "VERSION_TRINO                     : ${DB_SEEDER_VERSION_TRINO}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

echo --------------------------------------------------------------------------------
echo Stop and delete existing containers.
echo --------------------------------------------------------------------------------

echo "Docker stop/rm db_seeder_trino .............................. before:"
docker ps    | grep "db_seeder_trino" && docker stop db_seeder_trino
docker ps -a | grep "db_seeder_trino" && docker rm db_seeder_trino
echo "............................................................. after:"
docker ps -a

echo "Docker stop/rm db_seeder_db ................................. before:"
docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
echo "............................................................. after:"
docker ps -a

echo "--------------------------------------------------------------------------------"
echo "Start Trino Distributed Query Engine - creating and starting the container"
echo "--------------------------------------------------------------------------------"
start=$(date +%s)
echo "Docker create trino (Trino Distributed Query Engine)"

docker network create db_seeder_net  2>/dev/null || true
docker create --name    db_seeder_trino \
              --network db_seeder_net \
              -p        8080:8080/tcp \
              -v        $PWD/resources/docker/trino:/usr/lib/trino/etc \
              trinodb/trino:${DB_SEEDER_VERSION_TRINO}

echo "Docker start trino (Trino Distributed Query Engine) ..."
docker start db_seeder_trino

sleep 30

docker network ls
docker network inspect db_seeder_net

end=$(date +%s)
echo "Docker Trino Distributed Query Engine was ready in in $((end - start)) seconds"

docker ps

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
