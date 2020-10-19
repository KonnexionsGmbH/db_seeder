#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_presto.sh: Setup a Presto Distributed Query Engine Docker container.
#
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_VERSION_PRESTO}" = "" ]; then
    export DB_SEEDER_VERSION_PRESTO=latest
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a Presto Distributed Query Engine Docker container."
echo "--------------------------------------------------------------------------------"
echo "VERSION_PRESTO                    : ${DB_SEEDER_VERSION_PRESTO}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

echo --------------------------------------------------------------------------------
echo Stop and delete existing containers.
echo --------------------------------------------------------------------------------

echo "Docker stop/rm db_seeder_presto ............................ before:"
docker ps    | grep "db_seeder_presto" && docker stop db_seeder_presto
docker ps -a | grep "db_seeder_presto" && docker rm db_seeder_presto
echo "............................................................. after:"
docker ps -a

echo "Docker stop/rm db_seeder_db ................................ before:"
docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
echo "............................................................. after:"
docker ps -a

echo "--------------------------------------------------------------------------------"
echo "Start Presto Distributed Query Engine - creating and starting the container"
echo "--------------------------------------------------------------------------------"
start=$(date +%s)
echo "Docker create presto (Presto Distributed Query Engine)"

docker network create db_seeder_net  2>/dev/null || true
docker create --name    db_seeder_presto \
              --network db_seeder_net \
              -p        8080:8080/tcp \
              -v        $PWD/resources/docker/presto:/usr/lib/presto/etc \
              prestosql/presto:${DB_SEEDER_VERSION_PRESTO}

echo "Docker start presto (Presto Distributed Query Engine) ..."
docker start db_seeder_presto

sleep 30

docker network ls
docker network inspect db_seeder_net

end=$(date +%s)
echo "Docker Presto Distributed Query Engine was ready in in $((end - start)) seconds"

docker ps

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
