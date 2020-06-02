#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_database.sh: Setup a database Docker container.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DATABASE_BRAND_DEFAULT=oracle
export DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=no

export DB_SEEDER_VERSION_MYSQL=8.0.20
export DB_SEEDER_VERSION_ORACLE=db_19_3_ee

if [ -z "$1" ]; then
    read -p "Enter the desired database brand (mysql/oracle) [default: $DB_SEEDER_DATABASE_BRAND_DEFAULT] " DB_SEEDER_DATABASE_BRAND
    export DB_SEEDER_DATABASE_BRAND=$DB_SEEDER_DATABASE_BRAND

    if [ -z "$DB_SEEDER_DATABASE_BRAND" ]; then
        export DB_SEEDER_DATABASE_BRAND=$DB_SEEDER_DATABASE_BRAND_DEFAULT
    fi
else
    export DB_SEEDER_DATABASE_BRAND=$1
fi

if [ -z "$2" ]; then
    read -p "Delete the existing Docker container DB_SEEDER_DB (yes/no) [default: $DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT] " DB_SEEDER_DELETE_EXISTING_CONTAINER
    export DB_SEEDER_DELETE_EXISTING_CONTAINER=$DB_SEEDER_DELETE_EXISTING_CONTAINER

    if [ -z "$DB_SEEDER_DELETE_EXISTING_CONTAINER" ]; then
        export DB_SEEDER_DELETE_EXISTING_CONTAINER=$DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT
    fi
else
    export DB_SEEDER_DELETE_EXISTING_CONTAINER=$2
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a database Docker container."
echo "--------------------------------------------------------------------------------"
echo "DATABASE_BRAND            : $DB_SEEDER_DATABASE_BRAND"
echo "DELETE_EXISTING_CONTAINER : $DB_SEEDER_DELETE_EXISTING_CONTAINER"
echo --------------------------------------------------------------------------------
if [ "$DB_SEEDER_DATABASE_BRAND" = "mysql" ]; then
    echo "VERSION_MYSQL             : $DB_SEEDER_VERSION_MYSQL"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "oracle" ]; then
    echo "VERSION_ORACLE            : $DB_SEEDER_VERSION_ORACLE"
fi
echo --------------------------------------------------------------------------------
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if [ "$DB_SEEDER_DELETE_EXISTING_CONTAINER" != "no" ]; then
    echo "Docker stop/rm db_seeder_db"
    docker stop db_seeder_db
    docker rm -f db_seeder_db
fi

# ------------------------------------------------------------------------------
# MySQL Database                                  https://hub.docker.com/_/mysql
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "mysql" ]; then
    start=$(date +%s)
    echo "MySQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL)"
    docker create -e MYSQL_ROOT_PASSWORD=mysql --name db_seeder_db mysql:$DB_SEEDER_VERSION_MYSQL

    echo "Docker start db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER MySQL Database was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "oracle" ]; then
    echo "Oracle Database"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (Oracle $DB_SEEDER_VERSION_ORACLE)"
    docker create -e ORACLE_PWD=oracle --name db_seeder_db -p 1521:1521/tcp --shm-size 1G konnexionsgmbh/$DB_SEEDER_VERSION_ORACLE

    echo "Docker start db_seeder_db (Oracle $DB_SEEDER_VERSION_ORACLE) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 60; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Oracle Database was ready in $((end - start)) seconds"
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
