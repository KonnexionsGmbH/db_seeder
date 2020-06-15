#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_database.sh: Setup a database Docker container.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DATABASE_BRAND_DEFAULT=oracle
export DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=yes

export DB_SEEDER_VERSION_CRATEDB=4.1.6
export DB_SEEDER_VERSION_IBMDB2=11.5.0.0a
export DB_SEEDER_VERSION_MARIADB=10.4.13
export DB_SEEDER_VERSION_MSSQLSERVER=2019-latest
export DB_SEEDER_VERSION_MYSQL=8.0.20
export DB_SEEDER_VERSION_ORACLE=db_19_3_ee
export DB_SEEDER_VERSION_POSTGRESQL=12.3

if [ -z "$1" ]; then
    echo "===================================="
    echo "cratedb     - CrateDB"
    echo "ibmdb2      - IBM DB2 Database"
    echo "mariadb     - MariaDB Server"
    echo "mssqlserver - Microsoft SQL Server"
    echo "mysql       - MySQL"
    echo "oracle      - Oracle Database"
    echo "postgresql  - PostgreSQL Database"
    echo "------------------------------------"
    read -p "Enter the desired database brand [default: $DB_SEEDER_DATABASE_BRAND_DEFAULT] " DB_SEEDER_DATABASE_BRAND
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
if [ "$DB_SEEDER_DATABASE_BRAND" = "cratedb" ]; then
    echo "VERSION_CRATEDB           : $DB_SEEDER_VERSION_CRATEDB"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "ibmdb2" ]; then
    echo "VERSION_IBMDB2            : $DB_SEEDER_VERSION_IBMDB2"
    export DB_SEEDER_IBMDB2_DATABASE=kxn_db
    echo "IBMDB2_DATABASE           : $DB_SEEDER_IBMDB2_DATABASE"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "mariadb" ]; then
    echo "VERSION_MARIADB           : $DB_SEEDER_VERSION_MARIADB"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "mssqlserver" ]; then
    echo "VERSION_MSSQLSERVER       : $DB_SEEDER_VERSION_MSSQLSERVER"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "mysql" ]; then
    echo "VERSION_MYSQL             : $DB_SEEDER_VERSION_MYSQL"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "oracle" ]; then
    echo "VERSION_ORACLE            : $DB_SEEDER_VERSION_ORACLE"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "postgresql" ]; then
    echo "VERSION_POSTGRESQL        : $DB_SEEDER_VERSION_POSTGRESQL"
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
# CrateDB                                         https://hub.docker.com/_/crate
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "cratedb" ]; then
    start=$(date +%s)
    echo "CrateDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CrateDB $DB_SEEDER_VERSION_CRATEDB)"
    docker create --name db_seeder_db -p 5432:5432/tcp --env CRATE_HEAP_SIZE=2g crate:%DB_SEEDER_VERSION_CRATEDB% crate -Cnetwork.host=_site_ -Cdiscovery.type=single-node

    echo "Docker start db_seeder_db (CrateDB $DB_SEEDER_VERSION_CRATEDB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 10

    end=$(date +%s)
    echo "DOCKER CrateDB was ready in $((end - start)) seconds"
fi
# ------------------------------------------------------------------------------
# IBM DB2 Database                           https://hub.docker.com/r/ibmcom/db2
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "ibmdb2" ]; then
    start=$(date +%s)
    echo "IBM DB2 Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (IBM DB2 $DB_SEEDER_VERSION_IBMDB2)"
    docker run -itd --name db_seeder_db --restart unless-stopped -e DBNAME=$DB_SEEDER_IBMDB2_DATABASE -e DB2INST1_PASSWORD=ibmdb2 -e LICENSE=accept -p 50000:50000 --privileged=true ibmcom/db2:$DB_SEEDER_VERSION_IBMDB2

    sleep 120

    end=$(date +%s)
    echo "DOCKER IBM DB2 Database was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MariaDB Server                                https://hub.docker.com/_/mariadb
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "mariadb" ]; then
    start=$(date +%s)
    echo "MariaDB Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MariaDB $DB_SEEDER_VERSION_MARIADB)"
    docker create -e MYSQL_ROOT_PASSWORD=mariadb --name db_seeder_db -p 3306:3306/tcp mariadb:$DB_SEEDER_VERSION_MARIADB

    echo "Docker start db_seeder_db (MariaDB $DB_SEEDER_VERSION_MARIADB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER MariaDB Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server Database  https://hub.docker.com/_/microsoft-mssql-server
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "mssqlserver" ]; then
    start=$(date +%s)
    echo "Microsoft SQL Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Microsoft SQL Server $DB_SEEDER_VERSION_MSSQLSERVER)"
    docker create -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=mssqlserver_2019" --name db_seeder_db -p 1433:1433 mcr.microsoft.com/mssql/server:$DB_SEEDER_VERSION_MSSQLSERVER

    echo "Docker start db_seeder_db (Microsoft SQL Server $DB_SEEDER_VERSION_MSSQLSERVER) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER Microsoft SQL Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MySQL Database                                  https://hub.docker.com/_/mysql
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "mysql" ]; then
    start=$(date +%s)
    echo "MySQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL)"
    docker create -e MYSQL_ROOT_PASSWORD=mysql --name db_seeder_db -p 3306:3306/tcp mysql:$DB_SEEDER_VERSION_MYSQL

    echo "Docker start db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

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

# ------------------------------------------------------------------------------
# PostgreSQL Database                          https://hub.docker.com/_/postgres
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DATABASE_BRAND" = "postgresql" ]; then
    start=$(date +%s)
    echo "PostgreSQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (PostgreSQL $DB_SEEDER_VERSION_POSTGRESQL)"
    docker create -e POSTGRES_DB=kxn_db_sys -e POSTGRES_PASSWORD=postgresql -e POSTGRES_USER=kxn_user_sys --name db_seeder_db -p 5432:5432 postgres:$DB_SEEDER_VERSION_POSTGRESQL

    echo "Docker start db_seeder_db (PostgreSQL $DB_SEEDER_VERSION_POSTGRESQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER PostgreSQL Database was ready in $((end - start)) seconds"
fi

    sleep 20

docker ps

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
