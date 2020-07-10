#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_dbms.sh: Setup a database Docker container.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DEFAULT=sqlite
export DB_SEEDER_DBMS_EMBEDDED=no

export DB_SEEDER_CUBRID_DATABASE=kxn_db
export DB_SEEDER_DERBY_DATABASE=./tmp/kxn_db
export DB_SEEDER_FIREBIRD_DATABASE=kxn_db
export DB_SEEDER_H2_DATABASE=./tmp/kxn_db
export DB_SEEDER_HSQLDB_DATABASE=./tmp/kxn_db
export DB_SEEDER_IBMDB2_DATABASE=kxn_db
export DB_SEEDER_INFORMIX_DATABASE=kxn_db
export DB_SEEDER_MIMER_DATABASE=kxn_db
export DB_SEEDER_SQLITE_DATABASE=tmp/kxn_db

export DB_SEEDER_VERSION_CRATEDB=4.1.8
export DB_SEEDER_VERSION_CUBRID=10.2
export DB_SEEDER_VERSION_DERBY=10.15.2.0
export DB_SEEDER_VERSION_FIREBIRD=3.0.6
export DB_SEEDER_VERSION_H2=1.4.200
export DB_SEEDER_VERSION_HSQLDB=2.5.1
export DB_SEEDER_VERSION_IBMDB2=11.5.0.0a
export DB_SEEDER_VERSION_INFORMIX=14.10.FC3DE

export DB_SEEDER_VERSION_MARIADB=10.4.13
export DB_SEEDER_VERSION_MARIADB=10.5.3
export DB_SEEDER_VERSION_MARIADB=10.5.4

export DB_SEEDER_VERSION_MIMER=v11.0.3c
export DB_SEEDER_VERSION_MSSQLSERVER=2019-latest
export DB_SEEDER_VERSION_MYSQL=8.0.20

export DB_SEEDER_VERSION_ORACLE=db_12_2_ee
export DB_SEEDER_VERSION_ORACLE=db_18_3_ee
export DB_SEEDER_VERSION_ORACLE=db_19_3_ee

export DB_SEEDER_VERSION_POSTGRESQL=12.3-alpine

if [ -z "$1" ]; then
    echo "==========================================="
    echo "derby       - Apache Derby [client]"
    echo "derby_emb   - Apache Derby [embedded]"
    echo "cratedb     - CrateDB"
    echo "cubrid      - CUBRID"
    echo "firebird    - Firebird"
    echo "h2          - H2 Database Engine [client]"
    echo "h2_emb      - H2 Database Engine [embedded]"
    echo "hsqldb      - HyperSQL Database [client]"
    echo "hsqldb_emb  - HyperSQL Database [embedded]"
    echo "ibmdb2      - IBM Db2 Database"
    echo "informix    - IBM Informix"
    echo "mariadb     - MariaDB Server"
    echo "mimer       - Mimer SQL"
    echo "mssqlserver - Microsoft SQL Server"
    echo "mysql       - MySQL"
    echo "oracle      - Oracle Database"
    echo "postgresql  - PostgreSQL Database"
    echo "sqlite      - SQLite [embedded]"
    echo "-------------------------------------------"
    read -p "Enter the desired database management system [default: $DB_SEEDER_DBMS_DEFAULT] " DB_SEEDER_DBMS
    export DB_SEEDER_DBMS=$DB_SEEDER_DBMS

    if [ -z "$DB_SEEDER_DBMS" ]; then
        export DB_SEEDER_DBMS=$DB_SEEDER_DBMS_DEFAULT
    fi
else
    export DB_SEEDER_DBMS=$1
fi

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
    install -Dv /dev/null $DB_SEEDER_SQLITE_DATABASE
fi

if [ "$DB_SEEDER_DBMS_EMBEDDED" = "no" ]; then
    echo "Docker stop/rm db_seeder_db"
    docker ps -q --filter "name=db_seeder_db" | grep -q . && docker stop db_seeder_db && docker rm -fv db_seeder_db
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a database Docker container."
echo "--------------------------------------------------------------------------------"
echo "DBMS                      : $DB_SEEDER_DBMS"
echo "DBMS_EMBEDDED             : $DB_SEEDER_DBMS_EMBEDDED"
echo --------------------------------------------------------------------------------
if [ "$DB_SEEDER_DBMS" = "cratedb" ]; then
    echo "VERSION_CRATEDB           : $DB_SEEDER_VERSION_CRATEDB"
fi
if [ "$DB_SEEDER_DBMS" = "mariadb" ]; then
    echo "VERSION_MARIADB           : $DB_SEEDER_VERSION_MARIADB"
fi
if [ "$DB_SEEDER_DBMS" = "mssqlserver" ]; then
    echo "VERSION_MSSQLSERVER       : $DB_SEEDER_VERSION_MSSQLSERVER"
fi
if [ "$DB_SEEDER_DBMS" = "mysql" ]; then
    echo "VERSION_MYSQL             : $DB_SEEDER_VERSION_MYSQL"
fi
if [ "$DB_SEEDER_DBMS" = "oracle" ]; then
    echo "VERSION_ORACLE            : $DB_SEEDER_VERSION_ORACLE"
fi
if [ "$DB_SEEDER_DBMS" = "postgresql" ]; then
    echo "VERSION_POSTGRESQL        : $DB_SEEDER_VERSION_POSTGRESQL"
fi

( ./scripts/run_db_seeder_setup_files.sh %DB_SEEDER_DBMS% )

# ------------------------------------------------------------------------------
# CrateDB                                         https://hub.docker.com/_/crate
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "cratedb" ]; then
    start=$(date +%s)
    echo "CrateDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CrateDB $DB_SEEDER_VERSION_CRATEDB)"
    docker create --name db_seeder_db --env CRATE_HEAP_SIZE=2g -p 5432:5432/tcp crate:$DB_SEEDER_VERSION_CRATEDB crate -Cnetwork.host=_site_ -Cdiscovery.type=single-node

    echo "Docker start db_seeder_db (CrateDB $DB_SEEDER_VERSION_CRATEDB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 10

    end=$(date +%s)
    echo "DOCKER CrateDB was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# CUBRID                                  https://hub.docker.com/r/cubrid/cubrid
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "cubrid" ]; then
    start=$(date +%s)
    echo "CUBRID."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CUBRID $DB_SEEDER_VERSION_CUBRID)"
    docker create --name db_seeder_db -e CUBRID_DB=$DB_SEEDER_CUBRID_DATABASE -p 33000:33000/tcp cubrid/cubrid:$DB_SEEDER_VERSION_CUBRID

    echo "Docker start db_seeder_db (CUBRID $DB_SEEDER_VERSION_CUBRID) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    end=$(date +%s)
    echo "DOCKER CUBRID was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Apache Derby
#           https://hub.docker.com/repository/docker/konnexionsgmbh/apache_derby
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "derby" ]; then
    echo "Apache Derby"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (Apache Derby $DB_SEEDER_VERSION_DERBY)"
    docker create --name db_seeder_db -p 1527:1527/tcp konnexionsgmbh/apache_derby:$DB_SEEDER_VERSION_DERBY

    echo "Docker start db_seeder_db (Apache Derby $DB_SEEDER_VERSION_DERBY) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 10; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Apache Derby was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Firebird                        https://hub.docker.com/r/jacobalberty/firebird
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "firebird" ]; then
    start=$(date +%s)
    echo "Firebird."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Firebird $DB_SEEDER_VERSION_FIREBIRD)"
    docker create --name db_seeder_db -e FIREBIRD_DATABASE=$DB_SEEDER_FIREBIRD_DATABASE -e ISC_PASSWORD=firebird -p 3050:3050/tcp jacobalberty/firebird:$DB_SEEDER_VERSION_FIREBIRD

    echo "Docker start db_seeder_db (Firebird $DB_SEEDER_VERSION_FIREBIRD) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 10; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Firebird was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# H2 Database Engine
#     https://hub.docker.com/repository/docker/konnexionsgmbh/h2_database_engine
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "h2" ]; then
    echo "H2 Database Engine"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (H2 Database Engine $DB_SEEDER_VERSION_H2)"
    docker create --name db_seeder_db -p 9092:9092/tcp konnexionsgmbh/h2_database_engine:$DB_SEEDER_VERSION_H2

    echo "Docker start db_seeder_db (H2 Database Engine $DB_SEEDER_VERSION_H2) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 10; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER H2 Database Engine was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# HyperSQL Database
#      https://hub.docker.com/repository/docker/konnexionsgmbh/hypersql_database
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "hsqldb" ]; then
    echo "HyperSQL Database"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (HyperSQL Database $DB_SEEDER_VERSION_HSQLDB)"
    docker create --name db_seeder_db -p 9001:9001/tcp konnexionsgmbh/hypersql_database:$DB_SEEDER_VERSION_HSQLDB

    echo "Docker start db_seeder_db (HyperSQL Database $DB_SEEDER_VERSION_HSQLDB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 10; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER HyperSQL Database was ready in $((end - start)) seconds"
fi

## -----------------------------------------------------------------------------
# IBM Db2 Database                           https://hub.docker.com/r/ibmcom/db2
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    start=$(date +%s)
    echo "IBM Db2 Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (IBM Db2 $DB_SEEDER_VERSION_IBMDB2)"
    docker create --name db_seeder_db -e DBNAME=$DB_SEEDER_IBMDB2_DATABASE -e DB2INST1_PASSWORD=ibmdb2 -e LICENSE=accept -p 50000:50000 --privileged=true ibmcom/db2:$DB_SEEDER_VERSION_IBMDB2

    echo "Docker start db_seeder_db (IBM Db2 $DB_SEEDER_VERSION_IBMDB2)"
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 120

    end=$(date +%s)
    echo "DOCKER IBM Db2 Database was ready in $((end - start)) seconds"
fi

## -----------------------------------------------------------------------------
# IBM Informix       https://hub.docker.com/r/ibmcom/informix-developer-database
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "informix" ]; then
    start=$(date +%s)
    echo "IBM Informix."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (IBM Informix $DB_SEEDER_VERSION_INFORMIX)"
    docker create --name db_seeder_db -e LICENSE=accept -e DB_INIT=1 -p 9088:9088 --privileged ibmcom/informix-developer-database:$DB_SEEDER_VERSION_INFORMIX

    echo "Docker start db_seeder_db (IBM Informix $DB_SEEDER_VERSION_INFORMIX)"
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 10; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    docker exec -i db_seeder_db bash < scripts/run_db_seeder_setup_informix.input

    end=$(date +%s)
    echo "DOCKER IBM Informix was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MariaDB Server                                https://hub.docker.com/_/mariadb
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "mariadb" ]; then
    start=$(date +%s)
    echo "MariaDB Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MariaDB Server $DB_SEEDER_VERSION_MARIADB)"
    docker create --name db_seeder_db -e MYSQL_ROOT_PASSWORD=mariadb -p 3306:3306/tcp mariadb:$DB_SEEDER_VERSION_MARIADB --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

    echo "Docker start db_seeder_db (MariaDB Server $DB_SEEDER_VERSION_MARIADB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER MariaDB Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Mimer SQL                     https://hub.docker.com/r/mimersql/mimersql_v11.0
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "mimer" ]; then
    start=$(date +%s)
    echo "Mimer SQL."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Mimer SQL $DB_SEEDER_VERSION_MIMER)"
    docker create --name db_seeder_db -e MIMER_SYSADM_PASSWORD=mimersql -p 11360:1360/tcp mimersql/mimersql_v11.0:$DB_SEEDER_VERSION_MIMER

    echo "Docker start db_seeder_db (Mimer SQL $DB_SEEDER_VERSION_MIMER) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    docker exec -i db_seeder_db bash < scripts/run_db_seeder_setup_mimer.input

    end=$(date +%s)
    echo "DOCKER Mimer SQL was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server Database  https://hub.docker.com/_/microsoft-mssql-server
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "mssqlserver" ]; then
    start=$(date +%s)
    echo "Microsoft SQL Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Microsoft SQL Server $DB_SEEDER_VERSION_MSSQLSERVER)"
    docker create --name db_seeder_db -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=mssqlserver_2019" -p 1433:1433 mcr.microsoft.com/mssql/server:$DB_SEEDER_VERSION_MSSQLSERVER

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

if [ "$DB_SEEDER_DBMS" = "mysql" ]; then
    start=$(date +%s)
    echo "MySQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL)"
    docker create --name db_seeder_db -e MYSQL_ROOT_PASSWORD=mysql -p 3306:3306/tcp mysql:$DB_SEEDER_VERSION_MYSQL

    echo "Docker start db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "oracle" ]; then
    echo "Oracle Database"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (Oracle $DB_SEEDER_VERSION_ORACLE)"
    docker create --name db_seeder_db -e ORACLE_PWD=oracle -p 1521:1521/tcp --shm-size 1G konnexionsgmbh/$DB_SEEDER_VERSION_ORACLE

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

if [ "$DB_SEEDER_DBMS" = "postgresql" ]; then
    start=$(date +%s)
    echo "PostgreSQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (PostgreSQL $DB_SEEDER_VERSION_POSTGRESQL)"
    docker create --name db_seeder_db -e POSTGRES_DB=kxn_db_sys -e POSTGRES_PASSWORD=postgresql -e POSTGRES_USER=kxn_user_sys -p 5432:5432 postgres:$DB_SEEDER_VERSION_POSTGRESQL

    echo "Docker start db_seeder_db (PostgreSQL $DB_SEEDER_VERSION_POSTGRESQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER PostgreSQL Database was ready in $((end - start)) seconds"
fi

if [ "$DB_SEEDER_DBMS_EMBEDDED" == "no" ]; then
    docker ps
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
